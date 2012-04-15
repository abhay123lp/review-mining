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
	public ArrayList<Review> getReviewsByStoreId (int storeId, int limitIndex, int limitCount, int skipUserWithCount)
	{
		String query = "select * from reviews_review " +
				" where " +
					( storeId > 0 ? "store_id = " + storeId + " and ": "")+
					" reviewer_id in " +
						"(select id from reviews_reviewer where review_count > "+skipUserWithCount+") " +
				"order by id " +
				"limit " + limitIndex + "," + limitCount;
		//System.out.println(query);
		return getReviews( query);
	}
	
	/**
	 * 
	 * @param reviewerId of the reviewer
	 * @param limitIndex start index to limit the row count, -1 to select all 
	 * @param limitCount is the number of rows to select from limitIndex
	 * @return ArrayList of Review
	 */
	public ArrayList<Review> getReviewsByReviewerId(int reviewerId, int limitIndex, int limitCount) {
		String query = "select * from reviews_review " +
				" where " +
					" reviewer_id = " + reviewerId +
				"order by id " +
				(limitIndex < 0 ? " limit " + limitIndex + "," + limitCount : "");
		//System.out.println(query);
		return getReviews( query);
	}
	
	public ArrayList<Review> getReviewsHavingIdsInList(String idList) {
		String query = "select * from reviews_review " +
				" where " +
					" id in (" + idList + ")";
		//System.out.println(query);
		return getReviews( query);
	}
	
	/**
	 * 
	 * @return All the reviewer's id's
	 */
	public ArrayList<Integer> getReviewerIds() {
		return getReviewerIds("SELECT id FROM reviews_reviewer");
	}
	
	public ArrayList<Integer> getReviewerIds(int minReviewCount, int maxReviewCount) {
		return getReviewerIds("SELECT id FROM reviews_reviewer " +
				"where review_count >=" + minReviewCount +
				" and review_count <=" + maxReviewCount);
	}
	/**
	 * Private method that accepts a query on reviews_reviewer
	 * @param query, a select query
	 * @return ArrayList<Integer> of reviewer id's
	 */
	private ArrayList<Integer> getReviewerIds (String query) {
		ArrayList<Integer> ids = new ArrayList<Integer>();
		
		ResultSet rs = db.sql(query);
		
		try {
			while(rs.next()) {
				Integer i = rs.getInt("id");
				ids.add(i);
			}
		} catch( Exception e ) {
			System.err.println("Error reading resultset in DAO.getReviewerIds(): " + e.toString());
		}
		return ids;
	}
	
	/**
	 * 
	 * @return All the store id's
	 */
	public ArrayList<Integer> getStoreIds() {
		return getStoreIds("SELECT id FROM reviews_store");
	}
	
	/**
	 * Private method that accepts a query on reviews_store
	 * @param query, a select query
	 * @return ArrayList<Integer> of store id's
	 */
	private ArrayList<Integer> getStoreIds (String query) {
		ArrayList<Integer> storeIds = new ArrayList<Integer>();
		
		ResultSet rs = db.sql(query);
		
		try {
			while(rs.next()) {
				Integer i = rs.getInt("id");
				storeIds.add(i);
			}
		} catch( Exception e ) {
			System.err.println("Error reading resultset in DAO.getStoreIds(): " + e.toString());
		}
		return storeIds;
	}
	
	/**
	 * Private method to take any query and return the resulted Review list
	 * @param query, a select query on reviews_review table
	 * @return ArrayList of Review
	 */
	private ArrayList<Review> getReviews(String query) {
		ArrayList<Review> reviews = new ArrayList<Review>();
		ResultSet rs = db.sql(query);
		System.err.println(query);
		try {
			while(rs.next()) {
				Review review = new Review();
				
				review.id = rs.getInt("id");
				review.reviewerId = rs.getInt("reviewer_id");
				review.storeId = rs.getInt("store_id");
				review.date = rs.getDate("date");
				review.rating = rs.getInt("rating");
				review.text = removeHtmlTags(rs.getString("text") );
				review.dateModified = rs.getDate("date_modified");
				review.siteId = rs.getInt("site_id");
				review.duplicateCount = rs.getInt("duplicate_count");
				review.isModified = rs.getBoolean("is_modified");
				review.topReviewer = rs.getBoolean("top_reviewer");
				
				reviews.add(review);
			}
		} catch( Exception e ) {
			System.err.println("Error reading resultset in DAO.getReviews(): " + e.toString());
		}
		
		return reviews;
	}
	
	/**
	 * Removes html tags and special characters from a string 
	 * @param s
	 * @return
	 */
	private String removeHtmlTags(String s) {
		s = s.replaceAll("&nbsp;", "");
		s = s.replaceAll("<[^>]*>", "");
		return s;
	}
	
	public ArrayList<Integer> getIdsByVeryCustomQuery() {
		String query = "select id  " +
				" from reviews_review " +
				" where rating != 3 and reviewer_id in " +
					" (SELECT id FROM `reviews_reviewer` where review_count <= 30 and review_count > 1) " +
				" group by reviewer_id,DATE_FORMAT(date, \"%y-%m-%d\") having count(id) <= 1";
		ArrayList<Integer> idList = new ArrayList<Integer>();
		ResultSet rs = db.sql(query);
		
		try {
			while(rs.next()) {
				idList.add(rs.getInt(1));
			}
		} catch( Exception e ) {
			System.err.println("Error reading resultset in DAO.getIdsByVeryCustomQuery(): " + e.toString());
		}
		return idList;
	}
	
}
