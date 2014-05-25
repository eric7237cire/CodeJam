#if DEBUG
#define LOGGING_DEBUG
#define LOGGING_INFO
#define LOGGING_TRACE
#endif

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

using CodeJamUtils;
using Utils;
using Utils.tree;
using Logger = Utils.LoggerFile;

using CodeJam.Utils.graph;
using Utils.geom;
using NUnit.Framework;

namespace CodeJam.RoundQual_2014
{
    public class MineSweeperInput
    {
        public int R;
        public int C;
        public int M;
    }
    public class MineSweeper : InputFileProducer<MineSweeperInput>, InputFileConsumer<MineSweeperInput, string>
    {
        public MineSweeperInput createInput(Scanner scanner)
        {
            MineSweeperInput input = new MineSweeperInput();
            input.R = scanner.nextInt();
            input.C = scanner.nextInt();
            input.M = scanner.nextInt();

            return input;
        }

        public string processInput(MineSweeperInput input)
        {
            bool[][] mines;
            Ext.createArray(out mines, input.R, input.C, false);

            bool ok = backtrack(mines, input.R, input.C, 0, input.M, input.C);

            if (!ok)
            {
                return "\nImpossible";
            }
            return "\n"  + mineToString(mines);
        }

        static IEnumerable<Point<int>> iterateAdjacent(int r, int c, int rows, int cols)
        {
            for (int dy = -1; dy <= 1; ++dy)
            {
                for (int dx = -1; dx <= 1; ++dx)
                {
                    if (dx == 0 && dy == 0)
                        continue;

                    int rr = r + dy;
                    int cc = c + dx;

                    if (rr < 0 || cc < 0)
                        continue;

                    if (cc >= cols || rr >= rows)
                        continue;

                    yield return new Point<int>(cc, rr);
                }
            }

        }

        public static string mineToString(bool[][] mines)
        {
            StringBuilder sb = new StringBuilder();
            for(int r = 0; r < mines.Length; ++r)
            {
                for(int c = 0; c < mines[r].Length; ++c)
                {
                    if (r == mines.Length - 1 && c == mines[r].Length - 1)
                    {
                        Preconditions.checkState(mines[r][c] == false);
                        sb.Append('c');
                    }
                    else
                        sb.Append(mines[r][c] ? '*' : '.');
                }
                sb.Append('\n');
            }
            sb.Remove(sb.Length - 1, 1);
            return sb.ToString();
        }

        public static bool backtrack( bool[][] mines, int rows, int cols, int curRow, int minesLeftToPlace, int minesAbove)
        {
            Logger.LogTrace("backTrack\n{}rows {} cols {} curRow {} mines Left {} mines above {}",
            mineToString(mines), rows, cols, curRow, minesLeftToPlace, minesAbove);

            //Base case
            if (minesLeftToPlace == 0)
            {
                return ok(mines, rows, cols);
            }

            if (curRow >= rows)
            {
                return false;
            }

            

            Preconditions.checkState(minesLeftToPlace > 0);

            for (int m = 0; m < minesAbove && m < minesLeftToPlace; ++m )
            {
                mines[curRow][m] = true;
                int minesPlacedAtCurrentRow = m+1;
                if (backtrack(mines, rows,cols, curRow+1, minesLeftToPlace-minesPlacedAtCurrentRow, minesPlacedAtCurrentRow))
                {
                    return true;
                }
            }

            for (int m = 0; m < minesAbove && m < minesLeftToPlace; ++m)
            {
                mines[curRow][m] = false;
            }

            return false;
        }

        //Check solution
        public static bool ok(bool[][] mines, int rows, int cols)
        {
            int[][] adjCount;
            Ext.createArray(out adjCount, rows, cols, 0);

            int mineCount = 0;

            for(int r = 0; r < rows; ++r)
            {
                for(int c = 0; c < cols; ++c)
                {
                    if (mines[r][c])
                        mineCount++;

                    foreach(Point<int> adj in iterateAdjacent(r,c,rows,cols))
                    {                    
                        int rr = adj.Y;
                        int cc = adj.X;

                        if (mines[rr][cc])
                            ++adjCount[r][c];
                        
                    }
                }
            }

            List<Point<int>> toTrack = new List<Point<int>>();

            //Bottom right
            toTrack.Add(new Point<int>(cols - 1, rows - 1));

            HashSet<Point<int>> seen = new HashSet<Point<int>>();

            while(toTrack.Count > 0)
            {
                Point<int> cur = toTrack.GetLastValue();
                toTrack.RemoveAt(toTrack.Count - 1);

                if (seen.Contains(cur))
                    continue;

                seen.Add(cur);

                if (adjCount[cur.Y][cur.X] == 0)
                {
                    foreach(Point<int> adj in iterateAdjacent(cur.Y,cur.X, rows,cols))
                    {
                        toTrack.Add(adj);
                    }
                }
            }

            return rows * cols - mineCount == seen.Count;
        }
    }
    [TestFixture]
    public class TestMinesweeper
    {
        [Test]
        public void testBacktrack()
        {
            bool[][] mines;

            //5 rows, 6 columns
            int rows = 3;
            int cols = 3;
            Ext.createArray(out mines, rows, cols, false);

            bool ok = MineSweeper.backtrack(mines, rows, cols, 0, 5, cols);
            
            Logger.LogTrace("Solution: {} \n{}", ok, MineSweeper.mineToString(mines));
            
        }
        [Test]
        public void testOK()
        {
            int[] fillTo = new int[] { 6, 5, 0, 0, 0 };

            bool[][] mines;

            //5 rows, 6 columns
            int rows = 5;
            int cols = 6;
            Ext.createArray(out mines, rows, cols, false);

            for (int r = 0; r < rows; ++r )
            {
                for(int c = 0; c < fillTo[r]; ++c)
                {
                    mines[r][c] = true;
                }
            }
                         
            Assert.AreEqual(false, MineSweeper.ok(mines, rows, cols));

            mines[1][4] = false;

            Assert.AreEqual(true, MineSweeper.ok(mines, rows, cols));
        }
    }
}