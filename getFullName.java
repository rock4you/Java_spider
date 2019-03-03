package com.mytest.hello;

//使用企业简称在企查查搜索，将获取到的第一个企业名称作为对应的全称

import java.util.List;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class MyTest{
	public static void main(String[] args) throws Exception {
        // 创建WebClient
        WebClient webClient = new WebClient();
		// 取消 JS 支持
        webClient.getOptions().setJavaScriptEnabled(false);
        // 取消 CSS 支持
        webClient.getOptions().setCssEnabled(false);
        // 获取指定网页实体
        HtmlPage page = (HtmlPage) webClient.getPage("https://www.qichacha.com/");
        // 获取搜索输入框
        HtmlInput input = (HtmlInput) page.getHtmlElementById("searchkey");
        String[] company ={"阿里巴巴"};//,"中国石化","华为","京东","中建集团","腾讯","菲尼克斯","富士康","百度","天马微电子","挖财","奇瑞汽车","58同城","小米","微店","滴滴","北京字节跳动","广东欧珀","优信二手车","恒生电子","华星光电","海康威视","新同花顺","钱多多","江吉利控股","网易","IBM","君纵达","便利蜂","点荣金融","哔哩哔哩","中国石油","中车株洲电力机车","招银网络科技","北京奇虎科技","中化集团","科大讯飞","蘑菇街","爱奇艺","汉能","新浪微博","中国银联","趣拿软件","拍拍贷","北汽新能源","vivo公司","通联支付","微软","同盾科技","壹账通","众马科技","微医","宜人贷","途牛科技","拉扎斯网络","融道网","三七互娱","杭州大搜车","搜狗","上海巨人网络","融360","美团","中国石油","捷普电子有限公司","恒大集团","远洋集团","上汽通用凯迪拉克","中国国际航空公司","中冶赛迪","凡普金科","三一重工","中芯国际","有赞","伯恩光学","有贝网络科技","大华技术","拼多多","三胞集团","叮叮网","北京趣拿软件","远景能源","广州汽车集团","中建钢构","中铁五局","上汽依维柯红岩","广联达"};
        int num = company.length;
        for(int j=0;j<num;++j)
        {
        	//Thread.sleep(5000);
        	//往输入框 “填值”
	        input.setValueAttribute(company[j]);
	        // 获取搜索按钮
	        HtmlInput btn = (HtmlInput) page.getHtmlElementById("V3_Search_bt");
	        // “点击” 搜索
	        HtmlPage page2 = btn.click();
	        //System.out.println(page2.asXml());
	        // 选择元素
	        List<HtmlElement> spanList=(List)page2.getByXPath("//td/a");//("//h3[@class='res-title']/a");
	        for(int i=0;i<spanList.size();i++) {
	            // 输出新页面的文本
	            System.out.println( (i+1) +" "+spanList.get(i).asText());
	        }
        }
   }
}
