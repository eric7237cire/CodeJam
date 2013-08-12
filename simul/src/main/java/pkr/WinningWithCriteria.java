package pkr;

import pkr.possTree.PossibilityNode.HandCategory;
import pkr.possTree.PossibilityNode.WinningLosingCategory;

public class WinningWithCriteria extends Criteria
{

    public WinningWithCriteria(int round, String desc) 
    {
        super(round, desc);
    }

    @Override
    public boolean matches(CompleteEvaluation[] evals)
    {
        if (allMustMatch)
        {
            for (CompleteEvaluation eval : evals)
            {
                if (!eval.hasFlag(round, WinningLosingCategory.WINNING))
                    continue;
                
                boolean ok = false;
                for (HandCategory cat : matchHandCat)
                {
                    if (eval.hasFlag(round, cat))
                    {
                        ok = true;
                        break;
                    }
                }

                if (!ok)
                    return false;

                for (HandCategory cat : matchNegHandCat)
                {
                    if (eval.hasFlag(round, cat))
                    {
                        return false;
                    }
                }

            }

            return true;
        } else
        {
            for (CompleteEvaluation eval : evals)
            {
                if (!eval.hasFlag(round, WinningLosingCategory.WINNING))
                    continue;
                
                for (HandCategory cat : matchHandCat)
                {
                    if (eval.hasFlag(round, cat))
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
