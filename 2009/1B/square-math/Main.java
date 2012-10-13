import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayTable;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import com.google.common.collect.Table;

public class Main {

	
	public static void handleCase(int caseNumber, Scanner scanner,
			PrintStream os) {
		int width = scanner.nextInt();
		int queries = scanner.nextInt();

		List<Integer> rowLabels = new ArrayList<>();
		List<Integer> colLabels = new ArrayList<>();

		for (int i = 0; i < width; ++i) {
			rowLabels.add(i);
		}

		for (int i = 0; i < width; ++i) {
			colLabels.add(i);
		}

		Table<Integer, Integer, Character> table = ArrayTable.create(rowLabels,
				colLabels);

		Pattern oldPattern = scanner.delimiter();

		scanner.useDelimiter("\\s*");
		for (int r = 0; r < width; ++r) {
			for (int c = 0; c < width; ++c) {
				char ch = scanner.next().charAt(0);
				table.put(r, c, ch);
			}
		}

		log.info("Case # {}, Table {}", caseNumber, table);

		os.println("Case #" + caseNumber + ": ");

		scanner.useDelimiter(oldPattern);

		Position.setMaxCol(width);
		Position.setMaxRow(width);

		Main m = new Main();

		m.calculateExpression(table);
		
		for (int q = 0; q < queries; ++q) {
			int target = scanner.nextInt();

			// String exp = findTarget(table, width, target);
			String exp = m.getExpression(target);
			log.info("Exp {} Case {} Query {} / {}  ", new Object[] { exp,
					caseNumber, q, queries });
			os.println(exp);
		}
	}


	int expressionCount = 0;
	
	private String getExpression(int targetValue) {
		int maxPosition = Position.getMaxRow() * Position.getMaxCol();
		
		String retExp = null;
		for (int position = 0; position < maxPosition; ++position) {
			String exp = minExpression[position][targetValue + NEG_ADJUSTMENT];
			if (expressionOrder.compare(exp,  retExp) < 0) {
				retExp = exp;
			}
		}
			
		return retExp;
	}
	
	private static class PositionValueExp implements Comparable<PositionValueExp> {
		private int position;
		private int value;
		private String expression;
		public int getPosition() {
			return position;
		}
		public void setPosition(int position) {
			this.position = position;
		}
		public int getValue() {
			return value;
		}
		public void setValue(int value) {
			this.value = value;
		}
		public String getExpression() {
			return expression;
		}
		public void setExpression(String expression) {
			this.expression = expression;
		}
		public PositionValueExp(int position, int value, String expression) {
			super();
			this.position = position;
			this.value = value;
			this.expression = expression;
		}
		@Override
		public int compareTo(PositionValueExp o) {
			return ComparisonChain.start()
					.compare(expression.length(), o.expression.length())
					.compare(expression,  o.expression)
					.compare(position,  o.position)
					.compare(value,  o.value)
					.result();
		}
		
		
	}

	private void calculateExpression(
			Table<Integer, Integer, Character> table) {

		expressionCount = 0;

		int maxPosition = Position.getMaxRow() * Position.getMaxCol();
		int maxValue = 350 + NEG_ADJUSTMENT;
		minExpression = new String[maxPosition][maxValue];
		visited = new boolean[maxPosition][maxValue];

		SortedSet<PositionValueExp> minSet = new TreeSet<>(); 
		
		// Initialize known minimums
		for (int r = 0; r < Position.getMaxRow(); ++r) {
			for (int c = 0; c < Position.getMaxCol(); ++c) {
				Character ch = table.get(r, c);
				if (!Character.isDigit(ch)) {
					continue;
				}
				int digit = Character.digit(ch, 10);
				minExpression[new Position(r, c).toInt()][digit+NEG_ADJUSTMENT] = "" + ch;
				
				minSet.add(new PositionValueExp(new Position(r, c).toInt(), digit+NEG_ADJUSTMENT,  "" + ch));
				
			}
		}

		int iterations = 0;
		while (true ) {

			++iterations;
			// Among all unvisited states(i,j) find the one for which Min[i][j]
			// is the smallest. Let this state found be (k,l). That is an L
			
			if (minSet.isEmpty()) {
				//No min found, break loop
				break;
			}
			
			PositionValueExp pve = minSet.first();
			minSet.remove(pve);
			
			if (visited[pve.position][pve.value]) {
				continue;
			}
					
			String minExp = pve.expression;
			Integer minUnvisitedPositionIdx = pve.position;
			Integer minUnvisitedValue = pve.value - NEG_ADJUSTMENT; 
			
			visited[minUnvisitedPositionIdx][minUnvisitedValue+NEG_ADJUSTMENT] = true;
			
			//For All Neighbors p of Vertex k.
			Position minUnvisitedPosition = new Position(minUnvisitedPositionIdx);
			
			List<Position> signPositions = minUnvisitedPosition.getAdjPositions();

			final int minUnvisitedDigit = Character.digit(table.get(minUnvisitedPosition.getRow(), minUnvisitedPosition.getCol()), 10);
			Preconditions.checkState(minUnvisitedDigit >= 0 && minUnvisitedDigit <= 9);
			
			for (Position signPos : signPositions) {
				Character sign = table.get(signPos.getRow(), signPos.getCol());

				List<Position> digitPositions = signPos.getAdjPositions();

				for (Position digitPos : digitPositions) {
					
					Character curChar = table.get(digitPos.getRow(), digitPos.getCol());
					int curDigit = Character.digit(curChar,  10);
					
					int nextValue = NEG_ADJUSTMENT +  
							(sign == '+' ? minUnvisitedValue + curDigit
									: minUnvisitedValue - curDigit);
										
					
					if (nextValue < 0 || nextValue >= maxValue) {
						continue;
					}
					String curMin = minExpression[digitPos.toInt()][nextValue];
					String newMin = minExpression[minUnvisitedPositionIdx][minUnvisitedValue+NEG_ADJUSTMENT] + sign + curChar;
					
					if (expressionOrder.compare(newMin,  curMin) < 0) {
						minExpression[digitPos.toInt()][nextValue] = newMin;
					}
					
					if (!visited[digitPos.toInt()][nextValue]) {
						minSet.add(new PositionValueExp(digitPos.toInt(), nextValue, minExpression[digitPos.toInt()][nextValue]));
					}
				}
			}
			

		}


		
	}

	// position, value = expression
	private String[][] minExpression;
	private boolean[][] visited;
	private static int NEG_ADJUSTMENT = 10;

	final private Ordering<String> expressionOrder;

	Main() {
		minExpression = new String[400][265];

		expressionOrder = new Ordering<String>() {
			@Override
			public int compare(String s1, String s2) {

				return ComparisonChain.start()
						.compare(s1.length(), s2.length()).compare(s1, s2)
						.result();
			}
		}.nullsLast();

	}



	final static Logger log = LoggerFactory.getLogger(Main.class);

	public static void main(String args[]) throws Exception {

		// String fileName = args.length >= 1 ? args[0] :
		// args[0] = "C-small-practice.in";
		log.info("Input file {}", args[0]);

		Scanner scanner = new Scanner(new File(args[0]));

		OutputStream os = new FileOutputStream("output.txt");

		PrintStream pos = new PrintStream(os);

		int t = scanner.nextInt();

		for (int i = 1; i <= t; ++i) {

			handleCase(i, scanner, pos);

		}

		scanner.close();
	}
}