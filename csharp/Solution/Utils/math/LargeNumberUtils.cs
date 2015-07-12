using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Utils;

namespace CodeJam.Utils.math
{
    public static class LargeNumberUtils
    {

        public static int[][] GenerateModedCombin(int max, int mod)
        {
            int[][] c;
            Ext.createArray(out c, max + 1, max + 1, 0);

            for (int i = 0; i <= max; i++)
            {
                c[i][0] = 1;
                if (i != 0)
                    for (int j = 1; j <= max; j++)
                        c[i][j] = (c[i - 1][j - 1] + c[i - 1][j]) % mod;


            }

            return c;
        }
    }
}
