package codejam.y2012.round_final.xeno_archaeology;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.geometry.PointInt;
import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

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

        InputData in = new InputData(testCase);

        in.N = scanner.nextInt();

        in.blueTiles = Lists.newArrayList();
        in.redTiles = Lists.newArrayList();

        for (int i = 0; i < in.N; ++i) {
            PointInt p = new PointInt(scanner.nextInt(), scanner.nextInt());
            
            String color = scanner.next();
            
            if (".".equals(color)) {
                in.blueTiles.add(p);
            } else {
                Preconditions.checkState("#".equals(color));
                in.redTiles.add(p);
            }
            
        }
        return in;
    }
    
    class ManhatDistance implements Comparator<PointInt>
    {

        @Override
        public int compare(PointInt o1, PointInt o2) {
            int m1 = Math.abs(o1.getX()) + Math.abs((o1.getY()));
            int m2 = Math.abs(o2.getX()) + Math.abs((o2.getY()));
            return ComparisonChain.start()
                    .compare(m1, m2)
                    .compare(o2.getX(), o1.getX())
                    .compare(o2.getY(), o1.getY())
                    .result();
        }

       
        
    }

    /**
     * Looked at the solution.  Basically you can never have
     * a straight that encompasses another, so use a greedy strategy
     * to add the card to the shortest straight.
     */
    public String handleCase(InputData in) {

        PointInt bestCandidate = null;
        
        
        
        Ordering<PointInt> order = Ordering.from(new ManhatDistance()).nullsLast(); 
        
        for(int y = -201; y <= 201; ++y)
        {
            for(int x = -201; x <= 201; ++x)
            {
                //Suppose center is at x, y
                PointInt center = new PointInt(x,y);
                
                boolean ok = true;
                for(PointInt red : in.redTiles)
                {
                    int parity = center.getKingDistance(red) % 2;
                    if (parity == 0)
                    {
                        ok = false;
                        break;
                    }
                }
                
                for(PointInt blue : in.blueTiles)
                {
                    int parity = center.getKingDistance(blue) % 2;
                    if (parity == 1)
                    {
                        ok = false;
                        break;
                    }
                }
                
                if (ok && order.compare(center, bestCandidate) < 0)
                    bestCandidate = center;
            }
        }
                
        return String.format("Case #%d: %s", in.testCase,
                bestCandidate == null ? "Too damaged" : "" + bestCandidate.getX() + " " + bestCandidate.getY());
        
    }

}
