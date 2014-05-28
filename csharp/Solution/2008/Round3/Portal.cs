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

namespace Round1C_2014.Problem2
{
    public class PortalInput
    {
        public int R;
        public int C;
        public string[] grid;

        public Point<int> start;
        public Point<int> finish;
    }

    public class Portal : InputFileProducer<PortalInput>, InputFileConsumer<PortalInput, string>
    {
        public PortalInput createInput(Scanner scanner)
        {
            PortalInput input = new PortalInput();
            input.R = scanner.nextInt();
            input.C = scanner.nextInt();

            input.grid = new string[input.R];

            for (int r = input.R - 1; r >= 0; --r )
            {
                input.grid[r] = scanner.nextWord();
                int start = input.grid[r].IndexOf('O');
                if (start != -1)
                {
                    input.start = new Point<int>(r, start);
                }
                int stop = input.grid[r].IndexOf('X');
                if (stop != -1)
                {
                    input.finish = new Point<int>(r, stop);
                }
            }

            return input;
        }

        public string processInput(PortalInput input)
        {
            int[][] closestWall;
            calculateClosestWall(out closestWall, input);
            throw new NotImplementedException();
        }

        void calculateClosestWall(out int[][] closestWall, PortalInput input)
        {
            Ext.createArray(out closestWall, input.R, input.C, 1000);

            for(int r = 0; r < input.R; ++r)
            {
                for(int c = 0; c < input.C; ++c)
                {
                    if (input.grid[r][c] == '#')  
                    {
                        closestWall[r][c] = 0;
                        continue;
                    }

                    if (c == 0 || r == 0 || c == input.C - 1 || r == input.R - 1)
                    {
                        closestWall[r][c] = 1;
                        continue;
                    }

                    closestWall[r][c] = Math.Min(1 + closestWall[r][c-1], 1 + closestWall[r-1][c]);                    
                }
            }

            for (int r = input.R - 2; r > 0; --r)
            {
                for (int c = input.C - 2; c > 0; --c)
                {
                    if (input.grid[r][c] == '#')
                    {
                        continue;
                    }

                    closestWall[r][c] = Math.Min(1 + closestWall[r][c + 1], 1 + closestWall[r + 1][c]);
                }
            }
        }
    }

    internal class DijkstraNode : IComparable<DijkstraNode>
    {
        internal Point<int> curLocation;
        internal int distance;

        public DijkstraNode(Point<int> curLocation, int distance)
        {
            this.curLocation = curLocation;
            this.distance = distance;
        }

        //because PQ takes highest, reverse the order to get lowest distance
        public int CompareTo(DijkstraNode rhs)
        {
            return rhs.distance.CompareTo(distance);
        }
    }

    [TestFixture]
    public class PortalTest
    {
        [Test]
        public void Test()
        {
            PortalInput i = new PortalInput();
           

            Portal e = new Portal();
            Assert.AreEqual("2", e.processInput(i));
        }
    }
}