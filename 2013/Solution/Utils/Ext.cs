using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Utils
{
    public static class Ext
    {

        public static string ToUsString(this double d, int places)
        {
            return d.ToString("0." + new String('#', places), new CultureInfo("en-US"));
        }

        public static string ToBinaryString(this int i, int len)
        {
            return Convert.ToString(i, 2).PadLeft(len,'0').Substring(0, len);
        }
        

        public static string ToCommaString<T>(this IEnumerable<T> list)
        {
            return string.Join(", ", list);
        }

        //Extension method to be able to do "{0}".Format directly
        public static string FormatThis(this string str, params object[] args)
        {
            return String.Format(str, args);
        }

        //Add extension to list to get last value
        public static T GetLastValue<T>(this IList<T> list)
        {
            return list[list.Count - 1];
        }

        public static Tuple<T, T> binarySearch<T>(this List<T> list, T target) where T : IComparable<T>
        {
            int lowIdx, hiIdx;
            return binarySearch(list, target, out lowIdx, out hiIdx);
        }

        public static int lowerBound<T>(this List<T> list, T target) where T : IComparable<T>
        {
            return lowerBound(list, 0, list.Count, target);
        }
        public static int lowerBound<T>(this List<T> list, int lowIdx, int hiIdx, T target) where T : IComparable<T>
        {

            //using binary search ; invariant lo < sum <= hi
            //lowIdx = 0;
            //hiIdx = list.Count - 1;
            Preconditions.checkState(lowIdx <= hiIdx);

            if (list[lowIdx].CompareTo(target) >= 0)
            {
                return lowIdx;
            }
            
            while (hiIdx - lowIdx > 1)
            {
                int midIdx = lowIdx + (hiIdx - lowIdx) / 2;

                if (list[midIdx].CompareTo(target) >= 0)
                {
                    hiIdx = midIdx;
                }
                else
                {
                    lowIdx = midIdx;
                }
            }

            return lowIdx + 1;
        }

        //binary search sorted list
        public static Tuple<T, T> binarySearch<T>(this List<T> list, T target, out int lowIdx, out int hiIdx) where T : IComparable<T>
        {
           
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

        //binary search sorted list
        public static bool binarySearchGT<T>(this List<T> list, T target, out int lowIdx, out int hiIdx) where T : IComparable<T>
        {

            
            //using binary search ; invariant lo < sum <= hi
            lowIdx = 0;
            hiIdx = list.Count - 1;

            if (target.CompareTo(list[lowIdx]) <= 0)
            {
                return false;
            }

            if (target.CompareTo(list[hiIdx]) > 0)
            {
                return false;
            }

            while (hiIdx - lowIdx > 1)
            {
                int midIdx = lowIdx + (hiIdx - lowIdx) / 2;

                if (list[midIdx].CompareTo(target) >= 0)
                {
                    hiIdx = midIdx;
                }
                else
                {
                    lowIdx = midIdx;
                }
            }

            return true;
        }

    }
}
