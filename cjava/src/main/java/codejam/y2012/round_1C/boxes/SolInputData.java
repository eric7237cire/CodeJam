package codejam.y2012.round_1C.boxes;

import codejam.utils.main.AbstractInputData;

public class SolInputData extends AbstractInputData {
    
    static class Element
    {
        int type;
        long count;
        
        Element(long count, int type) {
            this.count = count;
            this.type = type;
        }
    }
    
    int N;
    int M;
    Element[] a;
    Element[] b;
    
    public SolInputData(int testCase) {
        super(testCase);
    }

}
