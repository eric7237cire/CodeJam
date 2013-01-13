package codejam.y2009.round_1B.square_math;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.GridChar;

import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>{

	
	

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

	/**
	 * Looking again it looks like I find min arith expressions
	 * and use those to calculate new ones.  That way it is 
	 * assured that the minimum is found.
	 * 
	 * @param table
	 */
	private void calculateExpression(
			GridChar table) {

		expressionCount = 0;

		int maxPosition = Position.getMaxRow() * Position.getMaxCol();
		int maxValue = 350 + NEG_ADJUSTMENT;
		minExpression = new String[maxPosition][maxValue];
		visited = new boolean[maxPosition][maxValue];

		SortedSet<PositionValueExp> minSet = new TreeSet<>(); 
		
		// Initialize known minimums
		for (int r = 0; r < Position.getMaxRow(); ++r) {
			for (int c = 0; c < Position.getMaxCol(); ++c) {
				Character ch = table.getEntry(r, c);
				if (!Character.isDigit(ch)) {
					continue;
				}
				int digit = Character.digit(ch, 10);
				minExpression[new Position(r, c).toInt()][digit+NEG_ADJUSTMENT] = "" + ch;
				
				minSet.add(new PositionValueExp(new Position(r, c).toInt(), digit+NEG_ADJUSTMENT,  "" + ch));
				
			}
		}

		while (true ) {

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
					
			//String minExp = pve.expression;
			Integer minUnvisitedPositionIdx = pve.position;
			Integer minUnvisitedValue = pve.value - NEG_ADJUSTMENT; 
			
			visited[minUnvisitedPositionIdx][minUnvisitedValue+NEG_ADJUSTMENT] = true;
			
			//For All Neighbors p of Vertex k.
			Position minUnvisitedPosition = new Position(minUnvisitedPositionIdx);
			
			List<Position> signPositions = minUnvisitedPosition.getAdjPositions();

			final int minUnvisitedDigit = Character.digit(table.getEntry(minUnvisitedPosition.getRow(), minUnvisitedPosition.getCol()), 10);
			Preconditions.checkState(minUnvisitedDigit >= 0 && minUnvisitedDigit <= 9);
			
			for (Position signPos : signPositions) {
				Character sign = table.getEntry(signPos.getRow(), signPos.getCol());

				List<Position> digitPositions = signPos.getAdjPositions();

				for (Position digitPos : digitPositions) {
					
					Character curChar = table.getEntry(digitPos.getRow(), digitPos.getCol());
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

	public Main() {
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

	
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        
        InputData input = new InputData(testCase);
        input.width = scanner.nextInt();
        input.queries = scanner.nextInt();
        
        input.grid = GridChar.buildFromScanner(scanner,input.width,input.width,' ');
        
        input.targets = new ArrayList<>();
        for (int q = 0; q < input.queries; ++q) {
            input.targets.add(scanner.nextInt());
        }
        return input;
    }

    @Override
    public String handleCase(InputData input) {
        
        StringBuffer sb = new StringBuffer();
        sb.append("Case #" + input.testCase + ": ");

        Position.setMaxCol(input.width);
        Position.setMaxRow(input.width);

        Main m = new Main();

        m.calculateExpression(input.grid);
        
        for (int q = 0; q < input.queries; ++q) {
            int target = input.targets.get(q);

            // String exp = findTarget(table, width, target);
            String exp = m.getExpression(target);
            log.debug("Exp {} Case {} Query {} / {}  ", new Object[] { exp,
                    input.testCase, q, input.queries });
            sb.append("\n");
            sb.append(exp);
            
        }
        
        return sb.toString();
    }
}