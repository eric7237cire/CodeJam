package codejam.y2012.round_1C.boxes;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;

public class BruteForce
{

    public BruteForce() {
        // TODO Auto-generated constructor stub
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

}
