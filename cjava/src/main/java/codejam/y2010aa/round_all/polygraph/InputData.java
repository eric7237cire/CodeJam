package codejam.y2010aa.round_all.polygraph;

import java.util.List;

import codejam.utils.main.AbstractInputData;

public class InputData extends AbstractInputData {

    static class Statement {
        int speaker;
        char type;
        
        int subject1;
        int subject2;
        public Statement(int speaker, char type, int subject1, int subject2) {
            super();
            this.speaker = speaker;
            this.type = type;
            this.subject1 = subject1;
            this.subject2 = subject2;
        }
        
        
    }
    
    List<Statement> statement;
    
    int M;
    int N;
    
    public InputData(int testCase) {
        super(testCase);
    }

}