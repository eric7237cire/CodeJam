package pkr.history;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class TestTrial
{

    @SuppressWarnings("unused")
    public static void main(String[] args) throws IOException {
        String brutefileName = "C:\\codejam\\CodeJam\\simul\\src\\test\\resources\\parser\\testTrial.txt";
        String fileName =  "C:\\codejam\\CodeJam\\simul\\output\\cleanhandshistory.txt";
        
        
        File file = new File(fileName);
        File inputFile = new File(brutefileName);
        
        List<FlopTurnRiverState[]> hands = Parser.parseFile(inputFile, file);
        
        StatsSession stats = Parser.computeStats(hands);
    }
}
