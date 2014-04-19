using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Utils
{
    public static class Ext
    {

        //Extension method to be able to do "{0}".Format directly
        public static string FormatThis(this string str, params object[] args)
        {
            return String.Format(str, args);
        }

        //Add extension to list to get last value
        public static T GetLastValue<T>(this List<T> list)
        {
            return list[list.Count - 1];
        }

        public static Tuple<T, T> binarySearch<T>(this List<T> list, T target) where T : IComparable<T>
        {
            int lowIdx, hiIdx;
            return binarySearch(list, target, out lowIdx, out hiIdx);
        }
        //binary search sorted list
        public static Tuple<T, T> binarySearch<T>(this List<T> list, T target, out int lowIdx, out int hiIdx) where T : IComparable<T>
        {
            //n * (n+1) / 2 == sum  
            //n^2 + n - 2* sum = 0
            //-b +/- sqrt(b^2 - 4ac) / 2a

            //using binary search ; invariant lo <= sum < hi
            lowIdx = 0;
            hiIdx = list.Count - 1;

            while (hiIdx - lowIdx > 1)
            {
                int midIdx = lowIdx + (hiIdx - lowIdx) / 2;

                if (list[midIdx].CompareTo(target) > 0)
                {
                    hiIdx = midIdx;
                }
                else
                {
                    lowIdx = midIdx;
                }
            }

            return new Tuple<T, T>(list[lowIdx], list[hiIdx]);
        }

    }
}
