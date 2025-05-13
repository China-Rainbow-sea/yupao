package com.rainbowsea.yupao.one;


import com.alibaba.excel.EasyExcel;

import java.util.List;

/**
 * 导入 Excel
 *
 */
public class ImportExcel {

    /**
     * 读取数据
     */
    public static void main(String[] args) {
        // todo 记得改为自己的测试文件
        String fileName = "E:\\Java\\project\\鱼皮星球项目\\伙伴匹配系统\\yupao\\yupao-backend\\src\\main\\resources\\testExcel.xlsx";
        //readByListener(fileName);
        synchronousRead(fileName);
    }



    /**
     * 同步读
     *
     * @param fileName
     */
    public static void synchronousRead(String fileName) {
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 同步读取会自动finish
        List<DemoExcelData> totalDataList =
                EasyExcel.read(fileName).head(DemoExcelData.class).sheet().doReadSync();
        for (DemoExcelData xingQiuTableUserInfo : totalDataList) {
            System.out.println(xingQiuTableUserInfo);
        }
    }

    /**
     * 监听器读取
     * @param fileName
     */
    public static void readByListener(String fileName) {
        EasyExcel.read(fileName, DemoExcelData.class, new TableListener()).sheet().doRead();
    }



}
