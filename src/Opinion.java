import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


public class Opinion {
	public Store store;
	public double SO;
	public Reviewer reviewer;
	public int opinion;
	
	public void calc_SO(){
		
	}
	
	public void get_avg_SO(){
		
	}
	
	public int getOpinion(int storeid){
		return opinion;
	}
	
	public long getCntFromGoogle(String keyword){
		try {
			
			String key = URLEncoder.encode(keyword, "UTF-8");
			URL url = new URL("http://www.google.com/search?hl=en&q="+key+"&aq=f&oq=&aqi=g10");
            URLConnection conn =  url.openConnection();
            conn.setRequestProperty("User-Agent",
                            "Mozilla/5.0 (X11; U; Linux x86_64; en-GB; rv:1.8.1.6) Gecko/20070723 Iceweasel/2.0.0.6 (Debian-2.0.0.6-0etch1)");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream())
            );
            String str;
            String content ="";
            while ((str = in.readLine()) != null) {
            	content += str;
            }

            in.close();
            
            Document doc = null;
        	doc =  Jsoup.parse(content);
        	//doc.body().toString();
        	//System.out.println(Jsoup.clean(str, myWhitelist));
        	
        	//System.out.println(doc.body().select("table tr td div#subform_ctrl").toString());
        	Elements newsHeadlines = doc.body().select("table tr td div#subform_ctrl");
        	String cnt = newsHeadlines.select("div > div:eq(1)").text().toString();
        	String delims = "[ ]";
        	String[] tokens = cnt.split(delims);
        	return Long.parseLong(tokens[1].replace(",", ""));
    }
    catch (MalformedURLException e) {}
    catch (IOException e) {}
		return 0;
		
	}
}
