package com.util;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.vo.SpiderProductDTO;
import com.vo.SpuGroupInTab;
import com.vo.SpuStandardInfoUnits;
import com.vo.TaobaoProductVO;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 抓取淘宝天猫详情
 * 
 * @author lsq
 *
 */
public class SeleniumUtils {

	/**
	 * 调测日志记录器。
	 */
	private static final Logger logger = LoggerFactory.getLogger(SeleniumUtils.class);

	//TODO:这里需要注意
	public static String seleniumParam = "/opt/firefox/firefox";  //linux
	//public static String seleniumParam = "D:/Program Files/Mozilla Firefox/firefox.exe";  //windows
	
	public static void main(String[] args) throws Exception {
		// String url = "https://item.taobao.com/item.htm?spm=a230r.1.14.45.BGfg0B&id=537219009724&ns=1&abbucket=10";
		String url = "https://item.taobao.com/item.htm?spm=a230r.1.14.263.iHx7Fx&id=529043267302&ns=1&abbucket=7";
		// url = "https://detail.tmall.com/item.htm?id=539341627790&spm=a230r.7195193.1997079397.7.NJq7Qt&abbucket=4&sku_properties=5919063:6536025;12304035:3222911;122216431:27772https://detail.tmall.com/item.htm?id=539341627790&spm=a230r.7195193.1997079397.7.NJq7Qt&abbucket=4&sku_properties=5919063:6536025;12304035:3222911;122216431:27772";
		url = "https://detail.tmall.com/item.htm?spm=a1z10.3-b-s.w4011-14765528127.81.nDDDdn&id=541888888509&rn=e25fa2d67951f4a7aa2845ca4914382b&abbucket=16&sku_properties=5919063:6536025;12304035:116177;122216431:27772";

		/*
		 * List<String> result = getProductDetailImgByUrl(url); for(String s :
		 * result) { logger.info(s); }
		 */

		commonMain(url);
	}

