package codejam.utils.datastructures;

import static org.junit.Assert.*;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.*;
import com.google.common.math.IntMath;

public class SegmentTreeSum
{
    final protected static Logger log = LoggerFactory.getLogger("main");

    public final static char NONE = 'n';
    public final static char ERASE = 'e';
    public final static char INVERT = 'i';
    public final static char SET = 's';

    private static class Node
    {
        public int value, len;
        char utype;
    }

    public SegmentTreeSum(int size)
    {
        this(size, null);
    }
    
    public SegmentTreeSum(int size, int[] initialValues) {
        double log2Size = Math.log10((double) size) / Math.log10(2.0);
        int len = 2 * IntMath.pow(2, (int) Math.floor(log2Size) + 1);

       // int len2 = 5 * size;
       // log.debug("Len {}  len2 {} ", len, len2);
        tree = new Node[len];

        for (int i = 0; i < tree.length; ++i)
        {
            tree[i] = new Node();
        }

        rightMax = size - 1;
        st_build(tree, 1, 0, size - 1, initialValues);
    }
    
    

    private final int rightMax;
    Node[] tree;

    //-----------------------------------
    void st_build(Node tree[], int vertex, int l, int r, int[] initialValues)
    {
        if (l == r)                       //If it's a leaf node, set its value to a[l] or a[r]
        {
            tree[vertex].value = initialValues == null ? 0 : initialValues[l];
            tree[vertex].utype = NONE;
            tree[vertex].len = 1;
        } else
        {
            int mid = (l + r) / 2;
            st_build(tree, vertex * 2, l, mid, initialValues);       //Calculate its children
            st_build(tree, vertex * 2 + 1, mid + 1, r, initialValues);
            tree[vertex].value = tree[vertex * 2].value + tree[vertex * 2 + 1].value; //Calculate its value
            tree[vertex].utype = NONE;
            tree[vertex].len = r - l + 1;
        }
    }

    //-----------------------------------
    void change(int i)
    {            //Apply the appropriate cast
        switch (tree[i].utype) {
        case SET:
            tree[i].value = tree[i].len;
            break;
        case ERASE:
            tree[i].value = 0;
            break;
        case INVERT:
            tree[i].value = tree[i].len - tree[i].value;
            break;
        default:
            return;
        }
        if (tree[i].len > 1)
        {
            set_utype(i * 2, tree[i].utype);   //After applying, push down the cast type to its children
            set_utype(i * 2 + 1, tree[i].utype);
        }
        tree[i].utype = NONE;             //Its cast type is now NULL
    }

    //-----------------------------------
    void set_utype(int vertex, char type)
    {
        if (type == INVERT)                  //Special case
        {
            switch (tree[vertex].utype) {
            case SET:
                tree[vertex].utype = ERASE;
                break;
            case ERASE:
                tree[vertex].utype = SET;
                break;
            case INVERT:
                tree[vertex].utype = NONE;
                break;
            case NONE:
                tree[vertex].utype = INVERT;
                break;
            default:
                break;
            }
        } else
            tree[vertex].utype = type;
    }

    public void update(int i, int j, char type)
    {
        update(1, 0, rightMax, i, j, type);
    }
    
    //-----------------------------------
    void update(int vertex, int left, int right, int i, int j, char type)
    {
        // log.debug("Update vertex {} left {} right {} i {} j {} type {}", vertex, l, r, i, j, type);
        int mid = (left + right) / 2;
        if (i <= left && right <= j)
            set_utype(vertex, type);   //If it fits in the interval, set it cast type
        change(vertex);                             //Do the cast type
        
        
        if (i <= left  && right <= j) {
          //No need to go any further ; lazy prop
            return;
        }
        else if (left > j || right < i)
            return;                     //If it's out the quit
        else
        {
            update(vertex * 2, left, mid, i, j, type);
            update(vertex * 2 + 1, mid + 1, right, i, j, type);
            tree[vertex].value = tree[vertex * 2].value + tree[vertex * 2 + 1].value;
            // log.debug("Update.  Set value vertex {} = {} left {} right {} i {} j {} type {}", vertex, tree[vertex].value, l, r, i, j, type);
        }
    }

