package test;


import java.io.File;
import java.net.URL;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pkr.history.Preprocessor;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import static org.junit.Assert.*;


@RunWith(JUnit4.class)
public class TestPreproc {
    private static Logger log = LoggerFactory.getLogger(TestPreproc.class);
    
    @Test
    public void test1() throws Exception
    {
        URL testInputUrl = getClass().getResource("/handsPreProc/test1.txt");
        URL resultUrl = getClass().getResource("/handsPreProc/result1.txt");
        
        String testOutputUrlStr = resultUrl.toString().replace("result1.txt", "testOutput1.txt");
        URL testOutputURL = new URL(testOutputUrlStr);
        File testOutputFile = new File(testOutputURL.toURI());
        log.debug("{}", testOutputFile.getCanonicalPath());
        
        File resultsFile = new File(resultUrl.toURI()); 
        log.debug("url {}", resultUrl);
        
        
        
        Preprocessor.clean(new File(testInputUrl.toURI()), testOutputFile);
        
        List<String> testOutput = Files.readLines(testOutputFile, Charsets.UTF_8);
        
        List<String> resultOutput = Files.readLines(resultsFile, Charsets.UTF_8);
        
        for(int i = 0; i < resultOutput.size(); ++i)
        {
            assertTrue(" Missing " + i, testOutput.size() > i);
            
            String resultLine = resultOutput.get(i);
            String testOutputLine = testOutput.get(i);
            
            assertEquals(resultLine, testOutputLine);
        }
    }
}
