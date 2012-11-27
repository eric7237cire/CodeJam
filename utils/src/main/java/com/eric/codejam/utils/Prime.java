package com.eric.codejam.utils;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.math.DoubleMath;

public class Prime {
    public static List<Integer> generatePrimes(int n) {
        boolean[] isPrime = new boolean[n+1];
        Arrays.fill(isPrime,true);
        List<Integer> primes = new ArrayList<Integer>(80000);
        
        int upperLimit = DoubleMath.roundToInt(Math.sqrt(n), RoundingMode.UP);
        for(int i = 2; i <= upperLimit; ++i) {
            if (!isPrime[i]) {
                continue;
            }
            for(int j = i * i; j <= n; j += i) {
                isPrime[j] = false;
            }
        }
        
        for(int j = 2; j <= n; ++j) {
            if (isPrime[j])
                primes.add(j);
        }
        
        return primes;
    }
}
