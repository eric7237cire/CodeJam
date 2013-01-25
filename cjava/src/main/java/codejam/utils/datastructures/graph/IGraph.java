package codejam.utils.datastructures.graph;

/**
 * Each vertex is an int.  
 * No vertex number is >= V
 * 
 *
 */
public interface IGraph
{
    //Number of verticies
    public int V();
    
    public boolean nodePresent(int node);
    
    public boolean isConnected(int nodeA, int nodeB);
}
