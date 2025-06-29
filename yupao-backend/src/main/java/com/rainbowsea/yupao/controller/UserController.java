package com.rainbowsea.yupao.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rainbowsea.yupao.common.BaseResponse;
import com.rainbowsea.yupao.common.ErrorCode;
import com.rainbowsea.yupao.utils.ResultUtils;
import com.rainbowsea.yupao.exception.BusinessException;
import com.rainbowsea.yupao.model.User;
import com.rainbowsea.yupao.model.request.UserLoginRequest;
import com.rainbowsea.yupao.model.request.UserRegisterRequest;
import com.rainbowsea.yupao.service.UserService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.rainbowsea.yupao.contant.UserConstant.USER_LOGIN_STATE;

@RestController
@RequestMapping("/user")
@Api("接口文档的一个别名处理定义 UserController")
@CrossOrigin(origins = {"http://localhost:5173","http://localhost:3000"})  // 配置前端访问路径的放行,可以配置多个
@Slf4j
public class UserController {


    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 用户登录
     *
     * @param userLoginRequest 用户的登录的请求体，用户登录发送的信息，封装到其中了
     * @param request          用户请求
     * @return RainbowSea
     */
    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {

        if (userLoginRequest == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }

        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();

        if (StringUtils.isAllBlank(userAccount, userPassword)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }

        User user = userService.userLogin(userAccount, userPassword, request);

        //return new BaseResponse<>(0,user,"ok");  可以将其封装为一个工具类，作为返回成功
        return ResultUtils.success(user);

    }


    /**
     * 用户注册
     * // @RequestBody 常用于处理POST请求中的JSON或XML数据，将这些数据转换为Java对象
     *
     * @param userRegisterRequest 用户请求体当中的信息，用户注册时发送的信息，封装到其中了
     * @return RainbowSea
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {

        if (userRegisterRequest == null) {
            //return ResultUtils.error(ErrorCode.PARAMS_ERROR);

            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String plantCode = userRegisterRequest.getPlantCode();

        if (StringUtils.isAllBlank(userAccount, userPassword, checkPassword, plantCode)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        long id = userService.userRegister(userAccount, userPassword, checkPassword, plantCode);
        return ResultUtils.success(id);
    }


    /**
     * 根据用户的名字查询用户信息
     *
     * @param username 用户名
     * @return 多个用户的 List 集合
     */
    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(@RequestBody String username, HttpServletRequest request) {
        // 仅仅管理员可以查询
        if (!userService.isAdmin(request)) {
            return ResultUtils.success(new ArrayList<>());
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNoneBlank(username)) {
            queryWrapper.like("username", username);
        }


        // 这样返回的话，会将查询的数据表中所有信息都会返回给前端的，这里我们需要过滤一下
        //return userService.list(queryWrapper);
        List<User> userList = userService.list(queryWrapper);
        List<User> list = userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(list);
       /* return userList.stream().map(user -> {
            userService.getSafetyUser(user);
            return user;
        }).collect(Collectors.toList());*/
    }


    /**
     * 根据 tags 标签查询用户信息，给前端展示，tags 可以是多个
     * @param tagNameList tags 标签列表，多个
     * @return userList 对应标签的列表用户信息
     */
    @GetMapping("/search/tags")
    public BaseResponse<List<User>> searchUsersByTags(@RequestParam(required = false) List<String> tagNameList) {

        if(CollectionUtils.isEmpty(tagNameList)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        List<User> userList = userService.searchUserByTags(tagNameList);
        return ResultUtils.success(userList);
    }



    /**
     * 根据用户ID 删除信息，逻辑删除
     *
     * @param id 用户ID
     * @return 成功返回 true,失败返回 false
     */
    @GetMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id, HttpServletRequest request) {
        // 仅仅管理员可以删除用户
        if (!userService.isAdmin(request)) {
            return null;
        }
        if (id <= 0) {
            return null;
        }

        boolean b = userService.removeById(id);
        return ResultUtils.success(b);

    }


    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {

        // 通过用户态来获取用户的信息
        // import static com.rainbowsea.usercenter.contant.UserConstant.USER_LOGIN_STATE;
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }

        Long userId = currentUser.getId();
        // todo 校验用户是否合法
        User user = userService.getById(userId);
        // 返回脱敏后的信息
        User safetyUser = userService.getSafetyUser(user);

        return ResultUtils.success(safetyUser);

    }


    /**
     * 用户注销
     *
     * @param request
     * @return 注销成功返回 1,失败返回 null
     */
    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            return null;
        }

        int result = userService.userLogout(request);

        return ResultUtils.success(result);

    }




    /**
     * 更新用户信息
     * @param user
     * @param request
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Integer> updateUser(@RequestBody User user,HttpServletRequest request) {
        // 校验参数是否为空
        if(user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        User loginUser = userService.getLoginUser(request);
        int result = userService.updateUser(user,loginUser);

        return ResultUtils.success(result);
    }


    /**
     * 首页 recommend 推荐显示内容，同时进行了分页处理
     *
     * @param request
     * @return
     */
   /* @GetMapping("/recommend")
    public BaseResponse<Page<User>> recommendUsers(long pageSize, long pageNum,HttpServletRequest request) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        Page<User> userList = userService.page(new Page<>(pageNum, pageSize), queryWrapper);

        //List<User> userList = userService.list(queryWrapper);
        //List<User> list = userList.stream().
        //        map(user -> userService.getSafetyUser(user)).
        //        collect(Collectors.toList());

        return ResultUtils.success(userList);
    }*/

    /**
     * 走缓存的方式，实现
     * @param pageSize 一页显示的大小个数
     * @param pageNum 显示第几页
     * @param request request
     * @return BaseResponse<Page<User>>
     */
    @GetMapping("/recommend")
    public BaseResponse<Page<User>> recommendUsers(long pageSize, long pageNum, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        String redisKey = String.format("yupao:user:recommend:%s", loginUser.getId());
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        // 如果有缓存，直接读缓存
        Page<User> userPage = (Page<User>) valueOperations.get(redisKey);
        if (userPage != null) {
            return ResultUtils.success(userPage);
        }

        // 无缓存，查数据库
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        userPage = userService.page(new Page<>(pageNum, pageSize), queryWrapper);
        // 写缓存
        try {
            valueOperations.set(redisKey, userPage, 30000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.error("redis set key error", e);
        }
        return ResultUtils.success(userPage);
    }


    /**
     * 首页 recommend 推荐显示内容，未使用 Mybatis-Plus 分页处理 --- 测试
     *
     * @param request
     * @return
     */
    //@GetMapping("/recommend")
    //public BaseResponse<List<User>> recommendUsers(long pageSize, long pageNum,HttpServletRequest request) {
    //    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
    //
    //    //Page<User> userList = userService.page(new Page<>(pageNum, pageSize), queryWrapper);
    //
    //    List<User> userList = userService.list(queryWrapper);
    //    List<User> list = userList.stream().
    //            map(user -> userService.getSafetyUser(user)).
    //            collect(Collectors.toList());
    //
    //    return ResultUtils.success(list);
    //}


    /**
     * 获取最匹配的用户
     *
     * @param num
     * @param request
     * @return
     */
    @GetMapping("/match")
    public BaseResponse<List<User>> matchUsers(long num, HttpServletRequest request) {
        if (num <= 0 || num > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        return ResultUtils.success(userService.matchUsers(num, user));
    }

}
