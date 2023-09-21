package com.atguigu.yygh.hosp.service;

import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface HospitalService {
    void save(Map<String, Object> paramMap);

    Hospital getByHoscode(String hoscode);

    Page<Hospital> selectHospPage(Integer page, Integer limit, HospitalQueryVo hospitalSetQueryVo);

    void updateStatus(String id, Integer status);

    Map<String,Object> getHospById(String id);

    String getHospName(String hoscode);

    //根据医院名称获取医院列表
    List<Hospital> findByHosname(String hosname);

    Map<String,Object> item(String hoscode);
}
