package test;


import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import pkr.history.FlopTurnRiverState;
import pkr.history.Parser;
import pkr.history.StatsSession;


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

    @Test
    public void testTapisSuivi1() throws Exception
    {
        List<FlopTurnRiverState[]> results = getList("testTapisSuivi1");
        
        assertEquals(1, results.size());
        
        FlopTurnRiverState[] handStates = results.get(0);
        
        StatsSession stats = Parser.computeStats(results);
    }
}
