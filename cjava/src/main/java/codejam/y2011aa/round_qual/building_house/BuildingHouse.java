package codejam.y2011aa.round_qual.building_house;

import java.util.Scanner;
import java.util.Stack;

import codejam.utils.Grid2d;
import codejam.utils.geometry.PointInt;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;


public class BuildingHouse extends InputFilesHandler
implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>
{

    public BuildingHouse() {
        super("C", 1, 1);
        setLogInfo();
    }

    @Override
    public InputData readInput(Scanner scanner, int testCase)
    {
        InputData in = new InputData(testCase);
        in.C = scanner.nextInt();
        in.R = scanner.nextInt();
        in.grid = Grid2d.buildFromScanner(scanner, in.R, in.C);
        return in;
    }

    /**
     * Go through column col and either add one if the square is usable
     * or reset the count to zero.
     * 
     * Example
     * 
     * ..#
     * .#.
     * ...
     * 
     * it would be 0, 1, 1 then 1, 0, 2 then 2, 1, 3
     * 
     */
    private static void updateColumnOnesToRight( int[] columnOnesToRight, int col, char[][] land)
    {
        for(int r = 0; r < land.length; ++r)
        {
            if (land[r][col] == 'S' || land[r][col] == 'G')
                columnOnesToRight[r]++;
            else
                columnOnesToRight[r] = 0;
        }
    }

    @Override
    public String handleCase(InputData in)
    {
        PointInt best_ll = new PointInt(0,0);
        PointInt best_ur = new PointInt(-1,-1);
        
        int[] usableSquaresToRight = new int[in.R+1];
        
        /**
         * Loop from right to left
         */
        for( int x = in.C-1; x >= 0; --x)
        {
            updateColumnOnesToRight(usableSquaresToRight, x, in.grid);
            Stack<PointInt> widthStack = new Stack<>();
            int curWidth = 0;
            for( int y = 0; y <= in.R; ++y )
            {
                //cout << "X= " << x << " Y= " << y << " 1s right " << columnOnesToRight[y] << endl;
                /**
                 * Here we widened 
                 */
                if ( usableSquaresToRight[y] > curWidth )
                {
                                        
                    curWidth = usableSquaresToRight[y];
                   // cout << "X= " << x << " Y= " << y <<  "Push " << width << endl;
                    
                    widthStack.push( new PointInt(curWidth, y) );
                     
                }
                /**
                 * Current width is less, look for rectangles
                 * in the stack with a width > than this width
                 *         
                 */
                if ( usableSquaresToRight[y] < curWidth )
                {
                    int y0;
                    do
                    {
                        PointInt rowWidth = widthStack.pop();
                        
                        //cout << "X= " << x << " Y= " << y <<  " Pop y " <<                    rowWidth.first << " Pop Width " << rowWidth.second << " cur width " << columnOnesToRight[y] << endl;
                        curWidth = rowWidth.x();
                        y0 = rowWidth.y();
                        //Check for a better rectangle
                        if (curWidth * (y-y0) > 
                            area(best_ll, best_ur) )
                        {
                            best_ll = new PointInt(x, y0);
                            best_ur = new PointInt(x+curWidth-1, y-1);
                            
                           // cout << "X= " << x << " Y= " << y <<  " Pop y " <<                    rowWidth.first << " Pop Width " << rowWidth.second << " cur width " << columnOnesToRight[y] << " New area max " << width*(y-y0) << endl;
                            
                            assert(curWidth * (y-y0) == area(best_ll, best_ur)); 
                            
                            
                        }
                        
                    } while( 
                        !widthStack.empty() && widthStack.lastElement().x() > usableSquaresToRight[y]);
                    
                    curWidth = usableSquaresToRight[y];                    
        
                    /**
                     * We can still make a retangle with the last popped rectagle (lowest y value)
                     */
                    if (curWidth != 0) {
                        //cout << "X= " << x << " Y= " << y <<  "Push after width " << width << " Push after y0 " << y0 << endl;
                        widthStack.push( new PointInt(curWidth, y0) );
                    }
                }
            }
        }
        
       
        return  String.format("Case #%d: %d", in.testCase,  area(best_ll, best_ur)); 

    }

    private static int area(PointInt lowerLeft, PointInt upperRight)
    {
        return (upperRight.x() - lowerLeft.x() + 1) * (upperRight.y() - lowerLeft.y() + 1);
    }

}
