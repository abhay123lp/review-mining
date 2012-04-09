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
		System.out.println("Hello,world!");
		//DBConnector db = new DBConnector();
		//db.sql("select * from reviews_review limit 2");
		
		DAO dao = new DAO();
		//ArrayList<Review> reviews = dao.getReviewsByStoreId(404, 0, 100, 1);
		//System.out.println(dao.getStoreIds().size());
		//System.out.println(dao.getReviewerIds().size());
		System.out.println(dao.getReviewerIds(40, 70));
	}

}
