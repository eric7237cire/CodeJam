package codejam.y2009;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.test.TesterBase;
import codejam.y2009.round_3.interesting_ranges.BruteForce;
import codejam.y2009.round_3.interesting_ranges.InputData;
import codejam.y2009.round_3.interesting_ranges.Interval;
import codejam.y2009.round_3.interesting_ranges.InterestingRanges;
import codejam.y2009.round_3.interesting_ranges.PalinSpace;


/**
 * Unit test for simple App.
 */
public class InterestingRangesTest extends TesterBase<InputData> {

	final static Logger log = LoggerFactory.getLogger(InterestingRangesTest.class);

	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	public InterestingRangesTest() {
		super(new InterestingRanges());
	}


	@BeforeClass
    public static void getTestData() {
        initTestData(InterestingRangesTest.class.getResourceAsStream(
                    "interesting-ranges.xml"));
            
    }
	
	@Test
	public void testPartial() 
	{
	    
	    InterestingRanges.getPartialRange(7, 3, BigInteger.valueOf(7500));
	}
	

	@Test
	public void testMainMethod() {
	    
	    
	    for(int i = 1; i < 200; ++i) {
	        log.debug("i is {}", i);
	        BigInteger check = BruteForce.countTotal(1, i, true);
	        Interval intv = InterestingRanges.calc(BigInteger.valueOf(i));
	        assertEquals("Failed at " + i, check, intv.totalEven);
	    }
	}
	
	@Test
	public void testNumBetween() {
	    assertEquals(new BigInteger("10999"), PalinSpace.calcNumBetween(7, 0));
	    assertEquals(new BigInteger("1099"), PalinSpace.calcNumBetween(7, 1));
	    assertEquals(new BigInteger("109"), PalinSpace.calcNumBetween(7, 2));
	    assertEquals(new BigInteger("10"), PalinSpace.calcNumBetween(7, 3));
	    
	    //1 to 9
	    assertEquals(new BigInteger("0"), PalinSpace.calcNumBetween(0, 0));
	    
	    //10 to 99
	    assertEquals(new BigInteger("10"), PalinSpace.calcNumBetween(1, 0));
	    
        //100 to 999
        assertEquals(new BigInteger("9"), PalinSpace.calcNumBetween(2, 0));
        assertEquals(new BigInteger("10"), PalinSpace.calcNumBetween(2, 1));
       
        //1000 to 9999
        assertEquals(new BigInteger("109"), PalinSpace.calcNumBetween(3, 0));
        assertEquals(new BigInteger("10"), PalinSpace.calcNumBetween(3, 1));
        
        //10000 to 99999
        assertEquals(new BigInteger("99"), PalinSpace.calcNumBetween(4, 0));
        assertEquals(new BigInteger("109"), PalinSpace.calcNumBetween(4, 1));
        assertEquals(new BigInteger("10"), PalinSpace.calcNumBetween(4, 2));
        
	}
	

	@Test
	public void testPalinSpace() {
	    
	    
	    Interval i = InterestingRanges.palinSpace.segments.get(3).get(new BigInteger("990"));
	    Interval check = InterestingRanges.calcEvenPairRanges(new BigInteger("4005"), new BigInteger("4994"));
	    assertEquals(check, i);
	    
	    i = InterestingRanges.palinSpace.segments.get(6).get(new BigInteger("800080"));
        check = InterestingRanges.calcEvenPairRanges(new BigInteger("5000006"), new BigInteger("5800085"));
        assertEquals(check, i);
        
        i = InterestingRanges.palinSpace.segments.get(7).get(new BigInteger("77000"));
        check = InterestingRanges.calcEvenPairRanges(new BigInteger("60000007"), new BigInteger("60077006"));
        assertEquals(check, i);
        
        i = InterestingRanges.palinSpace.segments.get(7).get(new BigInteger("200200"));
        check = InterestingRanges.calcEvenPairRanges(new BigInteger("60000007"), new BigInteger("60200206"));
        assertEquals(check, i);
        
        i = InterestingRanges.palinSpace.segments.get(7).get(new BigInteger("7000070"));
        check = InterestingRanges.calcEvenPairRanges(new BigInteger("60000007"), new BigInteger("67000076"));
        assertEquals(check, i);
        
        i = InterestingRanges.palinSpace.segments.get(7).get(new BigInteger("1000010"));
        check = InterestingRanges.calcEvenPairRanges(new BigInteger("60000007"), new BigInteger("61000016"));
        assertEquals(check, i);
        
        i = InterestingRanges.palinSpace.segments.get(7).get(new BigInteger("11000"));
        check = InterestingRanges.calcEvenPairRanges(new BigInteger("60000007"), new BigInteger("60011006"));
        assertEquals(check, i);
        
        i = InterestingRanges.palinSpace.segments.get(7).get(new BigInteger("11000"));
        check = InterestingRanges.calcEvenPairRanges(new BigInteger("60000007"), new BigInteger("60011006"));
        assertEquals(check, i);
        
        i = InterestingRanges.palinSpace.segments.get(7).get(new BigInteger("80000008"));
        check = InterestingRanges.calcEvenPairRanges(new BigInteger("10000002"), new BigInteger("90000009"));
        assertEquals(check, i);
        
        i = InterestingRanges.palinSpace.segments.get(7).get(new BigInteger("89999998"));
        check = InterestingRanges.calcEvenPairRanges(new BigInteger("10000002"), new BigInteger("99999999"));
        assertEquals(check, i);
        
        InterestingRanges.palinSpace.segments.get(6);
        
	    assertEquals(check, i);
	}
	@Test
	public void testFullRangeSmall() {
	    int exp = 0;
        //1 - 2 until 1 - 10
        for(int num = 1; num <= 10; ++num) {
            Interval intv = InterestingRanges.getFullRange(num, exp);
            Interval check = BruteForce.createInterval(1, num);
            assertEquals("Num " + num, check, intv);
        }
        
        exp = 1;
        //1 - 20 until 1 - 100
        for(int num = 1; num <= 10; ++num) {
            Interval intv = InterestingRanges.getFullRange(num, exp);
            Interval check = BruteForce.createInterval(1, 10*num);
            assertEquals("\nGood\n" + check + "\nBad\n" + intv + " num " + num + " exp " + exp, check, intv);
        }
        
        
	}
	
