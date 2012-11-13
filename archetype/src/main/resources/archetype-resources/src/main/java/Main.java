#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    public static void handleCase(int caseNumber, Scanner scanner,
            PrintStream os) {

        String ans = Main.solve(scanner);

        log.info("Starting case {}", caseNumber);

        os.println("Case ${symbol_pound}" + caseNumber + ": ");

    }

    private static String solve(Scanner scanner) {
        return "Ans";
    }

    public Main() {

        super();
    }

    public static void main(String args[]) throws Exception {

        if (args.length < 1) {
            args = new String[] { "sample.txt" };
        }
        log.info("Input file {}", args[0]);

        Scanner scanner = new Scanner(new File(args[0]));

        OutputStream os = new FileOutputStream(args[0] + ".out");
        PrintStream pos = new PrintStream(os);
        
        int t = scanner.nextInt();

        for (int i = 1; i <= t; ++i) {

            handleCase(i, scanner, pos);

        }

        scanner.close();
    }
}