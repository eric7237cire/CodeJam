package com.eric.codejam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang3.StringUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ch.qos.logback.classic.Level;

import com.eric.codejam.AppTest;
import com.eric.codejam.Main;

/**
 * Unit test for simple App.
 */
public class AppTest {

    final static Logger log = LoggerFactory.getLogger(AppTest.class);

    /**
     * Create the test case
     * 
     * @param testName
     *            name of the test case
     */
    public AppTest() {
        super();
    }

    private String getOutput(String testCase) {
        Scanner sc = new Scanner(testCase);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PrintStream pos = new PrintStream(os);

        Main.handleCase(1, sc, pos);

        try {
            String output = new String(os.toString("UTF-8"));
            log.info(output);
            return output.trim();
        } catch (UnsupportedEncodingException ex) {
            return null;
        }
    }
    
    private static String extractAns(String str) {
        Pattern p = Pattern.compile("Case \\d: (.*)");
        Matcher m = p.matcher(str);
        if (m.matches()) {
            return m.group(1);
        }
        
        return "Error";
    }

    private static Map<String, String> testInputData;
    private static Map<String, String> testOutputData;

    @BeforeClass
    public static void getTestData() {
        testInputData = new HashMap<>();
        testOutputData = new HashMap<>();
        
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger)LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.DEBUG);

        try {

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(ClassLoader
                    .getSystemResourceAsStream("test-data.xml"));
            doc.getDocumentElement().normalize();

            NodeList nl = doc.getElementsByTagName("test");
            for (int i = 0; i < nl.getLength(); ++i) {
                Node n = nl.item(i);
                Element e = (Element) n;
                String input = e.getElementsByTagName("input").item(0).getTextContent();
                String output = e.getElementsByTagName("output").item(0).getTextContent();
                
                String name = n.getAttributes().getNamedItem("name")
                        .getNodeValue();

                testInputData.put(name, input);
                testOutputData.put(name,  output);
            }
        } catch (Exception ex) {

        }

    }

    

    @Test
    public void testMain() {
        for(String name : testInputData.keySet()) {
            String input = testInputData.get(name).trim();
            String output = testOutputData.get(name).trim();
            
            String actualOutput = extractAns(getOutput(input));
            
            assertTrue("\nInput " + input + "\nexpected output " + output + "\n Actual " + actualOutput,StringUtils.equalsIgnoreCase(output, actualOutput));
        }
        

        //double output = Double.parseDouble();
        
        //assertEquals(7, output, DoubleComparator.TOLERANCE);
    }
    
	
	
}
