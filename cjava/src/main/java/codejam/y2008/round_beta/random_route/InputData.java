package codejam.y2008.round_beta.random_route;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Objects;

import codejam.utils.datastructures.graph.WeightedGraphInt;
import codejam.utils.main.AbstractInputData;

public class InputData extends AbstractInputData {

    static class Road {
        final int node1; //city with lower index
        final int node2;
        final int weight; //time between cities
        final Pair<Integer,Integer> edge;
        public Road(Pair<Integer,Integer> edge,  int weight) {
            super();
            this.node1 = edge.getLeft();
            this.node2 = edge.getRight();
            this.weight = weight;
            this.edge = edge;
        }
       
        @Override
        public int hashCode() {
            return Objects.hashCode(edge, weight);
        }
       
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Road o = (Road) obj;
            return Objects.equal(edge, o.edge) && Objects.equal(weight, o.weight);
        }
        
        
    }
    int nRoads;
    WeightedGraphInt graph;
    
    List<Road> roads;
    
    Map<Pair<Integer,Integer>, Integer> minWeight;
    Map<Road, Integer> count;
    
    int cityCount;
    
    public InputData(int testCase) {
        super(testCase);
    }

}