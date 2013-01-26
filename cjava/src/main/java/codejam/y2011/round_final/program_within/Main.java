package codejam.y2011.round_final.program_within;

import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public String[] getDefaultInputFiles() {
      //  return new String[] { "sample.in" };
     //    return new String[] { "B-small-practice.in" };
         return new String[] { "C-small-practice.in", "C-large-practice.in" };
    }

    @Override
    public InputData readInput(Scanner scanner, int testCase) {

        InputData in = new InputData(testCase);

        in.N = scanner.nextInt();

       
        return in;
    }

    /**
     * Looked at the solution.  Basically you can never have
     * a straight that encompasses another, so use a greedy strategy
     * to add the card to the shortest straight.
     */
    public String handleCase(InputData in) {

        
        
        List<Rule> rules = Lists.newArrayList();
        
        String numBinary = Integer.toBinaryString(in.N);
        int writeNumberStateBase = 100;
        int subtractStateBase = 200;
        
        int leftBoundaryMark = 4;
        int rightBoundaryMark = 5;
        
        rules.add(new Rule(0, 0, 'E', writeNumberStateBase + 0, leftBoundaryMark));
        
        //write the number in binary, 3 means 2 ; 2 means 1
        for(int c = 0; c < numBinary.length(); ++c)
        {
            char ch = numBinary.charAt(c);
            rules.add(new Rule(writeNumberStateBase+c, 0, 'E', 
                writeNumberStateBase+c+1, ch == '1' ? 3 : 2));
        }
        
        //Write right boundary ; go into subtraction mode
        rules.add(new Rule(writeNumberStateBase+numBinary.length(), 0,
            'W', subtractStateBase, rightBoundaryMark)); 
        
        //Subtraction--
        //S0 --> need to subtract 1
        //S1 --> done subtracting
        
        rules.add(new Rule(subtractStateBase, 3,
            'W', subtractStateBase+1, 2));
        rules.add(new Rule(subtractStateBase, 2,
            'W', subtractStateBase, 3));
        
        rules.add(new Rule(subtractStateBase+1, 3,
            'W', subtractStateBase+1, 3));
        rules.add(new Rule(subtractStateBase+1, 2,
            'W', subtractStateBase+1, 2));
        
        rules.add(new Rule(subtractStateBase, 4,
            'R', 0, 0));
        
        int copyPhaseBase = 300;
        
        //Done subtracting, hit left boundary
        rules.add(new Rule(subtractStateBase+1, 4,
            'E', copyPhaseBase, 7));
        
        /*
        Copy phase 0 -- start copy
        copy phase 1 -- copy 2 to right
        copy phase 2 -- copy 3 to right
        copy phase 3 -- done copy  
        */
        
        //Start copying
        rules.add(new Rule(copyPhaseBase, 3,
            'E', copyPhaseBase+2, leftBoundaryMark));
        rules.add(new Rule(copyPhaseBase, 2,
            'E', copyPhaseBase+1, leftBoundaryMark));
        
        //Copy
        rules.add(new Rule(copyPhaseBase+1, 3,
            'E', copyPhaseBase+2, 2));
        rules.add(new Rule(copyPhaseBase+1, 2,
            'E', copyPhaseBase+1, 2));
        
        rules.add(new Rule(copyPhaseBase+2, 3,
            'E', copyPhaseBase+2, 3));
        rules.add(new Rule(copyPhaseBase+2, 2,
            'E', copyPhaseBase+1, 3));
        
        //Hit right boundary
        rules.add(new Rule(copyPhaseBase+1, rightBoundaryMark,
            'E', copyPhaseBase+3, 2));
        rules.add(new Rule(copyPhaseBase+2, rightBoundaryMark,
            'E', copyPhaseBase+3, 3));
        
        //Write right boundary and start subtraction
        rules.add(new Rule(copyPhaseBase+3, 0,
            'W', subtractStateBase, rightBoundaryMark));
        
        StringBuffer ruleText = new StringBuffer();
        
        ruleText.append(rules.size());
        ruleText.append("\n");
            
        for(Rule rule : rules)
        {
           // log.debug("Rule: {}", rule);
            ruleText.append( rule.toString() );
            ruleText.append("\n");
        }
        
        try {
            /*
        String text =
                IOUtils.toString(this.getClass().getResourceAsStream(
                        "/codejam/y2011/rules1.txt"));
                        */
        String text = ruleText.toString();
       
        log.debug(text);
        
        Simulator s = Simulator.fromStr(text);
        s.printStateLimit = 20;
        
        Integer finalLoc = s.go();
        
        Preconditions.checkState(finalLoc.equals(in.N));
        
        log.debug("Final state {}", finalLoc); 
        
        } catch (Exception ex) {
            log.debug("ex",ex);
        }
        ruleText.deleteCharAt(ruleText.length() - 1);
        return String.format("Case #%d: ", in.testCase) + ruleText.toString();
        
    }

}
