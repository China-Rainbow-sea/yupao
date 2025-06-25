package com.rainbowsea.yupao.controller;


import com.rainbowsea.yupao.common.BaseResponse;
import com.rainbowsea.yupao.common.ErrorCode;
import com.rainbowsea.yupao.common.ResultUtils;
import com.rainbowsea.yupao.exception.BusinessException;
import com.rainbowsea.yupao.model.Team;
import com.rainbowsea.yupao.model.dto.TeamQuery;
import com.rainbowsea.yupao.service.TeamService;
import com.rainbowsea.yupao.service.UserService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/team")
@Api("接口文档的一个别名处理定义 TeamController ")
@CrossOrigin(origins = {"http://localhost:5173"})
@Slf4j
public class TeamController {

    @Resource
    private UserService userService;

    @Resource
    private TeamService teamService;


    /**
     * 插入 team 队伍，添加队伍
     * @param team team
     * @return team.getId()
     */
    @PostMapping("/add")
    public BaseResponse<Long> addTeam(@RequestBody Team team) {
        if(team == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        boolean save = teamService.save(team);

        if(!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"插入失败");
        }

        return ResultUtils.success(team.getId());
    }


    /**
     * 删除队伍，移除队伍
     * @param id 队伍的 id
     * @return Boolean 移除成功 true，否则 false
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteTeam(@RequestBody long id) {
        if(id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        boolean result = teamService.removeById(id);
        if(!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"插入失败");
        }

        return ResultUtils.success(true);
    }

    /**
     * 更新队伍内容
     * @param team Team 对象
     * @return Boolean 更新成功 true，否则 false
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateTeam(@RequestBody Team team) {
            if(team == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }

            boolean result = teamService.updateById(team);

            if(!result) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR,"插入失败");
            }

            return ResultUtils.success(true);
    }


    /**
     * 获取队伍信息
     * @param id 队伍的 ID
     * @return  BaseResponse<Team>
     */
    @GetMapping("/get")
    public BaseResponse<Team> getTeamById(long id) {
        if(id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Team team = teamService.getById(id);
        if(team == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        return ResultUtils.success(team);
    }


    @GetMapping("/list")
    public BaseResponse<List<Team>> listTeams(TeamQuery teamQuery) {

        return null;
    }
}
