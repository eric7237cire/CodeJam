package poker_simulator.criteria;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import pkr.CompleteEvaluation;
import poker_simulator.flags.HandCategory;
import poker_simulator.flags.Round;

public class AllPlayerMatcher implements iMatcher {

	private List<HandCategory> matchHandCat;
    private List<HandCategory> matchNegHandCat;
    
    public AllPlayerMatcher() {
		matchHandCat = Lists.newArrayList();
		matchNegHandCat = Lists.newArrayList();
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
	public boolean matches(Round round, CompleteEvaluation[] evals) {
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
