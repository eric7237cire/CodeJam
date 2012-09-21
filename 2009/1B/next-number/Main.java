import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;

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
    public static void main(String args[]) throws Exception {
        Scanner scanner = new Scanner(new File("input-sam.txt"));
        int t = scanner.nextInt();
        
        for(int i = 1; i <= t; ++i) {
            int nInt = scanner.nextInt();
            String s = new Integer(nInt).toString();
            
            Multiset<Character> remaining = HashMultiset.create();
            for(int si = 0; si < s.length(); ++si) {
                remaining.add(s.charAt(si));
            }
            
            //gen all combinations
            SortedSet<String> comb = new TreeSet<String>(new NumCom());
            
            List<Node> nodes = new ArrayList<Node>();
            nodes.add(new Node(remaining));
            
            while(nodes.size() > 0) {
                Node n = nodes.remove(0);
                
                if (n.remaining.size() == 0) {
                    comb.add(n.num);
                    continue;
                }
                for(Character c : n.remaining.elementSet()) {
                    if (c.equals('0') && n.num == "") {
                        continue;
                    }
                    Multiset<Character> newRemaining = HashMultiset.create(n.remaining);
                    newRemaining.remove(c);
                    nodes.add(new Node(newRemaining, c + n.num));
                }
            }
            
            for(Iterator<String> it = comb.iterator(); it.hasNext();) {
                String num = it.next();
                System.out.format("String %s\n", num);
            }
            
            System.out.format("Case #%d: %s\n", i, "bob");
        }
    }
}