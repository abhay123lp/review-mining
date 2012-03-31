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
	/*public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Hello,world!");
		DBConnector db = new DBConnector();
		db.sql("select * from reviews_review limit 2");
	}*/
	
	public static void main(String[] args){
		//input array 3,3,2
		int[] test = {3,3,2};
		HashMap hm = new HashMap();
		for (int index=0; index < test.length; index++)
		{
			if(hm.containsKey(test[index])){
				int cnt = (Integer) hm.get(test[index]);
				hm.put(test[index], (cnt+1)%2);
			}
			else{
				hm.put(test[index], 1);
			}	
		}
		
		Set set = hm.entrySet();
	    Iterator i = set.iterator();	    
	    while(i.hasNext()){
	      Map.Entry me = (Map.Entry)i.next();
	      if((Integer)me.getValue() == 1)
	      System.out.println(me.getKey() + " is a even number");
	    }
	
	}

}
