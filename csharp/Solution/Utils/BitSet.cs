using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Utils
{
    public static class BitSetExt
    {
        public static bool GetBit(this int bits, int pos)
        {
            return (bits >> pos & 1) == 1; 
        }

        public static int SetBit(this int bits, int pos)
        {
            return bits | 1 << pos; 
        }

        public static int ClearBit(this int bits, int pos)
        {
            return bits & ~(1 << pos);
        }

        public static long GetBit(this long bits, int pos)
        {
            return bits >> pos & 1L;
        }

        public static long SetBit(this long bits, int pos)
        {
            return bits | 1L << pos;
        }

        public static long ClearBit(this long bits, int pos)
        {
            return bits & ~(1L << pos);
        }
    }
    public struct BitSet
    {
        public int bits;

        public BitSet(int data = 0)
        {
            this.bits = data;
        }

        public bool this[int pos]
        {
            get { Preconditions.checkState(pos >= 0 && pos <= 31); return (bits >> pos & 1) == 1; }
            set { Preconditions.checkState(pos >= 0 && pos <= 31); if (value) bits |= 1 << pos; else bits &= ~(1 << pos); }
        }
    }
}
