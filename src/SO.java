import java.util.ArrayList;


public class SO {
	Tagging posTagger = new Tagging();
	
	/**
	 * Gets the two-words phrases of Adj-N, Adv-Adj, Adj-Adj, N-Adj or Adv-V form using POS and search
	 * @param text to be analyzed
	 * @return ArrayList<String> of phrases
	 */
	public ArrayList<String> getTwoWordPhrases(String text) {
		ArrayList<String> phrases = new ArrayList<String>();
		
		String taggedText = posTagger.getTagging(text, "");
		
		String[] tags = taggedText.split(" ");
		
		boolean n = false, j = false, r = false;
		for (int i=0; i<tags.length; i++) {
			if (tags[i].contains("_JJ")) {
				// JJ-JJ, RB-JJ, or NN-JJ, or reset for JJ-NN
				if (j || n || r) { // previous JJ or NN
					String s = tags[i-1].replaceAll("_[^ ]*", "") + " " + 
							tags[i].replaceAll("_[^ ]*", "");
					phrases.add(s);
				}
				j = true;
				n = r = false;
			}
			else if (tags[i].contains("_NN")) {
				// JJ-NN, or reset for NN-JJ
				if (j) { // previous JJ
					String s = tags[i-1].replaceAll("_[^ ]*", "") + " " + 
							tags[i].replaceAll("_[^ ]*", "");
					phrases.add(s);
				}
				n = true;
				j = r = false;
			}
			else if (tags[i].contains("_RB")) {
				// reset for RB-VB or RB-JJ
				n = j = false;
				r = true;
			}
			else if (tags[i].contains("_VB")) {
				// RB-VB
				if (r) { // previous JJ
					String s = tags[i-1].replaceAll("_[^ ]*", "") + " " + 
							tags[i].replaceAll("_[^ ]*", "");
					phrases.add(s);
				}
				n = j = r = false;
			}
			
		}
		
		return phrases;
	}
}
