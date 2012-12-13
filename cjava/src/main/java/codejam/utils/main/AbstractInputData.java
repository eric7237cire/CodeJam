package codejam.utils.main;

public class AbstractInputData implements Cloneable {
    public int testCase;
    
    public AbstractInputData(int testCase) {
        this.testCase = testCase;
    }
    
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
