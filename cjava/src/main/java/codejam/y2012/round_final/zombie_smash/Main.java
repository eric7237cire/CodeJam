package codejam.y2012.round_final.zombie_smash;

import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.datastructures.IndexMinPQ;
import codejam.utils.geometry.PointInt;
import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public String[] getDefaultInputFiles() {
        return new String[] { "sample.in" };
     //    return new String[] { "B-small-practice.in" };
       //  return new String[] { "B-small-practice.in", "B-large-practice.in" };
    }

    @Override
    public InputData readInput(Scanner scanner, int testCase) {

        /*
        each starting with a line containing a single integer Z, the number of zombies in the level.

        The next Z lines contain 3 space-separated integers each, representing the location and time at which a given zombie will appear and disappear.
    The ith line will contain the integers Xi, Yi and Mi, where:

        Xi is the X coordinate of the cell at which zombie i appears,
        Yi is the Y coordinate of the cell at which zombie i appears,
        Mi is the time at which zombie i appears, in milliseconds after the beginning of the game. 
        The time interval during which the zombie can smashed is inclusive: if you reach the cell 
        at any time in the range [Mi, Mi + 1000] with a charged Zombie Smasher, you can smash the 
        zombie in that cell.
        */
        
        InputData in = new InputData(testCase);

        in.Z = scanner.nextInt();
        
        in.zombieLoc = new PointInt[in.Z];

        in.zombieAppearance = new int[in.Z];

        for (int i = 0; i < in.Z; ++i) {
            in.zombieLoc[i] = new PointInt(scanner.nextInt(), scanner.nextInt());
            in.zombieAppearance[i] = scanner.nextInt();
        }
        return in;
    }

    class State implements Comparable<State>
    {
        //Time last zombie was smashed, Last zombie smashed, Number of zombies already smashed)
        int timeLastZombieSmashed;
        Integer lastZombieSmashed;
        int numberZombiesAlreadySmashed;
        public State(int timeLastZombieSmashed, Integer lastZombieSmashed, int numberZombiesAlreadySmashed) {
            super();
            this.timeLastZombieSmashed = timeLastZombieSmashed;
            this.lastZombieSmashed = lastZombieSmashed;
            this.numberZombiesAlreadySmashed = numberZombiesAlreadySmashed;
        }
        @Override
        public int compareTo(State o) {
            return ComparisonChain.start()
                    .compare(timeLastZombieSmashed, o.timeLastZombieSmashed)
                    .compare(o.numberZombiesAlreadySmashed, numberZombiesAlreadySmashed)
                    .compare(lastZombieSmashed, o.lastZombieSmashed)
                    .result();
        }
        
    }
    
    int gridTimeToTravel( PointInt p1, PointInt p2) {
        return 100 * Math.max( Math.abs( p1.getX() - p2.getX()), Math.abs( p1.getY() - p2.getY()));
    }
    
    public void generateStates(List<State> states, int[][] stateToIndex, InputData in)
    {
        states.clear();
        
        states.add(new State(0, null, 0)); // Include the initial state.
        
        for(int z = 0; z < in.Z; ++z)
        {
            for(int zombies_killed = 1; zombies_killed <= in.Z; ++zombies_killed)
            {
                // For other reachable states this will be revised later.
                int earliest_smash_time = Integer.MAX_VALUE;
                if (zombies_killed == 1)
                {
                  int earliest_arrival_time = gridTimeToTravel( new PointInt(0,0), in.zombieLoc[z]);
                  if (earliest_arrival_time <= in.zombieAppearance[z] + 1000)
                    earliest_smash_time = Math.max( in.zombieAppearance[z],
                                              earliest_arrival_time);
                }
                
                stateToIndex[z][zombies_killed] = states.size();
                
                states.add( new State(earliest_smash_time, z, zombies_killed) );
                
                
            }
        }
            
        
    }
    
    int solve(InputData in)
    {
        List<State> states  = Lists.newArrayList();
        int[][] stateToIndex = new int[in.Z][in.Z+1];
        generateStates(states, stateToIndex, in);
        
        IndexMinPQ<State> Q = new IndexMinPQ<>(states.size());
        
        for(int i = 0; i < states.size(); ++i) {
            Q.insert(i, states.get(i));
        }
        
        int maxZombiesSmashed = 0;
        
        while (!Q.isEmpty()) {
            State s = Q.minKey();
            int stateDeletedIdx = Q.delMin();

            Preconditions.checkState(stateDeletedIdx == 0 || stateToIndex[s.lastZombieSmashed][s.numberZombiesAlreadySmashed] == stateDeletedIdx);

            if (s.timeLastZombieSmashed == Integer.MAX_VALUE) // infinity
                break;

            maxZombiesSmashed = Math.max(maxZombiesSmashed, s.numberZombiesAlreadySmashed);

            for (int z = 0; z < in.Z; ++z) {
                if (s.lastZombieSmashed != null && z == s.lastZombieSmashed)
                    continue;

                PointInt fromLoc = s.lastZombieSmashed == null ? new PointInt(0,0) : in.zombieLoc[s.lastZombieSmashed];
                
                int earliest_arrival_time = s.timeLastZombieSmashed +
                Math.max(750, gridTimeToTravel( fromLoc, in.zombieLoc[z]));
                if (earliest_arrival_time <= in.zombieAppearance[z] + 1000) {
                    int earliest_smash_time = Math.max(in.zombieAppearance[z], earliest_arrival_time);

                    State updatedState = new State(earliest_smash_time, z, s.numberZombiesAlreadySmashed + 1);
                    int index = stateToIndex[updatedState.lastZombieSmashed][updatedState.numberZombiesAlreadySmashed];
                    
                    if (Q.contains(index) && Q.keyOf(index).timeLastZombieSmashed > earliest_smash_time)
                        Q.decreaseKey(index, updatedState);
                }

            }

        }
              
        return maxZombiesSmashed;
    }
                
    
    public String handleCase(InputData in) {

        int maxZombiesKilled = solve(in);
        
        return String.format("Case #%d: %d", in.testCase, maxZombiesKilled);
        
    }

}
