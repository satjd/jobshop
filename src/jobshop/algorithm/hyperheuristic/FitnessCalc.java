package jobshop.algorithm.hyperheuristic;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import jobshop.algorithm.hrules.AbstractRules;
import jobshop.algorithm.hrules.LWKRrules;
import jobshop.algorithm.hrules.MWKRrules;
import jobshop.algorithm.hrules.RandomSPTrules;
import jobshop.algorithm.hrules.SPTrules;
import jobshop.algorithm.hrules.WSPTrules;
import jobshop.calctime.Scheduler;

public class FitnessCalc
{
	private static ArrayList<AbstractRules> ruleList = new ArrayList<AbstractRules>(); //启发式规则的集合
	static
	{
		ruleList.add(new MWKRrules());
		ruleList.add(new RandomSPTrules());
		ruleList.add(new SPTrules());
		ruleList.add(new WSPTrules(0.5));
		ruleList.add(new LWKRrules());
	}
	public static double calcFitness(Chromosome cho) throws FileNotFoundException //计算个体适应度
	{
		return 1.0/(Scheduler.calcTime(cho, ruleList));
	}
}
