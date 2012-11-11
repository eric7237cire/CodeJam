package com.eric.codejam;

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

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import static org.junit.Assert.assertEquals;

import com.eric.codejam.Main.Tournament;

/**
 * Unit test for simple App.
 */
public class AppTest {
    
    @Test
    public void testCountTotalHapp() {
        Tournament t = new Tournament();
        t.rounds = 3;
        t.roundDays = new int[] {   0, 1, 3 };
        
        List<Tournament> list = new ArrayList<>();
        list.add(t);
        
        int h = BruteForce.bruteForceHappiness(list, 1, new int[1]);
        
        assertEquals(1, h);
        
        Happiness hap = new Happiness(1, 1);
        hap.addTournament(t);
        
        assertEquals(1, hap.getNumerator());
        
        h = BruteForce.bruteForceHappiness(list, 2, new int[2]);
        
        assertEquals(3, h);
        
        h = BruteForce.bruteForceHappiness(list, 7, new int[7]);
        
        assertEquals(17, h);
        
        h = BruteForce.bruteForceHappiness(list, 4, new int[4]);
        
        assertEquals(8, h);
        
        hap = new Happiness(4, 1);
        hap.addTournament(t);
        assertEquals(8, hap.getNumerator());
        
        t = new Tournament();
        t.rounds = 2;
        t.roundDays = new int[] { 0, 2 };
        
        list.add(t);
        
        h = BruteForce.bruteForceHappiness(list, 4, new int[4]);
        
        assertEquals(16*5 + 2, h);
        
        hap = new Happiness(4, 2);
        hap.addTournament(list.get(0));
        hap.addTournament(list.get(1));
        assertEquals(16*5+2, hap.getNumerator());
        
        t = new Tournament();
        t.rounds = 2;
        t.roundDays = new int[] { 0, 3 };
        list.add(t);
        BruteForce.debug = true;
        h = BruteForce.bruteForceHappiness(list, 4, new int[4]);
        
        assertEquals(560, h);
        
        hap = new Happiness(4, 3);
        hap.addTournament(list.get(2));
        hap.addTournament(list.get(0));
        hap.addTournament(list.get(1));
        
        assertEquals(560, hap.getNumerator());
        
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

	private static Map<String, String> testData;

	@BeforeClass
	public static void getTestData() {
		testData = new HashMap<>();

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
				String value = n.getTextContent();

				String name = n.getAttributes().getNamedItem("name")
						.getNodeValue();

				testData.put(name, value);
			}
		} catch (Exception ex) {

		}

	}

	

	@Test
	public void testS1() {
		String testCase = testData.get("s1");

		//double output = Double.parseDouble(extractAns(getOutput(testCase)));
		
		//assertEquals(7, output, DoubleComparator.TOLERANCE);
	}
	
	
}
