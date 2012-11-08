package com.eric.codejam;

import java.math.BigInteger;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BruteForce {
    
    final static Logger log = LoggerFactory.getLogger(Main.class);
    
    public static Interval createInterval(int lb, int up) {
        Interval range = new Interval();
        range.totalEven = countTotal(lb, up, true);
        range.evenLeft = countLeftEdge(lb, up, true);
        range.oddLeft = countLeftEdge(lb, up, false);
        range.evenRight = countRightEdge(lb, up, true);
        range.oddRight = countRightEdge(lb, up, false);
        range.isEvenSpanning = countPalin(lb,up) % 2 == 0;
        
        range.left = BigInteger.valueOf(lb);
        range.right = BigInteger.valueOf(up);
        range.size = BigInteger.valueOf(up-lb+1);
        
        return range;
    }
    /**
     * How many palin in a range
     * @param lb
     * @param up
     * @return
     */
    public static int countPalin(int lb, int up) {
        int count = 0;
        int countBlanks = 0;
        for(int i = lb; i <= up; ++i) {
            String s = Integer.toString(i, 10);
           // log.debug("String {}", s);
            if (s.endsWith(StringUtils.reverse(s.substring(0, s.length() / 2)))) {
                //log.debug("Is palin. {}.  Count {} Blanks {}", s, count, countBlanks);
                ++count;
                countBlanks = 0;
            } else {
                ++countBlanks;
            }
        }
        
        return count;
    }
    public static boolean isPalin(int i) {
        String s = Integer.toString(i, 10);
         return s.endsWith(StringUtils.reverse(s.substring(0, s.length() / 2)));
             
    }
    
    public static boolean isPalin(BigInteger i) {
        String s = i.toString(10);
         return s.endsWith(StringUtils.reverse(s.substring(0, s.length() / 2)));
             
    }
    
    /**
     * How many even # palin ranges
     * @param l
     * @param r
     * @param countEven
     * @return
     */
    public static BigInteger countTotal(int l, int r, boolean countEven) {
        int ret = 0;
        int rangeCount = 0;
        boolean[] isPalin = new boolean[r-l+1];
        for(int i = l; i <= r; ++i) {
            String s = Integer.toString(i, 10);
            if (s.endsWith(StringUtils.reverse(s.substring(0, s.length() / 2)))) {
                isPalin[i-l] = true;
            }
        }
        for(int lb = l; lb <= r; ++lb) {
            for(int up = lb; up <= r; ++up) {
                rangeCount++;
                int count = 0;
                for(int i = lb; i <= up; ++i) {
                    if (isPalin[i-l]) {                    
                        ++count;
                    }
                }
                
                if (countEven && count % 2 == 0) {
                    ++ret;
                } else if (!countEven && count % 2 == 1) {
                    ++ret;
                }
            }
        }
        
        //log.debug("Range count {} l {} r {}", rangeCount, l, r);
        return BigInteger.valueOf(ret);
    }
    
    public static int countLeftEdge(int l, int r,  boolean countEven) {
        int ret = 0;
        int rangeCount = 0;
        int lb = l;
            for(int up = lb; up <= r; ++up) {
                rangeCount++;
                int count = 0;
                for(int i = lb; i <= up; ++i) {
                    String s = Integer.toString(i, 10);
                   // log.debug("String {}", s);
                    if (s.endsWith(StringUtils.reverse(s.substring(0, s.length() / 2)))) {
                        ///log.debug("Is palin");
                        ++count;
                    }
                }
                
                if (countEven && count % 2 == 0) {
                    ++ret;
                } else if (!countEven && count % 2 == 1) {
                    ++ret;
                }
            }
        
        
        log.debug("Range odd count {} l {} r {}", rangeCount, l, r);
        return ret;
    }
    
    public static int countRightEdge(int l, int r,  boolean countEven) {
        int ret = 0;
        int rangeCount = 0;
        
        for(int lb = l; lb <= r; ++lb) {
            int up = r;
            rangeCount++;
            int count = 0;
            for(int i = lb; i <= up; ++i) {
                String s = Integer.toString(i, 10);
               // log.debug("String {}", s);
                if (s.endsWith(StringUtils.reverse(s.substring(0, s.length() / 2)))) {
                    ///log.debug("Is palin");
                    ++count;
                }
            }
            
            if (countEven && count % 2 == 0) {
                ++ret;
            } else if (!countEven && count % 2 == 1) {
                ++ret;
            }
        }
        
        
        log.debug("Range odd count {} l {} r {}", rangeCount, l, r);
        return ret;
    }
}
