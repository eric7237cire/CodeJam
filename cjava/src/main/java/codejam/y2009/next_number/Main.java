package codejam.y2009.next_number;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Ordering;

public class Main {

	public static void main(String args[]) throws Exception {

		boolean debug = false;

		PrintStream os = System.out;

		if (!debug) {
			os = new PrintStream(new File("out.txt"));
		}

		Scanner scanner = new Scanner(new File(debug ? "sample.txt"
				: "B-large-practice.in"));

		int t = scanner.nextInt();

		for (int i = 1; i <= t; ++i) {
			String nStr = scanner.next();

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
		
				os.format("Case #%d: %s\n", i, s);
				break;
			}

		}

		scanner.close();
	}
}