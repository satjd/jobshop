package jobshop.algorithm.hyperheuristic;

import java.io.FileNotFoundException;
import java.util.LinkedHashSet;
import java.util.LinkedList;

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
			newCm.shuffleGenes();
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
			newCm.shuffleGenes();
			population.add(newCm);
		}
	}
	
	public LinkedList<Chromosome> getPopulation()
	{
		return population;
	}
	
	public double getFittest() throws FileNotFoundException
	{
		double maxFitness = 0;
		for(Chromosome cm:population)
		{
			double tmpFitness = cm.getFitness();
			if(tmpFitness>maxFitness) maxFitness = tmpFitness;
		}
		return maxFitness;
	}
}
