using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using C5;
namespace Round1C
{
    public class Interval<CoordType, DataType>
    {
        public CoordType Start { get; internal set; }
        public CoordType Stop { get; internal set; }

        public DataType Data { get; set; }
    }


    class Node<CoordType>
    {
        CoordType location;

        Node<CoordType> rightNode;
        Node<CoordType> leftNode;

        TreeDictionary<CoordType, Node<CoordType>> nodesSortedByStart;
        TreeDictionary<CoordType, Node<CoordType>> nodesSortedByStop ;

        Node()
        {
            nodesSortedByStart = new TreeDictionary<CoordType, Node<CoordType>>();
            nodesSortedByStop = new TreeDictionary<CoordType, Node<CoordType>>();
        }
    }

    public class IntervalTree<CoordType, DataType>
    {
        private Node<CoordType> root;
    }


    public sealed class ReverseComparer<T> : IComparer<T>
    {
        private IComparer<T> comparer;


        public ReverseComparer()
            : this(Comparer<T>.Default)
        { }

        public ReverseComparer(IComparer<T> comparer)
        {
            if (comparer == null)
                throw new ArgumentNullException("comparer");

            this.comparer = comparer;
        }

        public int Compare(T x, T y)
        {
            return comparer.Compare(y, x);
        }
    }
}
