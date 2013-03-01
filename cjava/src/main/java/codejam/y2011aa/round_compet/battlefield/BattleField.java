package codejam.y2011aa.round_compet.battlefield;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import ch.qos.logback.classic.Level;
import codejam.utils.datastructures.graph.CC;
import codejam.utils.datastructures.graph.Graph;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class BattleField extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>
{

    public BattleField()
    {
        super("B", 1, 1);
        (( ch.qos.logback.classic.Logger) log).setLevel(Level.INFO);
    }
    
    @Override
    public InputData readInput(Scanner scanner, int testCase) {

        InputData in = new InputData(testCase);
        in.N = scanner.nextInt();
        in.R = scanner.nextInt();
        
        Graph graph = new Graph(in.N);
        
        
        for(int i = 0; i < in.R; ++i) {
            int n1 = scanner.nextInt();
            int n2 = scanner.nextInt();
            graph.addEdge(n1,n2);
        }
        in.graph = graph;
        
        return in;
    }
    
    class ConnectedNodes implements Comparable<ConnectedNodes> {
        
        //Set<Integer> oddDegreeNodes = Sets.newHashSet();
        //Set<Integer> evenDegreeNodes = Sets.newHashSet();
        public int nOddDegree;
        public int nEvenDegree;

        @Override
        public int compareTo(ConnectedNodes o)
        {
            return ComparisonChain.start()
                    .compare(o.nOddDegree, nOddDegree)
                    .compare(o.nEvenDegree, nEvenDegree).result();
        }
        
    }
        
    public String handleCase(InputData in) {

        //First need to connect everything together
        
        CC cc = new CC(in.graph);
        
        List<ConnectedNodes> connected = Lists.newArrayList();
        
        for(int i = 0; i < cc.count(); ++i) {
            connected.add(new ConnectedNodes());
        }
        
        //For each connected component, find degree of each vertex
        for(int i = 0; i < in.graph.V(); ++i) {
            int degree = in.graph.adj(i).size();
            int comNum = cc.id(i);
            if (degree % 2 == 0 && degree > 0)
                //connected.get(comNum).evenDegreeNodes.add(i);
                connected.get(comNum).nEvenDegree ++;
            else if (degree % 2 == 1)
                connected.get(comNum).nOddDegree++;
                //connected.get(comNum).oddDegreeNodes.add(i);
        }
        
        Iterables.removeIf(connected, new Predicate<ConnectedNodes>(){
           public boolean apply(ConnectedNodes cn) {
               return cn.nEvenDegree + cn.nOddDegree == 0;
           }
        });
        
        //Sort, put components with an odd degree first
        
        Collections.sort(connected);
        
        int edgesAdded = 0;
        while(connected.size() >= 2) {
            
            ConnectedNodes A = connected.get(0);
            ConnectedNodes B = connected.get(1);
            
            ConnectedNodes C = new ConnectedNodes();
            C.nEvenDegree = A.nEvenDegree+B.nEvenDegree;
            C.nOddDegree = A.nOddDegree + B.nOddDegree;
            if (A.nOddDegree > 0 && B.nOddDegree > 0) {
                edgesAdded++;
                C.nOddDegree -= 2;
                C.nEvenDegree += 2;
                
            } else if (A.nOddDegree > 0) {
                Preconditions.checkState(B.nOddDegree == 0);
                edgesAdded++;
                //Odd of A becomes even degree, but an even degree of B becomes odd
            } else {
                //2 even become odd
                edgesAdded++;
                C.nOddDegree+=2;
                C.nEvenDegree-=2;
            }
            
            
            connected.remove(1);
            connected.remove(0);
        
            connected.add(C);
            Collections.sort(connected);
            
        }
        
        //Make each odd degree even
        edgesAdded += connected.get(0).nOddDegree / 2 + connected.get(0).nOddDegree % 2;
        
        return String.format("Case #%d: %d", in.testCase, edgesAdded); 
                
    }
}
