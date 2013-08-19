package pkr.history;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.io.Files;

/*
 * Hand copy pastes seperated by **\n
 * 
 * Assumption is they follow in time
 */
public class Preprocessor {
    private static Pattern patHandBoundary = Pattern.compile("_*");
    //private final static Pattern COMMENT = Pattern.compile("//.*");
    private final static Pattern CUT_PASTE_BOUNDARY = Pattern.compile("\\*\\*");
    
    private static Logger log = LoggerFactory.getLogger(Preprocessor.class);
    
    public static void clean(File inputFile, File outputFile) throws IOException
    {
        
        List<String> inputLines = Files.readLines(inputFile, Charsets.UTF_8);
        List<String> outputLines = Lists.newArrayList();
        
        clean(inputLines, outputLines);
        
        outputFile.delete();
        boolean ok = outputFile.createNewFile();
        
        Preconditions.checkState(ok);
        
        OutputStreamWriter char_output = new OutputStreamWriter(
                new FileOutputStream(outputFile, false),
                Charset.forName("UTF-8").newEncoder() 
            );
        
        BufferedWriter os = new BufferedWriter(char_output);
        
        for(String line : outputLines)
        {
            os.write(line);
            log.debug(line);
            os.write("\n");
        }
        
        os.close();
    }
    
    /**
     * Un bloc continue (entre deux **)
     *
     */
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
        
        void removeLast(int n)
        {
            for(int i = 0; i < n; ++i)
            {
                handStarts.remove( handStarts.size() - 1 );
            }
        }
        
        void addLinesToOutput(List<String> output)
        {
            if (getNumHands() <= 0)
            {
                log.debug("Pas d'output");
                return;
            }
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
            return Math.max(0, handStarts.size() - 1);
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
           // if (1==1)
            //    throw new RuntimeException("oeu");
            //return "Block [lines=" + Joiner.on("\n").join(lines) + ", handStarts=" + handStarts + "]";
            return "Block [lines=" + lines.size() + ", handStarts=" + handStarts + ", num hands = " + getNumHands() + "]";
        }
        
        
    }
    
    private static void addNextBlock(List<Block> blocks, Block newBlock)
    {
        log.debug("Adding block {}", newBlock);
        
        blocks.add(newBlock);
        
    }
    private static int getNextBlock(List<Block> blocks, int currentLine, List<String> inputLines)
    {
        int curLineIdx = currentLine;
        
        Block newBlock = new Block();
        
        while(true)
        {
            //Plus d'entrée
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
            
            //Bloc est fini
            if (m.matches())
            {
                addNextBlock(blocks, newBlock);
                return curLineIdx;
            }            
            
            newBlock.addLine(curLine);
        }
    }
    
    
    
    private static void removeRedundant( Block blockPrev, Block blockNext)
    {
        log.debug("Remove redundant prev\n{} next\n{}", blockPrev, blockNext);
        
        int maxRedondant = Math.min(blockPrev.getNumHands(), blockNext.getNumHands());
        
        int minPrevStart = blockPrev.getNumHands() - maxRedondant;
        
        outer:
        for(int blockPrevStartHandIdx = minPrevStart; 
                blockPrevStartHandIdx < blockPrev.getNumHands(); ++blockPrevStartHandIdx)
        {
            log.debug("Comparison startHandIdx {}", blockPrevStartHandIdx);
            for(int blockprevHandIdx = blockPrevStartHandIdx; blockprevHandIdx < blockPrev.getNumHands(); ++blockprevHandIdx)
            {
                int blockNextHandIdx = blockprevHandIdx - blockPrevStartHandIdx;
                
                Preconditions.checkState(blockNextHandIdx >= 0 && blockNextHandIdx < blockNext.getNumHands());
                
                String handInPrev = blockPrev.getHandStr(blockprevHandIdx);
                String handInNext = blockNext.getHandStr(blockNextHandIdx);
                
                log.debug(" hand in prev \nidx {} \n{}\nhand Next idx {}\n{}\nDONE",
                        blockprevHandIdx, handInPrev,
                        blockNextHandIdx, handInNext);
                if (!handInNext.equals(handInPrev))
                {
                    log.debug("NO MATCH");
                    continue outer;
                } else {
                    log.debug("MATCH");
                }
            }
            
            //À ce point, tous les blocs entre startHandIdx et blocPrev.size() - 1 inclu sont des doublons 
            int numToRemove = blockPrev.getNumHands() - blockPrevStartHandIdx;
            
            log.debug("Removing {} from prev block", numToRemove);

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
            
            if (blocks.size() >= 2)
            {
                removeRedundant(blocks.get(blocks.size() - 2), currentBlock);
            }
            
        }
        
        for(Block block : blocks)
        {
            block.addLinesToOutput(outputLines);
        }
        
        outputLines.add("_____________________________\n");
    }
}
