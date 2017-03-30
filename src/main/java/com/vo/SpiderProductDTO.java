package com.vo;
import java.util.ArrayList;
import java.util.List;

/**
 * 淘宝封装实体
 * @author lsq
 *
 */
public class SpiderProductDTO {

	private Integer type;   //1. 2. 3.
	
	private List<String> productColors;   //商品颜色

	private List<String> productMemorys;  //商品内存
	
	private List<String> productParams;  //商品参数
	
	private List<String> productImgs;    //商品图片
	
	private List<String> productDetailList;    //商品详情（里面装图片）
	
	private String productPrice;  //商品价格

	//-----------------------返回----------------------------
	private String imageurls;         //柚安米自己的图片
	
	private List<ProductParamVO> param;     //参数
	
	private String productHtmlDetail;  //商品详情
	
	private List<ProductPriceItemDto> priceItem = new ArrayList<ProductPriceItemDto>();   //颜色内存条目
	
	
	
	
	public List<ProductParamVO> getParam() {
		return param;
	}

	public void setParam(List<ProductParamVO> param) {
		this.param = param;
	}

	public List<ProductPriceItemDto> getPriceItem() {
		return priceItem;
	}

	public void setPriceItem(List<ProductPriceItemDto> priceItem) {
		this.priceItem = priceItem;
	}

	public List<String> getProductDetailList() {
		return productDetailList;
	}

	public void setProductDetailList(List<String> productDetailList) {
		this.productDetailList = productDetailList;
	}

	public String getImageurls() {
		return imageurls;
	}

	public void setImageurls(String imageurls) {
		this.imageurls = imageurls;
	}

	public String getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(String productPrice) {
		this.productPrice = productPrice;
	}

	public String getProductHtmlDetail() {
		return productHtmlDetail;
	}

	public void setProductHtmlDetail(String productHtmlDetail) {
		this.productHtmlDetail = productHtmlDetail;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public List<String> getProductColors() {
		return productColors;
	}

	public void setProductColors(List<String> productColors) {
		this.productColors = productColors;
	}

	public List<String> getProductMemorys() {
		return productMemorys;
	}

	public void setProductMemorys(List<String> productMemorys) {
		this.productMemorys = productMemorys;
	}

	public List<String> getProductParams() {
		return productParams;
	}

	public void setProductParams(List<String> productParams) {
		this.productParams = productParams;
	}

	public List<String> getProductImgs() {
		return productImgs;
	}

	public void setProductImgs(List<String> productImgs) {
		this.productImgs = productImgs;
	}

	
	
	
}
