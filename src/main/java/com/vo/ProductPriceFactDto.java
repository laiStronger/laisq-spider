/*
 * 文件名：ProductPriceFactDto.java
 * 版权：Copyright 2014 youanmi Tech. Co. Ltd. All Rights Reserved. 
 * 描述： ProductPriceFactDto.java
 * 修改人：chenwenlong
 * 修改时间：2014年12月27日
 * 修改内容：新增
 */
package com.vo;

import java.io.Serializable;

/**
 *  添加类的一句话简单描述。
 * <p>
 *  详细描述
 * <p>
 *  示例代码
 * 
 * <pre>
 * </pre>
 * 
 * @author chenwenlong
 * @version YouAnMi-OTO 2014年12月27日
 * @since YouAnMi-OTO
 */
public class ProductPriceFactDto implements Serializable {
    private Long paramId;

    private String dictType;

    private String paramValue;

    private String displayName;

    private Integer paramOrder;


    public Long getParamId() {
        return paramId;
    }


    public void setParamId(Long paramId) {
        this.paramId = paramId;
    }


    public Integer getParamOrder() {
        return paramOrder;
    }


    public void setParamOrder(Integer paramOrder) {
        this.paramOrder = paramOrder;
    }


    public String getDictType() {
        return dictType;
    }


    public void setDictType(String dictType) {
        this.dictType = dictType;
    }


    public String getParamValue() {
        return paramValue;
    }


    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }


    public String getDisplayName() {
        return displayName;
    }


    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

}
