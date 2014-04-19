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

    }
}
