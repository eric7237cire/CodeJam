package codejam.y2008.round_1C.ugly_numbers;

import java.util.Arrays;
import java.util.Scanner;

import codejam.utils.datastructures.FenwickTree;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

public class UglyNumbers extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>
{

    public UglyNumbers() {
        //super("B", 1, 1);
        setLogInfo();
    }

    @Override
    public InputData readInput(Scanner scanner, int testCase)
    {

        InputData in = new InputData(testCase);

        in.S = scanner.next();

        return in;
    }

    @Override
    public String handleCase(InputData in)
    {

        final int MOD = (2 * 3 * 5 * 7);

        /*
        number of ways we get an expression evaluating 
        to x (mod 210) if we only consider the first i
        characters of the string.
        dyn[i][x]
        */
        long[][] dyn = new long[41][MOD];

        dyn[0][0] = 1;
        for (int i = 0; i < in.S.length(); i++)
        {
            for (int sgn = (i == 0) ? 1 : -1; sgn <= 1; sgn += 2)
            {
                int cur = 0;
                for (int j = i; j < in.S.length(); j++)
                {
                    cur = (cur * 10 + in.S.charAt(j) - '0') % MOD;
                    //log.info("Cur {}", cur);
                    for (int x = 0; x < MOD; x++)
                        dyn[j + 1][(x + sgn * cur + MOD) % MOD] += dyn[i][x];
                }
            }
        }

        long ret = 0;
        for (int x = 0; x < MOD; x++)
            if (x % 2 == 0 || x % 3 == 0 || x % 5 == 0 || x % 7 == 0)
                ret += dyn[in.S.length()][x];

        return String.format("Case #%d: %d ", in.testCase, ret);

    }
}