package codejam.y2009.round_final.marbles;

import java.util.Arrays;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.base.Preconditions;

public class Sol implements TestCaseHandler<InputData>, 
TestCaseInputScanner<InputData>, DefaultInputFiles {

    final static Logger log = LoggerFactory.getLogger(Sol.class);

    @Override
    public String[] getDefaultInputFiles() {
   //    return new String[] {"sample.in"};
        //return new String[] {"E-small-practice.in"};
       return new String[] {"E-small-practice.in", "E-large-practice.in"};
    }

	int[] pair;
	int[] dir;
	
	
	boolean[][] e;
	int INF = 10000;

	public boolean dfsAssignDirection(int v, InputData in, boolean[] g){
	    int n = in.N;
		g[v] = true;
		for(int i = 0;i<2*n;i++)
		{
		    //Are the 2 colors alternating?
			if (e[v][i]){
			    //If I is in the graph
				if (g[i]){
				    //Then the directions cannot be the same
				    //as one must go up, the other down
					if (dir[i]==dir[v]) return false;
				} else {
				    Preconditions.checkState(dir[i] == 0);
				    
				    //Set the direction of i to be the opposite of v
					dir[i] = 3-dir[v];
					
					//And add v to the graph
					if (!dfsAssignDirection(i, in,g)) return false;
				}
			}
		}
		return true;
	}



	public int[] solve(int left, int right, InputData in){
	    int n = in.N;
	    
		if (left==right){
			int[] res = new int[n+1];
			for(int i = 0;i<=n;i++) res[i] = 0;
			return res;
		}
		if (pair[left]<left) throw new RuntimeException("B2");
		
		
		Preconditions.checkState(dir[left] == 0);
		
		//Assign a direction to marble pair starting at left 
		dir[left] = 1;
		
		//Assign directions to all connected marble pairs, (those that are alternating)
		boolean[] g = new boolean[2*n];
		Arrays.fill(g,false);
		if (!dfsAssignDirection(left,in,g)) return new int[]{-1};
		
		//Looks like only the first marble gets assigned a direction, so
		//the second must be zero...
		for(int i = left;i<right;i++)
			if (dir[i]*dir[pair[i]]!=0) throw new RuntimeException("B3");
		
		//Check theory that only first marble every gets assigned a direction
		for(int i = 0;i<2*n;i++) { 
            if (i < pair[i])
                continue;
            
            Preconditions.checkState(dir[i] == 0);
        }
		
		/**
		 * Given res[upHeightLimit] = minDownHeight
		 */
		int[] res = new int[n+1];
        int nUp = 0;
        int nDown = 0;
        
        for (int i = left; i < right; i++)
        {
            //The pair is not alternating / connected to i
            if (dir[i] == 0 && dir[pair[i]] == 0)
            {
                int j = i;
                //Find right most non connected pair.  j can equal right 
                while (j < right && dir[j] == 0 && dir[pair[j]] == 0)
                    j++;
                int[] c = solve(i, j, in);
                
                //Not solvable, so we are done
                if (c[0] == -1)
                    return new int[] { -1 };
                
                //Anything below nUp is now impossible because
                //now there is a sub component there
                for (int t = 0; t < nUp; t++)
                    res[t] = INF;
                
                for (int t = nUp; t <= n; t++) {
                    /*Given a height limit of t we know
                     Currently we have nUp paths above this component
                     and nDown below.
                     
                       Recalculate if we have a up height limit of
                       t, then this component will only have t - nUp
                       up height to work with.  It's answer for the
                       minimum down height needed is then added to
                       the number of paths down that are current
                     */ 
                    res[t] = Math.max(res[t], nDown + c[t - nUp]);
                    
                }
                
                //Advance i 
                i = j - 1;
            } else if (dir[i] == 1)
            {
                //This height is now impossible
                res[nUp] = INF;
                nUp++;
            } else if (dir[pair[i]] == 1)
            {
                //An up path just finished
                nUp--;
            } else if (dir[i] == 2)
            {
                nDown++;
                //For each height limit, we need at least down
                for (int t = 0; t <= n; t++)
                    if (res[t] < nDown)
                        res[t] = nDown;
            } else if (dir[pair[i]] == 2)
            {
                nDown--;
            } else
                throw new RuntimeException("B");
        }
        for (int upHeight = 0; upHeight <= n; upHeight++)
        {
            if (res[upHeight] < INF)
            {
                //TODO the main magic point left to understand a bit more
                
                //t is min down needed, given i up height
                int downHeightMinimum = res[upHeight];
                
                //Now lets say we flip the situtation,
                //then given an upHeight = downHeightMinimum ; downHeightMinimum = upHeight,
                //the downHeight required should not exceed upHeight 
                
                //given t height, number of down needed can't exceed i up height (ie, swap up / down )
                res[downHeightMinimum] = Math.min(res[downHeightMinimum], upHeight);
            }
        }
        //Percolate values up
        for (int i = 0; i < n; i++)
            res[i + 1] = Math.min(res[i + 1], res[i]);
        return res;
	}

	public boolean isBetween(int l, int r, int x){
		return (l<x) && (x<r);
	}
	
	public InputData readInput(Scanner scanner, int testCase) {
	    InputData in = new InputData(testCase);
        
        in.N = scanner.nextInt();
        int n = in.N;
        String[] s = new String[2*n];
        for(int i = 0;i<2*n;i++)
            s[i] = scanner.next();
        
        in.s = s;
        return in;
	}

	@Override
    public String handleCase(InputData in) {
	    
	    int n = in.N;
		String[] s = in.s;
		        
		pair = new int[2*n];
		dir = new int[2*n];
		
		// find 2nd color
		for(int i = 0;i<2*n;i++)
			for(int j = 0;j<2*n;j++) if (i!=j && s[i].equals(s[j]))
				pair[i] = j;
		
		
		e = new boolean[2*n][2*n];
		for(int i = 0;i<2*n;i++) { 
		    
		    //Only look at the first one
		    if (i > pair[i])
		        continue;
		        
			for(int j = 0;j<2*n;j++) {
			    //Only first one
			    if (j > pair[j])
			        continue;
			    
			    if (i!=j && (isBetween(i,pair[i],j)!=isBetween(i,pair[i],pair[j]))){
					e[i][j] = true;
					
					
				}
			}
		}
		
		//Confirm that edges are only between the first marble		
		for(int i = 0;i<2*n;i++) { 
		    if (i < pair[i])
		        continue;
		    
		    for(int j = 0; j < 2*n; ++j) {
		        Preconditions.checkState(e[i][j] == false);
		        Preconditions.checkState(e[j][i] == false);
		    }
		}
		
		//Check that edges are symmetric / graph is not directed
		for(int i = 0;i<2*n;i++) {
		    for(int j = 0; j < 2*n; ++j) {
		        Preconditions.checkState(e[i][j] == e[j][i]);
		    }
		}
		
		int[] r = solve(0,2*n,in);
		if (r[0]==-1) {
		    return String.format("Case #%d: -1", in.testCase);
		}
		int min = 10000;
		for(int i =0;i<=n;i++)
			if (i+r[i]<min) min = i+r[i];
		
		return String.format("Case #%d: %d", in.testCase, min);
	}

	
}
