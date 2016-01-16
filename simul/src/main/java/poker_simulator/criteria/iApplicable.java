package poker_simulator.criteria;

import pkr.CompleteEvaluation;
import poker_simulator.flags.Round;

public interface iApplicable {
	public boolean isApplicable(Round round, CompleteEvaluation[] eval);
}
