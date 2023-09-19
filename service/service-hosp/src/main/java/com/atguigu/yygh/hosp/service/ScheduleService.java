package com.atguigu.yygh.hosp.service;

import com.atguigu.yygh.model.hosp.Schedule;
import com.atguigu.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface ScheduleService {
    void save(Map<String, Object> paramMap);

    Page<Schedule> findPageSchedule(int page, int limit, ScheduleQueryVo scheduleQueryVo);

    void remove(String hoscode, String hosScheduleId);

    //Map<String, Object> getRuleSchedule(Long page, Long limit, String hoscode, String depcode);

    //根据医院编号 和 科室编号 ，查询排班规则数据
    Map<String, Object> getRuleSchedule(Long page, Long limit, String hoscode, String depcode);

    // 根据医院编号和科室编号，和工作日期 查询排班规则数据

    List<Schedule> getDetailSchedule(String hoscode, String depcode, String workDate);
}
