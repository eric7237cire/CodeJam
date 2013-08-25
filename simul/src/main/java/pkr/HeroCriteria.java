package pkr;

import java.util.List;

import pkr.possTree.PossibilityNode.HandCategory;
import pkr.possTree.PossibilityNode.WinningLosingCategory;

import com.google.common.collect.Lists;

public class HeroCriteria extends Criteria
{

    //Only for hero only
    List<WinningLosingCategory> winLoseCat;

    
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
