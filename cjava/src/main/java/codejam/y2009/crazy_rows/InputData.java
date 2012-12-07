package codejam.y2009.crazy_rows;

import java.util.ArrayList;
import java.util.List;

import codejam.utils.main.AbstractInputData;
import codejam.y2009.crazy_rows.Main.Row;


public class InputData extends AbstractInputData {
    //Données

    int dimension ;

    List<Row> liste = new ArrayList<>();
    
    InputData(int testCase) {
        super(testCase);
    }
}

