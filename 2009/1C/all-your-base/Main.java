import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

	final static Logger log = LoggerFactory.getLogger(Main.class);
	
	

	public static void handleCase(int caseNumber, Scanner scanner,
			PrintStream os) {
		

		String codedNum = scanner.next();

		log.info("Case {} {}", caseNumber, codedNum);
		
		//List<Character> listChar = new ArrayList<>();
		//Set<Character> taken = new HashSet<>();
		
		Map<Character, Character> trans = new HashMap<>();

		for (int i = 0; i < codedNum.length(); ++i) {
			Character c = codedNum.charAt(i);
			if (trans.containsKey(c)) {
				continue;
			}
			
			Character tarC = trans.size() >= 2 ?
					Character.forDigit(trans.size(), 36) :
						(trans.size() == 0 ? '1' : '0')  ;
			
			trans.put(c, tarC);

		}

		
		
		log.info("Trans size {} {}",
				Math.max(2, trans.size()),
				//Character.forDigit(15, 36),
				trans.toString());

		
		//os.println("Case #" + caseNumber + ": " + numVal);
		
		StringBuilder transNum = new StringBuilder(codedNum);
		for(int i = 0; i < transNum.length(); ++i) {
			transNum.setCharAt(i, trans.get(transNum.charAt(i)));
		}
		String num = transNum.toString();
		String numVal = new BigInteger(num, Math.max(2, trans.size())).toString(10);
		
		os.println("Case #" + caseNumber + ": " + numVal);
		
		
	}

	public static void main(String args[]) throws Exception {

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