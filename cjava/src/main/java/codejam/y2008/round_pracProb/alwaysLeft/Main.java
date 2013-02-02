package codejam.y2008.round_pracProb.alwaysLeft;

import java.util.Map;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.datastructures.BitSetInt;
import codejam.utils.geometry.PointInt;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.collect.Maps;

public class Main extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    public Main()
    {
        //super();
        super("A", true,true);
    }
    
    
    @Override
    public InputData readInput(Scanner scanner, int testCase)
    {

        InputData in = new InputData(testCase);
        
        in.entranceToExit = scanner.next();
        in.exitToEntrance = scanner.next();

        return in;
    }

    PointInt[] directions = new PointInt[] {
        new PointInt(0,1), //North
        new PointInt(1,0), //East
        new PointInt(0,-1), //South
        new PointInt(-1,0) //West
    };
        
    @Override
    public String handleCase(InputData in)
    {
        PointInt currentSquare = new PointInt(0,-1);
        int directionIdx = 2; //South
        
        Map<PointInt, BitSetInt> wallMap = Maps.newHashMap();
        wallMap.put(currentSquare, new BitSetInt());
        
            
        return "Case #" + in.testCase + ": " + convertedAlienNumber;
    }
    
    void walkThroughMaze(String steps, Map<PointInt, BitSetInt> wallMap) {
        for(int c = 0; c < in.entranceToExit.length(); ++c) {
            char ch = in.entranceToExit.charAt(c);
            
            switch(ch) {
            case 'W':
                BitSetInt prevWalls = wallMap.get(currentSquare);
                prevWalls.set(directionIdx);
                
                currentSquare = PointInt.add(currentSquare, directions[directionIdx]);
                
                BitSetInt walls = wallMap.get(currentSquare);
                if (walls == null) {
                    walls = new BitSetInt();
                    wallMap.put(currentSquare, walls);
                }
                
                walls.set( (directionIdx + 2) % 4);
                break;
            case 'R':
                walls = wallMap.get(currentSquare);
                //Left and front must be walls
                walls.set( (directionIdx + 3) % 4 );
                walls.set( directionIdx  );
                
                directionIdx += 1;
                directionIdx %= 4;
                break;
            case 'L':
                //Front must be a wall
                walls.set( directionIdx  );
                
                directionIdx += 3;
                directionIdx %= 4;
                break;
            }
        }
        
        
    }

}