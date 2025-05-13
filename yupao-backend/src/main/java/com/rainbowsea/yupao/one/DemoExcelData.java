package com.rainbowsea.yupao.one;


import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class DemoExcelData {

    //@ExcelProperty(index = 1) 可以用列表匹配

    /**
     * id
     * 强制读取第几个，这里不建议用 index 和 name 同时使用，
     * 要么一个对象只用 index ，要么一个对象只用 name 去匹配
     */
    @ExcelProperty("成员编号")
    private String planeCode;

    /**
     * 用户昵称
     */
    @ExcelProperty("成员昵称")
    private String username;

}
