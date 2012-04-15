import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
// get more information on 
// http://www.computing.dcu.ie/~acahill/tagset.html
// http://nlp.stanford.edu/software/tagger.shtml
class Tagging {
	public String modelFile;
	private MaxentTagger tagger;
	
	Tagging() throws IOException, ClassNotFoundException{
		modelFile="models/wsj-0-18-left3words.tagger";
		//modelFile="models/wsj-0-18-bidirectional-distsim.tagger";
		tagger = new MaxentTagger(modelFile);
	}
	
	public void test() throws Exception {
	
		MaxentTagger tagger = new MaxentTagger(modelFile);
		@SuppressWarnings("unchecked")
		String testFile = "sample-input.txt";
		List<List<HasWord>> sentences = tagger.tokenizeText(new BufferedReader(new FileReader(testFile)));
		for (List<HasWord> sentence : sentences) {
			ArrayList<TaggedWord> tSentence = tagger.tagSentence(sentence);
			System.out.println(Sentence.listToString(tSentence, false));
		}
	}
  
	public String getSentences(String content){
		StringReader strRdr = new StringReader(content);
		DocumentPreprocessor dp = new DocumentPreprocessor(strRdr);
		for (List sentence: dp){
			System.out.println(sentence);
		}
		return "";
	}
	public String getTagging(String content, String model){
		//try {
			//MaxentTagger tagger = new MaxentTagger(modelFile);
			String tagged = tagger.tagTokenizedString(content);
			//System.out.println(tagged);
			return tagged;
		/*} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
	   //return null;
	}
	
	public ArrayList<ArrayList> getTaggingSplittedSentence(String text) {
		ArrayList<ArrayList> tagged = new ArrayList<ArrayList>();
		
		StringReader strRdr = new StringReader(text);
		DocumentPreprocessor dp = new DocumentPreprocessor(strRdr);
		for (List sentence: dp){
			ArrayList<TaggedWord> tSentence = tagger.tagSentence(sentence);
			tagged.add(tSentence);
			/*for (TaggedWord word: tSentence) {
				System.out.println(word.tag() +" -> "+ word.word() );
			}*/
			//System.out.println(sentence);
		}
		//System.out.println(tagged);
		return tagged;
	}



}

