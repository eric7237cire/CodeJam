package poker_simulator.criteria;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import pkr.CompleteEvaluation;
import pkr.Simulator;
import poker_simulator.flags.Round;

public class CriteriaEvaluator
{
	private static Logger log = LoggerFactory.getLogger(CriteriaEvaluator.class);
	private static Logger logOutput = LoggerFactory.getLogger("mainOutput");
	
	List<Criteria> criteres = Lists.newArrayList();

	List<Criteria> pairedBoardCriteres = Lists.newArrayList();
	List<Criteria> unPairedBoardCriteres = Lists.newArrayList();
	List<Criteria> allBoardCriteres = Lists.newArrayList();

	public CriteriaEvaluator()
	{
		super();
		
		for (Round round : Round.values())
		{
			
			CriteriaFactory.addUnpairedBoardCriteria(round, unPairedBoardCriteres);
			//CriteriaFactory.addPairedBoardCriteria(round, roundStr, pairedBoardCriteres);

			CriteriaFactory.addAnyBoardCriteria(round, allBoardCriteres);

		}

		criteres.addAll(pairedBoardCriteres);
		criteres.addAll(unPairedBoardCriteres);
		criteres.addAll(allBoardCriteres);

	}
	
	public void Apply(CompleteEvaluation[] evals)
	{
		for (Criteria c : criteres)
		{
			log.debug("Calculate criteria {}", c.desc);
			c.calculate(evals);
		}
	}

	public void Print()
	{
		logOutput.debug("\nPaired board criteria\n");
		for (Criteria c : pairedBoardCriteres)
		{
			c.printMsg();
		}
		logOutput.debug("\nUn paired board criteria\n");
		for (Criteria c : unPairedBoardCriteres)
		{
			c.printMsg();
		}
		logOutput.debug("\nAll board criteria\n");
		for (Criteria c : allBoardCriteres)
		{
			c.printMsg();
		}
	}
}
