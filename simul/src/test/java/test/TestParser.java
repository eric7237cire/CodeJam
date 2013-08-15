package test;


import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import pkr.history.FlopTurnRiverState;
import pkr.history.Parser;
import pkr.history.StatPlayerHand;
import pkr.history.StatsSession;
import pkr.history.StatsSessionPlayer;


import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class TestParser
{

    public TestParser() {
        // TODO Auto-generated constructor stub
    }
    
    public List<FlopTurnRiverState[]> getList(String testFile) throws URISyntaxException, IOException
    {
        URL testInputUrl = getClass().getResource("/parser/" + testFile + ".txt");
        //URL cleanInputUrl = getClass().getResource("/parser/clean" + testFile + ".txt");
        
        String testOutputUrlStr = testInputUrl.toString().replace(testFile, 
                "clean" + testFile);
        URL cleanInputUrl = new URL(testOutputUrlStr);
        
        File testInputRaw = new File(testInputUrl.toURI());
        File testInput = new File(cleanInputUrl.toURI());
        
        return Parser.parseFile(testInputRaw, testInput);
    }

    //@Test
    public void testTapisSuivi1() throws Exception
    {
        List<FlopTurnRiverState[]> results = getList("testTapisSuivi1");
        
        assertEquals(1, results.size());
        
        FlopTurnRiverState[] handStates = results.get(0);
        
        StatsSession stats = Parser.computeStats(results);
    }
    
    @Test
    public void testVPIP1() throws Exception
    {
        List<FlopTurnRiverState[]> results = getList("testVPIP1");
        
        assertEquals(5, results.size());
        
        FlopTurnRiverState[] handStates = results.get(0);
        
        StatsSession stats = Parser.computeStats(results);
        
        //Eric  -   1        -  2 
        //Morris  - 1     4  -    3
        //*       -   2       - 
        //Billy   - 1 2 3   5 - 
        //Anto    -     3     -    4
        
        List<StatsSessionPlayer> players = Arrays.asList(
                stats.playerSessionStats.get("Eric"),
                stats.playerSessionStats.get("Morris"),
                stats.playerSessionStats.get("מוריס"),
                stats.playerSessionStats.get("Billy"),
                stats.playerSessionStats.get("Anto"));
        
        int playerNum = 0;
        assertEquals(1, players.get(playerNum).vpipNumerator );
        assertEquals(4, players.get(playerNum).vpipDenom );
        assertEquals(5, players.get(playerNum).totalHands );
        
        playerNum = 1;
        assertEquals(2, players.get(playerNum).vpipNumerator );
        assertEquals(4, players.get(playerNum).vpipDenom );
        assertEquals(5, players.get(playerNum).totalHands );
        
        playerNum = 2;
        assertEquals(1, players.get(playerNum).vpipNumerator );
        assertEquals(3, players.get(playerNum).vpipDenom );
        assertEquals(3, players.get(playerNum).totalHands );
        
        playerNum = 3;
        assertEquals(4, players.get(playerNum).vpipNumerator );
        assertEquals(5, players.get(playerNum).vpipDenom );
        assertEquals(5, players.get(playerNum).totalHands );
        
        playerNum = 4;
        assertEquals(1, players.get(playerNum).vpipNumerator );
        assertEquals(2, players.get(playerNum).vpipDenom );
        assertEquals(3, players.get(playerNum).totalHands );
    }
}
