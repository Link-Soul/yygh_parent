package com.atguigu.yygh.hosp.controller.api;


import com.atguigu.yygh.common.exception.YyghException;
import com.atguigu.yygh.common.helper.HttpRequestHelper;
import com.atguigu.yygh.common.result.Result;

import com.atguigu.yygh.common.result.ResultCodeEnum;
import com.atguigu.yygh.common.util.MD5;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.hosp.service.HospitalSetService;
import com.atguigu.yygh.model.hosp.Hospital;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/hosp")
public class ApiController {

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private HospitalSetService hospitalSetService;

    @PostMapping("hospital/show")
    public Result getHospital(HttpServletRequest request){
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        //获取传过来的医院编号
        String hospSign = (String) paramMap.get("sign");
        String hoscode = (String)paramMap.get("hoscode");

        //签名校验
        String signKey = hospitalSetService.getSignKey(hoscode);
        String signKeyMD5 = MD5.encrypt(signKey);
        if (!signKeyMD5.equals(hospSign)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        Hospital byHoscode = hospitalService.getByHoscode(hoscode);
        return Result.ok(byHoscode);
    }


    @ApiOperation(value = "上传医院")
    @PostMapping("saveHospital")
    public Result saveHospital(HttpServletRequest request) {

        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());

        String hospSign = (String) paramMap.get("sign");
        //必须参数校验
        String hoscode = (String)paramMap.get("hoscode");

        if(StringUtils.isEmpty(hoscode)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        //传输过程中“+”转换为了“ ”，因此我们要转换回来
        String logoDataString = (String)paramMap.get("logoData");
        if(!StringUtils.isEmpty(logoDataString)) {
            String logoData = logoDataString.replaceAll(" ", "+");
            paramMap.put("logoData", logoData);
        }

        //签名校验
        String signKey = hospitalSetService.getSignKey(hoscode);

//        if(!HttpRequestHelper.isSignEquals(paramMap, signKey)) {
//            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
//        }
        String encrypt = MD5.encrypt(signKey);

        if (!encrypt.equals(hospSign)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        hospitalService.save(paramMap);
        return Result.ok();
    }





}

















