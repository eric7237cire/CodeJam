package com.eric.codejam;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eric.codejam.main.Runner;
import com.eric.codejam.multithread.Consumer.TestCaseHandler;
import com.eric.codejam.multithread.Producer.TestCaseInputReader;
import com.eric.codejam.utils.Tree;

public class Main implements TestCaseHandler<InputData>, TestCaseInputReader<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);
    
    
    @Override
    public String handleCase(int caseNumber, InputData input) {

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
    public InputData readInput(BufferedReader br, int testCase) throws IOException {
        
    
        String[] line = br.readLine().split(" ");
        
        InputData  input = new InputData(testCase);
        
        input.dirExisting = Integer.parseInt(line[0]);
        input.dirToCreate = Integer.parseInt(line[1]);
        input.dirsExisting = new ArrayList<>(input.dirExisting);
        input.dirsToCreate = new ArrayList<>(input.dirToCreate); 
        
        for(int i = 0; i < input.dirExisting; ++i) {
            input.dirsExisting.add(br.readLine());
        }
        for(int i = 0; i < input.dirToCreate; ++i) {
            input.dirsToCreate.add(br.readLine());
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