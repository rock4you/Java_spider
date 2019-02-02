package com.mytest.hello;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

//爬取豌豆荚app首页列出的的app信息
public class MyTest {
	public static void main(String[] args) throws IOException  {
		int downloaded = 1;//如果文档已经下载过了，这里可以将下面获取html的部分跳过
		if(downloaded == 0)
		{
    //通过分析网页内容，发现该网页由22个page组成，所以通过循环的方式将每个page都下载下来。
		String URLhead = "https://www.wandoujia.com/wdjweb/api/top/more?resourceType=0&page=";
		String URLend  = "&ctoken=lXCYFaegUjwiufv7GEXbIGPe";
		PrintWriter pw = null;
		pw = new PrintWriter(new FileWriter("URL.txt"), true);
		String URL = null;
		for (int page =0;page<22;++page) {
			URL = URLhead + page + URLend;
			CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build()).build();
			HttpGet httpGet = new HttpGet(URL);
			CloseableHttpResponse response =  httpClient.execute(httpGet);
	        if (response != null){
	            HttpEntity entity =  response.getEntity();
	            String result = EntityUtils.toString(entity, "UTF-8");
	            pw.write(result);
	        }
	        if (response != null){
	            response.close();
	        }
	        if (httpClient != null){
	            httpClient.close();
	        }
		}
		pw.close();
		}
		System.out.println("downloaded.");
		
		//问题1：按照上述方法写到txt文件后，双引号会多出一个转义字符\，需要先打开txt文件手工将\"全部替换为"再运行这个程序		
		File in = new File("URL.txt");
		Document doc = Jsoup.parse(in, "UTF-8", ""); 
		Elements appName = doc.select("h2[class=app-title-h2]");
			
		List<String> nameList = appName.eachText();//这里最好用一个字典或集合来处理，可以过滤同名app
		//String[] name = nameList.split(" "); //如果用appName.text配合nameList.split(" ")，当app名称含有空格时，会出现bug.所以这里用eachText转为List进行处理
		int nameLen = nameList.size();
		System.out.println(nameLen);
		
		Elements downCount = doc.select("span[class=install-count]");
		List<String> downList = downCount.eachText();		             
		int downLen = downList.size();		
		System.out.println(downLen);
		
		Elements appType = doc.select("a[class=tag-link]");
		List<String> typeList = appType.eachText();
		int typeLen = typeList.size();
		System.out.println(typeLen);
		
		int i;
		PrintWriter pw2 = null;
		pw2 = new PrintWriter(new FileWriter("out.txt"), true);
		for (i=0;i<typeLen;++i) {
			//System.out.println(name[i]+" "+down[i]+" "+type[i]);
			pw2.write(nameList.get(i)+" "+downList.get(i)+" "+typeList.get(i)+"\n");
		}
		pw2.close();
		System.out.println("OK.");//问题2：后续还要处理重复项目的问题
	}
}
