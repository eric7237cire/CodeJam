package codejam.y2009;

import static org.junit.Assert.assertEquals;

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

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import codejam.y2009.alphabetomials.Main;

/**
 * Unit test for simple App.
 */
public class AlphabetomialsTest {

    final static Logger log = LoggerFactory.getLogger(AlphabetomialsTest.class);

    /**
     * Create the test case
     * 
     * @param testName
     *            name of the test case
     */
    public AlphabetomialsTest() {
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

        String output = extractAns(getOutput(testCase));

        assertEquals("15 1032 7522 6864 253", output);
    }

    @Test
    public void testS2() {
        String testCase = testData.get("s2");

        String output = extractAns(getOutput(testCase));

        assertEquals("12 96 576", output);
    }

    @Test
    public void testCount() {
        Main m = new Main();
        m.polynomial = "a+e";
        int count = m.evalP("ejoe");
        assertEquals(2, count);

        m.polynomial = "aber+aab+c";
        count = m.evalP("abracadabra edgar");
        assertEquals(109, count);
    }

}
