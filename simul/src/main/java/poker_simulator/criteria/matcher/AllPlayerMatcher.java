package poker_simulator.criteria.matcher;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import pkr.CompleteEvaluation;
import poker_simulator.criteria.iMatcher;
import poker_simulator.flags.HandCategory;
import poker_simulator.flags.Round;

public class AllPlayerMatcher implements iMatcher {

	private List<HandCategory> matchHandCat;
    private List<HandCategory> matchNegHandCat;
    private Round round;
    
    public AllPlayerMatcher(Round round) {
		matchHandCat = Lists.newArrayList();
		matchNegHandCat = Lists.newArrayList();
		this.round = round;
	}
    
    public void AddMatchingHandCategories(HandCategory... handCats)
    {
    	Collections.addAll(matchHandCat,  handCats);
    }
    
    public void AddNotMatchingHandCategories(HandCategory... handCats)
    {
    	Collections.addAll(matchNegHandCat,  handCats);
    }
    
	@Override
	public boolean matches(CompleteEvaluation[] evals) {
		for (CompleteEvaluation eval : evals)
        {
            boolean ok = false;
            for (HandCategory cat : matchHandCat)
            {
                if (eval.hasFlag(round.getIndex(), cat))
                {
                    ok = true;
                    break;
                }
            }

            if (!ok)
                return false;

            for (HandCategory cat : matchNegHandCat)
            {
                if (eval.hasFlag(round.getIndex(), cat))
                {
                    return false;
                }
            }

        }

        return true;
	}

}
