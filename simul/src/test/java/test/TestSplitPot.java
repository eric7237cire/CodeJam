package test;


import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import pkr.history.FlopTurnRiverState;
import pkr.history.HandInfo;
import pkr.history.HandInfoCollector;

@RunWith(JUnit4.class)

public class TestSplitPot
{

    @Test
    public void testSplitPot() throws Exception
    {
        HandInfoCollector results = TestPreflopStats.getList("testSplitPot");
        
        assertEquals(2, results.listHandInfo.size());
        
        HandInfo hi = results.listHandInfo.get(0);
        
        assertEquals("Pisoi", hi.winnerPlayerName[0]);
        assertEquals("Eric", hi.winnerPlayerName[1]);
        
        hi = results.listHandInfo.get(1);
        
        assertEquals("Tooney", hi.winnerPlayerName[0]);
        assertEquals("Meliana", hi.winnerPlayerName[1]);
        
    }
    

}
