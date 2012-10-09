import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;
import com.google.common.collect.ArrayTable;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Table;

public class Main {
	
	private static class Node implements Comparable<Node> {
		private String expression;
		private int value;
		
		private int row;
		private int column;
		
		private final int target;
		
		Node(String expression,
				Table<Integer, Integer, Character> table,
		int row,
		int column,
		int target) {
			this.expression = expression + table.get(row,  column);
			this.row = row;
			this.column = column;
			this.target = target;
			
			int valueSum = Character.digit(this.expression.charAt(0), 10);
			
			for(int si = 1; si < expression.length() - 1; si += 2) {
				valueSum += Integer.parseInt(expression.substring(si,  si + 2));
			}
			
			value = valueSum;
			
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null)  
		      {  
		         return false;  
		      }  
		      if (getClass() != obj.getClass())  
		      {  
		         return false;  
		      }  
		      
		      Node rhs = (Node) obj;
		      
			return Objects.equal(rhs.value, value) && Objects.equal(rhs.row,  row) && Objects.equal(rhs.column,  column);
		}
		
		

		@Override
		public String toString() {
			return Objects.toStringHelper(this).add("row", row).add("column", column).add("Exp", expression).add("Val", value).toString();					
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(value, row, column);
		}

		@Override
		public int compareTo(Node o) {
			
			return ComparisonChain.start()
					.compare(expression.length(), o.expression.length())
					.compare(expression, o.expression)
					.compare(Math.abs(this.value - this.target), Math.abs(o.value - o.target))
					.compare(row, o.row)
					.compare(column, o.column)
					.result();
		}
		
		
	}
	
	public static void handleCase(int caseNumber, Scanner scanner,
			PrintStream os) {
		int width = scanner.nextInt();
		int queries = scanner.nextInt();
		
		List<Integer> rowLabels = new ArrayList<>();
		List<Integer> colLabels = new ArrayList<>();
		
		for(int i = 0; i < width; ++i) {
			rowLabels.add(i);
		}
		
		for(int i = 0; i < width; ++i) {
			colLabels.add(i);
		}
		
		Table<Integer, Integer, Character> table = ArrayTable.create(rowLabels,  colLabels);
		
		Pattern oldPattern = scanner.delimiter();
		
		
		scanner.useDelimiter("\\s*");
		for(int r=0; r < width; ++r) {
			for(int c=0; c < width; ++c) {
				char ch = scanner.next().charAt(0);
				table.put(r, c, ch);
			}
		}
		
		log.info("Case # {}, Table {}", caseNumber, table);
		
		os.println("Case #" + caseNumber + ": " );
		
		scanner.useDelimiter(oldPattern);
		
		for(int q = 0; q < queries; ++q) {
			int target = scanner.nextInt();
		
			String exp = findTarget(table, width, target);
			os.println(exp);
		}
	}
	
	private static String findTarget(Table<Integer, Integer, Character> table, final int tableWidth, final int target) {

		TreeSet<Node> nodeList = new TreeSet<>();
		
		for(int r=0; r < tableWidth; ++r) {
			for(int c=0; c < tableWidth; ++c) {
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
		
		while(!nodeList.isEmpty()) {
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
			 * 2+1-2
			 * +3-4+
			 * 5+2+1
			 * -4-0-
			 * 9+5+1
			 */
			
			seenNodes.add(n);
			
			if (n.row > 0) {
				nodeList.add(new Node(n.expression, table, n.row - 1, n.column, target ));
			}
			if (n.column > 0) {
				nodeList.add(new Node(n.expression, table, n.row, n.column - 1, target));
			}
			if (n.row < tableWidth - 1) {
				nodeList.add(new Node(n.expression, table, n.row + 1, n.column, target ));
			}
			if (n.column < tableWidth - 1) {
				nodeList.add(new Node(n.expression, table, n.row, n.column + 1, target));
			}
			
		}
		
		return "Error";
	}
	


	final static Logger log = LoggerFactory.getLogger(Main.class);
	
	
	public static void main(String args[]) throws Exception {

		//String fileName = args.length >= 1 ? args[0] :
		args[0] = "C-small-practice.in";
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