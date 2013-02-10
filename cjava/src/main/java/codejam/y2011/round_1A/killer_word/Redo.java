package codejam.y2011.round_1A.killer_word;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class Redo extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

    
    public Redo() {
        super("B", 1, 0, 1);
    }

    @Override
    public InputData readInput(Scanner scanner, int testCase)
    {
        Main m = new Main();
        return m.readInput(scanner, testCase);
    }

    final int MAX_WORD_LEN = 10;
    
    @Override
    public String handleCase(InputData in)
    {
        
        WordHolder[] holders = new  WordHolder[MAX_WORD_LEN];
        
        
        
        
        for(int len = 0; len < MAX_WORD_LEN; ++len) {
            holders[len] = new WordHolder(len+1);
            
        }
        
        Map<String, Integer> wordToDictIndex = Maps.newHashMap();
        
        int wordIndex = 0;
        for(String word : in.words) {
            wordToDictIndex.put(word, wordIndex++);
            
            holders[word.length()-1].addWord(word);
        }
        
        StringBuffer ans = new StringBuffer();
        ans.append("Case #" + in.testCase + ": ");
        
        log.debug("hnehu");
        
        
        for (int m = 0; m < in.M; ++m) {
            
            String bestWord = null;
            int bestScore = -1000;
            
            String guesses = in.guessLists.get(m);
            
            List<WordHolder> prevLists = Lists.newArrayList();
            for(int len = 0; len < MAX_WORD_LEN; ++len) {
                prevLists.add(holders[len]);
            }
            
            for(int g = 0; g < guesses.length(); ++g) {
                
                List<WordHolder> curWhList = Lists.newArrayList();
                
                Character guessChar = guesses.charAt(g);
                log.debug("Guessing " + guessChar);
                
                for(int i = 0; i < prevLists.size(); ++i) {
                    WordHolder wh = prevLists.get(i);
                    wh.getPossibleGuesses(curWhList, guessChar);
                }
                
                
                log.debug("After guess {}", guessChar);
                Iterator<WordHolder> whIt = curWhList.iterator();
                
                while(whIt.hasNext()) {
                    WordHolder wh = whIt.next();
                    log.debug("Wh {}", wh.toShortString());
                    
                    if (wh.getWordsRemainingCount() == 1) {
                        String remWord = wh.getRemainingWord();
                        
                        if (wh.score > bestScore) {
                            bestWord = null;
                            bestScore = wh.score;
                        }
                        
                        if (wh.score >= bestScore) {
                            if (bestWord == null) {
                                bestWord = remWord;
                            } else {
                                int existingIndex = wordToDictIndex.get(bestWord);
                                int curIndex = wordToDictIndex.get(remWord);
                                
                                if (curIndex < existingIndex) {
                                    bestWord = remWord;
                                }
                            }
                        
                        }
                        
                        whIt.remove();
                        log.debug("Removing Wh {}; bestScore {}; words {}", wh.toShortString(), bestScore, bestWord);
                    }
                }
                
                if (curWhList.isEmpty()) {
                    
                    break;
                }
                
                prevLists = curWhList;
            }
            
            ans.append(bestWord);
            
            ans.append(" ");
            
            
        }
        
        
        return ans.toString();
    }



}
