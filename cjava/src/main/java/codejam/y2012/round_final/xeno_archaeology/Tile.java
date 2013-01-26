package codejam.y2012.round_final.xeno_archaeology;


import com.google.common.collect.ComparisonChain;

public class Tile implements Comparable<Tile> 
{

    
    /*
     * For debugging
     * 
    static long breakX = 48;
    static long breakY = 1;
    static boolean breakCenterXOdd = breakX % 2 != 0;
    static boolean breakCenterYOdd = breakY % 2 != 0;
    
    if (isCenterXOdd==breakCenterXOdd && 
                                isCenterYOdd == breakCenterYOdd && 
                                pointInRectangle(breakX, breakY,A,B,C,D)
                                ) {
                            log.debug("test");
                        }
                        boolean isCenterXOdd = centerXOdd != 0;
                boolean isCenterYOdd = centerYOdd != 0;
                
                
    */
    
        final long y;
        final long x;
        final boolean isRed;
        
        public Tile(long x, long y, boolean isRed) {
            this.y = y;
            this.x = x;
            this.isRed = isRed;
        }

        public long getY()
        {
            return y;
        }


        public long getX()
        {
            return x;
   
        }
        
        public long getManhattenDistance(Tile rhs) {
            return Math.max( Math.abs(getX()-rhs.getX()) ,
                    Math.abs(getY()-rhs.getY()) );
        }

        @Override
        public int compareTo(Tile o)
        {
            /**
             * Use the rules in the problem.  Lowest manhatten distance,
             * then largest x, largest y
             */
            long m1 = Math.abs(getX()) + Math.abs((getY()));
            long m2 = Math.abs(o.getX()) + Math.abs((o.getY()));
            return ComparisonChain.start()
                    .compare(m1, m2)
                    .compare(o.getX(), getX())
                    .compare(o.getY(), getY())
                    .result();
        }

        @Override
        public String toString()
        {
            return "Tile [x=" + x + ", y=" + y + ", isRed=" + isRed + "]";
        }
        
    

}
