package codejam.y2011.round_1A.killer_word;

import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import codejam.utils.datastructures.BitSetInt;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class WordHolder
{

    //All the words, does not change
    private List<String> words;
    
    /**
     * 26 for each letter --
     * 
     * the Map key are the positions of the letter in the words contained in
     * the value, which is a bitset of indexes of words
     * 
     * Ex
     * 
     * words --
     *    bob
     * barley
     *  babby
     *    bab
     * 
     * letCom['b'] ['101'] = 1001 (bob - - bab)
     */
    private List<Map<BitSetInt, BitSet>> letterCombinatinToWord;
    
    //Each holder holds a specific length word
    private int wordSize;
    
    //How many words left / bits set in words remaining
    private int wordsRemainingCount;
    private BitSet wordsRemaining;
    
    //Which positions have been guessed
    private BitSetInt positionsFilled;
    
    //How many points he lost
    int score;
        
    @Override
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("WordHolder [words=" + words.size() + "]\n");
        
        for(int i = 0; i < 26; ++i) {
            Map<BitSetInt, BitSet> comWord = letterCombinatinToWord.get(i);
            
            for(BitSetInt cm : comWord.keySet()) {
                sb.append((char) (i+'a')).append(": ");
                for(int c = 0; c < wordSize; ++c) {
                    if (cm.isSet(c)) {
                        sb.append( (char) (i + 'a'));
                    } else {
                        sb.append("_");
                    }
                }
                
                BitSet wordSet = comWord.get(cm);
                
                sb.append(" , words : ");
                for(int w = 0; w < words.size(); ++w) {
                    if (wordSet.get(w)) {
                        sb.append(words.get(w));
                        sb.append(" ");
                    }
                }
                sb.append("\n");
            }
        }
        
        return sb.toString();
    }
    
    public String toShortString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("WordHolder [words=" + wordsRemainingCount + "] Score : " + score + "\n");
        
        for(int w = 0; w < wordSize; ++w) {
            if (positionsFilled.isSet(w)) {
                sb.append("*");
            } else {
                sb.append("_");
            }
        }
        
        
                
        sb.append(" , words : ");
        for(int w = 0; w < words.size(); ++w) {
            if (wordsRemaining.get(w)) {
                sb.append(words.get(w));
                sb.append(" ");
            }
        }
        sb.append("\n");

        return sb.toString();
    }

    public int getWordsRemainingCount()
    {
        return wordsRemainingCount;
    }

    public WordHolder(int size) {
        this.wordSize = size;
        
        letterCombinatinToWord = Lists.newArrayList();
        for(int i = 0; i < 26; ++i) {
            letterCombinatinToWord.add(new HashMap<BitSetInt,BitSet>());
        }
        
        this.words = Lists.newArrayList();
        
        positionsFilled = new BitSetInt();
        
        wordsRemaining = new BitSet();
        
        this.score = 0;
    }
    
    public static WordHolder copy(WordHolder wh) {
        WordHolder ret = new WordHolder(wh);
        //ret.letterCombinatinToWord
        return ret;
    }
    
    private WordHolder(WordHolder wh)
    {
        //Shallow copy everything
        this.wordSize = wh.wordSize;
        this.letterCombinatinToWord = wh.letterCombinatinToWord;
        this.words = wh.words;
        this.score = wh.score;
        
        //The fields that change below this line
        positionsFilled = new BitSetInt();
        
        wordsRemaining = BitSet.valueOf(wh.wordsRemaining.toLongArray());
    }
    
    public void addWord(String word) {
        Preconditions.checkState(word.length() == wordSize);
        
        BitSetInt[] letterBitmasks = new BitSetInt[26];
        for(int i = 0; i < 26; ++i) {
            letterBitmasks[i] = new BitSetInt();
        }
        
        for(int c = 0; c < word.length(); ++c) {
            int chCode = word.charAt(c) - 'a';            
            letterBitmasks[chCode].set(c);
        }
        
        for(int i = 0; i < 26; ++i) {
            Map<BitSetInt, BitSet> comWordMap = letterCombinatinToWord.get(i);
            
            Preconditions.checkArgument(comWordMap != null);
            
            BitSet wordSet = comWordMap.get(letterBitmasks[i]);
            
            if (wordSet == null) {
                wordSet = new BitSet();
                comWordMap.put(letterBitmasks[i], wordSet);
            }
            
            wordSet.set(words.size());
        }
        
        wordsRemaining.set(words.size());
        ++wordsRemainingCount;
        words.add(word);
    }
    
    public String getRemainingWord() {
        int index = wordsRemaining.nextSetBit(0);
        
        if (index != -1) {
            return (words.get(index));
        }
        
        return null;
    }

    public void getPossibleGuesses(List<WordHolder> wordHolders, List<WordHolder> singeWordLeft, Character nextLetterToGuess)
    {
        
        
        int chCode = nextLetterToGuess - 'a';
        
        Map<BitSetInt, BitSet> comWord = letterCombinatinToWord.get(chCode);
        
        for(BitSetInt cm : comWord.keySet()) {
            if ( (positionsFilled.getBits() & cm.getBits()) != 0) {
                //He would have already eliminated the choice
                continue;
            }
            
            WordHolder wh = new WordHolder(this);
            wh.positionsFilled.setAllBits(positionsFilled.getBits() | cm.getBits());
            wh.wordsRemaining.and(comWord.get(cm));
            wh.wordsRemainingCount = wh.wordsRemaining.cardinality();
            //Nothing revealed == another point, but the words remaining must have been decreased,
            //otherwise the letter would not have been guessed
            Preconditions.checkState(wh.wordsRemainingCount <= wordsRemainingCount);
            if (positionsFilled.equals( wh.positionsFilled) && wh.wordsRemainingCount < wordsRemainingCount) {
                wh.score++;
            }
            
            //Needed...why?  don't know
            if (wh.wordsRemainingCount == 0) {
                continue;
            }
            
            
            if (wh.wordsRemainingCount == 1) {
                //Put in a special list to look at
                singeWordLeft.add(wh);
            } else {
                wordHolders.add(wh);
            }
        }
        
        
        
    }

    public int getSize()
    {
        return wordSize;
    }
}
