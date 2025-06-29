package com.rainbowsea.yupao.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.rainbowsea.yupao.model.Team;
import com.rainbowsea.yupao.model.User;
import com.rainbowsea.yupao.model.dto.TeamQuery;
import com.rainbowsea.yupao.model.request.TeamJoinRequest;
import com.rainbowsea.yupao.model.request.TeamQuitRequest;
import com.rainbowsea.yupao.model.request.TeamUpdateRequest;
import com.rainbowsea.yupao.model.vo.TeamUserVO;

import java.util.List;

/**
 *
 */
public interface TeamService extends IService<Team> {


    /**
     * 创建队伍
     * @param team 队伍
     * @param loginUser User 用户
     * @return long 队伍的 Id
     */
    public long addTeam(Team team, User loginUser);

    /**
     * 搜索队伍
     * @param teamQuery
     * @param isAdmin
     * @return List<TeamUserVO>
     */
    List<TeamUserVO> listTeams(TeamQuery teamQuery,boolean isAdmin);

    /**
     * 更新队伍
     * @param teamUpdateRequest
     * @return boolean
     */
    boolean updateTeam(TeamUpdateRequest teamUpdateRequest,User loginUser);


    /**
     * 加入队伍
     * @param teamJoinRequest
     * @param loginUser
     * @return
     */
    boolean joinTeam(TeamJoinRequest teamJoinRequest, User loginUser);

    /**
     * 退出队伍
     * @param teamQuitRequest
     * @param loginUser
     * @return
     */
    boolean quitTeam(TeamQuitRequest teamQuitRequest, User loginUser);


    /**
     * 删除(解散)队伍
     * @param id 队伍中队长的 ID
     * @return
     */
    boolean deleteTeam(long id,User loginUser);


}
