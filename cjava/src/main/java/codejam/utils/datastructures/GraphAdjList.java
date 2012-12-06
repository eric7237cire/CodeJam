package codejam.utils.datastructures;

import java.util.ArrayList;
import java.util.List;

/*
 * Nodes 0 based ints
 */
public class GraphAdjList {

    private boolean[][] connected;
    int maxNodeNum;

    public GraphAdjList(int maxNodeNum) {
        this.maxNodeNum = maxNodeNum;
        connected = new boolean[maxNodeNum][maxNodeNum];
    }

    public void addConnection(int n1, int n2) {
         connected[n1][n1] = true;
         connected[n2][n2] = true;
        connected[n1][n2] = true;
        connected[n2][n1] = true;
    }

    public boolean isConnected(int n1, int n2) {
        return connected[n1][n2];
    }
    public boolean nodePresent(int n) {
        return isConnected(n,n);
    }

    public List<List<Integer>> getConnectedComponents() {
        int[] componentNumPerNode = new int[maxNodeNum];

        List<List<Integer>> groups = new ArrayList<>();

        int currentNodeNum = 0;
        int currentGroupNum = 0;

        while (true) {
            ++currentGroupNum;
            
            while (currentNodeNum < maxNodeNum
                    && (componentNumPerNode[currentNodeNum] != 0 || !nodePresent(currentNodeNum) )) {
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

                for (int i = 0; i < maxNodeNum; ++i) {
                    if (isConnected(node, i)) {
                        nodesToVisit.add(i);
                    }
                }
            }

        }

        return groups;
    }
}
