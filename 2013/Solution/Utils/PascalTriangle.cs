using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace PascalsTriangle
{
    public class PascalsTriangle<DataType>
    {
        private readonly int totalRows;

        DataType[][] jaggedArray;

        public PascalsTriangle(int totalRows)
        {
            this.totalRows = totalRows;
            jaggedArray = new DataType[this.totalRows][];

            for(int r = 0; r < totalRows; ++r)
            {
                jaggedArray[r] = new DataType[r + 1];
            }
        }

        /// <summary>
        /// stores a value in the triangle, 0 is the top row, 0 is the leftmost position
        /// </summary>
        /// <param name="row"></param>
        /// <param name="pos"></param>
        /// <param name="value"></param>

        public void store(int row, int pos, DataType value)
        {
            jaggedArray[row][pos] = value;
        }

        public DataType this[int row, int pos]
        {
            get { return jaggedArray[row][pos]; }
            set { store(row,pos, value); }
        }
    }
    public static class PascalTriangleCreator
    {
        //private DataType nonExistingParent;

        //public delegate DataType CreateNewNodeDelegate(DataType lhs, DataType rhs);

        //private CreateNewNodeDelegate createNewNode;

        private static int add(int a, int b)
        {
            checked
            {
                return a + b;
            }
        }

        private static double combine(double lhs, double rhs)
        {
            return lhs / 2d + rhs / 2d;
        }

        public static PascalsTriangle<int> createNormal(int rows)
        {
            return PascalTriangleCreator.create<int>(0, 1, add, rows);
        }

        /// <summary>
        /// Same as a normal pascal's triangle, but divided by 2^(row total).  Useful for probabilities.
        /// 
        /// ie, 4rd entry on 6th row is probability of getting exactly 3 heads in 5 coin flips
        /// </summary>
        /// <param name="rows"></param>
        /// <returns></returns>
        
        public static PascalsTriangle<double> createProb(int rows)
        {
            return PascalTriangleCreator.create<double>(0d, 1d, combine, rows);
        }

        public static PascalsTriangle<DataType> create<DataType>(DataType nonExistingParent, DataType root, 
            Func<DataType,DataType,DataType> createNewNode, int rows)
        {
            PascalsTriangle<DataType> triangle = new PascalsTriangle<DataType>(rows);

            triangle[0, 0] = root;

            for(int r = 1; r < rows; ++r)
            {
                //first (& last)
                triangle[r, 0] = createNewNode(nonExistingParent, triangle[r-1, 0]);

                triangle[r, r ] = createNewNode(triangle[r - 1, r - 1], nonExistingParent);

                for(int c = 1; c < r; ++c)
                {
                    triangle[r, c] = createNewNode(triangle[r - 1,c - 1], triangle[r - 1,c]);
                }
            }

            return triangle;
        }
    }
}
