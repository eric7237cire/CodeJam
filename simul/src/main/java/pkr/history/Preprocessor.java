package pkr.history;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import test.TestPreproc;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.io.Files;

public class Preprocessor {
    private static Pattern patHandBoundary = Pattern.compile("_*");
    private final static Pattern COMMENT =
            Pattern.compile("//.*");
    private final static Pattern CUT_PASTE_BOUNDARY = Pattern.compile("\\*\\*");
    
    private static Logger log = LoggerFactory.getLogger(TestPreproc.class);
    
    public static void clean(File inputFile, File outputFile) throws IOException
    {
        List<String> inputLines = Files.readLines(inputFile, Charsets.UTF_8);
        List<String> outputLines = Lists.newArrayList();
        
        clean(inputLines, outputLines);
        
        outputFile.delete();
        boolean ok = outputFile.createNewFile();
        
        Preconditions.checkState(ok);
        
        BufferedWriter os = new BufferedWriter(new FileWriter(outputFile, false));
        
        for(String line : outputLines)
        {
            os.write(line);
            os.write("\n");
        }
        
        os.close();
    }
    
    private static class Block
    {
        List<String> lines;
        
        List<Integer> handStarts;
        
        private boolean startFound;
        
        Block()
        {
            lines = Lists.newArrayList();
            handStarts = Lists.newArrayList();
        }
        
        boolean containsSomething()
        {
            return handStarts.size() > 0;
        }
        
        void addLine(String line)
        {
            //line = line.trim();
           /*
            *  if (line.isEmpty())  return;
            */
              
            
            Matcher m = patHandBoundary.matcher(line);
            
            if (!m.matches())
            {
                if (startFound)
                {
                    lines.add(line);
                    return;
                }
                    
            } else {    
            
                if (!startFound)
                {
                    startFound = true;
                } else {
                    
                    
                }
                
                lines.add(line);
                handStarts.add(lines.size() - 1);
            }
        }
        
        void cleanUnfinished()
        {
            int lastIndex = handStarts.get(handStarts.size() - 1);
            
            for(int i = lines.size() - 1; i > lastIndex; --i)
            {
                lines.remove(i);
            }
        }
        
        void removeFirst(int n)
        {
            for(int i = 0; i < n; ++i)
            {
                handStarts.remove(0);
            }
            
            
        }
        
        void removeLast(int n)
        {
            for(int i = 0; i < n; ++i)
            {
                handStarts.remove( handStarts.size() - 1 );
            }
        }
        
        void addLinesToOutput(List<String> output)
        {
            int startLine = handStarts.get(0);
            int endLine = handStarts.get(handStarts.size() -1);
            
            log.debug("Adding to output\nOUTPUT  {}", 
                    Joiner.on("\nOUTPUT  ").join(lines.subList(startLine, endLine)));
            
            output.addAll(lines.subList(startLine, endLine));
        }
        
        String getHandStr(int handIdx)
        {
            int startLine = handStarts.get(handIdx);
            int endLine = handStarts.get(handIdx+1);
            return Joiner.on("\n").join(lines.subList(startLine, endLine));
        }
        
        int getNumHands()
        {
            return handStarts.size() - 1;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            
            //return "Block [lines=" + Joiner.on("\n").join(lines) + ", handStarts=" + handStarts + "]";
            return "Block [lines=" + lines.size() + ", handStarts=" + handStarts + ", num hands = " + getNumHands() + "]";
        }
        
        
    }
    
    private static void addNextBlock(List<Block> blocks, Block newBlock)
    {
        newBlock.cleanUnfinished();
        log.debug("Adding block {}", newBlock);
        
        blocks.add(newBlock);
        
    }
    private static int getNextBlock(List<Block> blocks, int currentLine, List<String> inputLines)
    {
        int curLineIdx = currentLine;
        
        Block newBlock = new Block();
        
        while(true)
        {
            if (curLineIdx >= inputLines.size())
            {
                if (newBlock.containsSomething())
                {
                    addNextBlock(blocks, newBlock);
                    return curLineIdx;
                } else {
                    return -1;
                }
            }
            String curLine = inputLines.get(curLineIdx);
            log.debug("Processing line {} of {}.  [{}]", curLineIdx, inputLines.size(), curLine);
            ++curLineIdx;
            
            Matcher m = CUT_PASTE_BOUNDARY.matcher(curLine);
            
            if (m.matches())
            {
                addNextBlock(blocks, newBlock);
                return curLineIdx;
            }            
            
            newBlock.addLine(curLine);
            
            
            
        }
    }
    
    private static void cleanBlock( Block block)
    {
        
    }
    
    private static void removeRedundant( Block blockPrev, Block blockNext)
    {
        log.debug("Remove redundant prev\n{} next\n{}", blockPrev, blockNext);
        outer:
        for(int startHandIdx = 0; startHandIdx < blockPrev.getNumHands(); ++startHandIdx)
        {
            log.debug("Compaing startHandIdx {}", startHandIdx);
            for(int handIdx = startHandIdx; handIdx < blockPrev.getNumHands(); ++handIdx)
            {
                int blockIdx = handIdx - startHandIdx;
                
                String handInPrev = blockPrev.getHandStr(handIdx);
                String handInNext = blockNext.getHandStr(blockIdx);
                
                log.debug(" hand in prev \nidx {} \n{}\nhand Next idx {}\n{}\nDONE",
                        handIdx, handInPrev,
                        blockIdx, handInNext);
                if (!handInNext.equals(handInPrev))
                {
                    log.debug("NO MATCH");
                    continue outer;
                } else {
                    log.debug("MATCH");
                }
            }
            
            //À ce point, tous les blocs entre startHandIdx et blocPrev.size() - 1 inclu sont des doublons 
            int numToRemove = blockPrev.getNumHands() - startHandIdx;
            
            log.debug("Removing {} from next block", numToRemove);
            //TODO delete from blockPrev
            blockPrev.removeLast(numToRemove);
            return;
        }
    
        log.debug("No redundant hands found");
        return;
    }
    
    public static void clean(List<String> inputLines, List<String> outputLines)
    {
        List<Block> blocks = Lists.newArrayList();
        
        int currentLine = 0;
        
        while( (currentLine = getNextBlock(blocks, currentLine, inputLines)) >= 0)
        {
            Block currentBlock = blocks.get(blocks.size() - 1); 
            cleanBlock( currentBlock );
            
            if (blocks.size() >= 2)
            {
                removeRedundant(blocks.get(blocks.size() - 2), currentBlock);
            }
            
           // currentBlock.addLinesToOutput(outputLines);
        }
        
        //int startCurrentBlock = -1;
        for(Block block : blocks)
        {
            block.addLinesToOutput(outputLines);
        }
        
        outputLines.add("_____________________________\n");
    }
}
