import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.google.code.bing.search.client.BingSearchClient;
import com.google.code.bing.search.client.BingSearchClient.SearchRequestBuilder;
import com.google.code.bing.search.client.BingSearchServiceClientFactory;
import com.google.code.bing.search.schema.AdultOption;
import com.google.code.bing.search.schema.SearchOption;
import com.google.code.bing.search.schema.SearchResponse;
import com.google.code.bing.search.schema.SourceType;
import com.google.code.bing.search.schema.web.WebResult;
import com.google.code.bing.search.schema.web.WebSearchOption;


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
	public long getCntFromBing(String keyword){
		if(keyword.isEmpty())
			return 0;
		BingSearchServiceClientFactory factory = BingSearchServiceClientFactory.newInstance();
		BingSearchClient client = factory.createBingSearchClient();

		SearchRequestBuilder builder = client.newSearchRequestBuilder();
		builder.withAppId("BE8C2F9F7235DF9D96D54500BE8611952EF0D987");
		builder.withQuery(keyword);
		builder.withSourceType(SourceType.WEB);
		builder.withVersion("2.0");
		builder.withMarket("en-us");
		builder.withAdultOption(AdultOption.MODERATE);
		builder.withSearchOption(SearchOption.ENABLE_HIGHLIGHTING);

		builder.withWebRequestCount(10L);
		builder.withWebRequestOffset(0L);
		builder.withWebRequestSearchOption(WebSearchOption.DISABLE_HOST_COLLAPSING);
		builder.withWebRequestSearchOption(WebSearchOption.DISABLE_QUERY_ALTERATIONS);

		SearchResponse response = client.search(builder.getResult());
		long total = 0L;
		if(!response.getWeb().getResults().isEmpty())
			total = response.getWeb().getTotal();
		
		System.out.println("total: "+total);
		try {
			Thread.sleep(140);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//for (WebResult result : response.getWeb().getResults()) {
		        //System.out.println(result.getTitle());
		      //  System.out.println(result.getDescription());
		    //    System.out.println(result.getUrl());
		  //      System.out.println(result.getDateTime());
		//}
		return total;	
	}
	public long getCntFromGoogle(String keyword){
		try {
			
			String key = URLEncoder.encode(keyword, "UTF-8");
			URL url = new URL("http://www.google.com/search?hl=en&q="+key+"&aq=f&oq=&aqi=g10");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            
            conn.setRequestProperty("User-Agent",
                            "Mozilla/5.0 (X11; U; Linux x86_64; en-GB; rv:1.8.1.6) Gecko/20070723 Iceweasel/2.0.0.6 (Debian-2.0.0.6-0etch1)");
            int status = conn.getResponseCode();
            System.out.println(status);
            if (status != 200)
            {
            	try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	return getCntFromGoogle(keyword);
            }
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
        	String delims = "[ ]+";
        	String[] tokens = cnt.split(delims);
        	String finalcnt ="";
        	if(tokens.length < 3)
        		return 0;
        	if(tokens[1].contains(","))
        		finalcnt =tokens[1].replace(",", "");
        	else
        		finalcnt =tokens[1];
        	if(finalcnt.trim().isEmpty())
        		return 0;
        	return Long.parseLong(finalcnt);
    }
    catch (MalformedURLException e) {
    	e.printStackTrace();
    }
    catch (IOException e) {
    	e.printStackTrace();
    }
		return 0;
		
	}
}
