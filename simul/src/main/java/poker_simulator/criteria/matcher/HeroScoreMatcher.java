package poker_simulator.criteria.matcher;

import pkr.CompleteEvaluation;
import poker_simulator.criteria.iMatcher;
import poker_simulator.flags.Round;
import poker_simulator.scoring.HandLevel;

public class HeroScoreMatcher implements iMatcher
{

	private HandLevel handLevel;
	private Round round;
	
	public HeroScoreMatcher(HandLevel handLevel, Round round)
	{
		super();
		this.handLevel = handLevel;
		this.round = round;
	}

	@Override
	public boolean matches(CompleteEvaluation[] evals)
	{
		return evals[0].getRoundScore(round.getIndex()).handLevel == handLevel;
	}

}
