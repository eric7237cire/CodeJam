package codejam.y2013.round_qual.treasure;

import java.util.List;
import java.util.Scanner;
import java.util.Set;

import ch.qos.logback.classic.Level;
import codejam.utils.datastructures.graph.GraphInt;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.y2013.round_qual.treasure.InputData.Chest;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;


public class Treasure extends InputFilesHandler implements
TestCaseHandler<InputData>, TestCaseInputScanner<InputData> 
{
    public Treasure()
    {
        super("D", 1, 1);
        (( ch.qos.logback.classic.Logger) log).setLevel(Level.DEBUG);
       setLogInfo();
    }

    /* (non-Javadoc)
     * @see codejam.utils.main.Runner.TestCaseInputScanner#readInput(java.util.Scanner, int)
     */
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        
        InputData in = new InputData(testCase);
        
        int numStartingKeys, numChests;
        
        numStartingKeys = scanner.nextInt();
        numChests = scanner.nextInt();
        
        int maxKey = 0;
        
        //starting keys
        for(int i = 0; i < numStartingKeys; ++i)
        {
            int key = scanner.nextInt();
            in.startingKeys.add(key);
            
            maxKey = Math.max(key, maxKey);
        }
        
        
        
        //each chest
        for(int i = 0; i < numChests; ++i)
        {
            Chest c = new Chest();
            c.keyToOpen = scanner.nextInt();
            maxKey = Math.max(maxKey, c.keyToOpen);
            int keys = scanner.nextInt();
            
            //keys in chest
            for(int k = 0; k < keys; ++k)
            {
                int keyInChest = scanner.nextInt();
                maxKey = Math.max(maxKey, keyInChest);
                c.keys.add(keyInChest);
            }
            
            in.chests.add(c);
            
        }
            
        in.maxKey = maxKey;
        
        return in;
    }
    
    private final static int LIMIT = 1000000;
    
    private boolean backtrack2(int [] solution, boolean[] used, int solutionSize, int[] keys, InputData in) {

        if (solutionSize == in.chests.size())
        {
            return true;
        }
        ++count;
        log.debug("Backtrack, sol {} keys {}", solution, keys);
        
        if (count > LIMIT)
        {
            //log.warn("Slow");
            return false;
        }
        
        for(int i = 0; i < in.chests.size(); ++i)
        {
            
            if (used[i])
            {
                //Preconditions.checkState(ArrayUtils.contains(solution, i+1));
                continue;
                
                
            } else {
               // Preconditions.checkState(!ArrayUtils.contains(solution, i+1));
            }
            
            Preconditions.checkState(solution[solutionSize] == 0);
            
            Chest chest = in.chests.get(i);
            if (keys[chest.keyToOpen] == 0)
            {
                continue;
            }
            
            log.debug("sol {} keys {} Trying chest {}", solution, keys, i+1);
            
            keys[chest.keyToOpen]--;
            solution[solutionSize] = i + 1;            
            
            for(int k : chest.keys)
            {
                keys[k]++;
            }
            used[i] = true;
            boolean success = backtrack2(solution, used, 1+solutionSize, keys, in);
            if (success)
            {
                return true;
            }
            
            solution[solutionSize] = 0;
            for(int k : chest.keys)
            {
                keys[k]--;
            }
            keys[chest.keyToOpen]++;
            used[i] = false;
        }
        
        
        return false;
    }

    private boolean backtrack(List<Integer> solution, List<Integer> keys, InputData in) {

        if (solution.size() == in.chests.size())
        {
            return true;
        }
        ++count;
        log.debug("Backtrack, sol {} keys {}", solution, keys);
        
        if (count > LIMIT)
        {
            //log.warn("Slow");
            return false;
        }
        
        for(int i = 0; i < in.chests.size(); ++i)
        {
            
            if (solution.contains(i+1))
            {
                continue;
            }
            
            Chest chest = in.chests.get(i);
            if (!keys.contains(chest.keyToOpen))
            {
                continue;
            }
            
            log.debug("sol {} keys {} Trying chest {}", solution, keys, i+1);
            
            //List<Integer> newKeySet = Lists.newArrayList(keys);
            //boolean ok = newKeySet.remove((Object)chest.keyToOpen);
            boolean ok = keys.remove((Object)chest.keyToOpen);
            Preconditions.checkState(ok);
            
            solution.add(i+1);
            keys.addAll(chest.keys);
            boolean success = backtrack(solution, keys, in);
            if (success)
            {
                return true;
            }
            solution.remove(solution.size()-1);
            for(int k : chest.keys)
            {
                ok = keys.remove((Object) k);
                Preconditions.checkState(ok);
            }
            //keys.removeAll(chest.keys);
            keys.add(chest.keyToOpen);
        }
        
        
        return false;
    }

    private static int count = 0;
    
    public String useBf1(InputData in) 
    {
        List<Integer> solution = Lists.newArrayList();
        
        log.info("Problem {} # chests {}", in.testCase, in.chests.size());
        
        for(int c = 0; c < in.chests.size(); ++c)
        {
            Chest ch = in.chests.get(c);
            log.debug("Chest {} needs {} contains {}",c+1, ch.keyToOpen, ch.keys);
            
        }
       /* if (in.chests.size() > 12) {
            return String.format("Case #%d: SLOW", 
                    in.testCase); 
        }*/
        
        
        count = 0;
        boolean ok = backtrack(solution, in.startingKeys, in);
        
        log.info("Count {}", count);
        
        if (!ok)
        {
            return String.format("Case #%d: IMPOSSIBLE", 
                    in.testCase);
        } else {
            
            return String.format("Case #%d: %s", 
                    in.testCase, Joiner.on(' ').join(solution));
        }       
    }
    

    @Override
    public String handleCase(InputData in) 
    {
        log.info(useBf2(in));
        return realSolution(in);
        //return useBf1(in);
        //return useBf2(in);
    }
   
    /*
     * Input file D-small-practice.in
Problem 1 # chests 9
Case #1: 1 2 3 4 7 5 8 9 6
Problem 2 # chests 20
Case #2: SLOW
Problem 3 # chests 20
Case #3: SLOW
Problem 4 # chests 10
Case #4: 6 4 5 10 2 1 3 7 8 9
Problem 5 # chests 19
Case #5: 1 2 7 8 9 10 11 12 13 14 15 16 3 17 4 18 5 19 6
Problem 6 # chests 15
Case #6: 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15
Problem 7 # chests 1
Case #7: 1
Problem 8 # chests 20
Case #8: SLOW
Problem 9 # chests 20
Case #9: SLOW
Problem 10 # chests 1
Case #10: IMPOSSIBLE
Problem 11 # chests 1
Case #11: 1
Problem 12 # chests 20
Case #12: SLOW
Problem 13 # chests 20
Case #13: SLOW
Problem 14 # chests 1
Case #14: IMPOSSIBLE
Problem 15 # chests 5
Case #15: IMPOSSIBLE
Problem 16 # chests 10
Case #16: IMPOSSIBLE
Problem 17 # chests 15
Case #17: IMPOSSIBLE
Problem 18 # chests 20
Case #18: SLOW
Problem 19 # chests 2
Case #19: 2 1
Problem 20 # chests 4
Case #20: 2 1 4 3
Problem 21 # chests 5
Case #21: 1 2 3 4 5
Problem 22 # chests 20
Case #22: SLOW
Problem 23 # chests 13
Case #23: IMPOSSIBLE
Problem 24 # chests 15
Case #24: 1 2 3 5 6 4 7 8 9 10 11 12 15 13 14
Problem 25 # chests 3
Case #25: 1 2 3
Total time 6648

     */
    public String useBf2(InputData in) 
    {
       
        log.info("Problem {} # chests {}", in.testCase, in.chests.size());
        
        for(int c = 0; c < in.chests.size(); ++c)
        {
            Chest ch = in.chests.get(c);
            log.debug("Chest {} needs {} contains {}",c+1, ch.keyToOpen, ch.keys);
            
        }
        /*if (in.chests.size() > 11) {
            return String.format("Case #%d: SLOW", 
                    in.testCase); 
        }*/
        
        //Multiset<Integer> keySet = HashMultiset.create();
        //keySet.addAll(in.startingKeys);
        
        boolean[] used = new boolean[201];
        int[] keys = new int[201];
        
        for(int k : in.startingKeys)
        {
            keys[k]++;
        }
        
        int[] solArr = new int[201];
        
        count = 0;
        boolean ok = backtrack2(solArr, used, 0, keys, in);

        log.info("Count {}", count);
        
        if (!ok)
        {
            return String.format("Case #%d: IMPOSSIBLE", 
                    in.testCase);
        } else {
            StringBuffer sb = new StringBuffer();
            for(int i = 0; i < solArr.length; ++i)
            {
                if (solArr[i] == 0)
                    break;
                
                if (i > 0)
                    sb.append(' ');
                sb.append(solArr[i]);
            }
            return String.format("Case #%d: %s", 
                    in.testCase, sb.toString());
           
        }       
    }
    
    public String realSolution(InputData in) 
    {
       
        log.info("Problem {} # chests {}", in.testCase, in.chests.size());
        
        for(int c = 0; c < in.chests.size(); ++c)
        {
            Chest ch = in.chests.get(c);
            log.info(" Chest {} needs {} contains {}.  ",c+1, ch.keyToOpen, ch.keys);
            
        }
        
        boolean[] inSolution = new boolean[in.chests.size()];
        
        int[] curKeyCounts = new int[in.maxKey+1];
        for(int key : in.startingKeys)
        {
            curKeyCounts[key]++;
        }
        
        log.debug("Starting keys {}", curKeyCounts);
        
        int solutionSize = 0;
        
        int[] curSolution = new int[in.chests.size()];
        
        boolean solvable = checkConnectedNess(curSolution, solutionSize, inSolution, curKeyCounts, in);
        
        if (!solvable)
        {
            return String.format("Case #%d: IMPOSSIBLE", 
                    in.testCase);
        }
        
        //6 4 5 8 10 2 1 3 7 9
        
        while(solutionSize < in.chests.size())
        {
            boolean foundChest = false;
            log.debug("keys {}", keyList(curKeyCounts));
            log.debug("Cur sol {}", curSolution);
            Preconditions.checkState(curSolution[solutionSize] == 0);
            
            if (solutionSize == 3)
            {
                //return "guh";
            }
            
            for(int c = 0; c < in.chests.size(); ++c)
            {
                if (inSolution[c])
                {
                    continue;
                }
                Chest ch = in.chests.get(c);
                log.debug("Looking at Chest {} needs {} contains {}.  curSol {}",c+1, ch.keyToOpen, ch.keys, curSolution);
                
                if (curKeyCounts[ch.keyToOpen] > 0)
                {
                    curKeyCounts[ch.keyToOpen]--;
                    for(int keyInChest : ch.keys)
                    {
                        curKeyCounts[keyInChest]++;
                    }
                    inSolution[c] = true;
                    curSolution[solutionSize] = c+1;
                    ++solutionSize;
                    
                    boolean ok = checkConnectedNess(curSolution, solutionSize, inSolution, curKeyCounts, in);
                    
                    if (!ok)
                    {
                        curKeyCounts[ch.keyToOpen]++;
                        for(int keyInChest : ch.keys)
                        {
                            curKeyCounts[keyInChest]--;
                        }
                        inSolution[c] = false;
                        curSolution[solutionSize] = 0;
                        --solutionSize;
                        
                    } else {
                        log.debug("Adding chest {}", c+1);
                        foundChest = true;
                        break;
                    }
                }
                
                
            }
            
            Preconditions.checkState(foundChest);
        }
        
        
        
       
            StringBuffer sb = new StringBuffer();
            for(int i = 0; i < curSolution.length; ++i)
            {
                if (i > 0)
                    sb.append(' ');
                sb.append(curSolution[i]);
            }
            return String.format("Case #%d: %s", 
                    in.testCase, sb.toString());
           
           
    }

    /**
     * 
     * @param curSolution list of chests used, 0 based index used
     * @param solutionSize
     * @param inSolution [] is 0 based chest index
     * @param curKeyCounts [] is key, 0 never used
     * @param in
     * @return
     */
    boolean checkConnectedNess(int[] curSolution, int solutionSize, 
            boolean[] inSolution, int[] curKeyCounts, InputData in)
    {
        
        int[] keySums = new int[curKeyCounts.length];
        int[] keyNeeds = new int[curKeyCounts.length];
        //boolean[] canReach = new boolean[curKeyCounts.length];
        
        for(int chest = 0; chest < inSolution.length; ++chest)
        {
            if (inSolution[chest])
            {
                continue;
            }
            
            Chest chestObj = in.chests.get(chest);
            keyNeeds[chestObj.keyToOpen]++;
            
            for(int keyInChest : chestObj.keys)
            {
                keySums[keyInChest]++;
            }
        }
        for(int key = 1; key < curKeyCounts.length; ++key )
        {
            keySums[key] += curKeyCounts[key];
            
            if (keyNeeds[key] > keySums[key])
            {
                log.debug("Not enough keys for chests");
                return false;
            }
        }
        
        //À ce point, nous avons assez de clés, 
        //maintenant cherchons si on peut atteindre au moins une de chaque clé
        
        GraphInt g = createGraph(inSolution, curKeyCounts, in);
        
        for(int key = 1; key < curKeyCounts.length; ++key )
        {
            log.debug("Checking key {}", key);
            
            if (curKeyCounts[key] > 0 || keyNeeds[key] == 0)
            {
                continue;
            }
            
            boolean found = dfsSearch(0, new boolean[curKeyCounts.length], key, g);
            
            if (!found) 
            {
                log.debug("Could not reach key {}", key);
                return false;
            } else {
                log.debug("Can reach key {}", key);
            }
            
        }
        
        return true;
    }
    
    
    boolean dfsSearch(int curVertex, boolean[] visited, int targetVertex, GraphInt g)
    {
        if (curVertex == targetVertex)
            return true;
        
        visited[curVertex] = true;
        Set<Integer> adj = g.getNeighbors(curVertex);
        if (adj == null)
            return false;
        
        for(int v : adj)
        {
            if (visited[v])
                continue;
            
            boolean found = dfsSearch(v, visited, targetVertex, g);
            
            if (found)
                return true;
        }
        
        return false;
    }
    
    GraphInt createGraph(boolean[] inSolution, int[] curKeyCounts, InputData in)
    {
        GraphInt g = new GraphInt();
        for(int c = 0; c < in.chests.size(); ++c)
        {
            if (inSolution[c])
            {
                continue;
            }
            Chest ch = in.chests.get(c);
            //Créer connexion de clé nécessaire pour l'ouvrir vers les clé qu'il contient
            for(int k : ch.keys)
            {
                log.debug("Chest {} key {} to {}", c+1, ch.keyToOpen, k);
                g.addOneWayConnection(ch.keyToOpen, k);
            }
            //log.debug("Chest {} needs {} contains {}",c+1, ch.keyToOpen, ch.keys);
            
        }
        
        //root (node 0) to start keys 
        for(int key = 0; key < curKeyCounts.length; ++key)
        {
            if (curKeyCounts[key] == 0)
                continue;
            
            log.debug("Root to key  {}", key);
            g.addOneWayConnection(0, key);
        }
        
        return g;
    }
    
    String keyList(int[] keyCounts)
    {
        List<Integer> l = Lists.newArrayList();
        for(int k = 1; k < keyCounts.length; ++k)
        {
            for(int c = 0; c < keyCounts[k]; ++c)
            {
                l.add(k);
            }
        }
        
        return Joiner.on(", ").join(l);
    }
}
