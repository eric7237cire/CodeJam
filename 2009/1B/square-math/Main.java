import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayTable;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import com.google.common.collect.Table;

public class Main {

	private static class Node implements Comparable<Node> {
		private String expression;
		private int value;

		private int row;
		private int column;

		private final int target;

		Node(String expression, Table<Integer, Integer, Character> table,
				int row, int column, int target) {
			this.expression = expression + table.get(row, column);
			this.row = row;
			this.column = column;
			this.target = target;

			int valueSum = Character.digit(this.expression.charAt(0), 10);

			for (int si = 1; si < expression.length() - 1; si += 2) {
				valueSum += Integer.parseInt(expression.substring(si, si + 2));
			}

			value = valueSum;

		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}

			Node rhs = (Node) obj;

			return Objects.equal(rhs.value, value)
					&& Objects.equal(rhs.row, row)
					&& Objects.equal(rhs.column, column);
		}

		@Override
		public String toString() {
			return Objects.toStringHelper(this).add("row", row)
					.add("column", column).add("Exp", expression)
					.add("Val", value).toString();
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(value, row, column);
		}

		@Override
		public int compareTo(Node o) {

			return ComparisonChain
					.start()
					.compare(expression.length(), o.expression.length())
					.compare(expression, o.expression)
					.compare(Math.abs(this.value - this.target),
							Math.abs(o.value - o.target)).compare(row, o.row)
					.compare(column, o.column).result();
		}

	}

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

	private void calculateExpression(
			Table<Integer, Integer, Character> table) {

		expressionCount = 0;

		int maxPosition = Position.getMaxRow() * Position.getMaxCol();
		int maxValue = 250 + NEG_ADJUSTMENT;
		minExpression = new String[maxPosition][maxValue];
		visited = new boolean[maxPosition][maxValue];

		
		// Initialize known minimums
		// TODO why are they not visited?
		for (int r = 0; r < Position.getMaxRow(); ++r) {
			for (int c = 0; c < Position.getMaxCol(); ++c) {
				Character ch = table.get(r, c);
				if (!Character.isDigit(ch)) {
					continue;
				}
				int digit = Character.digit(ch, 10);
				minExpression[new Position(r, c).toInt()][digit+NEG_ADJUSTMENT] = "" + ch;
			}
		}

		int iterations = 0;
		while (true ) {

			++iterations;
			String minExp = null;
			Integer minUnvisitedPositionIdx = null;
			Integer minUnvisitedValue = null; 
			// Among all unvisited states(i,j) find the one for which Min[i][j]
			// is the smallest. Let this state found be (k,l). That is an L
			for (int position = 0; position < maxPosition; ++position) {
				for (int value = 0; value < maxValue; ++value) {
					
					if (visited[position][value]) {
						continue;
					}
					final String exp = minExpression[position][value];
					if (expressionOrder.compare(exp, minExp) < 0) {
						minExp = exp;
						minUnvisitedPositionIdx = position;
						minUnvisitedValue = value - NEG_ADJUSTMENT;
					}
				}
			}
			
			if (minExp == null) {
				//No min found, break loop
				break;
			}
			
			if (minUnvisitedPositionIdx == new Position(4, 1).toInt() &&
					minUnvisitedValue == 16) {
				log.info("");
			}
			
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
					
					int curValue = 
							sign == '+' ? minUnvisitedValue + curDigit
									: minUnvisitedValue - curDigit;
					
					if (curValue + NEG_ADJUSTMENT < 0 || curValue + NEG_ADJUSTMENT >= maxValue) {
						continue;
					}
					String curMin = minExpression[digitPos.toInt()][curValue+NEG_ADJUSTMENT];
					String newMin = minExpression[minUnvisitedPositionIdx][minUnvisitedValue+NEG_ADJUSTMENT] + sign + curChar;
					
					if (expressionOrder.compare(newMin,  curMin) < 0) {
						minExpression[digitPos.toInt()][curValue+NEG_ADJUSTMENT] = newMin;
					}
				}
			}
			
			
			/*For All Neighbors p of Vertex k.
   If (l-S[p]>=0 AND
    Min[p][l-S[p]]>Min[k][l]+Dist[k][p])
      Then Min[p][l-S[p]]=Min[k][l]+Dist[k][p]
   i.e.
If for state(i,j) there are enough money left for
going to vertex p (l-S[p] represents the money that
will remain after passing to vertex p), and the
shortest path found for state(p,l-S[p]) is bigger
than [the shortest path found for
state(k,l)] + [distance from vertex k to vertex p)],
then set the shortest path for state(i,j) to be equal
to this sum.
End For*/

		}


		
	}

	// position, value = expression
	private String[][] minExpression;
	private boolean[][] visited;
	private static int NEG_ADJUSTMENT = 10;

	final private Ordering<String> expressionOrder;

	private static Position debugPosition = new Position(2, 3);
	private static int debugTargetValue = 6;

	private static Position debugNextPosition = new Position(2, 1);
	private static Position debugSignPosition = new Position(2, 2);

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

	private String getExpression(Position pos, int targetValue,
			Table<Integer, Integer, Character> table, final int recLevel,
			final String rhsExpSoFar, final int rhsVal, boolean[] seenRhsVal,
			final int originalTargetValue) {

		String ms = minExpression[pos.toInt()][targetValue + 10];

		Preconditions.checkState(!"CALC".equals(ms));

		if ("-1".equals(rhsExpSoFar) && new Position(2, 2).equals(pos)

		) {
			log.info("ok");
		}

		if (targetValue == 6 && new Position(2, 3).equals(pos)) {
			log.info("ok");
		}

		if (targetValue == 7 && new Position(2, 1).equals(pos)) {
			log.info("ok");
		}

		if (ms != null) {
			return ms;
		}

		// memoize[pos.toInt()][targetValue + 10] = "CALC";

		log.debug(Objects.toStringHelper(this).add("pos", pos)
				.add("targetValue", targetValue).add("rhs Val", rhsVal)
				.add("rhs Exp", rhsExpSoFar).toString());

		Character c = table.get(pos.getRow(), pos.getCol());

		Preconditions.checkArgument(Character.isDigit(c));

		int posDigit = Character.digit(c, 10);

		// base case
		if (posDigit == targetValue) {
			log.debug("Returning " + c);
			minExpression[pos.toInt()][targetValue + 10] = "" + c;
			return "" + c;
		}

		++expressionCount;

		// error case, lets guess over 10 is not valid
		List<Position> signPositions = pos.getAdjPositions();

		String retExp = null;

		for (Position signPos : signPositions) {
			Character sign = table.get(signPos.getRow(), signPos.getCol());

			List<Position> digitPositions = signPos.getAdjPositions();

			for (Position digitPos : digitPositions) {

				if (targetValue == 6 && new Position(2, 3).equals(pos)
						&& new Position(2, 2).equals(signPos)
						&& new Position(2, 1).equals(digitPos)) {
					log.info("ok");
				}

				if (targetValue == 7 && new Position(2, 1).equals(pos)
						&& new Position(2, 0).equals(signPos)
						&& new Position(1, 0).equals(digitPos)) {
					log.info("ok2");
				}

				// Just for a sanity check
				int checkDigit = Character.digit(
						table.get(digitPos.getRow(), digitPos.getCol()), 10);
				Preconditions.checkState(checkDigit >= 0 && checkDigit <= 9);

				int digitPosTargetValue = sign == '+' ? targetValue - posDigit
						: targetValue + posDigit;

				/*
				 * if
				 * (seenTargetValues.get(digitPos).contains(digitPosTargetValue
				 * )) { log.debug("Snipping potential Loop " +
				 * " with target value " + digitPosTargetValue + " pos " +
				 * digitPos ); continue; }
				 * 
				 * if (sign == '-' &&
				 * seenTargetValues.get(digitPos).contains(digitPosTargetValue -
				 * posDigit)) { log.trace("GUESS Snipping potential Loop " +
				 * " with target value " + digitPosTargetValue + " pos " +
				 * digitPos ); continue; }
				 */

				if (digitPosTargetValue < -8) {
					log.trace("Snipping potential Loop.  Value too negative "
							+ " with target value " + digitPosTargetValue
							+ " pos " + digitPos);
					continue;
				}

				if ("CALC"
						.equals(minExpression[digitPos.toInt()][digitPosTargetValue + 10])) {
					log.debug("Snipping potential Loop "
							+ " with target value " + digitPosTargetValue
							+ " pos " + digitPos);
					continue;
				}

				int delta = Math.abs(targetValue - originalTargetValue);
				int newDelta = Math.abs(digitPosTargetValue
						- originalTargetValue);

				if (digitPosTargetValue > originalTargetValue + 9) {
					log.trace("Snipping potential Loop.  target going wrong direction "
							+ " with target value "
							+ digitPosTargetValue
							+ " pos " + digitPos);
					continue;
				}

				int newRhsVal = rhsVal + Integer.parseInt("" + sign + c);

				if (newRhsVal == 0) {
					log.trace("Snipping potential Loop.  target going wrong direction "
							+ " with target value "
							+ digitPosTargetValue
							+ " pos " + digitPos);
					continue;
				}

				if (seenRhsVal[(newRhsVal + 10)] == true) {
					log.trace("Snipping potential Loop.  target going wrong direction "
							+ " with target value "
							+ digitPosTargetValue
							+ " pos " + digitPos);
					continue;
				}

				boolean[] newSeenRhsVal = Arrays.copyOf(seenRhsVal,
						seenRhsVal.length);
				newSeenRhsVal[newRhsVal + 10] = true;

				log.debug("Building expression.  with target value "
						+ digitPosTargetValue + " pos " + digitPos);
				log.debug(StringUtils.repeat("   ", recLevel) + sign + c);

				final String getReturn = getExpression(digitPos,
						digitPosTargetValue, table, 1 + recLevel, rhsExpSoFar
								+ sign + c, newRhsVal, newSeenRhsVal,
						originalTargetValue);

				if (getReturn == "ERROR") {
					log.trace("Get return null");
					continue;
				}

				String expression = getReturn + sign + c;

				if (expressionOrder.compare(expression, retExp) < 0) {
					retExp = expression;
				}
			}
		}

		if (targetValue == 7 && new Position(2, 1).equals(pos)) {
			log.info("ok");
		}

		if (targetValue == 6 && new Position(2, 3).equals(pos)) {
			log.info("ok");
		}

		if (retExp == null) {
			log.debug("returning null");
			minExpression[pos.toInt()][targetValue + 10] = "ERROR";
			return "ERROR";
		}
		// Preconditions.checkState(retExp != null);

		minExpression[pos.toInt()][targetValue + 10] = retExp;
		return retExp;

	}

	private static String findTarget(Table<Integer, Integer, Character> table,
			final int tableWidth, final int target) {

		TreeSet<Node> nodeList = new TreeSet<>();

		for (int r = 0; r < tableWidth; ++r) {
			for (int c = 0; c < tableWidth; ++c) {
				Character ch = table.get(r, c);
				if (!Character.isDigit(ch)) {
					continue;
				}

				nodeList.add(new Node("", table, r, c, target));
			}
		}

		Set<Node> seenNodes = new HashSet<>();

		log.info("Begin findTarget {}", nodeList);

		int nodesProcessed = 0;

		while (!nodeList.isEmpty()) {
			Node n = nodeList.first();
			nodeList.remove(n);

			log.debug("Process node {}.  Node list {}", n, nodeList);

			if (n.value == target) {
				log.info("Nodes processed {}", nodesProcessed);
				return n.expression;
			}

			if (seenNodes.contains(n)) {
				continue;
			}

			nodesProcessed++;

			/*
			 * 
			 * 2+1-2 +3-4+ 5+2+1 -4-0- 9+5+1
			 */

			seenNodes.add(n);

			if (n.row > 0) {
				nodeList.add(new Node(n.expression, table, n.row - 1, n.column,
						target));
			}
			if (n.column > 0) {
				nodeList.add(new Node(n.expression, table, n.row, n.column - 1,
						target));
			}
			if (n.row < tableWidth - 1) {
				nodeList.add(new Node(n.expression, table, n.row + 1, n.column,
						target));
			}
			if (n.column < tableWidth - 1) {
				nodeList.add(new Node(n.expression, table, n.row, n.column + 1,
						target));
			}

		}

		return "Error";
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