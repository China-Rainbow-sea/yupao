package com.rainbowsea.yupao.service;

import com.rainbowsea.yupao.model.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author huo
 * @description 针对表【user(用户)】的数据库操作Service
 * @createDate 2025-04-14 16:03:21
 */
public interface UserService extends IService<User> {


    /**
     * 用户注册
     *
     * @param userAccount   用户账号
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @param plantCode 用户中心用户的编号
     * @return 新用户 id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword,String plantCode);


    /**
     * 用户登录
     *
     * @param userAccount  用户账号
     * @param userPassword 用户密码
     * @param request      请求
     * @return 脱敏后的用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);


    /**
     * 用户脱敏
     *
     * @param originUser 用户
     * @return 返回脱敏后的安全用户User 对象
     */
    User getSafetyUser(User originUser);

    /**
     * 用户注销，移除 session 会话当中存储的用户态信息
     * @param request
     * @return 返回 1 表示注销成功
     */
    int userLogout(HttpServletRequest request);

    /**
     * 根据标签搜索用户
     * @param tagNameList 用户要拥有的标签
     * @return List<User>
     */
    List<User> searchUserByTags(List<String> tagNameList);


    /**
     * 更新用户信息
     * @param user 用户信息
     * @return int 返回修改的记录行数
     */
    int updateUser(User user,User loginUser);


    /**
     * 获取当前登录用户信息
     * @param request
     * @return User
     */
    User getLoginUser(HttpServletRequest request);


    /**
     * 是否为管理员
     * @param request
     * @return
     */
    boolean isAdmin(HttpServletRequest request);

    /**
     * 是否为管理员
     * @param loginUser
     * @return
     */
    boolean isAdmin(User loginUser);


    /**
     * 匹配用户
     * @param num
     * @param loginUser
     * @return
     */
    List<User> matchUsers(long num, User loginUser);
}
