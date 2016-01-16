package poker_simulator.criteria;

import pkr.CompleteEvaluation;
import poker_simulator.flags.Round;

@FunctionalInterface
public interface iMatcher {
	public boolean matches(Round round, CompleteEvaluation[] evals);
}
