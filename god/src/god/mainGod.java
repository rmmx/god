/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package god;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.Microphone;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.Result;
import edu.cmu.sphinx.util.props.ConfigurationManager;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author rolando
 */
public class mainGod {
    
        private static final String ACOUSTIC_MODEL =
        "resource:/WSJ_8gau_13dCep_16k_40mel_130Hz_6800Hz";
    private static final String DICTIONARY_PATH =
        "resource:/WSJ_8gau_13dCep_16k_40mel_130Hz_6800Hz/dict/cmudict.0.6d";
    private static final String GRAMMAR_PATH =
        "resource:/edu/cmu/sphinx/demo/dialog/";
    private static final String LANGUAGE_MODEL =
        "resource:/edu/cmu/sphinx/demo/dialog/weather.lm";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        Configuration configuration = new Configuration();
        configuration.setAcousticModelPath(ACOUSTIC_MODEL);
        configuration.setDictionaryPath(DICTIONARY_PATH);
        configuration.setGrammarPath(GRAMMAR_PATH);
        configuration.setUseGrammar(true);

        configuration.setGrammarName("dialog");
        LiveSpeechRecognizer jsgfRecognizer =
            new LiveSpeechRecognizer(configuration);

        configuration.setGrammarName("digits.grxml");
        LiveSpeechRecognizer grxmlRecognizer =
            new LiveSpeechRecognizer(configuration);

        configuration.setUseGrammar(false);
        configuration.setLanguageModelPath(LANGUAGE_MODEL);
        LiveSpeechRecognizer lmRecognizer =
            new LiveSpeechRecognizer(configuration);
        
        
        List<String> lstlRecognition;
        String strRecognition;
        
        ConfigurationManager confManager;

		try {

			confManager = new ConfigurationManager(mainGod.class.getResource("god.config.xml"));

		} catch (Exception e) {
			throw new Exception("Exception, loading SPHINX conf File"
					+ e.getMessage());
			
		}

		Recognizer recognizer = (Recognizer) confManager.lookup("recognizer");
		recognizer.allocate();

		// start the microphone or exit the program if this is not possible
		Microphone microphone = (Microphone) confManager.lookup("microphone");

		
		//if (!microphone.isRecording()) {
			microphone.startRecording();
		//}

		 //if (!microphone.startRecording()) {
		 //System.out.println("Cannot start microphone! ");
		 //recognizer.deallocate();
		 //System.exit(1);
		 //}

		Result tempRecognition = null;
		

		long stopTime = System.nanoTime() + TimeUnit.SECONDS.toNanos(5);

		for (long cont = stopTime; cont > System.nanoTime();) {

			tempRecognition = recognizer.recognize();//Main function to identify speech

			strRecognition = tempRecognition.getBestFinalResultNoFiller();

			System.out.println("sphinx recognition: " + tempRecognition.getBestPronunciationResult());
			System.out.println("sphinx recognition: " + tempRecognition.getBestResultNoFiller());			
			System.out.println("sphinx recognition: " + tempRecognition.getBestActiveToken());
			System.out.println("sphinx recognition: " + strRecognition);

		
			
			

		

			//System.out.println(System.currentTimeMillis());

		}

		recognizer.deallocate();
		recognizer.resetMonitors();
		microphone.stopRecording();
		recognizer = null;
        
    }
}
