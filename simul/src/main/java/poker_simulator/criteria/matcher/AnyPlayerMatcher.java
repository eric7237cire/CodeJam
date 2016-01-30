package poker_simulator.criteria.matcher;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import pkr.CompleteEvaluation;
import poker_simulator.criteria.iMatcher;
import poker_simulator.flags.HandCategory;
import poker_simulator.flags.Round;

public class AnyPlayerMatcher implements iMatcher {

	private List<HandCategory> matchHandCat;
    private Round round;
    
    public AnyPlayerMatcher(Round round) {
		matchHandCat = Lists.newArrayList();
		this.round = round;
	}
    
    public void AddMatchingHandCategories(HandCategory... handCats)
    {
    	Collections.addAll(matchHandCat,  handCats);
    }
    
   
	@Override
	public boolean matches(CompleteEvaluation[] evals) {
		for (CompleteEvaluation eval : evals)
        {
            for (HandCategory cat : matchHandCat)
            {
                if (eval.hasFlag(round.getIndex(), cat))
                {
                    return true;
                }
            }
        }

        return false;
	}

}
