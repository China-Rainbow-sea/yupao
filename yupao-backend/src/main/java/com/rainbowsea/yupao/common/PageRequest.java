package com.rainbowsea.yupao.common;


import lombok.Data;

import java.io.Serializable;

/**
 * 通用分页请求参数
 * 可以通过让其他通用类，继承分页类，从而让其他通用类也包含有分页属性
 */
@Data
public class PageRequest implements Serializable {

    private static final long serialVersionUID = 9075699384270981491L;

    /**
     * 页面大小
     */
    protected int pageSize = 10;

    /**
     * 当前是第几页
     */
    protected int pageNum = 1;
}
