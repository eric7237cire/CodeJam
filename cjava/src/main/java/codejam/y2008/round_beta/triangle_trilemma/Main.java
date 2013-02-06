package codejam.y2008.round_beta.triangle_trilemma;

import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.geometry.Line;
import codejam.utils.geometry.TriangleInt;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.collect.Lists;
import com.google.common.math.DoubleMath;

public class Main extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    public Main()
    {
       // super();
        super("A", true,true);
    }
    
    
    @Override
    public InputData readInput(Scanner scanner, int testCase)
    {

        InputData in = new InputData(testCase);
        
        in.t = new TriangleInt(scanner.nextInt(),scanner.nextInt(),
        scanner.nextInt(),scanner.nextInt(),
        scanner.nextInt(),scanner.nextInt());

        return in;
    }

        
    @Override
    public String handleCase(InputData in)
    {
        StringBuffer sb = new StringBuffer();
        sb.append("Case #" + in.testCase + ": ");
                
        List<Double> sides = in.t.getSides();
        
        List<Double> angles = Lists.newArrayList();
        
        for(int i = 0; i < 3; ++i) {
            angles.add(TriangleInt.lawCosines(sides.get( (i+1)%3 ),sides.get( (i+2)%3),sides.get(i)));
        }
        
        List<Line> lines = Lists.newArrayList();
        lines.add(new Line(in.t.p1.toPoint(),in.t.p2.toPoint()));
        lines.add(new Line(in.t.p1.toPoint(),in.t.p3.toPoint()));
        lines.add(new Line(in.t.p2.toPoint(),in.t.p3.toPoint()));
        
        for(int l = 0; l < lines.size(); ++l) {
            for(int l2 = l + 1; l2 < lines.size(); ++l2) {
                if (lines.get(l).isParallel(lines.get(l2))) {
                    sb.append("not a triangle");
                    return sb.toString();
                }
            }
        }
        
        boolean isIso = false;
        
        for(int s = 0; s < sides.size(); ++s) {
            for(int s2 = s+1; s2 < sides.size(); ++s2) {
                if (DoubleMath.fuzzyEquals(sides.get(s), sides.get(s2), 0.0001)) {
                    isIso = true;
                    break;
                }
            }
        }
        
        if (isIso) {
            sb.append("isosceles ");
        } else {
            sb.append("scalene ");
        }
        
        boolean isNotAcute = false;
        
        for(Double ang : angles) {
            if (DoubleMath.fuzzyEquals(Math.PI / 2, ang, 0.0001)) {
                isNotAcute = true;
                sb.append("right ");
                break;
            }
            if (DoubleMath.fuzzyCompare(ang, Math.PI / 2, 0.0001) > 0) {
                isNotAcute = true;
                sb.append("obtuse ");
                break;
            }
            
        }
        
        if (!isNotAcute) {
            sb.append("acute ");
        }
                    
        sb.append("triangle");
        return sb.toString();
    }

}