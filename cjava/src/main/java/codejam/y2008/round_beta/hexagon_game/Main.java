package codejam.y2008.round_beta.hexagon_game;

import java.util.Map;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import com.google.common.primitives.Ints;

public class Main extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    public Main()
    {
       // super();
        super("A", true,true);
    }
    
    
    @Override
    public InputData readInput(Scanner scanner, int testCase)
    {

        InputData in = new InputData(testCase);
        
        in.alienNumber = scanner.next();
        in.sourceLanguage = scanner.next();
        in.targetLanguage = scanner.next();

        return in;
    }

        
    @Override
    public String handleCase(InputData in)
    {
        Map<Character, Integer> sourceLanguage = Maps.newHashMap();
        BiMap<Character, Integer> targetLanguage = HashBiMap.create();
        
        /**
         * Characters ==> Digit maps (input digit's are in increasing order)
         */
        for(int sc = 0; sc < in.sourceLanguage.length(); ++sc) {
            sourceLanguage.put(in.sourceLanguage.charAt(sc), sc);
        }
        
        for(int tc = 0; tc < in.targetLanguage.length(); ++tc) {
            targetLanguage.put(in.targetLanguage.charAt(tc), tc);
        }
        
        /**
         * Calculate the alien number in base 10
         */
        long alienNumberBase10 = 0;
        
        int sourceBase = sourceLanguage.size();
        
        long baseMult = 1;
        for(int an = in.alienNumber.length() - 1; an >= 0; --an) {
            int alienNumberDigit = sourceLanguage.get(in.alienNumber.charAt(an));
            alienNumberBase10 += baseMult * alienNumberDigit;
            baseMult *= sourceBase;
        }
        
        /**
         * Convert it to target languages base
         */
        int targetBase = in.targetLanguage.length();
        
        StringBuffer convertedAlienNumber = new StringBuffer();
        while(alienNumberBase10 > 0) {
            int digit = Ints.checkedCast(alienNumberBase10 % targetBase);
            convertedAlienNumber.append(targetLanguage.inverse().get(digit));
            
            alienNumberBase10 /= targetBase;
        }
        convertedAlienNumber.reverse();
            
        return "Case #" + in.testCase + ": " + convertedAlienNumber;
    }

}