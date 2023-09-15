package com.atguigu.easyexcel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;

public class TestRead {
    public static void main(String[] args) {
        String fileName = "E:\\home\\01.xlsx";

        EasyExcel.read(fileName, UserData.class, new ExcelListener()).sheet().doRead();

    }
}
