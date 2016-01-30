package poker_simulator.scoring;

import java.util.Comparator;

import pkr.CompleteEvaluation;

public class CompareByRoundScore implements Comparator<CompleteEvaluation>
{
    final int round;
    
    public CompareByRoundScore(int round) {
        super();
        this.round = round;
    }

    @Override
    public int compare(CompleteEvaluation o1, CompleteEvaluation o2)
    {
        return o1.getRoundScore(round).compareTo(o2.getRoundScore(round));
    }
}