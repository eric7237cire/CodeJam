package codejam.y2008.round_pracContest.cycles;

import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import codejam.utils.datastructures.graph.CCbfs;
import codejam.utils.datastructures.graph.GraphInt;
import codejam.utils.datastructures.graph.GraphIntAlgorithms;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.LargeNumberUtils;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class Main extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    final static Logger fileLog = LoggerFactory.getLogger("file");
    
    public Main()
    {
        
        super("C", 1,1);
       // (( ch.qos.logback.classic.Logger) log).setLevel(Level.OFF);
        (( ch.qos.logback.classic.Logger) fileLog).setLevel(Level.OFF);
    }
    
    
    @Override
    public InputData readInput(Scanner scanner, int testCase)
    {

        InputData in = new InputData(testCase);
        
        in.N = scanner.nextInt();
        in.K = scanner.nextInt();
        
        in.forbiddenEdges = Lists.newArrayList();
        
        for(int i = 0; i < in.K; ++i) {
            in.forbiddenEdges.add(new ImmutablePair<>(scanner.nextInt()-1, scanner.nextInt()-1));
        }
        return in;
    }
    
   
    
    @Override
    public String handleCase(InputData in)
    {
        int mod = 9901;
        int[] fact = LargeNumberUtils.generateModFactorial(in.N, mod);
        
        //For each subset of the forbidden edges
        
        int count = 0;
        
        nextCombin:
        for(int combin = 0; combin < 1 << in.K; ++combin)
        {
            GraphInt g = new GraphInt();
            
            int edgeCount = 0;
            
            boolean debug = false;
           
            
            //Build graph using those edges
            for(int edge = 0; edge < in.K; ++edge) {
                if ( (combin & 1 << edge) == 0)
                    continue;
                
                g.addConnection(in.forbiddenEdges.get(edge).getLeft(), in.forbiddenEdges.get(edge).getRight());
                
                ++edgeCount;
            }
            
            if (debug)
            log.debug("\n*********************************************\n" +
            		"Looking at sub graph\n {}\n edges {} max degree {} combin {}", g, edgeCount,
            		GraphIntAlgorithms.getMaximumDegree(g),
                    StringUtils.leftPad(Integer.toBinaryString(combin), in.K, '0'));
            
            /**
             * In a cycle, 2 and only 2 edges will be used, so it is impossible to use
             * all the edges if there exist more than 2 incident to the same vertex
             */
            if (GraphIntAlgorithms.getMaximumDegree(g) > 2) {
                fileLog.debug("combin {} sub {}", combin, 0);
                continue;
            }
        
            /*
             * To count number of cycles, we first permute the verticies that are
             * not incident to the current subset of forbidden edges
             */
            
            int permComponents = in.N - g.V();
            int perm = fact[ permComponents ];
            if (debug)
            log.debug("Perm components {} perm {}", permComponents, perm);
            
            CCbfs cc = new CCbfs(g);
            List<List<Integer>> connectedComp = cc.go();
            
            for(List<Integer> comp : connectedComp) {
                //First get an edge count, if it is >= # of verticies, there is a cycle
                int degreeSum = 0;
                for(Integer vComp : comp) {
                    degreeSum += g.getNeighbors(vComp).size();
                }
                
                Preconditions.checkState(degreeSum % 2 == 0);
                int nEdges = degreeSum / 2;
                
                //In order for these vertiecs to be connected, we need at least V - 1 edges
                Preconditions.checkState(nEdges >= comp.size() - 1);
                
                /**
                 * If there is a sub cycle, then it is impossible to have 
                 * a hamiltonian cycle of length N as it would mean at least 1 vertex
                 * would be visited more than once.
                 */
                if (nEdges >= comp.size() && comp.size() < in.N) {
                    if (debug)
                    log.debug("sub Cycle detected");
                    
                    fileLog.debug("combin {} sub {}", combin, 0);
                    
                    continue nextCombin;
                }
                
                /**
                 * Adding this chain means one more element to permute
                 */
                ++permComponents;
                
                /**
                 * m places to place chain, both backwards and forwards
                 */
                perm *= 2 * permComponents;
                perm %= mod;
            }
            
            //To divide in modulo arithmetic
            long inv = LargeNumberUtils.InverseEuler(2 * permComponents, mod);
            perm = (int) ((perm * inv) % mod); ///= 2 * permComponents;
            
            int mult = 1;
            
            if (edgeCount % 2 == 1) {
                mult = -1;
            }
            
            count += mult * perm;
            if (count < mod) {
                count += mod;
            }
            count %= mod;
            if (debug)
            log.debug("Count is now {} added {}", count, mult*perm);
            
            fileLog.debug("combin {} sub {}", combin, perm*2);
        }
        
        return  String.format("Case #%d: %d", in.testCase, count);
        
    }
    
    

}