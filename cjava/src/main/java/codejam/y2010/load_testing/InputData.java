package codejam.y2010.load_testing;

import codejam.utils.main.AbstractInputData;

public class InputData extends AbstractInputData {
    //Données
    
    int L; //load
    int P; //unacceptable load
    int C; //factor L + C(L) >= P
    InputData(int testCase) {
        super(testCase);
    }
}
