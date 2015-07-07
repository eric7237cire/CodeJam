using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Utils;


namespace CodeJam.Utils.geom
{
    public class PointInt32 : IEquatable<PointInt32>, IComparable<PointInt32> 
    {
        
		public static PointInt32[] NESW = new PointInt32[] {
			new PointInt32(0, 1),
			new PointInt32(1, 0),
			new PointInt32(0, -1),
			new PointInt32(-1, 0)
		};

        private Int32 x;
        private Int32 y;

        public Int32 X
        {
            get
            {
                return x;
            }
            set
            {
                x = value;
            }
        }

        public Int32 Y
        {
            get
            {
                return y;
            }
            set
            {
                y = value;
            }
        }

        public PointInt32(Int32 x, Int32 y)
        {
            this.x = x;
            this.y = y;
        }

        private const int prime1 = 17;
        private const int prime2 = 23;

        public override bool Equals(object obj)
        {
            return this.Equals(obj as PointInt32);

        }
        public override int GetHashCode()
        {
            
            int hash = 17;
            hash = hash * 23 + x.GetHashCode();
            hash = hash * 23 + y.GetHashCode();
            return hash;
            
        }


        public bool Equals(PointInt32 other)
        {
            if (ReferenceEquals(null, other)) return false;
            return (this.x.Equals(other.x))
                && (this.y.Equals(other.y));
        }

        public static bool operator ==(PointInt32 leftOperand, PointInt32 rightOperand)
        {
            if (ReferenceEquals(null, leftOperand)) return ReferenceEquals(null, rightOperand);
            return leftOperand.Equals(rightOperand);
        }

        public static bool operator !=(PointInt32 leftOperand, PointInt32 rightOperand)
        {
            return !(leftOperand == rightOperand);
        }

        public override string ToString()
        {
            return "({0}, {1})".FormatThis(x.ToString(), y.ToString());
        }

        public int CompareTo(PointInt32 other)
        {
            int yCmp = Y.CompareTo(other.Y);
            if (yCmp != 0)
                return yCmp;

            return X.CompareTo(other.X);
        }
                        
        public static PointInt32 operator -(PointInt32 lhs, PointInt32 rhs)
        {
            return new PointInt32( lhs.X - rhs.X, lhs.Y - rhs.Y );
        }
        public static PointInt32 operator +(PointInt32 lhs, PointInt32 rhs)
        {
            return new PointInt32(lhs.X + rhs.X, lhs.Y + rhs.Y);
        }
        public static PointInt32 operator /(PointInt32 lhs, Int32 rhs)
        {
            return new PointInt32(  lhs.X / rhs, lhs.Y / rhs );
        }
		        public static PointDouble operator /(PointInt32 lhs, Double rhs)
        {
            return new PointDouble( 
                lhs.X / rhs,
                lhs.Y / rhs);
        }
	}
		    public class PointInt64 : IEquatable<PointInt64>, IComparable<PointInt64> 
    {
        
		public static PointInt64[] NESW = new PointInt64[] {
			new PointInt64(0, 1),
			new PointInt64(1, 0),
			new PointInt64(0, -1),
			new PointInt64(-1, 0)
		};

        private Int64 x;
        private Int64 y;

        public Int64 X
        {
            get
            {
                return x;
            }
            set
            {
                x = value;
            }
        }

        public Int64 Y
        {
            get
            {
                return y;
            }
            set
            {
                y = value;
            }
        }

        public PointInt64(Int64 x, Int64 y)
        {
            this.x = x;
            this.y = y;
        }

        private const int prime1 = 17;
        private const int prime2 = 23;

        public override bool Equals(object obj)
        {
            return this.Equals(obj as PointInt64);

        }
        public override int GetHashCode()
        {
            
            int hash = 17;
            hash = hash * 23 + x.GetHashCode();
            hash = hash * 23 + y.GetHashCode();
            return hash;
            
        }


        public bool Equals(PointInt64 other)
        {
            if (ReferenceEquals(null, other)) return false;
            return (this.x.Equals(other.x))
                && (this.y.Equals(other.y));
        }

