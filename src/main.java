import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import edu.stanford.nlp.ling.TaggedWord;


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
	 * step 1: get rid of data which are likely to be fake
	 * step 2: tag position of speech
	 * step 3: search phrase for the count number in google.com
	 * step 4: calc SO 
	 * step 5: get the opinion and compare with rating
	 * step 6: calc recall and precision
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//Opinion opinion2 = new Opinion();
		//opinion2.getCntFromTweeter("source:twitter4j yusukey");
		//runSentenceWiseAlgo();
	   
		//testGoogleSearchResult();
		testTwoWordPhrase();
	}
	public static void runSentenceWiseAlgo(){
		HashMap<Review, ArrayList<ArrayList<String>>> hm = new HashMap<Review, ArrayList<ArrayList<String>>>();
		
		try {
			BufferedWriter fw = new BufferedWriter(new FileWriter("output_result_sentencewise.txt"));
			SO so = new SO();
			hm = so.getTwoWordPhraseFromFile();
			Iterator entries = hm.entrySet().iterator();
			int amount = 0;
			Opinion opinion2 = new Opinion();
			long excellent = opinion2.getCntFromBing("excellent");
			long poor = opinion2.getCntFromBing("poor");
			
			while (entries.hasNext() && amount<100) {
				int cnt = 0;
				double sum=0.0;
				Map.Entry<Review, ArrayList<ArrayList<String>>> thisEntry = (Entry<Review, ArrayList<ArrayList<String>>>) entries.next();
				Review key = (Review)(thisEntry.getKey());
				ArrayList<ArrayList<String>> value =(ArrayList<ArrayList<String>>)(thisEntry.getValue());
				for (int i = 0; i < value.size(); i++){
					ArrayList<String> sentences = value.get(i);
					for (String phrase : sentences){
						System.out.println(phrase.trim());
						Opinion opinion = new Opinion();
						long postive = opinion.getCntFromBing(phrase.trim() + " excellent");
						long negtive = opinion.getCntFromBing(phrase.trim() + " poor");
						double score = Math.log((double)(postive*poor)/(double)(negtive*excellent+0.001));
						sum += score;
						cnt++;
					}
				}
					
				amount++;
				System.out.println(" "+key.id+","+key.rating+","+(double)sum/cnt);
				fw.write(key.id+","+key.rating+","+(double)sum/cnt+"\r\n");
			}
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void runNonSentenceSplitAlgo(){
		HashMap<Review, ArrayList<String>> hm = new HashMap<Review, ArrayList<String>>();
		
		try {
			BufferedWriter fw = new BufferedWriter(new FileWriter("output_result.txt"));
			
			SO so = new SO();
			//ArrayList<Review> reviews = dao.getReviewsByStoreId(404, 0, 1, 1);
			//ArrayList<String> phrases = null;
			
			hm = so.getTwoWordPhraseFromFile2();
			Iterator entries = hm.entrySet().iterator();
			int amount = 0;
			Opinion opinion2 = new Opinion();
			long excellent = opinion2.getCntFromBing("excellent");
			long poor = opinion2.getCntFromBing("poor");
			while (entries.hasNext() && amount<100) {
				
			  int cnt = 0;
			  Map.Entry<Review, ArrayList<String>> thisEntry = (Map.Entry<Review, ArrayList<String>>) entries.next();
			  Review key = (Review)(thisEntry.getKey());
			  ArrayList<String> value =(ArrayList<String>)(thisEntry.getValue());
			  double sum =0.0;
			  System.out.println("amount: "+amount);
			  // phrase

			  for (String phrase : value){
				  if(phrase.contains("/") || phrase.contains(",") || phrase.contains(".com") || phrase.contains("!"))
						continue;
				  System.out.print(phrase);
					Opinion opinion = new Opinion();
					long postive = opinion.getCntFromBing(phrase.trim() + " excellent");
					
					long negtive = opinion.getCntFromBing(phrase.trim() + " poor");
					
					double score = Math.log((double)(postive*poor)/(double)(negtive*excellent+0.001));
					//System.out.println(postive +"\t"+excellent+"\t"+negtive+"\t"+poor+"\tscore: "+score);
					sum += score;
					cnt++;
			  }
			  amount++;
			  System.out.println(" "+key.id+","+key.rating+","+(double)sum/cnt);
			  fw.write(key.id+","+key.rating+","+(double)sum/cnt+"\r\n");
			  
			}
			fw.close();
			/*for (Review review: reviews) {
				//ArrayList<String> phrases = so.getTwoWordPhrases(review.text);
				//System.out.println("count: "+cnt);
				double sum = 0.0;
				for (String phrase: phrases){
					//System.out.println("phrase: "+ phrase);
					if(phrase.contains("/") || phrase.contains(","))
						continue;
					Opinion opinion = new Opinion();
					long postive = opinion.getCntFromGoogle(phrase + " excellent");
					long excellent = opinion.getCntFromGoogle("excellent");
					long negtive = opinion.getCntFromGoogle(phrase + " poor");
					long poor = opinion.getCntFromGoogle("poor");
					double score = Math.log((double)(postive*poor)/(double)(negtive*excellent));
					sum += score;
					//System.out.println(postive +"\t"+excellent+"\t"+negtive+"\t"+poor+"\tscore: "+score);
				}
				cnt++;
				System.out.println(review.id+","+review.rating+","+(double)sum/cnt);
			}*/
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		int count = 0, reviewcount=0;
		int quota = 50;
		// Online from Xinjiang
		int amount = 0;
		Opinion opinion2 = new Opinion();
		long excellent = opinion2.getCntFromBing("excellent");
		long poor = opinion2.getCntFromBing("poor");
		//Timestamp t = 
		try {
			ArrayList<Integer> reviewIds = dao.getIdsByVeryCustomQuery();
			FileWrite phraseFile = new FileWrite("reviewPhrases.txt");
			FileWrite ratingsFile = new FileWrite("ratings.txt");
			FileWrite fw = new FileWrite("test_output_0.txt");
			while (true && reviewcount < 300) {
				int start = count * quota;
				if (start >= reviewIds.size()) break;
				int end = start + quota >= reviewIds.size() ? reviewIds.size()
															: start + quota; // end index is exclusive
				String idString = reviewIds.subList(start, end).toString().replace('[', ' ').replace(']', ' ');
				
				
				Tagging tagging = new Tagging();
				SO so = new SO();
				//int tp = 0, tn = 0, fp = 0, fn = 0;
				ArrayList<Review> reviews = dao.getReviewsHavingIdsInList(idString);
				for (Review review: reviews) {
					try {
						ArrayList<String> phrases = new ArrayList<String>();//so.getTwoWordPhrases(review.text);
						ArrayList<ArrayList> tokenizeds = tagging.getTaggingSplittedSentence(review.text);
						ArrayList<ArrayList> phraseList = new ArrayList<ArrayList>();
						int i=0, k, sum=0, multiplier=1;
						
						int cntSO = 0;  // for online SO from Xinjiang
						double sumSO=0.0;   // for online SO from Xinjiang
						
						fw.writeLine(review.text + "\r\n" + tokenizeds);
						for (ArrayList sentenceTokens: tokenizeds) { 
							phrases = so.getTwoWordPhrases2(sentenceTokens);
							// check But
							if (sentenceTokens.size() > 0) {
								TaggedWord tw = (TaggedWord) sentenceTokens.get(0);
								multiplier = tw.word().toLowerCase().equals("but")
										? -1 : 1;
							}
							fw.writeLine(phrases.toString());
							phraseList.add(phrases);
							for (String s: phrases) {
								k = so.getPolarityOfPhrase(s);
								fw.writeLine(s + " : " + k );
								sum += k * multiplier;
								i++;
								
								// SO online from Xinjiang
								Opinion opinion = new Opinion();
								long postive = opinion.getCntFromBing(s.trim() + " excellent");
								long negtive = opinion.getCntFromBing(s.trim() + " poor");
								double score = Math.log((double)(postive*poor)/(double)(negtive*excellent+0.001));
								sumSO += score * multiplier;
								cntSO++;
							}
						}
						double cr = (i > 0 ?(1.0*sum/i):0);
						double crSO = cntSO > 0 ? sumSO / cntSO : 0;
						fw.writeLine("rating: "+ review.rating + " " + cr);
						phraseFile.writeLine(review.id + "::"
								+ review.rating + "::"
								+ phraseList.toString());
		
						ratingsFile.writeLine(review.id + " " + review.rating + " " + cr + " "
								+ ((review.rating > 3) ? (cr > 0 ? 0 : 3): (cr < 0 ? 1 : 2) ) + " "
								+ crSO + " " + ((review.rating > 3) ? (crSO > 0 ? 0 : 3): (crSO < 0 ? 1 : 2) ) + " ");
						// tp 0, tn 1, fp 2, fn 3
						
						System.out.println("... processed " + ++reviewcount + " ... id: " + review.id);

						//if (count > 50) break; // more causes memory overflow
						//if (count % 50 == 0) so = new SO();
						
						if (count % 20 == 0) {
							fw.close(); // split file
							fw = new FileWrite("test_output_"+(count/20)+".txt");
						}
					}catch (Exception e) {
						System.err.println("error getting tagged: " + e.toString());
					}
				}
				count++; // count 50s
			}
			phraseFile.close();
			ratingsFile.close();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	
	public static void testGoogleSearchResult(){
		try {
			long cnt1 = testSearch();
			System.out.println(cnt1);
			Opinion o = new Opinion();
			long cnt2 = o.getCntFromGoogle("Xinjiang Shao");
			System.out.println(cnt2);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static long testSearch() throws IOException{
		
            try {
                    URL url = new URL("http://www.google.com/search?hl=en&q=ipod&aq=f&oq=&aqi=g10");
                    HttpURLConnection connect = (HttpURLConnection) url.openConnection();
                    connect.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; U; Linux x86_64; en-GB; rv:1.8.1.6) Gecko/20070723 Iceweasel/2.0.0.6 (Debian-2.0.0.6-0etch1)");
                    int status = connect.getResponseCode();
                    System.out.println(status);
                    if (status != 200)
                    {
                    	try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    	return testSearch();
                    }
                   // URLConnection conn =  url.openConnection();
                   // conn.setRequestProperty("User-Agent",
                    //                "Mozilla/5.0 (X11; U; Linux x86_64; en-GB; rv:1.8.1.6) Gecko/20070723 Iceweasel/2.0.0.6 (Debian-2.0.0.6-0etch1)");
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(connect.getInputStream())
                    );
                    String str;
                    String content ="";
                    while ((str = in.readLine()) != null) {
                    	content += str;
                		//System.out.println(newsHeadlines.toString());
                		//System.out.println(str);
                    }

                    in.close();
                    System.out.println(content);
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
            catch (MalformedURLException e) { e.printStackTrace(); }
            catch (IOException e) {}
            return -1;
	}
}
