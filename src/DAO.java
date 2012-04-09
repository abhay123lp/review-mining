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
				" where " +
					( storeId > 0 ? "store_id = " + storeId + " and ": "")+
					" reviewer_id in " +
						"(select id from reviews_reviewer where review_count > "+skipUserWithCount+") " +
				"order by id " +
				"limit " + limitIndex + "," + limitCount;
		System.out.println(query);
		ResultSet rs = db.sql(query);
		
		try {
			while(rs.next()) {
				Review review = new Review();
				
				review.id = rs.getInt("id");
				review.reviewerId = rs.getInt("reviewer_id");
				review.storeId = rs.getInt("store_id");
				review.date = rs.getDate("date");
				review.rating = rs.getInt("rating");
				review.text = rs.getString("text");
				review.dateModified = rs.getDate("date_modified");
				review.siteId = rs.getInt("site_id");
				review.duplicateCount = rs.getInt("duplicate_count");
				review.isModified = rs.getBoolean("is_modified");
				review.topReviewer = rs.getBoolean("top_reviewer");
				
				reviews.add(review);
			}
		} catch( Exception e ) {
			System.err.println("Error reading resultset in DAO.etReviews(): " + e.toString());
		}
		
		return reviews;
	}
}
