package codejam.y2013.round_1A.energy;

import java.math.BigInteger;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.tuple.Pair;

import codejam.utils.datastructures.graph.MincostMaxflow;
import codejam.utils.datastructures.graph.MincostMaxflow2;
import codejam.utils.datastructures.graph.MincostMaxflow2.Edge;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.math.NumericBigInt;
import codejam.utils.multithread.Consumer.TestCaseHandler;


public class ManageYourEnergy extends InputFilesHandler implements
TestCaseHandler<InputData>, TestCaseInputScanner<InputData> 
{
    public ManageYourEnergy()
    {
        super("B", 1, 1);
        setLogInfo();
        
    }

    /* (non-Javadoc)
     * @see codejam.utils.main.Runner.TestCaseInputScanner#readInput(java.util.Scanner, int)
     */
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        
        InputData in = new InputData(testCase);
        
        in.maxEnergy = scanner.nextInt();
        in.energyRecup = scanner.nextInt();
        in.nActivity = scanner.nextInt();
        
        in.actValue = new int[in.nActivity];
        
        for(int i = 0; i < in.actValue.length; ++i)
        {
            in.actValue[i] = scanner.nextInt();
        }
        
        
        return in;
    }
    


    /* (non-Javadoc)
     * @see codejam.utils.multithread.Consumer.TestCaseHandler#handleCase(java.lang.Object)
     */
    @Override
    public String handleCase(InputData in) 
    {
        return handleBigInts(in);
    }
    
    public String handleBigInts(InputData in) 
    {
        MincostMaxflow<BigInteger, BigInteger> flow = new MincostMaxflow<>(new NumericBigInt(0L), new NumericBigInt(0L));
        
        //flow.
        //List<Edge>[] graph = MincostMaxflow2.createGraph(2+2*in.nActivity);
        int sink = 1+2*in.nActivity;
        int source = 0;
        
        flow.addArc(0, 1, new NumericBigInt(in.maxEnergy), new NumericBigInt(0));
        
        
        /*
         * Activity 1:
         * 
         * Collector node 1, output node 2
         * 
         * Activity 2:
         * 
         * Collector node 3, output node 4
         */
        
        for(int act = 1; act <= in.nActivity; ++act)
        {
            int actInputNodeId = act*2 - 1;
            int actOutputNodeId = actInputNodeId+1;
            
            
            flow.addArc(actInputNodeId, actOutputNodeId, new NumericBigInt(in.maxEnergy), new NumericBigInt(0));
            
            
            //Edges between activities output of current to input of next
            if (act < in.nActivity)
                flow.addArc(actOutputNodeId, actInputNodeId+2, new NumericBigInt(in.maxEnergy), new NumericBigInt(0));
            
            //Energy recup
            if (act > 1)
                flow.addArc(source, actInputNodeId, new NumericBigInt(in.energyRecup), new NumericBigInt(0));
            
            flow.addArc(actOutputNodeId, sink, new NumericBigInt(in.maxEnergy), new NumericBigInt(-in.actValue[act-1]));
        }
        
        Pair<BigInteger, BigInteger> flowCost= flow.getFlow(source, sink);
        
       log.info("Flow {} flow cost {}", flowCost.getLeft(), flowCost.getRight());
        return String.format("Case #%d: %d", 
                in.testCase, flowCost.getRight().negate()
                );
       
       
    }
    public String handle1(InputData in) 
    {
        List<Edge>[] graph = MincostMaxflow2.createGraph(2+2*in.nActivity);
        int sink = 1+2*in.nActivity;
        
        MincostMaxflow2.addEdge(graph, 0, 1, in.maxEnergy, 0);
        
        /*
         * Activity 1:
         * 
         * Collector node 1, output node 2
         * 
         * Activity 2:
         * 
         * Collector node 3, output node 4
         */
        
        for(int act = 1; act <= in.nActivity; ++act)
        {
            int actInputNodeId = act*2 - 1;
            int actOutputNodeId = actInputNodeId+1;
            
            MincostMaxflow2.addEdge(graph, actInputNodeId, actOutputNodeId, 
                    in.maxEnergy, 0);
            
            //Edges between activities output of current to input of next
            if (act < in.nActivity)
                MincostMaxflow2.addEdge(graph, actOutputNodeId, actInputNodeId+2, 
                        in.maxEnergy, 0);
            
            //Energy recup
            if (act > 1)
                MincostMaxflow2.addEdge(graph, 0, actInputNodeId, in.energyRecup, 0);
            
            MincostMaxflow2.addEdge(graph, actOutputNodeId, sink, in.maxEnergy, -in.actValue[act-1]);
        }
        
        long[] res = MincostMaxflow2.minCostFlow(graph, 0, sink, Integer.MAX_VALUE);
        long flow = res[0];
        long flowCost = res[1];
       log.info("Flow {} flow cost {}", flow, flowCost);
        return String.format("Case #%d: %d", 
                in.testCase, -flowCost
                );
       
       
    }

   
}
