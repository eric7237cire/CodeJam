import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

import com.google.common.base.Predicate;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multiset;
import com.google.common.collect.Ordering;

public class Main {
    static class NumCom implements Comparator<String> {

        /* (non-Javadoc)
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        @Override
        public int compare(String o1, String o2) {
            if (o1.length() != o2.length()) {
                return new Integer(o1.length()).compareTo(new Integer(o2.length()));
            }
            
            return new Integer(Integer.parseInt(o1, 10)).compareTo(Integer.parseInt(o2,10));
        }
        
    }
    
    static class Node {
        Multiset<Character> remaining;
        String num;
        
        Node(Multiset<Character> remaining) {
            this.remaining = remaining;
            num = "";
        }
        
        Node(Multiset<Character> remaining, String num) {
            this.remaining = remaining;
            this.num = num;
        }
    }
    
    
    public static void main(String args[]) throws Exception {
        
        boolean debug = false; 
        
        PrintStream sos = System.out;
        PrintStream os = System.out;
        
        if (!debug) {
            os = new PrintStream(new File("out.txt"));
        }
        
        
        Scanner scanner = new Scanner(new File(debug ? "sample.txt" : "B-small-practice.in"));
        
        int t = scanner.nextInt();
        
        for(int i = 1; i <= t; ++i) {
            int nInt = scanner.nextInt();
            String nStr = new Integer(nInt).toString();
            
            List<Integer> listDigits  = new ArrayList<Integer>();
            
            listDigits.add(0);
            
            for(int si = 0; si < nStr.length(); ++si) {
                listDigits.add(Character.digit(nStr.charAt(si), 10));
            }
            
            //Find right side group that is not descending
            Ordering<Integer> intRevOrder = Ordering.natural().reverse();
            Ordering<Integer> intOrder = Ordering.natural();
            
            for(int leftIndex = 1; leftIndex < listDigits.size() ; ++ leftIndex) {
                List<Integer> subList = listDigits.subList(leftIndex, listDigits.size());
                
                //os.print(subList);
                if (intRevOrder.isOrdered(subList)) {
                	//sos.println("yeah");
                	
                	final int nextDigitValue = listDigits.get(leftIndex - 1);
                	
                	Iterable<Integer> candidates = Iterables.filter(subList, new Predicate<Integer>() {
                		 public boolean apply(Integer input) {
                			 return input > nextDigitValue;
                		 }
                	});
                	
                	Integer swapValue = intOrder.min(candidates);
                	int swapIndex = listDigits.lastIndexOf(swapValue);
                	
                	Collections.swap(listDigits, swapIndex, leftIndex - 1);
                	//sos.print(listDigits);
                	//sos.print(subList);
                	Collections.sort(subList);
                	
                	if (listDigits.get(0) == 0) {
                		listDigits.remove(0);
                	}
                	
                	String s = StringUtils.join(listDigits, "");
                	//os.print(s);
                	
                	os.format("Case #%d: %s\n", i, s);
                	break;
                } 
            }
            
        }
    }
}