	/**
	 * 抓取的入口
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static SpiderProductDTO commonMain(String url) throws Exception {
		logger.info("抓取的入口,url:" + url);
		Document doc = getDocumentByUrl(url);

		SpiderProductDTO vo = new SpiderProductDTO();
		if (url.contains("detail.tmall.com")) {
			vo = getTianMaoMessage(doc, url);
		} else if (url.contains("item.taobao.com")) {
			vo = getTaoBaoMessage(doc, url);
		}

		return vo;
	}

	/**
	 * 通过url获取document
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static Document getDocumentByUrl(String url) throws Exception {
		WebDriver webDriver = null;
		Document doc = null;

		try {
			webDriver = createWebDriver();

			webDriver.get(url);

			if (url.contains("tmall")) { // 天猫：滑动到最下面是id为footer的元素；淘宝：滑动到最下面是class为footer的元素
				WebElement webElement = webDriver.findElement(By.id("footer"));
				Thread.sleep(5000);
				logger.info("webElement:" + webElement);

				new Actions(webDriver).moveToElement(webElement).perform();
			} else {
				new Actions(webDriver).moveToElement(
						webDriver.findElement(By.className("tb-footer")))
						.perform();

			}

			String htmlStr = webDriver.getPageSource();
			logger.info("--------------------------------------webDriver:" + webDriver);
			logger.info("--------------------------------------htmlStr:" + htmlStr);

			// 转为doc
			doc = Jsoup.parse(htmlStr);
			logger.info("--------------------------------------doc:" + doc);

		} catch (Exception e) {
			e.printStackTrace();
			logger.info("--------------------------------------getDocumentByUrl接口出错！" + e.getMessage(),e);
		} finally {
			if (webDriver != null) {
				webDriver.close();
			}
		}

		return doc;
	}

	/**
	 * 抓取天猫的商品
	 * 
	 * @param htmlStr
	 * @return
	 * @throws Exception
	 */
	public static SpiderProductDTO getTianMaoMessage(Document doc, String url)
			throws Exception {
		SpiderProductDTO vo = new SpiderProductDTO();
		List<String> productColors = new ArrayList<String>(); // 颜色
		List<String> productMemorys = new ArrayList<String>(); // 内存
		List<String> productParams = new ArrayList<String>(); // 参数
		List<String> productImgs = new ArrayList<String>(); // 图片
		List<String> productDetailList = new ArrayList<String>(); // 详情

		// 价格
		Elements priceEles = doc.select(".tm-promo-price").select(".tm-price");
		String productPrice = priceEles.text();
		if (StringUtils.isBlank(productPrice)) {
			Elements tempPriceEles = doc.select(".tm-price").eq(0);
			productPrice = tempPriceEles.text();
		}
		vo.setProductPrice(productPrice);

		// 获取机身颜色和机身内存
		Elements skinEles = doc.select(".tb-skin").eq(0);

		Elements dls = skinEles.select("dl");
		for (Element dl : dls) {
			Elements uls = dl.getElementsByTag("ul");
			for (Element ul : uls) {
				String type = ul.attr("data-property"); // 类型
				if (type.equals("机身颜色") || type.equals("颜色分类")) {
					Elements lis = ul.select("li");
					for (Element li : lis) {
						String styleStr = li.attr("style");
						if (styleStr.contains("display: none")) {
							continue;
						}
						Elements span = li.select("span").eq(0);
						String text = span.text();
						productColors.add(text);
					}
				}

				if (type.equals("机身内存") || type.equals("存储容量")) {
					Elements lis = ul.select("li");
					for (Element li : lis) {
						Elements span = li.select("span").eq(0);
						String text = span.text();
						productMemorys.add(text);
					}
				}
			}
		}

		// 抓取参数
		Elements paramEles = doc.select("#J_AttrUL").eq(0);
		Elements paramlis = paramEles.select("li");
		for (Element sigleli : paramlis) {
			String paramStr = sigleli.text();
			productParams.add(paramStr);
		}

		// 抓取图片
		Elements pictureEles = doc.select("#J_UlThumb").eq(0);
		Elements picturelis = pictureEles.select("li");
		for (Element pictureli : picturelis) {
			Elements picturea = pictureli.getElementsByTag("a");
			for (Element pictureimg : picturea) {
				Elements imgs = pictureimg.getElementsByTag("img");
				for (Element imgEle : imgs) {
					String img = imgEle.attr("src");
					img = img.replaceAll("60x60q90", "300x300q90"); // 改变图片的大小
					if (!img.equals("")) {
						productImgs.add(img);
					}
				}
			}
		}

		// 抓取商品详情
		productDetailList = getProductDetailImg(doc, url);

		vo.setProductColors(productColors);
		vo.setProductMemorys(productMemorys);
		vo.setProductParams(productParams);
		vo.setProductImgs(productImgs);
		vo.setProductDetailList(productDetailList);
		
		logger.info("--------------------------------------颜色：" + productColors);
		logger.info("--------------------------------------内存：" + productMemorys);
		logger.info("--------------------------------------参数：" + productParams);
		logger.info("--------------------------------------商品图片：" + productImgs);
		logger.info("--------------------------------------商品详情：" + productDetailList);
		return vo;
	}

