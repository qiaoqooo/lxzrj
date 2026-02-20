package org.example.userservice.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.example.common.result.Result;
import org.example.common.util.UserContext;
import org.example.userservice.entity.FavoriteJob;
import org.example.userservice.mapper.FavoriteJobMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "职位收藏")
@RestController
@RequestMapping("/userservice/favorite")
public class FavoriteJobController {

    @Autowired
    private FavoriteJobMapper favoriteJobMapper;

    @ApiOperation("收藏/取消收藏 职位（切换）")
    @PostMapping("/toggle")
    public Result<Boolean> toggle(@RequestParam("jobId") Long jobId) {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            return Result.fail(401, "未登录");
        }
        LambdaQueryWrapper<FavoriteJob> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FavoriteJob::getUserId, userId).eq(FavoriteJob::getJobId, jobId);
        FavoriteJob exist = favoriteJobMapper.selectOne(wrapper);
        if (exist != null) {
            favoriteJobMapper.deleteById(exist.getId());
            return Result.success(false); // 返回 false 表示已取消收藏
        } else {
            FavoriteJob fav = new FavoriteJob();
            fav.setUserId(userId);
            fav.setJobId(jobId);
            fav.setCreatedAt(LocalDateTime.now());
            favoriteJobMapper.insert(fav);
            return Result.success(true); // 返回 true 表示已收藏
        }
    }

    @ApiOperation("检查是否已收藏某职位")
    @GetMapping("/check")
    public Result<Boolean> check(@RequestParam("jobId") Long jobId) {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            return Result.fail(401, "未登录");
        }
        LambdaQueryWrapper<FavoriteJob> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FavoriteJob::getUserId, userId).eq(FavoriteJob::getJobId, jobId);
        Long count = favoriteJobMapper.selectCount(wrapper);
        return Result.success(count != null && count > 0);
    }

    @ApiOperation("获取当前用户收藏的所有职位ID列表")
    @GetMapping("/list")
    public Result<List<Long>> list() {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            return Result.fail(401, "未登录");
        }
        LambdaQueryWrapper<FavoriteJob> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FavoriteJob::getUserId, userId).orderByDesc(FavoriteJob::getCreatedAt);
        List<FavoriteJob> favs = favoriteJobMapper.selectList(wrapper);
        if (favs == null || favs.isEmpty()) {
            return Result.success(Collections.emptyList());
        }
        List<Long> jobIds = favs.stream().map(FavoriteJob::getJobId).collect(Collectors.toList());
        return Result.success(jobIds);
    }
}












