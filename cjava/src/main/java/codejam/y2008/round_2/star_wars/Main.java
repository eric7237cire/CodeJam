package codejam.y2008.round_2.star_wars;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import ch.qos.logback.classic.Level;
import codejam.utils.linear.Simplex;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.DoubleFormat;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.math.DoubleMath;

public class Main extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

    
    public Main()
    {
        super("C", 1, 1, 1);
        (( ch.qos.logback.classic.Logger) log).setLevel(Level.INFO);
    }
    
    
   
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
       
        InputData in = new InputData(testCase);
       
        in.N = scanner.nextInt();
        in.x = new int[in.N];
        in.y = new int[in.N];
        in.z = new int[in.N];
        in.p = new int[in.N];
        
        for(int i = 0; i < in.N; ++i)
        {
            in.x[i] = scanner.nextInt();
            in.y[i] = scanner.nextInt();
            in.z[i] = scanner.nextInt();
            in.p[i] = scanner.nextInt();
        }
        return in;
    }

  
    int findFurthestShip(double x, double y, double z, double Y, boolean[] edgeShips, InputData in)
    {
        double largestPowerNeed = Integer.MIN_VALUE;
        int furthestShipIndex = -1;
        
        for(int i = 0; i < in.N; ++i)
        {
            double powerNeeded = (Math.abs(in.x[i]-x)+Math.abs(in.y[i]-y)+Math.abs(in.z[i]-z))/in.p[i];
            
            if (DoubleMath.fuzzyCompare(powerNeeded, Y, 1e-5) > 0 && powerNeeded > largestPowerNeed)
            {
                log.info("Ship {} needed power {}",i,powerNeeded);
                largestPowerNeed = powerNeeded;
                furthestShipIndex = i;
            }
        }
        
        if (furthestShipIndex >= 0)
            edgeShips[furthestShipIndex] = true;
        
       return furthestShipIndex; 
    }
    
    @Override
    public String handleCase(InputData in) {
       
        boolean[] edgeShips = new boolean[in.N];
        
        int iterCheck = 0;
        double ans = 42;
        
        findFurthestShip(0,0,0,0.00001, edgeShips,in);
        
        while(true)
        {
            ++iterCheck;
            
            Preconditions.checkState(iterCheck <= 300);
            
            Simplex s = new Simplex(4);
            s.addObjectiveFunctionToMinimize(Arrays.asList(0d,0d,0d,1d));
            
            for(int i = 0; i < in.N; ++i)
            {
                if (!edgeShips[i])
                    continue;
                
                for(int dx=-1; dx <= 1; dx+=2)
                {
                    for(int dy=-1; dy <= 1; dy+=2)
                    {
                        for(int dz=-1; dz <= 1; dz+=2)
                        {
                            /*
                             * abs(x_i  - x) + ... <= p_i * Y
                             * x_i - x    ==>  -x ... <= p_i * Y - x_i
                             * -x_i + x
                             */
                            s.addConstraintLTE(Arrays.<Double>asList(
                                    (double)dx,
                                    (double)dy,
                                    (double)dz,
                                    (double)-in.p[i]
                                    ), (double) dx * in.x[i] + dy * in.y[i] + dz * in.z[i]);
                            
                            log.debug( "{} x {} y {} z - {} Y <= {} + {} + {} = {}",
                                    dx,dy,dz, -in.p[i],dx * in.x[i], dy * in.y[i], dz * in.z[i],
                                    (double) dx * in.x[i] + dy * in.y[i] + dz * in.z[i]);
                        }
                        
                    }
                    
                }
            }
        
            List<Double> sol = Lists.newArrayList();
            s.solve(sol);
            int si = findFurthestShip(sol.get(0),sol.get(1),sol.get(2),sol.get(3), edgeShips,in);
            log.info("Adding ship {} to the boundary list. sol {} {} {}  Y {}", si, sol.get(0),sol.get(1),sol.get(2),sol.get(3));
            if (si < 0) {
                ans = sol.get(3);
                break;
            }
                
        }
        

        return String.format("Case #%d: %s", in.testCase, DoubleFormat.df7.format(ans));
        
    }
}