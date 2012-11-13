package com.eric.codejam.multithread;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eric.codejam.main.AbstractInputData;

public class Producer<InputData extends AbstractInputData> implements Runnable {

    public interface TestCaseInputReader<InputData> {
        public InputData readInput(BufferedReader br, int testCase)
                throws IOException;
    }

    final static Logger log = LoggerFactory.getLogger(Producer.class);

    private final BlockingQueue<InputData> queue;
    private final int testCaseTotal;
    private final BufferedReader br;
    private final TestCaseInputReader<InputData> inputReader;
    private InputData poisonPill;

    public Producer(BlockingQueue<InputData> q, int testCaseTotal,
            BufferedReader br, TestCaseInputReader<InputData> inputReader, InputData poisonPill) {
        queue = q;
        this.testCaseTotal = testCaseTotal;
        this.br = br;
        this.inputReader = inputReader;
        this.poisonPill = poisonPill;
    }

    public void run() {
        try {
            for (int i = 0; i < testCaseTotal; ++i) {
                try {
                    InputData input = inputReader.readInput(br, i + 1);
                    queue.put(input);
                } catch (IOException ex) {
                    log.error("prob IO", ex);
                }
            }

            for (int i = 0; i < 10; ++i) {
                log.debug("Poison pill");
                queue.put(poisonPill);
            }
            log.debug("Producer thread ending");
        } catch (InterruptedException ex) {
            log.error("prob", ex);
        }
    }

}
