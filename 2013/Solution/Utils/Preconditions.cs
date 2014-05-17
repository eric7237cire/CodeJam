using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Utils
{
    public static class Preconditions
    {
        [Conditional("DEBUG")]
        public static void checkState(bool condition, String msg = "empty")
        {
            if (!condition)
            {
                throw new ArgumentException(msg);
            }
        }
    }
}
