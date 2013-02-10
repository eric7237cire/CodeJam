package codejam.y2011;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import codejam.y2011.round_1A.killer_word.WordHolder;

import com.google.common.collect.Lists;

public class TestWordHolder
{

    @Test
    public void testWordHolder() {
        WordHolder wh = new WordHolder(7);
        
        wh.addWord("hhhaaab");
        wh.addWord("hccaaab");
        wh.addWord("hacaaab");
        wh.addWord("hccaaab");
        wh.addWord("accaaab");
        
        String s = wh.toString();
        
        System.out.println(s);
        
        List<WordHolder> list = Lists.newArrayList();
        
        wh.getPossibleGuesses(list, 'c');
        
        for(WordHolder whSub : list) {
            System.out.println(whSub.toShortString());
            
        }
        
    }
    
    @Test
    public void testWordHolder2() {
        WordHolder wh = new WordHolder(6);
        
        wh.addWord("potato");
        wh.addWord("tomato");
        wh.addWord("garlic");
        wh.addWord("pepper");        
        String s = wh.toString();
        
        System.out.println(s);
        
        List<WordHolder> list = Lists.newArrayList();
        
        wh.getPossibleGuesses(list, 't');
        
        assertEquals(3, list.size());
        for(WordHolder whSub : list) {
            System.out.println(whSub.toShortString());
            
        }
        
        List<WordHolder> list2 = Lists.newArrayList();
        
        list.get(2).getPossibleGuesses(list2, 'r');
        
        for(WordHolder whSub : list2) {
            System.out.println(whSub.toShortString());
            
        }
        //assertEquals(2, list.get(0).getSize());
        
    }

}