	@Test 
    public void testRangeSliceSmall() {
	    int exp = 0;
	    //1 - 2 until 1 - 10
	    for(int num = 2; num <= 10; ++num) {
	        Interval intv = InterestingRanges.getRangeSlice(num, exp);
	        Interval check = BruteForce.createInterval(1, num);
	        assertEquals("Num " + num, check, intv);
	    }
	    
	    exp = 1;
        //11 - 20 until 11 - 100
        for(int num = 2; num <= 10; ++num) {
            Interval intv = InterestingRanges.getRangeSlice(num, exp);
            Interval check = BruteForce.createInterval(11, 10*num);
            assertEquals("\nGood\n" + check + "\nBad\n" + intv + " num " + num + " exp " + exp, check, intv);
        }
        
        
        
	}
	
	
	@Test 
	public void testCountRangeSmall() {
	    
	    BigInteger c = BruteForce.countTotal(10, 99,true);
	    
	    Interval beforeFirstPalin = new Interval(10);
	    
	    Interval palin = new Interval(1);
	    Interval empties = Interval.createEmpty(10);
	    
	    Interval total = beforeFirstPalin;
	    assertEquals(BruteForce.countTotal(10, 10,true), total.totalEven);
	    for(int i = 0; i < 8; ++i) {
	        total = Interval.combin(total, palin);
	        total = Interval.combin(total, empties);
	        
	        if (i==0) {
	            assertEquals(BruteForce.countTotal(10, 21,true), total.totalEven);
	        }
	    }
	    
	    total = Interval.combin(total, palin);
	    
	    assertEquals(c, total.totalEven);
	    
	}
	@Test
	public void testIntervalSingle() {
	    Interval i1 = new Interval(10);
	    Interval t_i = BruteForce.createInterval(10,10);
	    
	    assertEquals(t_i, i1);
	    Interval i2 = new Interval(11);
	    Interval t_ii = BruteForce.createInterval(11,11);
	    
	    assertEquals(t_ii, i2);
	    
	    Interval c = Interval.combin(i1,i2);
	    Interval c_t = BruteForce.createInterval(10, 11);
	    
	    assertEquals(c_t, c);
	    
	    for (int i = 12; i <= 62; ++i) {
	        c = Interval.combin(c,new Interval(i));
            c_t = BruteForce.createInterval(10, i);
            
           // log.debug("{}", c);
            assertEquals("Fail " + i + " good \n" + c_t + "\n bad\n" + c,c_t, c);
	    }
        
        
	}
	
    @Test
    public void testSubtract() {

        Interval big = BruteForce.createInterval(1, 100);
        for (int i = 1; i <= 99; ++i) {
            Interval small = BruteForce.createInterval(1, i);
            Interval diff = Interval.subtract(small, big);
            Interval check = BruteForce.createInterval(i + 1, 100);

            // log.debug("{}", c);
            assertEquals("Fail " + i + " small \n " + small + " big \n " + big
                    + "\n good \n" + check + "\n bad\n" + diff, check, diff);
        }

    }

    @Test
    public void testSubtract2() {

        int TEST_UPPER = 20;

        Interval[] cacheInt = new Interval[TEST_UPPER + 1];
        for (int i = 1; i <= TEST_UPPER; ++i) {
            Interval small = BruteForce.createInterval(1, i);
            cacheInt[i] = small;
        }

        for (int lb = 2; lb <= TEST_UPPER / 2; ++lb) {
            for (int up = lb + 1; up <= TEST_UPPER; ++up) {
                Interval small = cacheInt[lb - 1];
                Interval big = cacheInt[up];

                Interval sub = Interval.subtract(small, big);
                // Interval check = BruteForce.createInterval(lb, up);
                BigInteger checkCount = BruteForce.countTotal(lb, up, true);

                // log.debug("{}", c);
                assertEquals("Fail " + lb + " small \n " + small + " big \n "
                        + big + "\n" + "\n bad\n" + sub, checkCount,
                        sub.totalEven);
            }

        }

    }

	
	
	

	
	
}
