package com.eric.codejam.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eric.codejam.multithread.Consumer;
import com.eric.codejam.multithread.Consumer.TestCaseHandler;
import com.eric.codejam.multithread.Producer;
import com.eric.codejam.multithread.Producer.TestCaseInputReader;

public class Runner {

    static int testCounter = 0;
    final static Logger log = LoggerFactory.getLogger(Runner.class);

    public static <InputData extends AbstractInputData> void goSingleThread(
            String inputFileName, TestCaseInputReader<InputData> inputReader,
            TestCaseHandler<InputData> testCaseHandler) {

        long overAllStart = System.currentTimeMillis();
        
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(
                    new File(inputFileName)));
            final BufferedReader br = new BufferedReader(isr);
            // final Scanner scanner = new Scanner(br);

            String line = br.readLine();

            final int t = Integer.parseInt(line);

            OutputStream os = new FileOutputStream(inputFileName + ".out");
            PrintStream pos = new PrintStream(os);



            for (int test = 1; test <= t; test++) {
                InputData input = inputReader.readInput(br, test);
                String ans = testCaseHandler.handleCase(test, input);
                log.debug(ans);
                pos.println(ans);
            }

            log.debug("Finished");

            os.close();
            // scanner.close();
            br.close();
        } catch (IOException ex) {
            log.error("Error", ex);
        }
        
        log.info("Total time {}",
                +(System.currentTimeMillis() - overAllStart));

    }
    
    public static <InputData extends AbstractInputData> void go(
            String inputFileName, TestCaseInputReader<InputData> inputReader,
            TestCaseHandler<InputData> testCaseHandler, InputData poisonPill, int numThreads) {
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(
                    new File(inputFileName)));
            final BufferedReader br = new BufferedReader(isr);
            // final Scanner scanner = new Scanner(br);

            String line = br.readLine();

            final int t = Integer.parseInt(line);

            OutputStream os = new FileOutputStream(inputFileName + ".out");
            PrintStream pos = new PrintStream(os);

            final String[] answers = new String[t];

            final int THREADS = numThreads;
            testCounter = 0;
            Thread[] threads = new Thread[THREADS];

            long overAllStart = System.currentTimeMillis();

            //Plus ten for poison pills
            BlockingQueue<InputData> q = new ArrayBlockingQueue<InputData>(t+10);
            Producer<InputData> p = new Producer<InputData>(q, t, br,
                    inputReader, poisonPill);

            threads[0] = new Thread(p);
            for (int i = 1; i < THREADS; ++i) {
                Consumer c1 = new Consumer<InputData>(q, answers,
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
        } catch (Exception ex) {
            log.error("Error", ex);
        }
    }
}
