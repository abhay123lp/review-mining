import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;


/**
 * 
 */

/**
 * @author shaoxinjiang
 *
 */
public class main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.Jdk14Logger");
		//testCrawler();
		try {
			testSearch();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*Document doc = null;
		try {
			doc = Jsoup.connect("http://www.google.com/search?hl=en&q=ipod&aq=f&oq=&aqi=g10").userAgent("Mozilla/5.0 (X11; U; Linux x86_64; en-GB; rv:1.8.1.6) Gecko/20070723 Iceweasel/2.0.0.6 (Debian-2.0.0.6-0etch1)").get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Elements newsHeadlines = doc.select("resultStats");
		System.out.println(newsHeadlines.toString());*/
		
		DAO dao = new DAO();
		testTwoWordPhrase();

	}
	public static void testTagging(){
		// test Tagging 
		//Tagging t = new Tagging() ;
		
		/*try {
			t.test();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		System.out.println(
		//		t.getTagging("It's a very good morning's tea. It is.", "")
				);

	}

	
	public static void testTwoWordPhrase() {
		DAO dao = new DAO();
		int count = 0;
		//Timestamp t = 
		try {
			FileWriter fw = new FileWriter("test_output.txt");
			//Tagging t = new Tagging();
			SO so = new SO();
			
			ArrayList<Review> reviews = dao.getReviewsByStoreId(404, 0, 100, 1);
			for (Review review: reviews) {
				ArrayList<String> phrases = so.getTwoWordPhrases(review.text);
				fw.write(review.text + "\n" + phrases.toString());
				int i=0, k, sum=0;
				for (String s: phrases) {
					k = so.getPolarityOfPhrase(s);
					fw.write(s + " : " + k );
					sum += k;
					i++;
				}
				fw.write("rating: "+ review.rating + " " + 
							(i > 0 ?(1.0*sum/i):0));
				System.out.println("... processed " + ++count + " ...");
				if (count > 50) break; // more causes memory overflow
			}
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	public static long testSearch() throws IOException{
		
            try {
                    URL url = new URL("http://www.google.com/search?hl=en&q=ipod&aq=f&oq=&aqi=g10");
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
                		//System.out.println(newsHeadlines.toString());
                		//System.out.println(str);
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
