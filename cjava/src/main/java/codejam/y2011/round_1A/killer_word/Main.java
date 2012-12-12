package codejam.y2011.round_1A.killer_word;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.datastructures.BitSetInt;
import codejam.utils.datastructures.Identifiable;
import codejam.utils.datastructures.TreeWithIds;
import codejam.utils.datastructures.TreeWithIds.Node;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.base.Preconditions;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);
    
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        InputData input = new InputData(testCase);
        input.N = scanner.nextInt();
        input.M = scanner.nextInt();
        input.words = new ArrayList<>();
        input.lists = new ArrayList<>();
        for(int i = 0; i < input.N; ++i) {
            input.words.add(scanner.next());
        }
        for(int i = 0; i < input.M; ++i) {
            input.lists.add(scanner.next());
        }
        return input;
    }
    
    static class NodeData implements Identifiable {
        int letter;
        char chLetter;
        BitSetInt letterPositions;
        int count ;
        NodeData() {
            letterPositions = new BitSetInt();
        }
        @Override
        public int getId() {
            return letterPositions.bits;
        }
    }

    @Override
    public String handleCase(InputData input) {
        // Create unattached nodes
        
        /**
         * freeNodes[ wordIndex ] [ letter ] = bit set with bits set for each letter that occurs in word at wordindex
         */
        BitSetInt[][] freeNodes = new BitSetInt[input.N][26];
        
        Map<Integer, TreeWithIds<NodeData>> treesForWordSize = new HashMap<>();
        
        for (int n = 0; n < input.N; ++n) {
            for (int l = 0; l < 26; ++l) {
                BitSetInt node = new BitSetInt();
                freeNodes[n][l] = node;
            }

            String word = input.words.get(n);
            for (int c = 0; c < word.length(); ++c) {
                char ch = word.charAt(c);
                int idx = (int) ch - (int) 'a';
                freeNodes[n][idx].setBit(c);
            }
            
            if (!treesForWordSize.containsKey(word.length())) {
                NodeData root = new NodeData();
                root.letter = -1;
                TreeWithIds<NodeData> tree = new TreeWithIds<>(root);
                treesForWordSize.put(word.length(),tree);
            }
        }

        StringBuffer ans = new StringBuffer();
        ans.append("Case #" + input.testCase + ": ");
        for (int m = 0; m < input.M; ++m) {
            
            for(TreeWithIds<NodeData> tree : treesForWordSize.values()) {
                tree.getRoot().getData().count=0;
                tree.getRoot().setChildren(null);
            }

            String line = input.lists.get(m);
            List<Node<NodeData>> lastNodes = new ArrayList<>();

            /*
             * For each word, go through the sequence of guesses
             * and add or increment nodes in the tree.
             * 
             * The idea is that he loses a point only if he guesses
             * incorrectly and he would have guessed only if that eliminated
             * another word.
             */
            for (int n = 0; n < input.N; ++n) {
                TreeWithIds<NodeData> tree = treesForWordSize.get(input.words.get(n).length());
                
                Node<NodeData> currentNode = tree.getRoot();
                currentNode.getData().count++;
                
                for (int c = 0; c < line.length(); ++c) {
                    char ch = line.charAt(c);
                    int idx = (int) ch - (int) 'a';

                    BitSetInt currentLevelNodeData = freeNodes[n][idx];

                    Node<NodeData> existingNode = currentNode
                            .getChildWithId(currentLevelNodeData.bits);
                    if (existingNode != null) {
                        existingNode.getData().count++;
                    } else {
                        NodeData newData = new NodeData();
                        newData.count = 1;
                        newData.letter = idx;
                        newData.chLetter = ch;
                        newData.letterPositions.bits = currentLevelNodeData.bits;
                        existingNode = currentNode.addChild(newData);
                    }

                    currentNode = existingNode;
                }

                lastNodes.add(currentNode);
            }

            int maxCost = -100;
            String chosenWord = "gahou";
            for (int n = 0; n < input.N; ++n) {
                int cost = 0;
                Node<NodeData> currentNode = lastNodes.get(n);
                while (currentNode.getParent() != null) {
                    Preconditions
                            .checkState(currentNode.getParent().getData().count >= currentNode
                                    .getData().count);
                    BitSetInt wordLetter = freeNodes[n][currentNode.getData().letter];
                    if (currentNode.getParent().getData().count > currentNode
                            .getData().count && wordLetter.bits == 0) {
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
