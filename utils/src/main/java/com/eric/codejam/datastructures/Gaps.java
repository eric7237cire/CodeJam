package com.eric.codejam.datastructures;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

public class Gaps {
    final Logger log = LoggerFactory.getLogger(this.getClass());
    
    //lb to up
    TreeMap<Integer, Integer> blocks = new TreeMap<>();
    
    public void mergeGap(int lb, int ub) {
        Preconditions.checkState(lb <= ub);
        //Check if there is anything directly before lb
        Gap blockLb = getGap(lb-1);
        
        Integer blocksToMergeFrom;
        Integer blocksToMergeTo;
        
        if (blockLb != null) {
            blocksToMergeFrom = blockLb.lb;
            lb = blockLb.lb;
        } else {
            blocksToMergeFrom = lb;
        }
        
        Gap blockUb = getGap(ub+1);
        if (blockUb != null) {
            blocksToMergeTo = blockUb.lb;
            ub = blockUb.ub;
        } else {
            blocksToMergeTo = blocks.floorKey(ub);
        }
        
        if (blocksToMergeFrom != null && blocksToMergeTo != null && blocksToMergeFrom <= blocksToMergeTo)
            blocks.subMap(blocksToMergeFrom,true,blocksToMergeTo,true).clear();
        
        blocks.put(lb,ub);
    }
    
    public void removeGap(int lb, int ub) {
        Preconditions.checkState(lb <= ub);
        
      //Check if there is anything directly before lb
        Gap blockLb = getGap(lb-1);
        
        Integer blocksToMergeFrom;
        Integer blocksToMergeTo;
        
        if (blockLb != null) {
            blocksToMergeFrom = blockLb.lb;
        } else {
            blocksToMergeFrom = lb;
        }
        
        Gap blockUb = getGap(ub+1);
        if (blockUb != null) {
            blocksToMergeTo = blockUb.lb;
        } else {
            blocksToMergeTo = blocks.floorKey(ub);
        }
        
        if (blocksToMergeFrom != null && blocksToMergeTo != null && blocksToMergeFrom <= blocksToMergeTo)
            blocks.subMap(blocksToMergeFrom,true,blocksToMergeTo,true).clear();
        
        if (blockLb != null && blockLb.lb <= lb-1) {
            mergeGap(blockLb.lb, lb-1);
        }
        
        if (blockUb != null && ub+1 <= blockUb.ub) {
            mergeGap(ub+1, blockUb.ub);
        }
    }
    
    public List<Gap> getAllGaps() {
        List<Gap> gaps = new ArrayList<>(blocks.size());
        Iterator<Map.Entry<Integer,Integer>> it = blocks.entrySet().iterator();
        
        while(it.hasNext()) {
            Map.Entry<Integer,Integer> gap = it.next();
            gaps.add(new Gap(gap.getKey(),gap.getValue()));
        }
        
        return gaps;
    }
    
    public Gap getGap(int x) {
        Map.Entry<Integer,Integer> gap = blocks.floorEntry(x);
        
        if (gap != null && gap.getValue() >= x) {
            return new Gap(gap.getKey(),gap.getValue());
        }
        
        return null;
    }
    
    // lb <= x <= up
    public static class Gap {
        public final int lb;
        public final int ub;
        public Gap(int lb, int up) {
            super();
            this.lb = lb;
            this.ub = up;
        }
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + lb;
            result = prime * result + ub;
            return result;
        }
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Gap other = (Gap) obj;
            return Objects.equal(lb,other.lb) && Objects.equal(ub,other.ub);
            
        }
        @Override
        public String toString() {
            return "Gap [lb=" + lb + ", ub=" + ub + "]";
        }
        
        
    }
}
