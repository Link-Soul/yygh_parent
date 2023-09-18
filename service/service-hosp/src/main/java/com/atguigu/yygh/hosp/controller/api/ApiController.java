package com.atguigu.yygh.hosp.controller.api;


import com.atguigu.yygh.common.exception.YyghException;
import com.atguigu.yygh.common.helper.HttpRequestHelper;
import com.atguigu.yygh.common.result.Result;

import com.atguigu.yygh.common.result.ResultCodeEnum;
import com.atguigu.yygh.common.util.MD5;
import com.atguigu.yygh.hosp.service.DepartmentService;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.hosp.service.HospitalSetService;
import com.atguigu.yygh.hosp.service.ScheduleService;
import com.atguigu.yygh.model.hosp.Department;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.model.hosp.Schedule;
import com.atguigu.yygh.vo.hosp.DepartmentQueryVo;

import com.atguigu.yygh.vo.hosp.ScheduleQueryVo;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/hosp")
public class ApiController {

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private HospitalSetService hospitalSetService;

    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private ScheduleService scheduleService;

    // 删除排班接口
    @PostMapping("schedule/remove")
    public Result remove(HttpServletRequest request){
        //获取传过来的医院编号
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        // 医院编号
        String hoscode = (String) paramMap.get("hoscode");
        // 科室编号
        String hosScheduleId = (String) paramMap.get("hosScheduleId");
        //签名校验
        String signKey = hospitalSetService.getSignKey(hoscode);
        String hospSign = (String) paramMap.get("sign");
        String encrypt = MD5.encrypt(signKey);
        if (!encrypt.equals(hospSign)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        scheduleService.remove(hoscode,hosScheduleId);

        return Result.ok();



    }


    // 查询排班接口
    @PostMapping("schedule/list")
    public Result findSchedule(HttpServletRequest request){
        //获取传过来的医院编号
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        // 医院编号
        String hoscode = (String) paramMap.get("hoscode");
        // 科室编号
        String depcode = (String) paramMap.get("depcode");
        // page和limit不能如上这样直接写，如果为空则直接空指针
        int page = StringUtils.isEmpty(paramMap.get("page"))
                ? 1 : Integer.parseInt((String) paramMap.get("page"));
        int limit = StringUtils.isEmpty(paramMap.get("limit"))
                ? 1 : Integer.parseInt((String) paramMap.get("limit"));

        //签名校验
        String signKey = hospitalSetService.getSignKey(hoscode);
        String hospSign = (String) paramMap.get("sign");
        String encrypt = MD5.encrypt(signKey);
        if (!encrypt.equals(hospSign)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }


        ScheduleQueryVo scheduleQueryVo = new ScheduleQueryVo();
        scheduleQueryVo.setHoscode(hoscode);
        scheduleQueryVo.setDepcode(depcode);
        Page<Schedule> pageModel = scheduleService.findPageSchedule(page,limit,scheduleQueryVo);


        return Result.ok(pageModel);


    }


    // 上传排班接口
    @PostMapping("saveSchedule")
    public Result saveSchedule(HttpServletRequest request){
        //获取传过来的医院编号
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
        String hoscode = (String) paramMap.get("hoscode");
        String depcode = (String) paramMap.get("depcode");

        //TODD 签名校验
        //签名校验
        String signKey = hospitalSetService.getSignKey(hoscode);
        String hospSign = (String) paramMap.get("sign");
        String encrypt = MD5.encrypt(signKey);
        if (!encrypt.equals(hospSign)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        scheduleService.save(paramMap);
        return Result.ok();
    }



    // 删除科室接口
    @PostMapping("department/remove")
    public Result removeDepartment(HttpServletRequest request){
        //获取传过来的医院编号
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
        String hoscode = (String) paramMap.get("hoscode");
        String depcode = (String) paramMap.get("depcode");

        //TODD 签名校验
        //签名校验
        String signKey = hospitalSetService.getSignKey(hoscode);
        String hospSign = (String) paramMap.get("sign");
        String encrypt = MD5.encrypt(signKey);
        if (!encrypt.equals(hospSign)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }


        departmentService.remove(hoscode,depcode);

        return Result.ok();
    }

    // 查询科室接口
    @PostMapping("department/list")
    public Result findDepartment(HttpServletRequest request){
        //获取传过来的医院编号
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
        // 医院编号
        String hoscode = (String) paramMap.get("hoscode");
        // page和limit不能如上这样直接写，如果为空则直接空指针
        int page = StringUtils.isEmpty(paramMap.get("page"))
                ? 1 : Integer.parseInt((String) paramMap.get("page"));
        int limit = StringUtils.isEmpty(paramMap.get("limit"))
                ? 1 : Integer.parseInt((String) paramMap.get("limit"));

        //签名校验
        String signKey = hospitalSetService.getSignKey(hoscode);
        String hospSign = (String) paramMap.get("sign");
        String encrypt = MD5.encrypt(signKey);
        if (!encrypt.equals(hospSign)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }


        DepartmentQueryVo departmentQueryVo = new DepartmentQueryVo();
        departmentQueryVo.setHoscode(hoscode);
        Page<Department> pageModel = departmentService.findPageDepartment(page,limit,departmentQueryVo);


        return Result.ok(pageModel);
    }



    // 保存科室接口
    @PostMapping("saveDepartment")
    public Result saveDepartment(HttpServletRequest request) {
        //获取传过来的医院编号
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

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

        departmentService.save(paramMap);
        return Result.ok();




    }


    @PostMapping("hospital/show")
    public Result getHospital(HttpServletRequest request){
        //获取传过来的医院编号
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
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

















