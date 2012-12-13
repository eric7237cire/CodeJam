package codejam.y2009.round_1B.next_number;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Ordering;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles {

    @Override
    public String[] getDefaultInputFiles() {
        return new String[] { "B-small-practice.in", "B-large-practice.in" };
    }
	@Override
    public InputData readInput(Scanner scanner, int testCase) {
       InputData input = new InputData(testCase);
       input.nextNum = scanner.next();
       return input;
    }

    @Override
    public String handleCase(InputData input) {
        String nStr = input.nextNum;

        List<Integer> listDigits = new ArrayList<Integer>();

        listDigits.add(0);

        for (int si = 0; si < nStr.length(); ++si) {
            listDigits.add(Character.digit(nStr.charAt(si), 10));
        }

        // Find right side group that is not descending
        Ordering<Integer> intRevOrder = Ordering.natural().reverse();
        Ordering<Integer> intOrder = Ordering.natural();

        for (int leftIndex = 1; leftIndex < listDigits.size(); ++leftIndex) {
            List<Integer> subList = listDigits.subList(leftIndex,
                    listDigits.size());

            if (!intRevOrder.isOrdered(subList)) {
                continue;
            }

            final int nextDigitValue = listDigits.get(leftIndex - 1);

            Iterable<Integer> candidates = Iterables.filter(subList,
                    new Predicate<Integer>() {
                        public boolean apply(Integer input) {
                            return input > nextDigitValue;
                        }
                    });

            Integer swapValue = intOrder.min(candidates);
            int swapIndex = listDigits.lastIndexOf(swapValue);

            Collections.swap(listDigits, swapIndex, leftIndex - 1);
            Collections.sort(subList);

            if (listDigits.get(0) == 0) {
                listDigits.remove(0);
            }

            String s = StringUtils.join(listDigits, "");

            return String.format("Case #%d: %s\n", input.testCase, s);

        }

        return "Error";

    }
}