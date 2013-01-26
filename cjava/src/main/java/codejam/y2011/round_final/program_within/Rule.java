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
        if (Direction != 'R')
        return String.format("%d %d -> %s %d %d", state, mark, ""+Direction,
            newState, newMark);
        else
            return String.format("%d %d -> R", state,mark);
    }
    
    public Rule() {
        
    }
    
    public Rule(int state, int mark, char Direction,
        int newState, int newMark)
    {
        this.state = state;
        this.mark = mark;
        this.Direction = Direction;
        this.newState = newState;
        this.newMark = newMark;
    }
    
}
