import java.sql.ResultSet;
import java.util.ArrayList;


public class DAO {
	
	DBConnector db;
	public DAO() {
		db = new DBConnector();
	}
	/**
	 * 
	 * @param storeId : of the store, 0 selects all stores 
	 * @param limitIndex : start position of limiting data rows
	 * @param limitCount : number of rows to fetch
	 * @param skipUserWithCount : skip users having less than this count
	 * @return ArrayList of Review
	 */
	public ArrayList<Review> getReviews (int storeId, int limitIndex, int limitCount, int skipUserWithCount)
	{
		ArrayList<Review> reviews = new ArrayList<Review>();
		String query = "select * from reviews_review " +
				"where " +
					( storeId > 0 ? "store_id = " + storeId : "")+
					"reviewer_id in " +
						"(select id from reviews_reviewer where review_count > "+skipUserWithCount+") " +
				"order by id " +
				"limit " + limitIndex + "," + limitCount;
		ResultSet rs = db.sql(query);
		
		try {
			while(rs.next()) {
				Review review = new Review();
				
				review.id = rs.getInt(1);
				review.reviewerId = rs.getInt(2);
				review.storeId = rs.getInt(3);
				review.date = rs.getDate(4);
				review.rating = rs.getInt(5);
				review.text = rs.getNString(6);
				review.dateModified = rs.getDate(7);
				review.siteId = rs.getInt(8);
				review.duplicateCount = rs.getInt(9);
				review.isModified = rs.getBoolean(10);
				review.topReviewer = rs.getBoolean(11);
				
				reviews.add(review);
			}
		} catch( Exception e ) {
			System.err.println("Error reading resultset in DAO.etReviews(): " + e.toString());
		}
		
		return reviews;
	}
}
