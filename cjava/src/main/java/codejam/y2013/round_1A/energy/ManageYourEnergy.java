package codejam.y2013.round_1A.energy;

import java.math.BigInteger;
import java.util.Arrays;
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
import codejam.utils.utils.IntegerPair;

import com.google.common.base.Preconditions;
import com.google.common.math.LongMath;


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
        //return handleMincostMaxflowBigInts(in);
        return handleUsingSolutionN2(in);
    }
    
    public String handleUsingSolutionN2(InputData in)
    {
        int[] energyAvail = new int[in.nActivity];
        int[] mustLeaveWith = new int[in.nActivity];
        
        //First is value, second is index
        IntegerPair[] sortedAct = new IntegerPair[in.nActivity];
        
        boolean[] used = new boolean[in.nActivity];
        
        log.debug("Initial/max energy {} recup {}", in.maxEnergy, in.energyRecup);
        
        for(int act = 0; act < in.nActivity; ++act)
        {
            log.debug("Activity {} value {}", act+1, in.actValue[act]);
            energyAvail[act] = in.maxEnergy;
            mustLeaveWith[act] = 0;
            sortedAct[act] = new IntegerPair(in.actValue[act], act);
        }
        
        Arrays.sort(sortedAct);
        
        long ans = 0;
        for(int sortAct = sortedAct.length-1; sortAct >= 0; --sortAct)
        {
            final int index = sortedAct[sortAct].second();
            final int energy = energyAvail[index] - mustLeaveWith[index];
            Preconditions.checkState(energy >= 0);
            
            ans += LongMath.checkedMultiply(energy, in.actValue[index]);
            log.debug("On activity {} spent {} for a gain of {}.  ans {}",
                    index+1, energy, energy * in.actValue[index], ans);
            
            used[index] = true;
            
            for(int idx = index-1;  idx >= 0; --idx)
            {
                if (used[idx])
                    break;
                int dist = index - idx;
                long energyCantUse = (long)energyAvail[index] - LongMath.checkedMultiply(in.energyRecup, dist);
                //Preconditions.checkState(energyCantUse <= canSpend[idx]);
                //canSpend[idx] -= energyCantUse;
                Preconditions.checkState((long)Integer.MAX_VALUE >= Math.max(energyCantUse, mustLeaveWith[idx]));
                mustLeaveWith[idx] = (int) Math.max(energyCantUse, mustLeaveWith[idx]);
                log.debug("before {}.  must reserve {} / {} on activity {}", index+1, 
                        mustLeaveWith[idx], energyAvail[idx], idx+1);
            }
            
            for(int idx = index + 1; idx < in.nActivity; ++idx)
            {
                if (used[idx])
                    break;
                
                int maxEnergy = (int) Math.min((long)mustLeaveWith[index]
                        +LongMath.checkedMultiply(in.energyRecup,(idx-index)),
                        (long)in.maxEnergy);
                Preconditions.checkState(maxEnergy <= energyAvail[idx]);
                energyAvail[idx] = maxEnergy;
                log.debug("after {}.  Can spend {} on activity {}", index+1, energyAvail[idx],idx+1);
            }
        }
        
        return String.format("Case #%d: %d", 
                in.testCase, ans
                );
    }
    
    public String handleMincostMaxflowBigInts(InputData in) 
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
    public String handleMincostMaxflowInt(InputData in) 
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