	/**
	 * 抓取淘宝的商品
	 * 
	 * @param htmlStr
	 * @return
	 * @throws Exception
	 */
	public static SpiderProductDTO getTaoBaoMessage(Document doc, String url)
			throws Exception {
		SpiderProductDTO vo = new SpiderProductDTO();
		List<String> productColors = new ArrayList<String>(); // 颜色
		List<String> productMemorys = new ArrayList<String>(); // 内存
		List<String> productParams = new ArrayList<String>(); // 参数
		List<String> productImgs = new ArrayList<String>(); // 图片
		List<String> productDetailList = new ArrayList<String>(); // 详情

		// 获取价格(淘宝价格可以抓取到)
		Elements priceEles = doc.select(".tb-rmb-num").eq(0);
		String productPrice = priceEles.text();
		productPrice = productPrice.replaceAll(" ", "");
		vo.setProductPrice(productPrice);

		// 获取机身颜色和机身内存
		Elements skinEles = doc.select(".tb-skin").eq(0);

		Elements dls = skinEles.select("dl");
		for (Element dl : dls) {
			Elements uls = dl.getElementsByTag("ul");
			for (Element ul : uls) {
				String type = ul.attr("data-property"); // 类型
				if (type.equals("机身颜色") || type.equals("颜色分类")) {
					Elements lis = ul.select("li");
					for (Element li : lis) {
						Elements span = li.select("span").eq(0);
						String text = span.text();
						productColors.add(text);
					}
				}

				if (type.equals("机身内存") || type.equals("存储容量")) {
					Elements lis = ul.select("li");
					for (Element li : lis) {
						Elements span = li.select("span").eq(0);
						String text = span.text();
						productMemorys.add(text);
					}
				}
			}
		}

		// 获取参数
		// Elements paramuls =
		// doc.getElementsByClass("attributes-list");tb-attributes-list
		Elements paramuls = doc.select(".attributes-list").eq(0);
		if (paramuls.size() > 0) {
			Elements lis = paramuls.select("li");
			for (Element li : lis) {
				String text = li.text();
				productParams.add(text);

			}
		}

		// 第2种解析的方式
		if (paramuls.size() == 0) {
			try {
				Elements scriptEle = doc.select("script");
				for (Element scriptE : scriptEle) {
					String scriptStr = scriptE.toString();
					if (scriptStr.contains("g_config.spuStandardInfo")
							&& scriptStr.contains("var g_config")) {

						int startIndex = scriptStr
								.indexOf("g_config.spuStandardInfo");
						int endIndex = scriptStr.indexOf("</script>");
						String jsonStr = scriptStr.substring(startIndex,
								endIndex);
						jsonStr = jsonStr
								.replace("g_config.spuStandardInfo = ", "")
								.replace("</script>", "").replace("};", "}");
						jsonStr = jsonStr.substring(0,
								jsonStr.lastIndexOf("}") + 1);
						// 解析json字符串tempStr
						JSONObject jsonObject1 = JSONObject
								.parseObject(jsonStr);
						// JSONArray temp1 =
						// (JSONArray)jsonObject1.get("spuGroupInTab");
						// TaobaoProductVO.
						TaobaoProductVO taobaoVO = Json2Entity(jsonObject1
								.toString());
						List<SpuGroupInTab> tabs = taobaoVO.getSpuGroupInTab();
						for (SpuGroupInTab tab : tabs) {
							List<SpuStandardInfoUnits> units = tab
									.getSpuStandardInfoUnits();
							for (SpuStandardInfoUnits unit : units) {
								String value = unit.getPropertyName() + ":"
										+ unit.getValueName();
								productParams.add(value);

							}
						}

					}
				}
			} catch (Exception e) {
				logger.info("--------------------------------------getTaoBaoMessage接口出错！" + e.getMessage(),e);
			}

		}

		// 抓取图片
		Elements pictureEles = doc.select("#J_UlThumb").eq(0);
		Elements picturelis = pictureEles.select("li");
		for (Element pictureli : picturelis) {
			// 需要过滤掉视频的情况
			Elements pictureSpan = pictureli.select(".tb-video-logo").eq(0);
			if (pictureSpan.size() > 0) { // 有的话，就跳过
				continue;
			}

			Elements picturea = pictureli.select("a");
			for (Element pictureimg : picturea) {
				Elements imgs = pictureimg.select("img");
				for (Element imgEle : imgs) {
					String img = imgEle.attr("src");

					if (img.contains("60x60q90")) {
						img = img.replaceAll("60x60q90", "300x300q90"); // 改变图片的大小
					}

					if (img.contains("50x50.")) {
						img = img.replaceAll("50x50.", "300x300."); // 改变图片的大小
					}

					if (img.equals("")) {
						img = imgEle.attr("data-src");
						img = img.replaceAll("50x50", "300x300"); // 改变图片的大小
					}

					if (!img.equals("")) {
						productImgs.add(img);
					}
				}
			}
		}

		// 抓取详情
		productDetailList = getProductDetailImg(doc, url);

		vo.setProductColors(productColors);
		vo.setProductMemorys(productMemorys);
		vo.setProductParams(productParams);
		vo.setProductImgs(productImgs);
		vo.setProductDetailList(productDetailList);
		return vo;
	}

	public static TaobaoProductVO Json2Entity(String json) {
		TaobaoProductVO vo = JSON.parseObject(json, TaobaoProductVO.class);
		return vo;
	}

	/**
	 * 根据doc获取商品详情图片
	 * 
	 * @param doc
	 * @return
	 */
	public static List<String> getProductDetailImg(Document doc, String url) {
		List<String> productDetailList = new ArrayList<String>();
		Element descEle = null;

		if (url.contains("tmall")) {
			descEle = doc.getElementById("description");
		} else {
			descEle = doc.getElementById("J_DivItemDesc");
		}

		logger.info("--------------------------------------descEle:" + descEle);
		if (descEle != null) {
			Elements imgs = descEle.getElementsByTag("img");
			for (Element imgEle : imgs) {
				String img = imgEle.attr("data-ks-lazyload");

				// 先只取出data-ks-lazyload里面的图片
				if (StringUtils.isNotBlank(img)) {
					productDetailList.add(img);
				} else { // 为空，就取src
					img = imgEle.attr("src");
					productDetailList.add(img);

				}
			}
		}

		return productDetailList;
	}

