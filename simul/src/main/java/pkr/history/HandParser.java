package pkr.history;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class HandParser
{

    public HandParser() {
        // TODO Auto-generated constructor stub
    }

    public static void main(String[] args) throws IOException {
        String brutefileName = "C:\\codejam\\CodeJam\\simul\\hands.txt";
        String fileName =  "C:\\codejam\\CodeJam\\simul\\output\\cleanhands.txt";
        File file = new File(fileName);
        File inputFile = new File(brutefileName);
        
        List<FlopTurnRiverState[]> hands = Parser.parseFile(inputFile, file);
        
        //StatsSession stats = computeStats(hands);
    }
}
