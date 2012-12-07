package codejam.utils.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.multithread.Consumer;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.multithread.Producer;

public class Runner {

    static int testCounter = 0;
    final static Logger log = LoggerFactory.getLogger(Runner.class);
    
    public interface TestCaseInputScanner<InputData> {
        public InputData readInput(Scanner scanner, int testCase)
                ;
    }
    
    public static <InputData extends AbstractInputData> void goSingleThread(
            String inputFileName, TestCaseInputScanner<InputData> inputReader,
            TestCaseHandler<InputData> testCaseHandler) {

        long overAllStart = System.currentTimeMillis();
        
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(
                    new File(inputFileName)));
            final BufferedReader br = new BufferedReader(isr);
            final Scanner scanner = new Scanner(br);

            final int t = scanner.nextInt();

            File outFile = new File(inputFileName.replaceAll("\\.in", "") + ".out");
            
            OutputStream os = new FileOutputStream(outFile);
            PrintStream pos = new PrintStream(os);



            for (int test = 1; test <= t; test++) {
                InputData input = inputReader.readInput(scanner, test);
                String ans = testCaseHandler.handleCase(input);
                log.debug(ans);
                pos.println(ans);
            }

            log.debug("Finished");
            
            
            

            os.close();
            // scanner.close();
            br.close();
            
            String checkFilePath = outFile.getCanonicalPath().replaceAll("\\.out",".correct");
            File checkFile = new File(checkFilePath);
            if (checkFile.exists()) {
                log.info("Running diff");
                doDiff(outFile.getCanonicalPath(), checkFile.getCanonicalPath());
            }
        } catch (IOException ex) {
            log.error("Error", ex);
        }
        
        log.info("Total time {}",
                +(System.currentTimeMillis() - overAllStart));

    }

    public static void doDiff(String outFile, String correctFile) {
        try {
        String cmd = "diff --ignore-all-space \"" + outFile + "\" \"" + correctFile + "\"";
        Runtime rt = Runtime.getRuntime();
        Process proc = rt.exec(cmd);
        
        // any error message?
        StreamGobbler errorGobbler = new 
            StreamGobbler(proc.getErrorStream(), "ERR");            
        
        // any output?
        StreamGobbler outputGobbler = new 
            StreamGobbler(proc.getInputStream(), "OUT");
            
        // kick them off
        errorGobbler.start();
        outputGobbler.start();
                                
        // any error???
        int exitVal = proc.waitFor();
        System.out.println("ExitValue: " + exitVal);
        } catch (Exception ex) {
            log.error("ex", ex);
        }
    }
    
    
    public static <InputData extends AbstractInputData> void go(
            String inputFileName, TestCaseInputScanner<InputData> inputReader,
            TestCaseHandler<InputData> testCaseHandler, InputData poisonPill, int numThreads) {
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(
                    new File(inputFileName)));
            final BufferedReader br = new BufferedReader(isr);
            final Scanner scanner = new Scanner(br);

            String line = br.readLine();

            final int t = Integer.parseInt(line);

            
            File outFile = new File(inputFileName.replaceAll("\\.in", "") + ".out");
            
            OutputStream os = new FileOutputStream(outFile);
            PrintStream pos = new PrintStream(os);

            final String[] answers = new String[t];

            final int THREADS = numThreads;
            testCounter = 0;
            Thread[] threads = new Thread[THREADS];

            long overAllStart = System.currentTimeMillis();

            //Plus ten for poison pills
            BlockingQueue<InputData> q = new ArrayBlockingQueue<InputData>(t+10);
            Producer<InputData> p = new Producer<InputData>(q, t, scanner,
                    inputReader, poisonPill);

            threads[0] = new Thread(p);
            for (int i = 1; i < THREADS; ++i) {
                Consumer<InputData> c1 = new Consumer<InputData>(q, answers,
                        testCaseHandler);
                threads[i] = new Thread(c1);
            }

            for (int i = 0; i < threads.length; i++) {
                threads[i].start();
            }
           
            for (int i = 0; i < threads.length; i++) {
                threads[i].join();
            }

            for (int test = 1; test <= t; test++) {
                pos.println(answers[test - 1]);
            }

            log.info("Finished");

            log.info("Total time {}",
                    +(System.currentTimeMillis() - overAllStart));

            os.close();
            // scanner.close();
            br.close();
            
            String checkFilePath = outFile.getCanonicalPath().replaceAll("\\.out",".correct");
            File checkFile = new File(checkFilePath);
            if (checkFile.exists()) {
                log.info("Running diff");
                doDiff(outFile.getCanonicalPath(), checkFile.getCanonicalPath());
            }
        } catch (Exception ex) {
            log.error("Error", ex);
        }
    }
}