	/**
	 * 主要给HtmlUnit使用，通过url抓取详情数据
	 * 
	 * @param url
	 * @return
	 */
	public static List<String> getProductDetailImgByUrl(String url) {
		List<String> productDetailList = new ArrayList<String>();
		WebDriver webDriver = null;

		try {
			webDriver = createWebDriver();

			webDriver.get(url);

			List<WebElement> webElements = new ArrayList<WebElement>();
			if (url.contains("tmall")) { // 天猫：滑动到最下面是id为footer的元素；淘宝：滑动到最下面是class为footer的元素
				new Actions(webDriver).moveToElement(
						webDriver.findElement(By.id("footer"))).perform();
				WebElement webElement = webDriver.findElement(By
						.id("description"));
				webElements = webElement.findElements(By.tagName("img"));

			} else {
				new Actions(webDriver).moveToElement(
						webDriver.findElement(By.className("footer")))
						.perform();
				WebElement webElement = webDriver.findElement(By
						.id("J_DivItemDesc"));
				webElements = webElement.findElements(By.tagName("img"));

			}

			// 等待时间
			// webDriver.manage().timeouts().implicitlyWait(30,
			// TimeUnit.SECONDS);

			String imgHtmlStr = "";
			for (WebElement we : webElements) {
				imgHtmlStr += we.getAttribute("outerHTML");
			}
			// logger.info(imgHtmlStr);

			if (StringUtils.isNotBlank(imgHtmlStr)) {
				Document doc = Jsoup.parse(imgHtmlStr);
				Elements imgs = doc.getElementsByTag("img");
				for (Element imgEle : imgs) {
					String img = imgEle.attr("data-ks-lazyload");

					// 先只取出data-ks-lazyload里面的图片
					if (StringUtils.isNotBlank(img)) {
						productDetailList.add(img);
					} else { // 为空，就取src
						img = imgEle.attr("src");
						productDetailList.add(img);

					}
				}
			}

		} catch (Exception e) {
			logger.info("--------------------------------------getProductDetailImgList接口出错！" + e.getMessage(),e);

		} finally {
			if (webDriver != null) {
				webDriver.close();
			}
		}

		return productDetailList;
	}

	/**
	 * 创建webDriver
	 * 
	 * @throws Exception
	 */
	public static WebDriver createWebDriver() throws Exception {
		String seleniumSwitch = "1"; // 1.本地
																			// 2.远程
		logger.info("seleniumParam值为:" + seleniumParam);
		logger.info("seleniumSwitch值为:" + seleniumSwitch);

		WebDriver webDriver = null;
			
		/**
		 * chrome
		 */
		// System.setProperty("webdriver.chrome.driver",
		// "D:/tmp/chromedriver.exe");
		// System.setProperty("webdriver.chrome.driver",
		// "C:/Program Files (x86)/Google/Chrome/Application/chrome.exe");
		// webDriver = new ChromeDriver();
		
		/**
		 * firefox
		 */
		if (StringUtils.isBlank(seleniumSwitch) || seleniumSwitch.equals("1")) {
			System.setProperty("webdriver.firefox.bin", seleniumParam); //linux版本
			
			// 加载Firefox默认配置
			ProfilesIni pi = new ProfilesIni();
			FirefoxProfile profile = pi.getProfile("default");
			// 启动Firefox浏览器
			
			webDriver = new FirefoxDriver(profile);
			
		} else { // 远程(在另一台电脑上打开本机即可 )
			
			DesiredCapabilities ffCapabilities = DesiredCapabilities.firefox();
			ffCapabilities.setPlatform(Platform.LINUX);
			ffCapabilities.setBrowserName("firefox");
			webDriver = new RemoteWebDriver(new URL(seleniumParam),ffCapabilities);
			
		}
		
		/**
		 * 远程(在另一台电脑上打开本机即可 )
		 */
		/*
		 * DesiredCapabilities ffCapabilities = DesiredCapabilities.firefox();
		 * ffCapabilities.setPlatform(Platform.LINUX);
		 * ffCapabilities.setBrowserName("firefox");
		 * ffCapabilities.setVersion("45.0"); webDriver = new
		 * RemoteWebDriver(new URL("http://192.168.1.7:4444/wd/hub"),
		 * ffCapabilities);
		 */

		return webDriver;
	}

}
