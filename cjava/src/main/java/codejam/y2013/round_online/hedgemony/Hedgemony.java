package codejam.y2013.round_online.hedgemony;

import java.util.Scanner;

import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;


/*
 * Lord Cohen is a baron with the best-looking hedge in the country.
 *  His award-winning hedge consists of N bushes planted side by side
 *   in a straight line. The bushes are numbered left to right from 1 to N. 
 *   The baron's neighbours all cut their own hedges so that all of 
 *   their bushes have the same height. But Lord Cohen has a secret 
 *   key to his landscaping success. His gardener follows a special
 *    rule when trimming the hedge, which is why the baron's hedge
 *     is always in its award-winning condition.

The rule is -- to start on the left at bush #2 and move to the right. 
The gardener cuts the top of each bush to make it exactly as tall 
as the average of the two bushes on either side. If the bush is 
already as short as the average or shorter, then the gardener 
does not touch this bush and moves on to the next bush on the right,
 until the second-to-last bush. The baron is certain that this
  procedure is the key to success.

Input

The first line of the input gives the number of test cases, 
T. T test cases follow. Each one consists of two lines. 
The first line will contain an integer N, and the second 
line will contain N space-separated integers denoting the 
heights of the bushes, from bush #1 to bush #N.

Output

For each test case, output one line containing "Case #x: y",
 where x is the case number (starting from 1) and y is the
  height of bush number N - 1 after the gardener has
   finished trimming the hedge according to the baron's special procedure.

Answers with a relative error of at most 10-4 will be considered correct. 
See the FAQ for an explanation of what that means, and what formats 
of floating-point numbers we accept.

Limits

1 ≤ T ≤ 100.
Each height will be an integer betwween 1 and 1000, inclusive.

Small dataset

3 ≤ N ≤ 10.

Large dataset

3 ≤ N ≤ 1000.

Sample


Input 
    
Output 
 
6
5
1 2 3 6 7
5
1 2 3 4 7
3
7 7 7
5
7 8 7 9 9
5
5 8 9 9 9
6
1 2 2 2 2 2
Case #1: 5.000000
Case #2: 4.000000
Case #3: 7.000000
Case #4: 8.000000
Case #5: 8.500000
Case #6: 1.937500

 */
public class Hedgemony extends InputFilesHandler implements
TestCaseHandler<InputData>, TestCaseInputScanner<InputData> 
{
    public Hedgemony()
    {
        super("A", 1, 1);
        setLogInfo();
        
    }

    /* (non-Javadoc)
     * @see codejam.utils.main.Runner.TestCaseInputScanner#readInput(java.util.Scanner, int)
     */
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        InputData in = new InputData(testCase);
        in.N = scanner.nextInt();
        in.heights = new double[in.N];
        
        for(int i = 0; i < in.N; ++i)
        {
            in.heights[i] = scanner.nextInt();                    
        }
        return in;
    }

    /* (non-Javadoc)
     * @see codejam.utils.multithread.Consumer.TestCaseHandler#handleCase(java.lang.Object)
     */
    @Override
    public String handleCase(InputData in) 
    {
        
        for(int i = 1; i < in.N - 1; ++i)
        {
            double avg = (in.heights[i-1] + in.heights[i+1]) / 2;
            if (avg < in.heights[i])
            {
                in.heights[i] = avg;
            }
        }
        
        return String.format("Case #%d: %f", in.testCase, in.heights[in.N-2]);
       
    }

}
