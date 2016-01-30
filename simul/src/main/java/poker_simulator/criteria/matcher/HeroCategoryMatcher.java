package poker_simulator.criteria.matcher;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import pkr.CompleteEvaluation;
import poker_simulator.criteria.iMatcher;
import poker_simulator.flags.HandCategory;
import poker_simulator.flags.Round;

public class HeroCategoryMatcher implements iMatcher
{

	private List<HandCategory> matchHandCat;
	private Round round;

	public HeroCategoryMatcher(Round round)
	{
		matchHandCat = Lists.newArrayList();
		this.round = round;
	}

	public void AddMatchingHandCategories(HandCategory... handCats)
	{
		Collections.addAll(matchHandCat, handCats);
	}

	@Override
	public boolean matches(CompleteEvaluation[] evals)
	{
		
		for (HandCategory cat : matchHandCat)
		{
			if (evals[0].hasFlag(round.getIndex(), cat))
			{
				return true;
			}
		}
	

		return false;
	}

}
