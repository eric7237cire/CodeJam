package poker_simulator.criteria;

import java.util.List;

import com.google.common.collect.Lists;

import pkr.CompleteEvaluation;
import pkr.possTree.PossibilityNode.TextureCategory;
import poker_simulator.flags.Round;

public class TextureMatcher implements iApplicable {


    //For applicable, used to determine if we should evaluate the flop or not
    private List<TextureCategory> mustHave;
    private List<TextureCategory> mustNotHave;
    private Round round;
    
    /**
     * 
     * @param mustHave which categories must be present
     * @param mustNotHave ..
     * @param round what round to evaluate the categories
     */
	public TextureMatcher(Round round) {
		super();
		this.mustHave = Lists.newArrayList();
		this.mustNotHave = Lists.newArrayList();
		this.round = round;
	}
	
	public void addTextureCategory(TextureCategory cat, boolean isMustHave)
	{
		if (isMustHave)
			mustHave.add(cat);
		else 
			mustNotHave.add(cat);
	}

	@Override
	public boolean isApplicable(CompleteEvaluation[] evals) {
		
		//use the first one since the flop is shared
		CompleteEvaluation eval = evals[0];
		
		for (TextureCategory cat : mustHave)
	    {
	        if (!eval.hasFlag(round.getIndex(), cat))
	            return false;
	    }

	    for (TextureCategory cat : mustNotHave)
	    {
	        if (eval.hasFlag(round.getIndex(), cat))
	            return false;
	    }
	    return true;
	}

	/*
	*/
}