    public int rangeSumQuery(int i, int j)
    {
        return rangeSumQuery(1, 0, rightMax, i, j);
    }
    
    //-----------------------------------
    int rangeSumQuery(int vertex, int left, int right, int i, int j)
    {
        int mid = (left + right) / 2;
        change(vertex);             //As traversing down, update the cast type of current index
        
        //Tree node is completely contained by i <= left <= right <= j 
        if (left >= i && right <= j)
            return (tree[vertex].value);
        
        //Tree node is outside
        if (left > j || right < i)
            return (0);
        int p1 = rangeSumQuery(vertex * 2, left, mid, i, j);
        int p2 = rangeSumQuery(vertex * 2 + 1, mid + 1, right, i, j);
        
        //Update tree value which is not the same as p1 and p2, since this is over the full range
        tree[vertex].value = tree[vertex * 2].value + tree[vertex * 2 + 1].value;
        /*
        log.debug("Answer vertex {} left {} right {} i {} j {} p1 {} p2 {} p1+p2 {}" +
        		" left tree val {} right tree val {} total val {}type {}", vertex, tree[vertex].value, left, right,  i, j, p1,p2,p1+p2, tree[vertex * 2].value,tree[vertex * 2 + 1].value,
        		tree[vertex].value);
        		*/
        return (p1 + p2);
    }
    
    /**
     * Finds index of value set to zero
     * @param startFrom
     * @return
     */
    public int findFirstUnsetValue(int startFrom)
    {
        return findFirstUnsetValue(1, 0, rightMax, startFrom);
    }
    
    int findFirstUnsetValue(int vertex, int left, int right, int i)
    {
        
        int mid = (left + right) / 2;
        change(vertex);             //As traversing down, update the cast type of current index
        
        checkState(right >= left);
        checkState(right <= rightMax);
        
        //Out of range
        if ( i > right )
            return -1;
        
        //Done searching
        if (left == right) {
            if (tree[vertex].value == 0)
                return left;
            else 
                return -1;
        }
        
        //See if it is even possible in this range
        int len = right - left + 1;
        
        if (tree[vertex].value == len) {
            return -1;
        }
                
        int p1 = findFirstUnsetValue(vertex * 2, left, mid, i);
                
        int p2 = findFirstUnsetValue(vertex * 2 + 1, mid + 1, right, i);
        
        //Take into account lazy propogations
        tree[vertex].value = tree[vertex * 2].value + tree[vertex * 2 + 1].value;
        

        if (p1 >= 0)
            return p1;
        
        return p2;
        
        //Update tree value which is not the same as p1 and p2, since this is over the full range
        
        /*
        log.debug("Answer vertex {} left {} right {} i {} j {} p1 {} p2 {} p1+p2 {}" +
                " left tree val {} right tree val {} total val {}type {}", vertex, tree[vertex].value, left, right,  i, j, p1,p2,p1+p2, tree[vertex * 2].value,tree[vertex * 2 + 1].value,
                tree[vertex].value);
                */
    }
    
    public int findLowestIndexWithSum(int targetSum)
    {
        return findLowestIndexWithSum(1, 0, rightMax, targetSum);
    }
    
    int findLowestIndexWithSum(int vertex, int left, int right, int targetSum)
    {
        
        int mid = (left + right) / 2;
        change(vertex);             //As traversing down, update the cast type of current index
        
        checkState(right >= left);
        checkState(right <= rightMax);
        

        checkState(tree[vertex].value >= targetSum);
        
        if (left == right) {
            return left;
        }
                
        int p1 = rangeSumQuery(vertex * 2, left, mid, left, mid);
        
        
        if (targetSum <= p1) 
        {
            return findLowestIndexWithSum(vertex*2, left, mid, targetSum);
        } else {
            //Must be in the second half, subtract first half
            return findLowestIndexWithSum(vertex*2+1, mid+1, right, targetSum - p1);
        }
        
        
        /*
        log.debug("Answer vertex {} left {} right {} i {} j {} p1 {} p2 {} p1+p2 {}" +
                " left tree val {} right tree val {} total val {}type {}", vertex, tree[vertex].value, left, right,  i, j, p1,p2,p1+p2, tree[vertex * 2].value,tree[vertex * 2 + 1].value,
                tree[vertex].value);
                */
    }

}
