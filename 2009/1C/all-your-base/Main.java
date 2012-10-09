import java.io.File;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

	final static Logger log = LoggerFactory.getLogger(Main.class);
	
	public static void handleCase(int caseNumber, Scanner scanner) {
		log.info("Case {}", caseNumber);
	}
	
	public static void main(String args[]) throws Exception {

		
		log.info("Input file {}", args[0]);

		Scanner scanner = new Scanner(new File(args[0]));

		int t = scanner.nextInt();

		for (int i = 1; i <= t; ++i) {
			
			handleCase(i, scanner);

		}

		scanner.close();
	}
}