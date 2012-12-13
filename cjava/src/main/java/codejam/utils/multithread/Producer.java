package codejam.utils.multithread;

import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.AbstractInputData;
import codejam.utils.main.Runner.TestCaseInputScanner;

public class Producer<InputData extends AbstractInputData> implements Runnable {

   

    final static Logger log = LoggerFactory.getLogger(Producer.class);

    private final BlockingQueue<InputData> queue;
    private final int testCaseTotal;
    private final Scanner br;
    private final TestCaseInputScanner<InputData> inputReader;
    private InputData poisonPill;

    public Producer(BlockingQueue<InputData> q, int testCaseTotal,
            Scanner scanner, TestCaseInputScanner<InputData> inputReader) {
        queue = q;
        this.testCaseTotal = testCaseTotal;
        this.br = scanner;
        this.inputReader = inputReader;
        //this.poisonPill = poisonPill;
    }

    @SuppressWarnings("unchecked")
    public void run() {
        try {
            InputData input = inputReader.readInput(br, 1);
            queue.put(input);
            this.poisonPill = (InputData) input.clone();
            this.poisonPill.testCase = -1;
            
            for (int i = 1; i < testCaseTotal; ++i) {
                
                    input = inputReader.readInput(br, i + 1);
                    queue.put(input);
                
                    
            }

            for (int i = 0; i < 10; ++i) {
                log.debug("Poison pill");
                queue.put(poisonPill);
            }
            log.info("Producer thread ending");
        } catch (InterruptedException ex) {
            log.error("prob", ex);
        } catch (CloneNotSupportedException ex) {
            log.error("prob", ex);
        }
    }

}
