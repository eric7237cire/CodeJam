package test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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


@RunWith(JUnit4.class)
public class TestPreproc {
    private static Logger log = LoggerFactory.getLogger(TestPreproc.class);
    
    public void test(int testNum) throws Exception
    {
        URL testInputUrl = getClass().getResource("/handsPreProc/test" + testNum + ".txt");
        URL resultUrl = getClass().getResource("/handsPreProc/result" + testNum + ".txt");
        
        String testOutputUrlStr = resultUrl.toString().replace("result" + testNum + ".txt", 
                "testOutput" + testNum + ".txt");
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
            
            assertEquals("Line " + (i + 1), resultLine, testOutputLine);
        }
    }
    
    @Test
    public void test1() throws Exception
    {
        test(1);
    }
    
    @Test
    public void test2() throws Exception
    {
        test(2);
    }
    
    @Test
    public void test3() throws Exception
    {
        test(3);
    }
    
    @Test
    public void test4() throws Exception
    {
        test(4);
    }
    
    @Test
    public void test5() throws Exception
    {
        test(5);
    }
}
