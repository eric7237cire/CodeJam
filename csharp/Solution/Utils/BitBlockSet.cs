using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CodeJam.Utils
{
    public class BitBlockSet : IEquatable<BitBlockSet>
    {
       
   
        private BitBlockSet() { 
        }
  
        /*=========================================================================
        ** Allocates space to hold length bit values. All of the values in the bit
        ** array are set to false.
        ** 
        ** Exceptions: ArgumentException if length < 0.
        =========================================================================*/
        public BitBlockSet(int length) 
            : this(length, false) {
        } 
 
        /*=========================================================================
        ** Allocates space to hold length bit values. All of the values in the bit
        ** array are set to defaultValue. 
        **
        ** Exceptions: ArgumentOutOfRangeException if length < 0. 
        =========================================================================*/
        public BitBlockSet(int length, bool defaultValue) {
            
            
  
            m_array = new ulong[GetArrayLength(length, BitsPerInt32)];
            m_length = length; 
  
            ulong fillValue = defaultValue ? ulong.MaxValue : 0;
            for (int i = 0; i < m_array.Length; i++) { 
                m_array[i] = fillValue;
            }
 
        }
  
       
        public BitBlockSet(bool[] values) { 
            if (values == null) { 
                throw new ArgumentNullException("values");
            } 
           
 
            m_array = new ulong[GetArrayLength(values.Length, BitsPerInt32)];
            m_length = values.Length; 
 
            
 
            m_array = new ulong[values.Length]; 
            m_length = values.Length * BitsPerInt32; 
 
            Array.Copy(values, m_array, values.Length); 
 
        }
  
        /*=========================================================================
        ** Allocates a new BitBlockSet with the same length and bit values as bits. 
        ** 
        ** Exceptions: ArgumentException if bits == null.
        =========================================================================*/
        public BitBlockSet(BitBlockSet bits) {
            if (bits == null) {
                throw new ArgumentNullException("bits");
            } 
          
  
            int arrayLength = GetArrayLength(bits.m_length, BitsPerInt32); 
            m_array = new ulong[arrayLength];
            m_length = bits.m_length; 
 
            Array.Copy(bits.m_array, m_array, arrayLength);
 
        }
  
        public bool this[int index] { 
                get {
                    return Get(index); 
                }
                set {
                    Set(index,value);
                } 
         }

        // Summary:
        //     Indicates whether the current object is equal to another object of the same
        //     type.
        //
        // Parameters:
        //   other:
        //     An object to compare with this object.
        //
        // Returns:
        //     true if the current object is equal to the other parameter; otherwise, false.
        public bool Equals(BitBlockSet other)
        {
            if (m_length != other.m_length)
                return false;

            for(int i = 0; i < m_array.Length; ++i)
            {
                if (m_array[i] != other.m_array[i])
                    return false;
            }

            return true;
        }
  
        /*========================================================================== 
        ** Returns the bit value at position index.
        ** 
        ** Exceptions: ArgumentOutOfRangeException if index < 0 or
        **             index >= GetLength().
        =========================================================================*/
        public bool Get(int index) { 
            
  
            return (m_array[index / BitsPerInt32] & (1UL << (index % BitsPerInt32))) != 0;
        }
 
        /*========================================================================== 
        ** Sets the bit value at position index to value.
        ** 
        ** Exceptions: ArgumentOutOfRangeException if index < 0 or 
        **             index >= GetLength().
        =========================================================================*/
        public void Set(int index, bool value) {
            
  
            if (value) { 
                m_array[index / BitsPerInt32] |= (1ul << (index % BitsPerInt32));
            } else { 
                m_array[index / BitsPerInt32] &= ~(1ul << (index % BitsPerInt32));
            }
 
        }

        public int NumberOfSetBits()
        {
            int sum = 0;
            for(int i = 0; i < m_array.Length; ++i)
            {
                sum += NumberOfSetBits(m_array[i]);
            }

            return sum;
        }
        public static int NumberOfSetBits(ulong i)
        {
            i = i - ((i >> 1) & 0x5555555555555555UL);
            i = (i & 0x3333333333333333UL) + ((i >> 2) & 0x3333333333333333UL);
            return (int)(unchecked(((i + (i >> 4)) & 0xF0F0F0F0F0F0F0FUL) * 0x101010101010101UL) >> 56);
        }
 
        /*==========================================================================
        ** Returns a reference to the current instance ANDed with value.
        ** 
        ** Exceptions: ArgumentException if value == null or
        **             value.Length != this.Length. 
        =========================================================================*/
        public BitBlockSet And(BitBlockSet value) {
            if (value==null) 
                throw new ArgumentNullException("value");
            
 
            int ints = GetArrayLength(m_length, BitsPerInt32); 
            for (int i = 0; i < ints; i++) { 
                m_array[i] &= value.m_array[i];
            } 
 
            
            return this;
        } 
 
        /*========================================================================= 
        ** Returns a reference to the current instance ORed with value. 
        **
        ** Exceptions: ArgumentException if value == null or 
        **             value.Length != this.Length.
        =========================================================================*/
        public BitBlockSet Or(BitBlockSet value) {
            if (value==null) 
                throw new ArgumentNullException("value");
            
  
            int ints = GetArrayLength(m_length, BitsPerInt32);
            for (int i = 0; i < ints; i++) {
                m_array[i] |= value.m_array[i];
            } 
 
          
            return this; 
        }
  
        /*=========================================================================
        ** Returns a reference to the current instance XORed with value.
        **
        ** Exceptions: ArgumentException if value == null or 
        **             value.Length != this.Length.
        =========================================================================*/
        public BitBlockSet Xor(BitBlockSet value) { 
            if (value==null)
                throw new ArgumentNullException("value"); 
            
  
            int ints = GetArrayLength(m_length, BitsPerInt32);
            for (int i = 0; i < ints; i++) { 
                m_array[i] ^= value.m_array[i]; 
            }
  
            
            return this;
        }
  
        /*=========================================================================
        ** Inverts all the bit values. On/true bit values are converted to 
        ** off/false. Off/false bit values are turned on/true. The current instance 
        ** is updated and returned.
        =========================================================================*/
        public BitBlockSet Not() {
            int ints = GetArrayLength(m_length, BitsPerInt32);
            for (int i = 0; i < ints; i++) {
                m_array[i] = ~m_array[i]; 
            }
  
            
            return this;
        } 
 
        public int Length {
            get {
                return m_length;
            } 
            set { 
                
                
 
                int newints = GetArrayLength(value, BitsPerInt32); 
                if (newints > m_array.Length || newints  < m_array.Length) {
                    // grow or shrink (if wasting more than _ShrinkThreshold ints) 
                    ulong[] newarray = new ulong[newints]; 
                    Array.Copy(m_array, newarray, newints > m_array.Length ? m_array.Length : newints);
                    m_array = newarray; 
                }
 
                if (value > m_length) {
                    // clear high bit values in the last int 
                    int last = GetArrayLength(m_length, BitsPerInt32) - 1;
                    int bits = m_length % 32; 
                    if (bits > 0) { 
                        m_array[last] &= (1ul << bits) - 1;
                    } 
 
                    // clear remaining int values
                    Array.Clear(m_array, last + 1, newints - last - 1);
                } 
 
                m_length = value; 
            }
        } 
 
       
  
        public int Count 
        {
            get
            {
                
                return m_length; 
            }
        } 
  
       
  
      
 
        public bool IsReadOnly
        { 
            get
            { 
                return false; 
            }
        } 
 
        public bool IsSynchronized
        {
            get
            {
                return false; 
            } 
        }

        public override String ToString()
        {
            StringBuilder sb = new StringBuilder();
            
            for (int i = 0; i < this.Count; ++i)
            {
                sb.Append(this[i] ? '1' : '0');
            }
            return sb.ToString();
        }
  
        public IEnumerator GetEnumerator()
        {
            return new BitBlockSetEnumeratorSimple(this);
        } 
 
        // XPerY=n means that n Xs can be stored in 1 Y. 
        private const int BitsPerInt32 = 64; 
        private const int BytesPerInt32 = 8;
        private const int BitsPerByte = 8; 
 
        /// <summary>
        /// Used for conversion between different representations of bit array.
        /// Returns (n+(div-1))/div, rearranged to avoid arithmetic overflow. 
        /// For example, in the bit to int case, the straightforward calc would
        /// be (n+31)/32, but that would cause overflow. So instead it's 
        /// rearranged to ((n-1)/32) + 1, with special casing for 0. 
        ///
        /// Usage: 
        /// GetArrayLength(77, BitsPerInt32): returns how many ints must be
        /// allocated to store 77 bits.
        /// </summary>
        /// <param name="n"> 
        /// <param name="div">use a conversion constant, e.g. BytesPerInt32 to get
        /// how many ints are required to store n bytes 
        /// <returns></returns> 
        private static int GetArrayLength(int n, int div) {
            return n > 0 ? (((n - 1) / div) + 1) : 0;
        }
 
        [Serializable] 
        private class BitBlockSetEnumeratorSimple : IEnumerator, ICloneable
        { 
            private BitBlockSet BitBlockSet; 
            private int index;
            
            private bool currentElement;
 
            internal BitBlockSetEnumeratorSimple(BitBlockSet BitBlockSet) {
                this.BitBlockSet = BitBlockSet; 
                this.index = -1;
                
            } 
 
            [System.Security.SecuritySafeCritical]  // auto-generated 
            public Object Clone() {
                return MemberwiseClone();
            }
  
            public virtual bool MoveNext() {
                if (index < (BitBlockSet.Count-1)) { 
                    index++;
                    currentElement = BitBlockSet.Get(index); 
                    return true;
                }
                else
                    index = BitBlockSet.Count; 
 
                return false; 
            } 
 
            public virtual Object Current { 
                get {
                    return currentElement; 
                } 
            }
  
            public void Reset() {
                index = -1;
            } 
        }
  
        private ulong[] m_array; 
        private int m_length;
       
       
    }
  

    
}
