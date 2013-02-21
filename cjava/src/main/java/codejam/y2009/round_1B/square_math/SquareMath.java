package codejam.y2009.round_1B.square_math;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;

import ch.qos.logback.classic.Level;
import codejam.utils.Grid2d;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;

public class SquareMath extends InputFilesHandler implements
TestCaseHandler<InputData>, TestCaseInputScanner<InputData>{

    public SquareMath(){
        super("C",1,1);
        (( ch.qos.logback.classic.Logger) log).setLevel(Level.INFO);
        
        expressionOrder = new Ordering<String>() {
            @Override
            public int compare(String s1, String s2) {

                return ComparisonChain.start()
                        .compare(s1.length(), s2.length()).compare(s1, s2)
                        .result();
            }
        }.nullsLast();

    }
	
	

	int expressionCount = 0;
	
	private String getExpression(int targetValue) {
		int maxPosition = minExpression.length;
		
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
			char[][] table) {

		expressionCount = 0;

		int maxPosition = Grid2d.getSize(table); 
		int maxRow = table.length;
		int maxCol = table[0].length;
		
		int maxValue = 350 + NEG_ADJUSTMENT;
		minExpression = new String[maxPosition][maxValue];
		visited = new boolean[maxPosition][maxValue];

		SortedSet<PositionValueExp> minSet = new TreeSet<>(); 
		
		// Initialize known minimums
		for (int r = 0; r < maxRow; ++r) {
			for (int c = 0; c < maxCol; ++c) {
				char ch = table[r][c];
				if (!Character.isDigit(ch)) {
					continue;
				}
				int digit = Character.digit(ch, 10);
				minExpression[r * maxCol + c][digit+NEG_ADJUSTMENT] = "" + ch;
				
				minSet.add(new PositionValueExp(r*maxCol + c, digit+NEG_ADJUSTMENT,  "" + ch));
				
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
			int row = minUnvisitedPositionIdx / maxCol;
			int col = minUnvisitedPositionIdx % maxCol;
			
			
			final int minUnvisitedDigit = Character.digit(table[row][ col ], 10);
			Preconditions.checkState(minUnvisitedDigit >= 0 && minUnvisitedDigit <= 9);
			
			for (int dir = 0; dir <= 3; ++dir) {
			    int signPosRow = row + Grid2d.deltaDir[dir][0];
			    int signPosCol = col + Grid2d.deltaDir[dir][1];
			    
			    if (signPosRow < 0 || signPosRow >= maxRow)
			        continue;
			    if (signPosCol < 0 || signPosCol >= maxCol)
			        continue;
			    
				Character sign = table[signPosRow][signPosCol];

				
				for (int digitDelta = 0; digitDelta <= 3; ++digitDelta) {
					
				    int digitPosRow = signPosRow + Grid2d.deltaDir[digitDelta][0];
				    int digitPosCol = signPosCol + Grid2d.deltaDir[digitDelta][1];
				    
				    if (digitPosRow < 0 || digitPosRow >= maxRow)
	                    continue;
	                if (digitPosCol < 0 || digitPosCol >= maxCol)
	                    continue;
				    
					Character curChar = table[digitPosRow][digitPosCol];
					int curDigit = Character.digit(curChar,  10);
					
					int nextValue = NEG_ADJUSTMENT +  
							(sign == '+' ? minUnvisitedValue + curDigit
									: minUnvisitedValue - curDigit);
										
					
					if (nextValue < 0 || nextValue >= maxValue) {
						continue;
					}
					
					int digitPosIdx = digitPosRow*maxCol + digitPosCol;
					
					String curMin = minExpression[digitPosIdx][nextValue];
					String newMin = minExpression[minUnvisitedPositionIdx][minUnvisitedValue+NEG_ADJUSTMENT] + sign + curChar;
					
					if (expressionOrder.compare(newMin,  curMin) < 0) {
						minExpression[digitPosIdx][nextValue] = newMin;
					}
					
					if (!visited[digitPosIdx][nextValue]) {
						minSet.add(new PositionValueExp(digitPosIdx, nextValue, minExpression[digitPosIdx][nextValue]));
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


	
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        
        InputData input = new InputData(testCase);
        input.width = scanner.nextInt();
        input.queries = scanner.nextInt();
        
        input.grid = Grid2d.buildFromScanner(scanner,input.width,input.width);
        
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


        SquareMath m = new SquareMath();

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