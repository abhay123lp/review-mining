import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

class Tagging {

  public void test() throws Exception {
	String modelFile="config/models/wsj-0-18-bidirectional-distsim.tagger";
    MaxentTagger tagger = new MaxentTagger(modelFile);
    @SuppressWarnings("unchecked")
    String testFile = "bin/config/sampleinput.txt";
    List<List<HasWord>> sentences = tagger.tokenizeText(new BufferedReader(new FileReader(testFile)));
    for (List<HasWord> sentence : sentences) {
      ArrayList<TaggedWord> tSentence = tagger.tagSentence(sentence);
      System.out.println(Sentence.listToString(tSentence, false));
    }
  }

}

