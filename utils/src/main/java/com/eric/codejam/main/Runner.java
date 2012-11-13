package com.eric.codejam.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eric.codejam.multithread.Consumer;
import com.eric.codejam.multithread.Producer;
import com.eric.codejam.multithread.Consumer.TestCaseHandler;
import com.eric.codejam.multithread.Producer.TestCaseInputReader;

public class Runner {

    static int testCounter = 0;
    final static Logger log = LoggerFactory.getLogger(Runner.class);

    public static <InputData extends AbstractInputData> void go(
            String inputFileName, TestCaseInputReader<InputData> inputReader,
            TestCaseHandler<InputData> testCaseHandler, InputData poisonPill) {
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

            final int THREADS = 5;
            testCounter = 0;
            Thread[] threads = new Thread[THREADS];

            long overAllStart = System.currentTimeMillis();

            BlockingQueue<InputData> q = new ArrayBlockingQueue<InputData>(t);
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
            /*
             * for (int i = 0; i < threads.length; i++) { // final B inst = new
             * B(); threads[i] = new Thread(new Runnable() { public void run() {
             * while (true) { int ltest = 0; synchronized (answers) { ltest =
             * test; test++; if (ltest >= t) { return; } try { input[ltest] =
             * readInput(br); } catch (IOException ex) {
             * 
             * } } long t = System.currentTimeMillis(); String ans =
             * handleCase(ltest + 1, input[ltest]); input[ltest] = null;
             * 
             * log.info("Test {} calc finished in {}", ltest, +
             * (System.currentTimeMillis() - t)); answers[ltest] = ans;
             * 
             * } } }); //threads[i].setPriority(Thread.MIN_PRIORITY);
             * threads[i].start(); }
             */
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
