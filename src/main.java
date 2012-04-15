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
	 * step 1: get rid of data which are likely to be fake
	 * step 2: tag position of speech
	 * step 3: search phrase for the count number in google.com
	 * step 4: calc SO 
	 * step 5: get the opinion and compare with rating
	 * step 6: calc recall and precision
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		/*DAO dao = new DAO();
		try {
			int cnt = 0;
			SO so = new SO();
			ArrayList<Review> reviews = dao.getReviewsByStoreId(404, 0, 1, 1);
			for (Review review: reviews) {
				ArrayList<String> phrases = so.getTwoWordPhrases(review.text);
				System.out.println("count: "+cnt);
				double sum = 0.0;
				for (String phrase: phrases){
					System.out.println("phrase: "+ phrase);
					if(phrase.contains("/") || phrase.contains(","))
						continue;
					Opinion opinion = new Opinion();
					long postive = opinion.getCntFromGoogle(phrase + " excellent");
					long excellent = opinion.getCntFromGoogle("excellent");
					long negtive = opinion.getCntFromGoogle(phrase + " poor");
					long poor = opinion.getCntFromGoogle("poor");
					double score = Math.log((double)(postive*poor)/(double)(negtive*excellent));
					sum += score;
					System.out.println(postive +"\t"+excellent+"\t"+negtive+"\t"+poor+"\tscore: "+score);
				}
				cnt++;
				System.out.println("averge score: "+ (double)sum/cnt);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		
		//testGoogleSearchResult();
		//testTagging();
		testTwoWordPhrase();
	}
	
	public static void testTagging(){
		
		try {
			// test Tagging 
			Tagging t = new Tagging() ;
			//t.test();
			System.out.println(
					t.getTagging("It's a very good , morning's tea. It is.", "")
					);
			t.getTaggingSplittedSentence("It's a very good , morning's tea. It is.");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
	public static void testTwoWordPhrase() {
		DAO dao = new DAO();
		int count = 0, reviewcount=0;
		int quota = 50;
		//Timestamp t = 
		try {
			ArrayList<Integer> reviewIds = dao.getIdsByVeryCustomQuery();
			FileWrite phraseFile = new FileWrite("reviewPhrases.txt");
			FileWrite ratingsFile = new FileWrite("ratings.txt");
			FileWrite fw = new FileWrite("test_output_0.txt");
			while (true) {
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
						int i=0, k, sum=0;
						fw.writeLine(review.text + "\r\n" + tokenizeds);
						for (ArrayList sentenceTokens: tokenizeds) { 
							phrases = so.getTwoWordPhrases(sentenceTokens);
							fw.writeLine(phrases.toString());
							phraseList.add(phrases);
							for (String s: phrases) {
								k = so.getPolarityOfPhrase(s);
								fw.writeLine(s + " : " + k );
								sum += k;
								i++;
							}
						}
						double cr = (i > 0 ?(1.0*sum/i):0);
						fw.writeLine("rating: "+ review.rating + " " + cr);
						phraseFile.writeLine(review.id + "::"
								+ review.rating + "::"
								+ phraseList.toString());
		
						ratingsFile.writeLine(review.id + " " + review.rating + " " + cr + " "
								+ ((review.rating > 3) ? (cr > 0 ? 0 : 3): (cr < 0 ? 1 : 2) ));
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
}
