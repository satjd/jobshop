package jobshop.algorithm.hyperheuristic;

import java.io.FileNotFoundException;
import java.util.LinkedList;

/*
 * Population.java
 * 
 * 	Population��  ��������Ⱦɫ�幹�ɵ���Ⱥ
 * 		LinkedList<Chromosome> population  ��Ⱥ
 * 		int populationSize                 ��Ⱥ��ģ
 * 		int geneLength                     ���򳤶�
 * 		int bound                          ��������ȡֵ��Χ
 * 		
 * 		����Chromosome getFittest()         ���������Ⱥ�е�������Ӧ�ȸ���
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
