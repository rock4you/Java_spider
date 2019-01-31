import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Test {
	public static void main(String[] args) throws IOException {
		String url = "https://www.wandoujia.com/top/app";
		Connection con = Jsoup.connect(url).timeout(30000);
		con.userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3192.0 Safari/537.36");
		Document doc = con.get();
				
		Elements appName = doc.select("h2[class=app-title-h2]");
		String nameList = appName.text();
		String[] name = nameList.split(" ");
		int nameLen = name.length;
		
		Elements downCount = doc.select("span[class=install-count]");
		String downList = downCount.text();
		String[] down = downList.split(" ");
		//int downLen = down.length;		
		
		Elements appType = doc.select("a[class=tag-link]");
		String typeList = appType.text();
		String[] type = typeList.split(" ");
		//int typeLen = type.length;		
		
		int i;
		for (i=0;i<nameLen;++i) {
			System.out.println(name[i]+" "+down[i]+" "+type[i]);
		}		
	}
}
