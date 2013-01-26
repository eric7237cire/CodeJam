package codejam.y2011.round_final.program_within;

public class Rule
{

    //Trigger
    int state;
    int mark;
    
    //New direction
    char Direction;

    int newState;
    int newMark;
    @Override
    public String toString()
    {
        return "Rule [state=" + state + ", mark=" + mark + ", Direction=" + Direction + ", newState=" + newState + ", newMark=" + newMark + "]";
    }
    
    
}
