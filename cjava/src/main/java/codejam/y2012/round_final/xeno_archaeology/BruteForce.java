package codejam.y2012.round_final.xeno_archaeology;

import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

public class BruteForce
{

    public static String bruteForce(InputData in) {

        Tile bestCandidate = null;
        
        Ordering<Tile> order = Ordering.natural().nullsLast(); 
        
        List<Tile> blueTiles = Lists.newArrayList();
        List<Tile> redTiles = Lists.newArrayList();
        
        for(Tile tile : in.tiles) {
            if (tile.isRed) {
                redTiles.add( tile );
            } else {
                blueTiles.add( tile );
            }
        }
        
        for(int y = -201; y <= 201; ++y)
        {
            for(int x = -201; x <= 201; ++x)
            {
                //Suppose center is at x, y
                Tile center = new Tile(x,y,false);
                
                boolean ok = true;
                for(Tile red : redTiles)
                {
                    long parity = red.getManhattenDistance(center) % 2;
                    if (parity == 0)
                    {
                        ok = false;
                        break;
                    }
                }
                
                for(Tile blue : blueTiles)
                {
                    long parity = center.getManhattenDistance(blue) % 2;
                    if (parity != 0)
                    {
                        ok = false;
                        break;
                    }
                }
                
                if (ok && order.compare(center, bestCandidate) < 0)
                    bestCandidate = center;
            }
        }
                
        return String.format("Case #%d: %s", in.testCase,
                bestCandidate == null ? "Too damaged" : "" + bestCandidate.getX() + " " + bestCandidate.getY());
        
    }

}
