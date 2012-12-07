package codejam.y2010.file_fix;

import java.util.List;

import codejam.utils.main.AbstractInputData;

public class InputData extends AbstractInputData {
    //Données
    int dirExisting;
    int dirToCreate;
    List<String> dirsExisting;
    List<String> dirsToCreate;
    
    InputData(int testCase) {
        super(testCase);
    }
}
