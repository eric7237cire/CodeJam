package com.eric.codejam.multithread;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eric.codejam.main.AbstractInputData;
import com.eric.codejam.main.Runner.TestCaseInputScanner;

public class Producer<InputData extends AbstractInputData> implements Runnable {

   

    final static Logger log = LoggerFactory.getLogger(Producer.class);

    private final BlockingQueue<InputData> queue;
    private final int testCaseTotal;
    private final Scanner br;
    private final TestCaseInputScanner<InputData> inputReader;
    private InputData poisonPill;

    public Producer(BlockingQueue<InputData> q, int testCaseTotal,
            Scanner scanner, TestCaseInputScanner<InputData> inputReader, InputData poisonPill) {
        queue = q;
        this.testCaseTotal = testCaseTotal;
        this.br = scanner;
        this.inputReader = inputReader;
        this.poisonPill = poisonPill;
    }

    public void run() {
        try {
            for (int i = 0; i < testCaseTotal; ++i) {
                
                    InputData input = inputReader.readInput(br, i + 1);
                    queue.put(input);
                
            }

            for (int i = 0; i < 10; ++i) {
                log.debug("Poison pill");
                queue.put(poisonPill);
            }
            log.info("Producer thread ending");
        } catch (InterruptedException ex) {
            log.error("prob", ex);
        }
    }

}
