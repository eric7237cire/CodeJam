package codejam.y2012.round_1C.boxes;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Scanner;
import java.util.TreeMap;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public String[] getDefaultInputFiles() {
         return new String[] {"sample.in"};
      // return new String[] { "A-small-practice.in", "A-large-practice.in" };
    }

    @Override
    public InputData readInput(Scanner scanner, int testCase) {

        InputData in = new InputData(testCase);
        in.N = scanner.nextInt();
        in.M = scanner.nextInt();
        
        in.a = Lists.newArrayList();
        in.b = Lists.newArrayList();
        
        for(int n = 0; n < in.N; ++n) {
            in.a.add(new ImmutablePair<>(scanner.nextLong(), scanner.nextInt()));
            
        }
        
        for(int m = 0; m < in.M; ++m) {
            in.b.add(new ImmutablePair<>(scanner.nextLong(), scanner.nextInt()));
        }

        return in;
    }

    public int[][] bruteForce(InputData in) {
        List<Integer> aExpanded = Lists.newArrayList();
        
        for(int n = 0; n < in.N; ++n) {
            Pair<Long,Integer> pair = in.a.get(n);
            for(long i = 0; i < pair.getLeft(); ++i) {
                aExpanded.add(pair.getRight());
            }
        }
        
        List<Integer> bExpanded = Lists.newArrayList();
        for(int m = 0; m < in.M; ++m) {
            Pair<Long,Integer> pair = in.b.get(m);
            for(long i = 0; i < pair.getLeft(); ++i) {
                bExpanded.add(pair.getRight());
            }
        }
        
        
        int m = aExpanded.size();
        int n = bExpanded.size();
        
        int[][] lcs = new int[m][n];
        
        for(int i= 0; i < m; ++i) {
            
            for(int j = 0; j < n; ++j) {
                
                if (aExpanded.get(i) == bExpanded.get(j)) {
                    if (i==0 || j == 0) {
                        lcs[i][j] = 1;
                    } else {
                        lcs[i][j] = 1 + lcs[i-1][j-1];
                    }
                    continue;
                } 
                
                int max = 0;
                
                if (i > 0 && lcs[i-1][j] > max) {
                    max = lcs[i-1][j];
                } 
                if (j > 0 && lcs[i][j-1] > max){
                    max = lcs[i][j-1];
                } 
                
                lcs[i][j] = max;
            }
        }
        
        return lcs;

    }
    
    
    private static class Block {
        /**
         * Where the block would start in the LCS array
         */
        long startingIndex;
        
        /**
         * How many items
         */
        long count;
        
        /**
         * Toy / Box type
         */
        int type;

        public Block(long startingIndex, long count, int type) {
            super();
            this.startingIndex = startingIndex;
            this.count = count;
            this.type = type;
        }

        @Override
        public String toString() {
            return "Block [startingIndex=" + startingIndex + ", count=" + count + ", type=" + type + "]";
        }
    }
    
    /**
     * 
     * @param indexType
     * @param maxIndex
     * @param currentIndex
     * @return pair.  count of ==> type
     */
    static Block getBlock(TreeMap<Long,Integer> indexType, long maxIndex, long currentIndex ) {
        //Get next highest value
        Map.Entry<Long,Integer> nextEntry = indexType.ceilingEntry(currentIndex+1);
        Map.Entry<Long,Integer> currentEntry = indexType.floorEntry(currentIndex);
        
        // 1 x 10 ; 2 x 20 ; 3 x 30 
        //0 -> 1  ; 10 -> 2 ; 30 -> 3
        if (nextEntry == null) {
            return new Block(currentIndex, maxIndex - currentIndex, currentEntry.getValue());            
        } else {
            return new Block(currentIndex, nextEntry.getKey() - currentIndex, currentEntry.getValue());            
        }
    }
    
    private static long getBlockStartCount(TreeMap<Long,Long> prevRow, Block blockOfB) {
        long prevCountLastRow = getMaxBeforeIndex(prevRow, blockOfB.startingIndex);
        
        return prevCountLastRow;
    }
    
    private static void updateRow(TreeMap<Long,Long> row, long blockStartIndex, long blockEndIndex,
            long blockStartCount, long nextBlockRowIndex, int[][] bruteForce) {
        long blockLength = blockEndIndex - blockStartIndex + 1;
        long blockEndCount = blockStartCount + blockLength - 1;
                
        row.put(blockStartIndex, blockStartCount);
        row.put(blockEndIndex, blockEndCount);
        
        long checkRowIndex = nextBlockRowIndex ;
        
        log.debug("Update row.  changing start index {} to {} and increasing by 1 until {} to {}", 
                blockStartIndex, blockStartCount, blockEndIndex, blockEndCount
                );
        
        log.debug("Brute force checks at row index {}. \nStart index {} should be {}, end index {} should be {}",
                checkRowIndex,
                blockStartIndex, 
                bruteForce[(int)checkRowIndex][(int) blockStartIndex],
                blockEndIndex,
                bruteForce[(int)checkRowIndex][(int) blockEndIndex]
                );
        Preconditions.checkState(bruteForce[(int)checkRowIndex][(int) blockStartIndex] == (int)blockStartCount);            
        Preconditions.checkState(bruteForce[(int)checkRowIndex][(int) blockEndIndex] == (int)blockEndCount);
        
    }
    
    
    static class UpdateRowInfo {
        long startIndex;
        long stopIndex;
        long startIndexCount;
        public UpdateRowInfo(long startIndex, long stopIndex, long startIndexCount) {
            super();
            this.startIndex = startIndex;
            this.stopIndex = stopIndex;
            this.startIndexCount = startIndexCount;
        }
    }
    
    private static UpdateRowInfo getMidBlockStart(TreeMap<Long,Long> prevRow, 
            long maxLength, Block blockOfB) {
        //Find defined points in the block, we are looking for a point that 
        //means the count is no longer increasing.  We dont need to worry
        //about points outside because either it means the #'s are constant or they
        //are increasing.  Both mean that we cannot improve further by matching mid block.
        
        //-1 since if increasing stops at end of block, we cannot improve
        NavigableSet<Long> keys = prevRow.subMap(blockOfB.startingIndex, 
                true, blockOfB.startingIndex + blockOfB.count - 1, false).navigableKeySet();
        
        //Should be at most 2 key, when increasing starts at front of block (possible) and 1 
        //when the increasing stops
        Preconditions.checkState(keys.size() <= 2);
        
        if (keys.isEmpty())
            return null;
        
        long index = keys.last() + 1;
        
        Preconditions.checkState(index < blockOfB.startingIndex+blockOfB.count);
        
        long count = prevRow.get(keys.last()) + 1;
            
        long stopIndex = Math.min(blockOfB.startingIndex+blockOfB.count - 1, index + maxLength - 1);
        
        UpdateRowInfo uri = new UpdateRowInfo(index, stopIndex, count);
        
        return uri;
    }
    
    /**
     * 
     * @return the maximum start, how much length remaining
     */
    private static long[] getMaximumBlockStart(TreeMap<Long,Long> prevRow,
            long maxLength, long currentIndex, List<Block> prevMatchingBlocks) {
        
        //Check maxLength through 1 and all defined points 
        
        //Intialize with -1
        long[] maxStart = new long[] { 1 + getMaxBeforeIndex(prevRow, currentIndex), 1 };
        
        long prevBlockCombinedLen = 1;
        for(int prevBlockIdx = prevMatchingBlocks.size() - 1; prevBlockIdx >= 0; --prevBlockIdx) {
            Block prevBlock = prevMatchingBlocks.get(prevBlockIdx);
            
            prevBlockCombinedLen += prevBlock.count;
            
            long lenNeededStartBlock = prevBlockCombinedLen;            
            long lenNeededEndBlock = lenNeededStartBlock - prevBlock.count;
            
            long searchIndexStart = prevBlock.startingIndex - 1;
            long searchIndexEnd = searchIndexStart + prevBlock.count;
            
            if (lenNeededEndBlock > maxLength)
                break;
            
            if (lenNeededStartBlock > maxLength) {
                searchIndexStart += lenNeededStartBlock - maxLength;
                lenNeededStartBlock -= lenNeededStartBlock - maxLength;
            }
            
            //Check directly the beginning and end of the block
            long count = getMaxBeforeIndex(prevRow, searchIndexStart+1) + lenNeededStartBlock;
            
            if (count > maxStart[0]) {
                maxStart = new long[] { count, lenNeededStartBlock };
            }
            
            count = getMaxBeforeIndex(prevRow, searchIndexEnd+1) + lenNeededEndBlock;
            
            if (count > maxStart[0]) {
                maxStart = new long[] { count, lenNeededEndBlock };
            }
            
            NavigableSet<Long> keys = prevRow.subMap(searchIndexStart, true, searchIndexEnd, true).descendingKeySet();
        
            for(Long index : keys) {
                long lengthUsed = lenNeededStartBlock - (index - searchIndexStart);
                count = prevRow.get(index) + lengthUsed;
                
                if (count > maxStart[0]) {
                    log.debug("!!! Found a higher start");
                    maxStart = new long[] { count, lengthUsed };
                }
            }
        }
        
                
        
       return maxStart; 
    }
    
    static List<Block> getMatchingBlocksOfB(long bMaxIndex, TreeMap<Long,Integer> bIndexType, Block blockOfA) {
        
        List<Block> blocksOfB = Lists.newArrayList();
        
        long currentColIndex = 0;
        while(currentColIndex < bMaxIndex ) {
            Block blockOfB = getBlock(bIndexType, bMaxIndex, currentColIndex);
           // log.debug("bPair count {} type {}  currentColIndex {}", bPair.getLeft(), bPair.getRight(), currentColIndex);
            
            if (blockOfB.type == blockOfA.type) {
                blocksOfB.add(blockOfB);
            }
            
            currentColIndex += blockOfB.count;
        }
        
        return blocksOfB;
    }
    
    /**
     * 
     * @param row index --> count
     * @param currentRowIndex
     * @return
     */
    static long getMaxBeforeIndex(TreeMap<Long,Long> row, long currentRowIndex) {
        Map.Entry<Long,Long> indexCount  = row.floorEntry(currentRowIndex - 1);
        Map.Entry<Long,Long> nextIndexCount = row.ceilingEntry(currentRowIndex);
        
        //If the next value is present, use it
        //say we want 5 and the row has 3 -> 5  10 - > 12, return value 7
        long max  = 0;
        
        if (nextIndexCount != null ) {
            long diffIndexes = nextIndexCount.getKey() - currentRowIndex;
            if (nextIndexCount.getValue() >= diffIndexes) { 
                // minus one because we want the value before current index
                max = nextIndexCount.getValue() - diffIndexes - 1;
            }
        }
        
        if (indexCount != null) {
            //Cannot be less than previous value either
            max = Math.max(max, indexCount.getValue());
        }
        
        return max;
    }
    
    private long[] buildIndexTypeArrays(TreeMap<Long,Integer> aIndexType, TreeMap<Long,Integer> bIndexType, InputData in) {
long currentColIndex = 0;
        
        for(int n = 0; n < in.N; ++n) {
            Pair<Long,Integer> pair = in.a.get(n);
            aIndexType.put(currentColIndex, pair.getValue());
                
            currentColIndex += pair.getLeft();
        }
        
        long aMaxIndex = currentColIndex;
        
        
        currentColIndex = 0;
        for(int m = 0; m < in.M; ++m) {
            Pair<Long,Integer> pair = in.b.get(m);
            bIndexType.put(currentColIndex, pair.getValue());
                
            currentColIndex += pair.getLeft();
        }
        long bMaxIndex = currentColIndex;
        
        return new long[] {aMaxIndex, bMaxIndex};
    }
    
    private TreeMap<Long,Long> buildFirstBlockRow(InputData in, TreeMap<Long,Integer> bIndexType, long bMaxIndex, int[][] bruteForce) {
      //Initialize first row
        TreeMap<Long,Long> blockRow = new TreeMap<>();
        Pair<Long,Integer> aPair = in.a.get(0); //getPair(aIndexType, aMaxIndex, 0);
        long currentColIndex = 0;
        long aPairRemaining = aPair.getKey();
        
        long currentRowIndex = aPair.getKey() - 1;
        
        
        while(currentColIndex < bMaxIndex && aPairRemaining > 0) {
            Block bPair = getBlock(bIndexType, bMaxIndex, currentColIndex);
            
            if (bPair.type != aPair.getValue()) {
                currentColIndex += bPair.count;
                continue;
            }
            
            long overlap = Math.min(aPairRemaining, bPair.count);
            
            long prevCount = getMaxBeforeIndex(blockRow, currentColIndex);
            
            long blockStartCount = prevCount + 1;
            long blockEndCount = prevCount + overlap;
            long blockStartIndex = currentColIndex;
            long blockEndIndex = currentColIndex + overlap - 1;
            blockRow.put(blockStartIndex, blockStartCount);
            blockRow.put(blockEndIndex, blockEndCount);
            Preconditions.checkState(bruteForce[(int)currentRowIndex][(int)currentColIndex] == (int)prevCount + 1);            
            Preconditions.checkState(bruteForce[(int)currentRowIndex][(int) blockEndIndex] == (int)blockEndCount);
                        
            aPairRemaining -= overlap;
            currentColIndex += overlap;
        }
        
        return blockRow;
    }
    
    
    public String handleCase(InputData in) {

        int[][] bruteForce = bruteForce(in);

        // Count --> type
        TreeMap<Long, Integer> aIndexType = new TreeMap<>();
        TreeMap<Long, Integer> bIndexType = new TreeMap<>();

        long[] maxes = buildIndexTypeArrays(aIndexType, bIndexType, in);
        
        log.debug("A index ==> type {}", aIndexType);
        
        log.debug("B index ==> type {}", bIndexType);

        long aMaxIndex = maxes[0];
        long bMaxIndex = maxes[1];

        // Index --> Count
        TreeMap<Long, Long> blockRow = buildFirstBlockRow(in, bIndexType, bMaxIndex, bruteForce);

        // Index in bruteForceArray corresponding to when blockOfA has been fully processed
        long endBlockOfARowIndex = in.a.get(0).getLeft() - 1;

        log.debug("After first row {}", blockRow);

        TreeMap<Long, Long> prevBlockRow = blockRow;
        blockRow = new TreeMap<>();

        // Go through each A block
        for (int i = 1; i < in.N; ++i) {

            Block blockOfA = getBlock(aIndexType, aMaxIndex, endBlockOfARowIndex+1);
            endBlockOfARowIndex += blockOfA.count;

            //TreeMap<Long, Long> prevRow = new TreeMap<Long, Long>();

            log.debug("Processing block A {}  end at lcs row index {} ", blockOfA, endBlockOfARowIndex);

            List<Block> matchingBlocks = getMatchingBlocksOfB(bMaxIndex, bIndexType, blockOfA);


            for (int mbIndex = 0; mbIndex < matchingBlocks.size(); ++mbIndex) {

                Block blockOfB = matchingBlocks.get(mbIndex);
                
                log.debug("Matching block B {}", blockOfB);

                // Long nextBlockStartCount = mbIndex == matchingBlocks.size() - 1 ? null :
                // getBlockStartCount(prevRow, matchingBlocks.get(mbIndex + 1));

                long[] startCount = getMaximumBlockStart(prevBlockRow, blockOfA.count, blockOfB.startingIndex, matchingBlocks.subList(0, mbIndex));

                long aBlockLengthRemaining = blockOfA.count - startCount[1];
                long bBlockLengthRemaining = blockOfB.count - 1;

                long overlap = Math.min(aBlockLengthRemaining, bBlockLengthRemaining);

                updateRow(blockRow, blockOfB.startingIndex, blockOfB.startingIndex + overlap, 
                        startCount[0], endBlockOfARowIndex, bruteForce);


                // also match at the end
                if (bBlockLengthRemaining <= aBlockLengthRemaining) {
                    // blockA fits exactly in blockB
                    continue;
                }
                
                UpdateRowInfo uri = getMidBlockStart(prevBlockRow, blockOfA.count, blockOfB);
                
                if (uri == null)
                    continue;
                
                updateRow(blockRow, uri.startIndex, uri.stopIndex, uri.startIndexCount, endBlockOfARowIndex, bruteForce);

                /*
                // Block A is smaller
                long offset = blockOfB.startingIndex + bBlockLengthRemaining - aBlockLengthRemaining;

                //Check that offset finishes at the end index
                
                Preconditions.checkState(offset + overlap == blockOfB.startingIndex + blockOfB.count - 1);
                startCount = getMaximumBlockStart(prevBlockRow, blockOfA.count, offset, matchingBlocks.subList(0, mbIndex));

                updateRow(blockRow, offset, offset + overlap, startCount[0], endBlockOfARowIndex, bruteForce);
                */
            }

            log.info("Row {} ", blockRow);
            // Index --> count

            // Remove all previousRow entries that have been improved
            Iterator<Map.Entry<Long, Long>> prevRowIt = prevBlockRow.entrySet().iterator();
            while (prevRowIt.hasNext()) {

                Map.Entry<Long, Long> entry = prevRowIt.next();
                long count = getMaxBeforeIndex(blockRow, entry.getKey() + 1);
                if (count >= entry.getValue()) {
                    prevRowIt.remove();
                }
            }

            // Now add all row entries
            for (Map.Entry<Long, Long> entry : blockRow.entrySet()) {
                Preconditions.checkState(!prevBlockRow.containsKey(entry.getKey()) || prevBlockRow.get(entry.getKey()).equals(entry.getValue()));

                prevBlockRow.put(entry.getKey(), entry.getValue());
            }
            log.info("New prev row {}", prevBlockRow);
            blockRow = new TreeMap<>();
        }

        long max = prevBlockRow.get(prevBlockRow.lastKey());

        long maxBF = bruteForce[bruteForce.length - 1][bruteForce[0].length - 1];

        Preconditions.checkState(max == maxBF);
        return String.format("Case #%d: No", in.testCase);
    }

}
