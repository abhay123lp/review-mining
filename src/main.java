import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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
		
		//DBConnector db = new DBConnector();
		//db.sql("select * from reviews_review limit 2");
		
		//DAO dao = new DAO();
		//ArrayList<Review> reviews = dao.getReviews(404, 0, 100, 1);
		//testTagging();
		
		DAO dao = new DAO();
		//ArrayList<Review> reviews = dao.getReviewsByStoreId(404, 0, 100, 1);
		//System.out.println(dao.getStoreIds().size());
		//System.out.println(dao.getReviewerIds().size());
		//System.out.println(dao.getReviewerIds(40, 70));
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
			FileWrite fw = new FileWrite("test_output.txt");
			//Tagging t = new Tagging();
			SO so = new SO();
			
			ArrayList<Review> reviews = dao.getReviewsByStoreId(404, 0, 100, 1);
			for (Review review: reviews) {
				ArrayList<String> phrases = so.getTwoWordPhrases(review.text);
				fw.writeLine(review.text + "\n" + phrases.toString());
				int i=0, k, sum=0;
				for (String s: phrases) {
					k = so.getPolarityOfPhrase(s);
					fw.writeLine(s + " : " + k );
					sum += k;
					i++;
				}
				fw.writeLine("rating: "+ review.rating + " " + 
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
}
