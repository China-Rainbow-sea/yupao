package com.rainbowsea.yupao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rainbowsea.yupao.common.ErrorCode;
import com.rainbowsea.yupao.exception.BusinessException;
import com.rainbowsea.yupao.model.User;
import com.rainbowsea.yupao.service.UserService;
import com.rainbowsea.yupao.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.rainbowsea.yupao.contant.UserConstant.USER_LOGIN_STATE;

/**
 * @author huo
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2025-04-14 16:03:21
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    /**
     * 加盐 混淆，让密码更加没有规律，更加安全一些
     */
    private static final String SALT = "rainbowsea";


    @Resource
    private UserMapper userMapper;

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String plantCode) {


        // 1. 校验
        if (StringUtils.isAllBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
            //return -1;
        }
        // 账号长度不小于 4 位 ，不能大于 128
        if (userAccount.length() < 4 || userAccount.length() > 128) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
            //return -1;
        }

        //  密码就不小于 8 位 , 注意两个还有一个校验密码
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
            //return -1;
        }
        // 账号不包含特殊字符
        // 正则表达式匹配字母、数字、下划线
        String regex = "^[a-zA-Z0-9_]+$";
        boolean matcher = userAccount.matches(regex);
        //  matches() 满足正则表达式，则返回 true，否则返回 false
        if (!matcher) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户不能含有特殊字符");
            //return -1;
        }

        /**
         * 用户编号不能超过 5
         */
        if (plantCode.length() > 5) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户编号不能超过 5 位数");
            //return -1;
        }

        // 密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两者密码不相同");
            //return -1;
        }

        // 用户中心的用户编号不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("planetCode", plantCode);// planetCode 不可以随便写，要是对应数据库当中的字段名称
        Long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {  // > 0 查询到，重复用户编号，不可以注册
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户中心的用户编号不能重复");
            //return -1;
        }


        // 账号不可重复，这里查询数据库，一般将其放在最后面，当前面的校验都没通过，
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        count = this.count(queryWrapper);  // 查询到就大于 0
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户不可重复");
            //return -1;
        }


        // 密码加密+加盐
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        // 校验通过插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "注册失败");
            //return -1;
        }

        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {

        // 1. 校验
        if (StringUtils.isAllBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
            //return null;
        }
        // 账号长度不小于 4 位 ，不能大于 128
        if (userAccount.length() < 4 || userAccount.length() > 128) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号错误");
            //return null;
        }

        //  密码就不小于 8 位
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
            //return null;
        }
        // 账号不包含特殊字符
        // 正则表达式匹配字母、数字、下划线
        String regex = "^[a-zA-Z0-9_]+$";
        boolean matcher = userAccount.matches(regex);
        //  matches() 满足正则表达式，则返回 true，否则返回 false
        if (!matcher) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户不能含有特殊字符");
            //return null;
        }


        // 密码加密+加盐，注意:你查询密码的时候，因为你的当时插入数据是怎么加密的，你查询对面的时候也是怎么加密的
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);  // 注意：这里的字段名是数据库当中对应的字段名，不可以随便写
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            log.info("user login failed, userAccount cannot userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或密码错误");
            //return null;
        }


        // 3.用户脱敏
        User safeUser = getSafetyUser(user);


        // 4. 记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, safeUser);

        return safeUser;
    }


    /**
     * 用户脱敏
     *
     * @param originUser 用户
     * @return 返回脱敏后的安全用户User 对象
     */
    @Override
    public User getSafetyUser(User originUser) {
        if (originUser == null) {
            return null;
        }

        User safeUser = new User();
        safeUser.setId(originUser.getId());
        safeUser.setUsername(originUser.getUsername());
        safeUser.setUserAccount(originUser.getUserAccount());
        safeUser.setAvatarUrl(originUser.getAvatarUrl());
        safeUser.setGender(originUser.getGender());
        safeUser.setPhone(originUser.getPhone());
        safeUser.setEmail(originUser.getEmail());
        safeUser.setUserStatus(originUser.getUserStatus());
        safeUser.setCreateTime(originUser.getCreateTime());
        safeUser.setUpdateTime(originUser.getUpdateTime());
        safeUser.setUserRole(originUser.getUserRole());
        safeUser.setPlanetCode(originUser.getPlanetCode());
        safeUser.setTags(originUser.getTags());
        return safeUser;
    }


    /**
     * 用户注销
     *
     * @param request
     * @return 返回 1 表示注销成功
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        // 移除用户存储在 session 会话当中的用户态
        request.getSession().removeAttribute(USER_LOGIN_STATE);

        return 1;
    }


    /**
     * 根据标签搜索用户(内存标签)
     *
     * @param tagNameList 用户要拥有的标签
     * @return List<User> 用户列表
     */
    @Override
    public List<User> searchUserByTags(List<String> tagNameList) {

        // 方式1: SQL 查询：实现简单，可以通过拆分查询进一步优化。
        if (CollectionUtils.isEmpty(tagNameList)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        //QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //// 拼接 and 查询
        //// like '%Java%' and like '%C++%'
        //for (String tagName : tagNameList) {
        //    // column 是数据表当中的字段名,不可以随便写
        //    queryWrapper = queryWrapper.like("tags", tagName);
        //
        //}
        //
        //List<User> userList = userMapper.selectList(queryWrapper);
        //
        //// 使用 stream 进行过滤显示
        //return userList.stream().map(this::getSafetyUser).collect(Collectors.toList());

        // 方式2: 内存查询：灵活，可以通过并发进一步优化。就是查询出来后，通过 steam 进行处理，过滤。
        // 1. 先查询所有用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        List<User> userList = userMapper.selectList(queryWrapper);
        Gson gson = new Gson();
        // 2. 在内存中判断是否包含要求的标签
        return userList.stream().filter(user -> {
            String tagsStr = user.getTags();
            if (StringUtils.isBlank(tagsStr)) {
                return false;
            }
            // 将 set 集合对象转换为 JSON 字符串
            Set<String> tempTagNameSet = gson.fromJson(tagsStr, new TypeToken<Set<String>>() {
            }.getType());
            for (String tagName : tagNameList) {
                // 判断 set 集合当中是否含有该 tagName 标签，不含有该标签就返回 false 过滤掉
                if (!tempTagNameSet.contains(tagName)) {
                    return false;
                }
            }
            return false;
        }).map(this::getSafetyUser).collect(Collectors.toList());

    }


    /**
     * 根据标签搜索用户（SQL查询版）
     *
     * @param tagNameList 用户要拥有的标签
     * @return List<User> 用户列表
     */
    @Deprecated
    private List<User> searchUserByTagsBySQL(List<String> tagNameList) {
        // 方式1: SQL 查询：实现简单，可以通过拆分查询进一步优化。
        if (CollectionUtils.isEmpty(tagNameList)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // 拼接 and 查询
        // like '%Java%' and like '%C++%'
        for (String tagName : tagNameList) {
            // column 是数据表当中的字段名,不可以随便写
            queryWrapper = queryWrapper.like("tags", tagName);

        }

        List<User> userList = userMapper.selectList(queryWrapper);

        // 使用 stream 进行过滤显示
        return userList.stream().map(this::getSafetyUser).collect(Collectors.toList());

    }
}