        public static bool operator ==(PointInt64 leftOperand, PointInt64 rightOperand)
        {
            if (ReferenceEquals(null, leftOperand)) return ReferenceEquals(null, rightOperand);
            return leftOperand.Equals(rightOperand);
        }

        public static bool operator !=(PointInt64 leftOperand, PointInt64 rightOperand)
        {
            return !(leftOperand == rightOperand);
        }

        public override string ToString()
        {
            return "({0}, {1})".FormatThis(x.ToString(), y.ToString());
        }

        public int CompareTo(PointInt64 other)
        {
            int yCmp = Y.CompareTo(other.Y);
            if (yCmp != 0)
                return yCmp;

            return X.CompareTo(other.X);
        }
                        
        public static PointInt64 operator -(PointInt64 lhs, PointInt64 rhs)
        {
            return new PointInt64( lhs.X - rhs.X, lhs.Y - rhs.Y );
        }
        public static PointInt64 operator +(PointInt64 lhs, PointInt64 rhs)
        {
            return new PointInt64(lhs.X + rhs.X, lhs.Y + rhs.Y);
        }
        public static PointInt64 operator /(PointInt64 lhs, Int64 rhs)
        {
            return new PointInt64(  lhs.X / rhs, lhs.Y / rhs );
        }
		        public static PointDouble operator /(PointInt64 lhs, Double rhs)
        {
            return new PointDouble( 
                lhs.X / rhs,
                lhs.Y / rhs);
        }
	}
		    public class PointDouble : IEquatable<PointDouble>, IComparable<PointDouble> 
    {
        
		public static PointDouble[] NESW = new PointDouble[] {
			new PointDouble(0, 1),
			new PointDouble(1, 0),
			new PointDouble(0, -1),
			new PointDouble(-1, 0)
		};

        private Double x;
        private Double y;

        public Double X
        {
            get
            {
                return x;
            }
            set
            {
                x = value;
            }
        }

        public Double Y
        {
            get
            {
                return y;
            }
            set
            {
                y = value;
            }
        }

        public PointDouble(Double x, Double y)
        {
            this.x = x;
            this.y = y;
        }

        private const int prime1 = 17;
        private const int prime2 = 23;

        public override bool Equals(object obj)
        {
            return this.Equals(obj as PointDouble);

        }
        public override int GetHashCode()
        {
            
            int hash = 17;
            hash = hash * 23 + x.GetHashCode();
            hash = hash * 23 + y.GetHashCode();
            return hash;
            
        }


        public bool Equals(PointDouble other)
        {
            if (ReferenceEquals(null, other)) return false;
            return (this.x.Equals(other.x))
                && (this.y.Equals(other.y));
        }

        public static bool operator ==(PointDouble leftOperand, PointDouble rightOperand)
        {
            if (ReferenceEquals(null, leftOperand)) return ReferenceEquals(null, rightOperand);
            return leftOperand.Equals(rightOperand);
        }

        public static bool operator !=(PointDouble leftOperand, PointDouble rightOperand)
        {
            return !(leftOperand == rightOperand);
        }

        public override string ToString()
        {
            return "({0}, {1})".FormatThis(x.ToString(), y.ToString());
        }

        public int CompareTo(PointDouble other)
        {
            int yCmp = Y.CompareTo(other.Y);
            if (yCmp != 0)
                return yCmp;

            return X.CompareTo(other.X);
        }
                        
        public static PointDouble operator -(PointDouble lhs, PointDouble rhs)
        {
            return new PointDouble( lhs.X - rhs.X, lhs.Y - rhs.Y );
        }
        public static PointDouble operator +(PointDouble lhs, PointDouble rhs)
        {
            return new PointDouble(lhs.X + rhs.X, lhs.Y + rhs.Y);
        }
        public static PointDouble operator /(PointDouble lhs, Double rhs)
        {
            return new PointDouble(  lhs.X / rhs, lhs.Y / rhs );
        }
			}
		}
