package codejam.y2010.round_3.hotdog;

import java.util.List;

import codejam.utils.main.AbstractInputData;

public class InputData extends AbstractInputData {
    //Données
    public static class Corner {
        int location;
        int count;
        public Corner(int location, int count) {
            super();
            this.location = location;
            this.count = count;
        }
    }
    int C;
    
    List<Corner> vendors;
    
    InputData(int testCase) {
        super(testCase);
    }
}
