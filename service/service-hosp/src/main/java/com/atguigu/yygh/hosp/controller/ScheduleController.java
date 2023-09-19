package com.atguigu.yygh.hosp.controller;


import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.hosp.service.ScheduleService;
import com.atguigu.yygh.model.hosp.Schedule;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "医院设置管理1")
@RestController
@RequestMapping("/admin/hosp/schedule")
//@CrossOrigin        //加上这个就允许跨域，也就是访问不同地址的url
// nginx有配置类统一跨域，这个就不需要了
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private MongoTemplate mongoTemplate;


    // 根据医院编号和科室编号，查询排班规则数据
    @ApiOperation(value = "根据医院编号和科室编号，查询排班规则数据")
    @GetMapping("getScheduleRule/{page}/{limit}/{hoscode}/{depcode}")
    public Result getScheduleRule(@PathVariable Long page,
                                  @PathVariable Long limit,
                                  @PathVariable String hoscode,
                                  @PathVariable String depcode){

        Map<String,Object> map = scheduleService.getRuleSchedule(page,limit,hoscode,depcode);
        return Result.ok(map);

    }
    // 根据医院编号和科室编号，和工作日期 查询排班规则数据
    @ApiOperation(value = "根据医院编号和科室编号，查询排班规则数据")
    @GetMapping("getScheduleDetail/{hoscode}/{depcode}/{workDate}")
    public Result getScheduleDetail(@PathVariable String hoscode,
                                    @PathVariable String depcode,
                                    @PathVariable String workDate){
        List<Schedule> list = scheduleService.getDetailSchedule(hoscode,depcode,workDate);
        return Result.ok(list);
    };


}
