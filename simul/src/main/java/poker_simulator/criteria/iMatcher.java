package poker_simulator.criteria;

import pkr.CompleteEvaluation;

@FunctionalInterface
public interface iMatcher {
	public boolean matches(CompleteEvaluation[] evals);
}
