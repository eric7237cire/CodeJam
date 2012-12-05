package com.eric.codejam;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eric.codejam.Main.SingleColumnCounts;
import com.google.common.base.Objects;
import com.google.common.math.IntMath;
import com.google.common.math.LongMath;
import com.google.common.primitives.Ints;

public class OldLargeSolution {
    final static Logger log = LoggerFactory.getLogger(Main.class);
    
    final Main m;
    public OldLargeSolution(Main m) {
        this.m=m;
    }
    
    public static class TermCount {
        int termCount;
        int leadingZeroCount;
        boolean hasAnyZerosAsDigits;
        
        @Override
        public int hashCode() {
            return Objects.hashCode(termCount, leadingZeroCount, hasAnyZerosAsDigits);
        }
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            TermCount other = (TermCount) obj;
            return Objects.equal(leadingZeroCount, other.leadingZeroCount) &&
                    Objects.equal(termCount, other.termCount) &&
                    Objects.equal(hasAnyZerosAsDigits, other.hasAnyZerosAsDigits);
        }
        public TermCount(int termCount, int leadingZeroCount, boolean hasAnyZerosAsDigits) {
            super();
            this.termCount = termCount;
            this.leadingZeroCount = leadingZeroCount;
            this.hasAnyZerosAsDigits = hasAnyZerosAsDigits;
        }
        @Override
        public String toString() {
            return "TermCount [termCount=" + termCount + ", leadingZeroCount="
                    + leadingZeroCount + ", hasAnyZerosAsDigits="
                    + hasAnyZerosAsDigits + "]";
        }
        
    }
    
    public static class OutgoingTermCount {
        Map<TermCount, Long> frequency;
        
        public OutgoingTermCount() {
            frequency = new HashMap<>();
        }
        
        public void add(TermCount tc, long countLong) {
            
            if (frequency.containsKey(tc)) {
                countLong = LongMath.checkedAdd(countLong, frequency.get(tc));                
            }
            
            frequency.put(tc, countLong % Main.MOD);
            
        }
        
        public long getSumCount() {
            long count = 0;
            for(TermCount tc : frequency.keySet() ) {
                
                count = LongMath.checkedAdd(count, frequency.get(tc));
            }
            return count;
        }
    }
    public void combineCounts(SingleColumnCounts columnCount, OutgoingTermCount prevColumnCount, OutgoingTermCount tally) {
        // log.debug("Combine {} {}", columnCount.set.elementSet().size(), prevColumnCount.frequency.size());
         
         for(int colTermCount : columnCount.set.elementSet()) {
             int colTermFreq = columnCount.set.count(colTermCount);
             
             for(TermCount prevColTermCount : prevColumnCount.frequency.keySet()) {
                 long tcFreq = prevColumnCount.frequency.get(prevColTermCount);
                 
                 /*Case 1.  Fix terms from this column to terms from previous column that do not have leading zeros
                 */
                 if (colTermCount <= prevColTermCount.termCount) {
                     //Choose which prev column terms to attach a digit to the left 
                     long p = m.perm(prevColTermCount.termCount, colTermCount);
                     int zeroLeadingTerms = prevColTermCount.termCount - colTermCount;
                     
                     tally.add(new TermCount(colTermCount, IntMath.checkedAdd(zeroLeadingTerms,prevColTermCount.leadingZeroCount), prevColTermCount.hasAnyZerosAsDigits), 
                             LongMath.checkedMultiply(LongMath.checkedMultiply(p, tcFreq) % Main.MOD, colTermFreq));
                     
                 }
                 
                 //Case 2.  Create a term with trailing zeros.  This means any term with leading zeros cannot be used
                 if (colTermCount - 1 <= prevColTermCount.termCount && !prevColTermCount.hasAnyZerosAsDigits) {
                     long p = LongMath.checkedMultiply(colTermCount, m.perm(prevColTermCount.termCount, colTermCount-1)) % Main.MOD;
                     int zeroLeadingTerms = prevColTermCount.termCount - (colTermCount-1);
                     
                     tally.add(new TermCount(colTermCount, zeroLeadingTerms, true), LongMath.checkedMultiply(LongMath.checkedMultiply(p, tcFreq) % Main.MOD, colTermFreq));
                 }
                 
                 //Case 3.  Attach a term to a prev col term that had leading zeros
                 if (colTermCount - 1 <= prevColTermCount.termCount && prevColTermCount.leadingZeroCount > 0) {
                     long p =LongMath.checkedMultiply( LongMath.checkedMultiply(colTermCount, m.perm(prevColTermCount.leadingZeroCount, 1)),
                              m.perm(prevColTermCount.termCount, colTermCount-1) ) % Main.MOD;
                     int zeroLeadingTerms = prevColTermCount.termCount - (colTermCount-1) ;
                     
                     tally.add(new TermCount(colTermCount, zeroLeadingTerms, true), LongMath.checkedMultiply(LongMath.checkedMultiply(p, tcFreq) % Main.MOD, colTermFreq));
                 }
                 
                 
             }
             
           //Case 2 again in case prev column is completely empty.  Create a term with trailing zeros.  This means any term with leading zeros cannot be used
             if (colTermCount == 1 && prevColumnCount.frequency.isEmpty()) {
                 tally.add(new TermCount(1,0, true), 1);
             }
         }
         
         if (columnCount.set.isEmpty()) {
             for(TermCount prevColTermCount : prevColumnCount.frequency.keySet()) {
                 long tcFreq = prevColumnCount.frequency.get(prevColTermCount);
                 tally.add(new TermCount(0, IntMath.checkedAdd( prevColTermCount.leadingZeroCount,prevColTermCount.termCount), prevColTermCount.hasAnyZerosAsDigits), tcFreq);
             }
         }
       
     }
     
     public long solve(final long n, SingleColumnCounts[][] termCounts, final int base) {
         
         final int maxCol = Main.getMaxColumn(n,base);
         final int maxCarryOver = Main.getMaxCarryOver(base);
         final int maxColumnSum = (base-1)*base / 2;
         
         OutgoingTermCount[][] outTermCounts = new OutgoingTermCount[maxCol][maxCarryOver+1];
         
         //Initialize first column
         
         int columnDigit = Ints.checkedCast(n % base);
         
         for(int outgoingCarry = 0; outgoingCarry <= maxCarryOver; ++ outgoingCarry) {
             int columnSum = outgoingCarry * base + columnDigit;
             
             if (columnSum > maxColumnSum)
                 break;
             
             SingleColumnCounts singleColCount = termCounts[columnSum][base-1];
             
             if (singleColCount == null)
                 break;
             
             if ( (long)columnSum > n)  {
                 break;
             }
             
             OutgoingTermCount outTermCount = new OutgoingTermCount();
             for(Integer termCount : singleColCount.set.elementSet()) {
                 outTermCount.frequency.put(new TermCount(termCount,0,false), (long) singleColCount.set.count(termCount));
             }
             
             outTermCounts[0][outgoingCarry] = outTermCount;
         }
         
         
         for(int column = 2; column <= maxCol; ++column) {
             
             log.debug("Column {} n {} base {} max carry over {}",column,n,base, maxCarryOver);
             
             columnDigit = Main.getDigitInColumn(n,column,base);
             
             for(int outgoingCarry = 0; outgoingCarry <= maxCarryOver; ++ outgoingCarry) {
                 boolean atLeastOneValid = false;
                 OutgoingTermCount outTermCount = new OutgoingTermCount();
                                 
                 for(int incomingCarry = 0; incomingCarry <= maxCarryOver; ++incomingCarry) {
                     int columnSum = outgoingCarry * base + columnDigit - incomingCarry;
                     
                     if (columnSum < 0 || columnSum > maxColumnSum)
                         continue;
                     
                     OutgoingTermCount incomingTermCount = outTermCounts[column-1 - 1][incomingCarry];
                     
                     if (incomingTermCount == null)
                         continue;
                 
                     atLeastOneValid = true;
                     
                     SingleColumnCounts singleColCount = termCounts[columnSum][base-1];
                     
                     combineCounts(singleColCount, incomingTermCount,outTermCount);
                     
                     //OutgoingTermCount checkTermCount = new OutgoingTermCount();
                     //combineCounts(singleColCount, incomingTermCount,checkTermCount);
                     
                    /* if (n==53)
                     log.debug("n {} column {} next carry {} in carry {} count {} new total count {}",n, column, 
                             outgoingCarry, incomingCarry, checkTermCount.getSumCount(), outTermCount.getSumCount());*/
                     
                 }
                 
                 if (outTermCount.frequency.size() == 0 && !atLeastOneValid) {
                     outTermCount = null;
                 }
                 
                 outTermCounts[column-1][outgoingCarry] = outTermCount;
             }
             
             for(int incomingCarry = 0; incomingCarry <= maxCarryOver; ++incomingCarry) {
                 outTermCounts[column-2][incomingCarry] = null;
             }
         }
         
         long count = 0;
         
         int outgoingCarry = 0;
        // for(int outgoingCarry = 0; outgoingCarry <= maxCarryOver; ++ outgoingCarry) {
             OutgoingTermCount outTermCount = outTermCounts[maxCol-1][outgoingCarry];
             
             
             for(TermCount tc : outTermCount.frequency.keySet() ) {
                 
                 count = LongMath.checkedAdd(count,outTermCount.frequency.get(tc));
             }
        // }
         
         return count;
         
     }
}
