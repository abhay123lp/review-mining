import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import edu.stanford.nlp.ling.TaggedWord;


public class SO {
	

	static HashMap<String, Integer> opinionLexicon = loadLexicons();
	Tagging posTagger = null;

	public SO () throws IOException, ClassNotFoundException {
		posTagger = new Tagging();
	}
	
	private static HashMap<String, Integer> loadLexicons() {
		HashMap<String, Integer> lx = new HashMap<String, Integer>();
		try {
    		FileReader input = new FileReader("positive-words.txt");
    		BufferedReader bufRead = new BufferedReader(input);
    		String line;
    		line = bufRead.readLine();
    		while (line != null){
    			if (line.startsWith(";") || line.length() == 0)
    			{	
    				line = bufRead.readLine();
    				continue;
    			}
    			lx.put(line.trim(), new Integer(1));
    			line = bufRead.readLine();	
    		}
    		bufRead.close();
    		
    		input = new FileReader("negative-words.txt");
    		bufRead = new BufferedReader(input);
    		line = bufRead.readLine();
    		while (line != null){
    			if (line.startsWith(";") || line.length() == 0)
    			{	
    				line = bufRead.readLine();
    				continue;
    			}
    			lx.put(line.trim(), new Integer(-1));
    			line = bufRead.readLine();	
    		}
    		bufRead.close();
    		
    	}catch (IOException e){
    		e.printStackTrace();
    	}
		return lx;
	}
	/**
	 * Gets the two-words phrases of Adj-N, Adv-Adj, Adj-Adj, N-Adj or Adv-V form using POS and search
	 * @param text to be analyzed
	 * @return ArrayList<String> of phrases
	 */
	public ArrayList<String> getTwoWordPhrases(String text) {
		ArrayList<String> phrases = new ArrayList<String>();
		
		if (posTagger == null) return null;
		String taggedText = posTagger.getTagging(text, "");
		//posTagger.getSentences(text);
		
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
			else n = j = r = false; // end of a sentence
			
		}
		
		return phrases;
	}

	/**
	 * Gets the two-words phrases of Adj-N, Adv-Adj, Adj-Adj, N-Adj or Adv-V form using POS and search
	 * @param text to be analyzed
	 * @return ArrayList<String> of phrases
	 */
	public ArrayList<String> getTwoWordPhrases(ArrayList<TaggedWord> taggedWords) {
		ArrayList<String> phrases = new ArrayList<String>();
		
		boolean n = false, j = false, r = false;
		for (int i=0; i<taggedWords.size(); i++) {
			if (taggedWords.get(i).tag().startsWith("JJ")) {
				// JJ-JJ, RB-JJ, or NN-JJ, or reset for JJ-NN
				if (j || n || r) { // previous JJ or NN
					String s = taggedWords.get(i-1).word() + " " + taggedWords.get(i).word();
					phrases.add(s);
				}
				j = true;
				n = r = false;
			}
			else if (taggedWords.get(i).tag().startsWith("NN")) {
				// JJ-NN, or reset for NN-JJ
				if (j) { // previous JJ
					String s = taggedWords.get(i-1).word() + " " + taggedWords.get(i).word();
					phrases.add(s);
				}
				n = true;
				j = r = false;
			}
			else if (taggedWords.get(i).tag().startsWith("RB")) {
				// reset for RB-VB or RB-JJ
				n = j = false;
				r = true;
			}
			else if (taggedWords.get(i).tag().startsWith("VB")) {
				// RB-VB
				if (r) { // previous JJ
					String s = taggedWords.get(i-1).word() + " " + taggedWords.get(i).word();
					phrases.add(s);
				}
				n = j = r = false;
			}
			else n = j = r = false; // end of a sentence
			
		}
		
		return phrases;
	}
	/**
	 * Returns a phrase if it's world are majority in +ve or -ve side
	 * Need to formulate better algorithm, e.g. "extremely bad" vs "extremely good"
	 * @param s is the string
	 * @return -1, 1 for majority class, 0 for neutral or not-found.  
	 */
	public int getPolarityOfPhrase(String s) {
		int k = 0;
		for (String st: s.split(" ")) {
			st = st.replaceAll("[^\\w]*", "");
			if( opinionLexicon.containsKey(st) )
				k += opinionLexicon.get(st);
		}
		return k > 0 ? 1 : k < 0 ? -1 : 0;
	}
	
	public HashMap<Review,ArrayList<String>> getTwoWordPhraseFromFile(){
		HashMap<Review,ArrayList<String>> hm = new HashMap<Review,ArrayList<String>>();
		BufferedReader buf = null;
		try {
			buf = new BufferedReader(new FileReader("reviewPhrases.txt"));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String line = null;
		List<String[]> rows = new ArrayList<String[]>();
		try {
			while((line=buf.readLine())!=null) {
			   if (line.isEmpty())
					   continue;
			   String[] row = line.split("::");
			   Review review = new Review();
			   review.id = Integer.parseInt(row[0]);
			   review.rating = Integer.parseInt(row[1]);
			   //ArrayList<String> phrases = new ArrayList<String>();
			   ArrayList<String> list = new ArrayList<String>(Arrays.asList(row[2].substring(1, row[2].length() - 1).split(",")));
			   hm.put(review,  list);
			   //phrases = (ArrayList<String>) Arrays.asList(row[2].split(","));
			   //System.out.println(list.toString());
			   //rows.add(row);   
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hm;
		
	}
}
