package com.eric.codejam;

import java.math.BigInteger;

import com.google.common.base.Preconditions;

//Using idea from solution
/*
 * 
The first idea required to solve the small dataset helps us 
reduce the number of segments to consider from O(N^2) to O(N).
 To achieve that, we rely on the following observation: [L,R] 
 can be represented as [0,R] minus [0,L-1]; thus it contains an
  even number of palindromes if and only if [0,L-1] and [0,R] 
  both contain even or both contain odd number of palindromes.

But how exactly does that help us? Suppose we know which of
 [0,X] segments contain even number of palindromes (we'll call
  them just "even 0-segments" further on) and which contain odd
   number of palindromes ("odd 0-segments"). We know that each
    interesting segment corresponds to exactly one pair of 
    even 0-segments or to exactly one pair of odd 0-segments. 
    But this is true the other way around as well: each pair 
    of distinct even 0-segments corresponds to exactly one
     interesting segment, and so does each pair of distinct
      odd 0-segments! (when X is between L-1 and R, inclusive).

That means that if there are A even 0-segments and B odd 0-segments, the answer is A*(A-1)/2+B*(B-1)/2.


An example

say we consider one segment 114 to 549

it can be represented 1 to 549 minus 1 to 113

the segment 114 to 549 is even if both 1 to 549 and 1 to 113 are odd or both are even

So a segment is represented ie   120 to 540   as 1..120 - 1..114  and 1..540 - 1..114

if both 1..120 and 1..540 are even or both are odd, then regardless of 1..114, it is even.

 The choosing all combinations of even and odd pairs we get
 even C 2 + odd C 2 


 */
public class SolutionInterval {
    BigInteger palinsCovered;
    
    BigInteger totalEven;
    
    BigInteger oddZeroSegments;
    BigInteger evenZeroSegments;
    
    BigInteger left;
    BigInteger right;
    BigInteger size; // right + left - 1

    SolutionInterval() {
        left = BigInteger.ZERO;
        right = BigInteger.ZERO;
        size = BigInteger.ZERO;
        
        oddZeroSegments = BigInteger.ZERO;
        evenZeroSegments = BigInteger.ZERO;
        
        palinsCovered = BigInteger.ZERO;
        totalEven = BigInteger.ZERO;
    }

    
    
    static SolutionInterval combin(SolutionInterval lhs, SolutionInterval rhs) {
        if (rhs.size.compareTo(BigInteger.ZERO) == 0) {
            return lhs;
        }
        if (lhs.size.compareTo(BigInteger.ZERO) == 0) {
            return rhs;
        }
        SolutionInterval total = new SolutionInterval();
        total.left = lhs.left;
        total.right = lhs.right.add(rhs.size);
        total.size = lhs.size.add(rhs.size);
        total.palinsCovered = lhs.palinsCovered.add(rhs.palinsCovered);

        total.evenZeroSegments = lhs.evenZeroSegments.add(rhs.evenZeroSegments);
        total.oddZeroSegments = lhs.oddZeroSegments.add(rhs.oddZeroSegments);    

        total.totalEven = total.evenZeroSegments
                .multiply(total.evenZeroSegments.subtract(BigInteger.ONE))
                .divide(BigInteger.valueOf(2))
                .add(total.oddZeroSegments.multiply(
                        total.oddZeroSegments.subtract(BigInteger.ONE)).divide(
                        BigInteger.valueOf(2)));
        return total;
    }
}
