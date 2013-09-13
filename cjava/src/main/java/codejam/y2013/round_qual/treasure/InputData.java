package codejam.y2013.round_qual.treasure;

import java.util.List;

import codejam.utils.main.AbstractInputData;

import com.google.common.collect.Lists;

public class InputData extends AbstractInputData {
    
    public static class Chest
    {
        int keyToOpen;
        List<Integer> keys = Lists.newArrayList();
        
    }
    public InputData(int testCase) {
        super(testCase);
        chests = Lists.newArrayList();
        startingKeys = Lists.newArrayList();
    }
    
    List<Chest> chests;
    
    List<Integer> startingKeys;
    
}
