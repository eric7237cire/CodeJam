package codejam.y2011.round_1A.killer_word;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import codejam.utils.datastructures.BitSetInt;
import codejam.utils.datastructures.TreeInt;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.base.Preconditions;

public class Main extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

    
    public Main() {
        super("B", 1, 1, 1);
    }
    
    
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        InputData input = new InputData(testCase);
        input.N = scanner.nextInt();
        input.M = scanner.nextInt();
        input.words = new ArrayList<>();
        input.guessLists = new ArrayList<>();
        for(int i = 0; i < input.N; ++i) {
            input.words.add(scanner.next());
        }
        for(int i = 0; i < input.M; ++i) {
            input.guessLists.add(scanner.next());
        }
        return input;
    }
    
    static class NodeData {
        int letter;
        char chLetter;
        BitSetInt letterPositions;
        int count ;
        NodeData() {
            letterPositions = new BitSetInt();
        }
        
        public int getId() {
            return letterPositions.getBits();
        }

        
    }

    @Override
    public String handleCase(InputData input) {
        // Create unattached nodes
        
        /**
         * freeNodes[ wordIndex ] [ letter ] = bit set with bits set for each letter that occurs in word at wordindex
         */
        BitSetInt[][] freeNodes = new BitSetInt[input.N][26];
        
        
        
        Map<Integer, TreeInt<NodeData>> treesForWordSize = new HashMap<>();
        
        for (int n = 0; n < input.N; ++n) {
            for (int l = 0; l < 26; ++l) {
                BitSetInt node = new BitSetInt();
                freeNodes[n][l] = node;
            }

            String word = input.words.get(n);
            for (int c = 0; c < word.length(); ++c) {
                char ch = word.charAt(c);
                int idx = (int) ch - (int) 'a';
                freeNodes[n][idx].set(c);
            }
            
            if (!treesForWordSize.containsKey(word.length())) {
                NodeData root = new NodeData();
                root.letter = -1;
                TreeInt<NodeData> tree = new TreeInt<>(1);
                tree.setStats(false);
                tree.getRoot().setData(root);
                treesForWordSize.put(word.length(),tree);
            }
        }

        StringBuffer ans = new StringBuffer();
        ans.append("Case #" + input.testCase + ": ");
        for (int m = 0; m < input.M; ++m) {
            
            for(TreeInt<NodeData> tree : treesForWordSize.values()) {
                tree.reset();
                NodeData root = new NodeData();
                root.letter = -1;
                root.count = 0;
                tree.getRoot().setData(root);
            }

            String line = input.guessLists.get(m);
            List<TreeInt<NodeData>.Node> lastNodes = new ArrayList<>();

            /*
             * For each word, go through the sequence of guesses
             * and add or increment nodes in the tree.
             * 
             * The idea is that he loses a point only if he guesses
             * incorrectly and he would have guessed only if that eliminated
             * another word.
             */
            for (int n = 0; n < input.N; ++n) {
                TreeInt<NodeData> tree = treesForWordSize.get(input.words.get(n).length());
                
                TreeInt<NodeData>.Node currentNode = tree.getRoot();
                currentNode.getData().count++;
                
                
                
                for (int c = 0; c < line.length(); ++c) {
                    char ch = line.charAt(c);
                    int idx = (int) ch - (int) 'a';

                    BitSetInt currentLevelNodeData = freeNodes[n][idx];

                    TreeInt<NodeData>.Node existingNode = null;
                    
                   
                    
                    for(TreeInt<NodeData>.Node cn : currentNode.getChildren()) {
                        if(cn.getData().letterPositions.getBits() == currentLevelNodeData.getBits()) {
                            existingNode = cn;
                            break;
                        }
                    }
                    
                    if (existingNode != null) {
                        existingNode.getData().count++;
                        Preconditions.checkState(tree.getRoot().getData().count >= existingNode.getData().count);
                    } else {
                        NodeData newData = new NodeData();
                        newData.count = 1;
                        newData.letter = idx;
                        newData.chLetter = ch;
                        newData.letterPositions.setAllBits(currentLevelNodeData.getBits());
                        existingNode = currentNode.addChild(tree.getNodes().size()+1);
                        existingNode.setData(newData);
                    }

                    currentNode = existingNode;
                }

                lastNodes.add(currentNode);
            }

            int maxCost = -100;
            String chosenWord = "gahou";
            for (int n = 0; n < input.N; ++n) {
                int cost = 0;
                TreeInt<NodeData>.Node currentNode = lastNodes.get(n);
                while (currentNode.getParent() != null) {
                    Preconditions
                            .checkState(currentNode.getParent().getData().count >= currentNode
                                    .getData().count);
                    BitSetInt wordLetter = freeNodes[n][currentNode.getData().letter];
                    if (currentNode.getParent().getData().count > currentNode
                            .getData().count && wordLetter.getBits() == 0) {
                        //The point of it all.  If the total number of possible words 
                        //decreased and the letter was not correct, he loses a point
                        ++cost;
                    }
                    currentNode = currentNode.getParent();
                }

                if (cost > maxCost) {
                    chosenWord = input.words.get(n);
                    maxCost = cost;
                }
            }
            ans.append(chosenWord).append(' ');
        }

        return ans.toString();
    }

}
