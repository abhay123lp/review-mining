import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
// get more information on 
// http://www.computing.dcu.ie/~acahill/tagset.html
// http://nlp.stanford.edu/software/tagger.shtml
class Tagging {
	public String modelFile;
	
	Tagging(){
		modelFile="models/wsj-0-18-bidirectional-distsim.tagger";
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
  
	public void getTagging(String content, String model){
		try {
			MaxentTagger tagger = new MaxentTagger(modelFile);
			String tagged = tagger.tagTokenizedString(content);
			System.out.println(tagged);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	  
	}

}

