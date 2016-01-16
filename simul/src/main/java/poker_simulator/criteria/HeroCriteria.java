package poker_simulator.criteria;

import java.util.List;

import pkr.CompleteEvaluation;
import poker_simulator.flags.*;
import com.google.common.collect.Lists;

public class HeroWiningOrLosingMatcher implements iMatcher
{

    //Only for hero only
    public List<WinningLosingCategory> winLoseCat;

    
    public HeroCriteria(int round, String desc) {
        super(round, desc);
        


        winLoseCat = Lists.newArrayList();
    }

    @Override
    public boolean matches(CompleteEvaluation[] evals)
    {

        boolean matchesWinLose = winLoseCat.size() == 0;

        for (WinningLosingCategory cat : winLoseCat)
        {
            if (evals[0].hasFlag(round, cat))
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
            if (evals[0].hasFlag(round, cat))
            {
                return true;
            }
        }

        return false;
    }
}
