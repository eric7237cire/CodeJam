package codejam.utils.datastructures.graph;

import java.util.ArrayList;
import java.util.List;

/**
 * Find connected components
 * @author thresh
 *
 */
public class CCbfs
{
    GraphInt graph;
    
    public CCbfs(GraphInt graph) {
        this.graph = graph;
    }
    
    public List<List<Integer>> go() {
        int maxNodeNum = graph.getMaxVNum() + 1;
        
        int[] componentNumPerNode = new int[maxNodeNum];

        List<List<Integer>> groups = new ArrayList<>();

        int currentNodeNum = 0;
        int currentGroupNum = 0;

        while (true) {
            ++currentGroupNum;
            
            while (currentNodeNum < maxNodeNum
                    && (componentNumPerNode[currentNodeNum] != 0 || !graph.nodeConnections.containsKey(currentNodeNum) )) {
                // find next node num with no group num
                ++currentNodeNum;
            }

            if (currentNodeNum >= maxNodeNum) {
                break;
            }
            
            groups.add(new ArrayList<Integer>());

            // Search all nodes connected
            boolean[] visited = new boolean[maxNodeNum];

            List<Integer> nodesToVisit = new ArrayList<>();
            nodesToVisit.add(currentNodeNum);

            while (!nodesToVisit.isEmpty()) {
                Integer node = nodesToVisit.remove(nodesToVisit.size() - 1);

                if (visited[node]) {
                    continue;
                }

                visited[node] = true;
                componentNumPerNode[node] = currentGroupNum;
                groups.get(currentGroupNum - 1).add(node);

                for (int adjNode : graph.getNeighbors(node)) {
                    nodesToVisit.add(adjNode);                    
                }
            }

        }

        return groups;
    }
    

}
