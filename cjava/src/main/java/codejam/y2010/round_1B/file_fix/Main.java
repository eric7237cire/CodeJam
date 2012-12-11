package codejam.y2010.round_1B.file_fix;

import java.util.ArrayList;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.datastructures.Tree;
import codejam.utils.main.Runner;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);
    
    
    @Override
    public String handleCase(InputData input) {
        int caseNumber = input.testCase;
        
        log.info("Starting calculating case {}", caseNumber);
        
        Tree<String> tree = new Tree<>("");
        for(String dir : input.dirsExisting) {
            String[] dirComp = dir.split("/");
            
            Tree.Node<String> curNode = tree.getRoot();
            
            for(int level = 1; level < dirComp.length; ++level) {
                String comp = dirComp[level];
                
                Tree.Node<String> childNode = curNode.getChild(comp);
                
                if (childNode != null) {
                    curNode = childNode;
                    continue;
                } else {
                    Tree.Node<String> newNode = new Tree.Node<>(comp, curNode);
                    curNode.getChildren().add(newNode);
                    curNode = newNode;
                }
            }
        }

        int added = 0;
        
        for(String dir : input.dirsToCreate) {
            String[] dirComp = dir.split("/");
            
            Tree.Node<String> curNode = tree.getRoot();
            
            for(int level = 1; level < dirComp.length; ++level) {
                String comp = dirComp[level];
                
                Tree.Node<String> childNode = curNode.getChild(comp);
                
                if (childNode != null) {
                    curNode = childNode;
                    continue;
                } else {
                    Tree.Node<String> newNode = new Tree.Node<>(comp, curNode);
                    curNode.getChildren().add(newNode);
                    curNode = newNode;
                    ++added;
                }
            }
        }
        
        log.info("Done calculating answer case {}", caseNumber);
        
        //DecimalFormat decim = new DecimalFormat("0.00000000000");
        //decim.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
        
        return ("Case #" + caseNumber + ": " + added);
    }
    
    
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        
    
        
        InputData  input = new InputData(testCase);
        
        input.dirExisting = scanner.nextInt();
        input.dirToCreate = scanner.nextInt();
        input.dirsExisting = new ArrayList<>(input.dirExisting);
        input.dirsToCreate = new ArrayList<>(input.dirToCreate); 
        
        for(int i = 0; i < input.dirExisting; ++i) {
            input.dirsExisting.add(scanner.next());
        }
        for(int i = 0; i < input.dirToCreate; ++i) {
            input.dirsToCreate.add(scanner.next());
        }
        
        return input;
        
    }

    


    public Main() {
        super();
    }
    
    
    public static void main(String args[]) throws Exception {

        if (args.length < 1) {
           // args = new String[] { "sample.txt" };
            args = new String[] { "A-small-practice.in" };
//            args = new String[] { "B-large-practice.in" };
         }
         log.info("Input file {}", args[0]);

         Main m = new Main();
         Runner.goSingleThread(args[0], m, m);
         //Runner.go(args[0], m, m, new InputData(-1));

        
       
    }

    
}