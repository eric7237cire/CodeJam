package poker_simulator.criteria;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import pkr.CompleteEvaluation;
import poker_simulator.flags.HandCategory;
import poker_simulator.flags.Round;

public class AnyPlayerMatcher implements iMatcher {

	private List<HandCategory> matchHandCat;
    
    
    public AnyPlayerMatcher() {
		matchHandCat = Lists.newArrayList();
	}
    
    public void AddMatchingHandCategories(HandCategory... handCats)
    {
    	Collections.addAll(matchHandCat,  handCats);
    }
    
   
	@Override
	public boolean matches(Round round, CompleteEvaluation[] evals) {
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
