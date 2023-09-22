package com.atguigu.yygh.msm.service;

import com.atguigu.yygh.model.user.UserInfo;
import com.atguigu.yygh.vo.user.LoginVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface MsmService {

    //发送手机验证码
    boolean send(String phone, String code);
}
