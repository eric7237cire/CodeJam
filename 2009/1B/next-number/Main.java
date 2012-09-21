import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

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
    
    private static boolean strictlyDesc(String s) {
        if (s.length() <= 1) {
            return true;
        }
        
        for(int si = 0; si < s.length() - 1; ++si) {
            
            if (Character.digit(s.charAt(si), 10) < Character.digit(s.charAt(si+1), 10)) {
                return false;
            }
        }
        
        return true;
    }
    
    public static void main(String args[]) throws Exception {
        
        boolean debug = true; 
        
        PrintStream os = System.out;
        
        if (!debug) {
            os = new PrintStream(new File("out.txt"));
        }
        
        
        Scanner scanner = new Scanner(new File(debug ? "input-sam.txt" : "B-small-practice.in"));
        int t = scanner.nextInt();
        
        for(int i = 1; i <= t; ++i) {
            int nInt = scanner.nextInt();
            String nStr = new Integer(nInt).toString();
            
            if (strictlyDesc(nStr)) {
                
                /*
                String newStr = nStr.replaceAll("0", "");
                newStr = new StringBuffer(newStr).reverse().toString();
                
                newStr = StringUtils.rightPad(newStr, nStr.length()+1, '0');

                os.format("Case #%d: %s\n", i, newStr);
                continue;*/
                nStr = '0' + nStr ;
            }
            
            Multiset<Character> remaining = HashMultiset.create();
            for(int si = 0; si < nStr.length(); ++si) {
                remaining.add(nStr.charAt(si));
            }
            
            NumCom numCom = new NumCom();
            
            //gen all combinations
            SortedSet<String> comb = new TreeSet<String>(numCom);
            
            List<Node> nodes = new ArrayList<Node>();
            nodes.add(new Node(remaining));
            
            while(nodes.size() > 0) {
                Node n = nodes.remove(0);
                
                if (n.remaining.size() == 0) {
                    if (numCom.compare(nStr, n.num) < 0) {
                        comb.add(n.num);
                    }
                    
                    continue;
                }
                for(Character c : n.remaining.elementSet()) {
                    if (c.equals('0') && n.num == "") {
                        continue;
                    }
                    Multiset<Character> newRemaining = HashMultiset.create(n.remaining);
                    newRemaining.remove(c);
                    nodes.add(new Node(newRemaining, n.num + c));
                }
            }
            
            for(Iterator<String> it = comb.iterator(); it.hasNext();) {
                String num = it.next();
                System.out.format("String %s\n", num);
            }
            
            os.format("Case #%d: %s\n", i, comb.first());
        }
    }
}