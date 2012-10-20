package com.eric.codejam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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

	/**
	 * Rigourous Test :-)
	 */
	@Test
	public void testBaseCase() {
		String testCase = testData.get("testBaseCase");

		String output = getOutput(testCase);

		assertEquals("Case #1: Yes 1", output);
	}

	@Test
	public void testBaseCaseNo() {
		String testCase = "2 2 1 \n" + ".#\n" + "##";

		String output = getOutput(testCase);

		assertEquals("Case #1: No", output);
	}

	@Test
	public void testDigRightTwo() {
		String testCase = "3 3 1 " + "...\n" + "###\n" + ".##";

		String output = getOutput(testCase);

		assertEquals("Case #1: Yes 2", output);
	}

	@Test
	public void testDigFallTwo() {
		String testCase = "3 3 2 " + "...\n" + "###\n" + ".##";

		String output = getOutput(testCase);

		assertEquals("Case #1: Yes 1", output);
	}

	@Test
	public void testMultistep() {
		String testCase = testData.get("testMultistep");

		String output = getOutput(testCase);

		assertEquals("Case #1: Yes 2", output);
	}

	@Test
	public void testWalkRight() {
		String testCase = "4 5 1\n" + ".....\n" + "##..#\n" + "###..\n"
				+ "####.\n";

		String output = getOutput(testCase);

		assertEquals("Case #1: Yes 0", output);
	}

	@Test
	public void testDigThenWalk1() {
		String testCase = "4 5 1\n" + ".....\n" + "#####\n" + "..###\n"
				+ ".#...\n";

		String output = getOutput(testCase);

		assertEquals("Case #1: Yes 2", output);
	}

	@Test
	public void testCantWalk1() {
		String testCase = "3 6 1\n" + "..####\n" + "##.#.#\n" + "###.##\n";
		String output = getOutput(testCase);

		assertEquals("Case #1: Yes 2", output);

	}

	@Test
	public void testCantWalk2() {
		String testCase = "3 5 2\n" + "...##\n" + "#.#.#\n" + "##.##\n";
		String output = getOutput(testCase);

		assertEquals("Case #1: No", output);

	}

	@Test
	public void testFindRange() {
		String testCase = "4 5 1\n" + ".....\n" + "#####\n" + // 1
				"..###\n" + ".#...\n";

		Scanner sc = new Scanner(testCase);


		Main m = Main.buildMain(sc);

		int[] range = m.findWalkableRange(1, // row
				2, // col
				1, 2); // dug

		assertTrue(ArrayUtils.isEquals(new int[] { 2, 2 }, range));

		range = m.findWalkableRange(1, 2, 2, 4);

		assertTrue(ArrayUtils.isEquals(new int[] { 2, 4 }, range));

		range = m.findWalkableRange(0, 3);

		assertTrue(ArrayUtils.isEquals(new int[] { 0, 4 }, range));

	}
}
