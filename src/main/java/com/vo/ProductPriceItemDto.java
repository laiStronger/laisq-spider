/*
 * 文件名：ProductPriceItemDto.java
 * 版权：Copyright 2014 youanmi Tech. Co. Ltd. All Rights Reserved. 
 * 描述： ProductPriceItemDto.java
 * 修改人：chenwenlong
 * 修改时间：2014年12月27日
 * 修改内容：新增
 */
package com.vo;

import java.io.Serializable;
import java.util.List;


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
public class ProductPriceItemDto implements Serializable {

    private Long paramId;

    private Long skuId;

    private Float itemPrice;

    private Integer quantity;

    private String valIds;

    private String values;

    private List<ProductPriceFactDto> facts;


    public String getValIds() {
        return valIds;
    }


    public void setValIds(String valIds) {
        this.valIds = valIds;
    }


    public Long getSkuId() {
        return skuId;
    }


    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }


    public Float getItemPrice() {
        return itemPrice;
    }


    public void setItemPrice(Float itemPrice) {
        this.itemPrice = itemPrice;
    }


    public Integer getQuantity() {
        return quantity;
    }


    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }


    public List<ProductPriceFactDto> getFacts() {
        return facts;
    }


    public void setFacts(List<ProductPriceFactDto> facts) {
        this.facts = facts;
    }


    public Long getParamId() {
        return paramId;
    }


    public void setParamId(Long paramId) {
        this.paramId = paramId;
    }


    public String getValues() {
        return values;
    }


    public void setValues(String values) {
        this.values = values;
    }

}
