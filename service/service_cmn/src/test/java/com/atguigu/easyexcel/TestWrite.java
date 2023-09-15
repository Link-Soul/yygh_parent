package com.atguigu.easyexcel;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

public class TestWrite {
    public static void main(String[] args) {

        List<UserData> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            UserData data = new UserData();
            data.setUid(i);
            data.setUsername("list"+i);
            list.add(data);
        }

        //设置excel文件路径名称
        String fileName = "E:\\home\\01.xlsx";
        EasyExcel.write(fileName, UserData.class)
                .sheet("用户信息")
                .doWrite(list);
    }
}
