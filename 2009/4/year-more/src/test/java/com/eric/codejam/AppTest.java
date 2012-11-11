package com.eric.codejam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

import com.eric.codejam.Main.Tournament;

/**
 * Unit test for simple App.
 */
public class AppTest {
    
    @Test
    public void testRealSolution() {
        Tournament t = new Tournament();
        t.rounds = 3;
        t.roundDays = new int[] {   0, 1, 3 };
        
        List<Tournament> list = new ArrayList<>();
        list.add(t);
        
        
        Happiness hap = Happiness.create(1, 1, 1000, list);
                
        assertEquals(1, hap.getNumerator().intValue());
        
        RealSolution rs = RealSolution.create(1, 1, 10, list);
        
        assertEquals(1,  rs.wholeNumber);
        
        rs = RealSolution.create(7, 1, 10, list);
        
        //assertEquals(17 / 7,  rs.getNumerator() / rs.denom);
        
        
        t = new Tournament();
        t.rounds = 2;
        t.roundDays = new int[] { 0, 2 };
        
        list.add(t);
        
        rs = RealSolution.create(4, 2, 1000, list);
        
        assertEquals(16*5 + 2, rs.wholeNumber * 16 + rs.getNumerator());
        
        hap = new Happiness(4, 2, 100);
        hap.addTournament(list.get(0));
        hap.addTournament(list.get(1));
        assertEquals(16*5+2, hap.getNumerator().intValue());
        
        t = new Tournament();
        t.rounds = 2;
        t.roundDays = new int[] { 0, 3 };
        list.add(t);
    }
    
    @Test
    public void testCountTotalHapp() {
        Tournament t = new Tournament();
        t.rounds = 3;
        t.roundDays = new int[] {   0, 1, 3 };
        
        List<Tournament> list = new ArrayList<>();
        list.add(t);
        
        int h = BruteForce.bruteForceHappiness(list, 1, new int[1]);
        
        assertEquals(1, h);
        
        Happiness hap = new Happiness(1, 1, 1000);
        hap.addTournament(t);
        
        assertEquals(1, hap.getNumerator().intValue());
        
        h = BruteForce.bruteForceHappiness(list, 2, new int[2]);
        
        assertEquals(3, h);
        
        h = BruteForce.bruteForceHappiness(list, 7, new int[7]);
        
        assertEquals(17, h);
        
        h = BruteForce.bruteForceHappiness(list, 4, new int[4]);
        
        assertEquals(8, h);
        
        hap = new Happiness(4, 1, 1000);
        hap.addTournament(t);
        assertEquals(8, hap.getNumerator().intValue());
        
        t = new Tournament();
        t.rounds = 2;
        t.roundDays = new int[] { 0, 2 };
        
        list.add(t);
        
        h = BruteForce.bruteForceHappiness(list, 4, new int[4]);
        
        assertEquals(16*5 + 2, h);
        
        hap = new Happiness(4, 2, 100);
        hap.addTournament(list.get(0));
        hap.addTournament(list.get(1));
        assertEquals(16*5+2, hap.getNumerator().intValue());
        
        t = new Tournament();
        t.rounds = 2;
        t.roundDays = new int[] { 0, 3 };
        list.add(t);
       // BruteForce.debug = true;
        h = BruteForce.bruteForceHappiness(list, 4, new int[4]);
        
        assertEquals(560, h);
        
        hap = Happiness.create(4, 3, 1000, list);
        
        assertEquals(560, hap.getNumerator().intValue());
        
        h = BruteForce.bruteForceHappiness(list, 8, new int[8]);
        
        hap = Happiness.create(8, 3, 1000, list);
        
        assertEquals(h, hap.getNumerator().intValue());
        
        hap = Happiness.create(8, 3, 4, list);
        
        assertEquals(h, hap.getNumerator().intValue());
        
    }
    

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
		Pattern p = Pattern.compile("Case #\\d: (.*)");
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
