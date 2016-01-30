package poker_simulator.criteria.matcher;

import java.util.Collections;
import java.util.List;

import pkr.CompleteEvaluation;
import poker_simulator.criteria.iMatcher;
import poker_simulator.flags.*;
import com.google.common.collect.Lists;

public class HeroWiningOrLosingMatcher implements iMatcher
{

    //Only for hero only
    public List<WinningLosingCategory> winLoseCat;

    private List<HandCategory> matchHandCat;
    
    public HeroWiningOrLosingMatcher() {
        
        winLoseCat = Lists.newArrayList();
    }

    public void AddMatchingHandCategories(HandCategory... handCats)
    {
    	Collections.addAll(matchHandCat,  handCats);
    }

	@Override
	public boolean matches(CompleteEvaluation[] evals) {
		boolean matchesWinLose = winLoseCat.size() == 0;

        for (WinningLosingCategory cat : winLoseCat)
        {
            if (evals[0].hasFlag(Round.RIVER.getIndex(), cat))
            {
                matchesWinLose = true;
                break;
            }
        }

        if (!matchesWinLose)
        {
            return false;

        }
        for (HandCategory cat : matchHandCat)
        {
            if (evals[0].hasFlag(Round.RIVER.getIndex(), cat))
            {
                return true;
            }
        }

        return false;
	}
}
