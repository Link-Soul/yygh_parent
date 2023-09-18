package com.atguigu.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.cmn.client.DictFeignClient;
import com.atguigu.yygh.hosp.repository.HospitalRepository;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class HospitalServiceImpl implements HospitalService {
    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private DictFeignClient dictFeignClient;
    // 在跨模块调用的时候会红色波浪下划线警告，
    // 可以通过在原来的地方加上个@Repository注解到ioc容器，就不报错了

    @Override
    public void save(Map<String, Object> paramMap) {
        // 把map变成hospital
        // tojson变成字符串，
        // log.info(JSONObject.toJSONString(paramMap));
        Hospital hospital = JSONObject.parseObject(JSONObject.toJSONString(paramMap),Hospital.class);
        //判断是否存在
        Hospital targetHospital = hospitalRepository.getHospitalByHoscode(hospital.getHoscode());
        if(null != targetHospital) {    // 存在 修改
            hospital.setStatus(targetHospital.getStatus());
            hospital.setCreateTime(targetHospital.getCreateTime());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        } else {                        // 不存在 添加
            //0：未上线 1：已上线
            hospital.setStatus(0);
            hospital.setCreateTime(new Date());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        }
    }

    @Override
    public Hospital getByHoscode(String hoscode) {
        return hospitalRepository.getHospitalByHoscode(hoscode);
    }

    @Override
    public Page<Hospital> selectHospPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo) {
        Pageable pageable = PageRequest.of(page-1,limit);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);  //忽略大小写
        Hospital hospital = new Hospital();
        BeanUtils.copyProperties(hospitalQueryVo,hospital);
        // 创建
        Example<Hospital> example = Example.of(hospital,matcher);
        // 实现查询
        Page<Hospital> pages = hospitalRepository.findAll(example, pageable);

        //远程调用：获取医院的等级信息
        List<Hospital> content = pages.getContent();
        // 获取查询list集合，便利进行医院等级封装
        pages.getContent().stream().forEach(item ->{
            this.setHospitalHosType(item);
        });

        return pages;
    }

    private Hospital setHospitalHosType(Hospital hospital) {
        // 根据dictCode获取等级
        String hostypeString = dictFeignClient.getName("Hostype", hospital.getHostype());
        // 查询省市地区
        String ProvinceCode = dictFeignClient.getName(hospital.getProvinceCode());
        String CityCode = dictFeignClient.getName(hospital.getCityCode());
        String DistrictCode = dictFeignClient.getName(hospital.getDistrictCode());

        hospital.getParam().put("hostypeString",hostypeString);
        hospital.getParam().put("fullAddress",ProvinceCode+CityCode+DistrictCode);
        return hospital;

    }

}
