import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

	final static Logger log = LoggerFactory.getLogger(Main.class);

	public static void handleCase(int caseNumber, Scanner scanner,
			PrintStream os) {
		log.info("Case {}", caseNumber);

		String codedNum = scanner.next();

		List<Character> listChar = new ArrayList<>();
		Set<Character> taken = new HashSet<>();
		
		Map<Character, Character> trans = new HashMap<>();

		for (int i = 0; i < codedNum.length(); ++i) {
			Character c = codedNum.charAt(i);
			if (taken.contains(c)) {
				continue;
			}
			
			Character tarC = trans.size() >= 2 ?
					Character.forDigit(trans.size(), 10) :
						(trans.size() == 0 ? '1' : '0')  ;
			
			trans.put(c, tarC);

			listChar.add(c);
			taken.add(c);
		}

		log.info("list {}", listChar);

		String num = codedNum;
		
		
		Integer numVal = null;
		if (listChar.size() >= 2) {
			Collections.swap(listChar, 0, 1);

			for (int i = 0; i < listChar.size(); ++i) {
				num = num.replaceAll(Pattern.quote("" + listChar.get(i)),
						Integer.toString(i));
			}

			log.info("Num {}, val base 10 {}", num,
					Integer.parseInt(num, listChar.size()));
			numVal = Integer.parseInt(num, listChar.size());
		} else {
			num = codedNum.replaceAll(".", "1");
			numVal = Integer.parseInt(num, 2);
		}
			

		//os.println("Case #" + caseNumber + ": " + numVal);
		
		StringBuilder transNum = new StringBuilder(codedNum);
		for(int i = 0; i < num.length(); ++i) {
			transNum.setCharAt(i, trans.get(transNum.charAt(i)));
		}
		num = transNum.toString();
		numVal = Integer.parseInt(num, Math.max(2, trans.size()));
		
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