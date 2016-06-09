package jobshop.algorithm.hyperheuristic;

import java.io.FileNotFoundException;
import java.util.LinkedList;

/*
 * Population.java
 * 
 * 	Population类  描述了由染色体构成的种群
 * 		LinkedList<Chromosome> population  种群
 * 		int populationSize                 种群规模
 * 		int geneLength                     基因长度
 * 		int bound                          基因编码的取值范围
 * 		
 * 		函数Chromosome getFittest()         返回这个种群中的最优适应度个体
 * 		
 */

public class Population
{
	private LinkedList<Chromosome> population;
	private int populationSize;
	private int geneLength;
	private int bound;
	
	public Population(int populationSize,int geneLength,int bound)
	{
		this.populationSize = populationSize;
		this.geneLength = geneLength;
		population = new LinkedList<Chromosome>();
		for(int i=0;i<populationSize;i++)
		{
			Chromosome newCm = new Chromosome(geneLength,bound);
//			newCm.shuffleGenes();
			population.add(newCm);
		}
	}
	
	public Population(Population p)
	{
		this.populationSize = p.populationSize;
		this.geneLength = p.geneLength;
		for(int i=0;i<populationSize;i++)
		{
			System.out.println(p.bound);
			Chromosome newCm = new Chromosome(p.geneLength,p.bound);
			//newCm.shuffleGenes();
			population.add(newCm);
		}
	}
	
	public LinkedList<Chromosome> getPopulation()
	{
		return population;
	}
	
	public void resetPopulation()
	{
		for(Chromosome cm:population)
		{
			cm.resetFitness();
		}
	}
	
	public Chromosome getFittest() throws FileNotFoundException
	{
		double maxFitness = 0;
		Chromosome maxCm = null;
		for(Chromosome cm:population)
		{
			double tmpFitness = cm.getFitness();
			if(tmpFitness>maxFitness) 
			{
				maxFitness = tmpFitness;
				maxCm = cm;
			}
		}
		return maxCm;
	}
}
