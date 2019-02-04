package com.mytest.hello;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

/*
* 爬取豌豆荚app首页列出的的app信息
* 在eclipse下创建Maven项目，并导入htmlunit, httpclient和jsoup
*/

public class MyTest {
	public static void main(String[] args) throws IOException  {
		int isdebuf = 1; //是否打印调试信息
		String webFile = "web.txt";//将网页存储到指定文件中
		String outFile = "out.txt";//将由webFile中提取出来的APP信息存储到outFile中
		
		int downloaded = 0;//如果网页文件尚未下载，则下载豌豆荚网页，保存到web.txt
		
		if(downloaded == 0)
		{
			//通过分析网页内容，发现该网页由22个page组成，所以通过循环的方式将每个page都下载下来。
			String URLhead = "https://www.wandoujia.com/wdjweb/api/top/more?resourceType=0&page=";
			String URLend  = "&ctoken=lXCYFaegUjwiufv7GEXbIGPe";
			PrintWriter pw = null;
			pw = new PrintWriter(new FileWriter(webFile), true);
			String URL = null;
			
			for (int page =0;page<22;++page) {
				URL = URLhead + page + URLend;//根据page的编号拼接出网页的实际地址
				CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build()).build();
				HttpGet httpGet = new HttpGet(URL);
				CloseableHttpResponse response =  httpClient.execute(httpGet);
		        if (response != null){
		            HttpEntity entity =  response.getEntity();
		            String result = EntityUtils.toString(entity, "UTF-8");
		            pw.write(result.replace("\\\"", "\""));//如果不replace直接写入文件，每个双引号前会多一个转义字符\
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
		if(isdebuf==1) {
			System.out.println("web done.");
		}
		
		File in = new File(webFile);//打开本地的文件
		Document doc = Jsoup.parse(in, "UTF-8", ""); //用jsoup处理本地的文件
		Elements appName = doc.select("h2[class=app-title-h2]");
			
		List<String> nameList = appName.eachText();//这里最好用一个字典或集合来处理，可以过滤同名app
		//如果用appName.text配合nameList.split(" ")，当app名称含有空格时，会出现bug.所以这里用eachText转为List进行处理
		int nameLen = nameList.size();
		
		Elements downCount = doc.select("span[class=install-count]");
		List<String> downList = downCount.eachText();		             
		int downLen = downList.size();
		
		Elements appType = doc.select("a[class=tag-link]");
		List<String> typeList = appType.eachText();
		int typeLen = typeList.size();
		
		if((nameLen != downLen)||(nameLen !=typeLen) )
		{//如果提取出来的app名称数、下载量标签数、类型标签数三者不等，则说明提取错误或网页不完整，直接报错退出。
			System.out.println("Error!");
			return ;
		}
		
		PrintWriter pw2 = null;
		pw2 = new PrintWriter(new FileWriter(outFile), true);
		HashMap<String, String> hashMap = new HashMap<String, String>();//使用字典避免重复项
		String key = null;
		String value=null;
		for (int i=0;i<typeLen;++i) {
			key = nameList.get(i);
			value=downList.get(i)+" "+typeList.get(i);
			hashMap.put(key, value);
		}
		for(String key2: hashMap.keySet()) {
			value = hashMap.get(key2);
			pw2.write(key2+" "+value+"\n");
		}
		pw2.close();
		if(isdebuf==1) {
			System.out.println("OK.");
		}
	}
}
