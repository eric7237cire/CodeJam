package codejam.y2012.round_3.havannah;

import java.util.Map;
import java.util.Scanner;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.y2012.round_3.perfect_game.InputData;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles
{

    final static Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public String[] getDefaultInputFiles()
    {
        return new String[] { "sample.in" };
        //    return new String[] { "B-small-practice.in" };
        //  return new String[] { "B-small-practice.in", "B-large-practice.in" };
    }


    void enumerateCorners(InputData in, Map<Pair<Integer,Integer>, Integer> cellToIndex)
    {
        
    }
    
    @Override
    public InputData readInput(Scanner scanner, int testCase)
    {

        InputData in = new InputData(testCase);

        return in;
    }

    
    public String handleCase(InputData in)
    {
        StringBuffer r = new StringBuffer( String.format("Case #%d: ", in.testCase) );
        

        return r.toString();
    }

}
