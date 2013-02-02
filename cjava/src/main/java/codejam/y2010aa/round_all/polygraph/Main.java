package codejam.y2010aa.round_all.polygraph;

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.math.MatrixInt;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.y2010aa.round_all.polygraph.InputData.Statement;

import com.google.common.collect.Lists;

public class Main extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    public Main()
    {
        super();
       // super("D", true,false);
    }
    
    
    @Override
    public InputData readInput(Scanner scanner, int testCase)
    {

        InputData in = new InputData(testCase);
        
        in.N = scanner.nextInt();
        in.M = scanner.nextInt();
        
        in.statement = Lists.newArrayList();
        
        for(int m = 0; m < in.M; ++m) {
            int speaker = scanner.nextInt();
            char type = scanner.next().charAt(0);
            int subject1 = scanner.nextInt();
            int subject2 = -1;
            if (type == 'S' || type == 'D') {
                subject2 = scanner.nextInt();
            }
            
            in.statement.add(new Statement(speaker,type,subject1,subject2));
        }

        return in;
    }

    MatrixInt buildMatrix(InputData in) {
        MatrixInt matrix = new MatrixInt(in.M, in.N+1);
        
        for(int m = 0; m < in.M; ++m) {
            Statement s = in.statement.get(m);
            s.speaker--;
            s.subject1--;
            s.subject2--;
            
            switch(s.type) {
            case 'T':
                /**
                 * Person i says : person j lives in truthville.
                 * Thus, either person i and j are liars or both are truth-tellers
                 */
                matrix.data[m][s.speaker]++;
                matrix.data[m][s.subject1]++;
                matrix.data[m][in.N] = 0;
                break;
            case 'L':
                /**
                 * Person i says : person j lives in liarville.
                 * Thus, either person i is telling the truth and j is a liar -or-
                 * person i is a liar and j is a truth teller
                 */
                matrix.data[m][s.speaker]++;
                matrix.data[m][s.subject1]++;
                matrix.data[m][in.N] = 1;
                break;
            case 'S':
                /**
                 * Person i says, person j and k are from the same city
                 * Cases
                 * i T/F  j T/F  k T/F
                 * T       T       T       = odd
                 * T       F       F         
                 * F       T       F
                 * F       F       T 
                 */
                matrix.data[m][s.speaker]++;
                matrix.data[m][s.subject1]++;
                matrix.data[m][s.subject2]++;
                matrix.data[m][in.N] = 1;
                break;
            case 'D':
                /**
                 * Person i says, person j and k are from the same city
                 * Cases
                 * i T/F  j T/F  k T/F
                 * T       F       T       
                 * T       T       F         
                 * F       T       T
                 * F       F       F 
                 */
                matrix.data[m][s.speaker]++;
                matrix.data[m][s.subject1]++;
                matrix.data[m][s.subject2]++;
                matrix.data[m][in.N] = 0;
                break;
            }
            
            matrix.data[m][s.speaker] %= 2;
            matrix.data[m][s.subject1] %= 2;
            
            if (s.subject2>=0)
                matrix.data[m][s.subject2] %= 2;
        }
        
        return matrix;
    }
    
    @Override
    public String handleCase(InputData in)
    {

        MatrixInt matrix = buildMatrix(in);

        log.debug("Original matrix\n");
        matrix.debugPrint();
        
        log.debug("\nRow echelon matrix\n");
        matrix.rowEchelonForm();
        matrix.debugPrint();
        
        log.debug("\nReduced Row echelon matrix\n");
        matrix.reducedRowEchelonForm();
        matrix.debugPrint();

        StringBuffer sb = new StringBuffer();
        sb.append(String.format("Case #%d:", in.testCase));

        Boolean[] people = new Boolean[in.N];
        matrix.getAns(people);

        for (Boolean b : people)
        {
            sb.append(' ');
            if (Boolean.TRUE.equals(b))
            {
                sb.append('T');
            } else if (Boolean.FALSE.equals(b))
            {
                sb.append('L');
            } else
            {
                sb.append('-');
            }
        }

        return sb.toString();
    }

}