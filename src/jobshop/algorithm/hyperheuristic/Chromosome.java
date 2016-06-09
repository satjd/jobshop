package jobshop.algorithm.hyperheuristic;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


/*
 * Chromosome.java
 * 
 * 	Chromosome类   用来描述染色体，即种群中的个体信息
 * 		ArrayList<Integer> genes   基因编码
 * 		int length                 基因长度
 * 		int bound                  基因编码的取值范围
 * 		double fitness             基因适应度
 */



public class Chromosome implements Comparable<Chromosome>
{
	private ArrayList<Integer> genes;
	private int length;
	private int bound;
	private double fitness;
	
	public Chromosome(int length,int bound)
	{
		this.length = length;
		this.bound = bound;
		Random r = new Random();
		this.fitness = 0;
		genes = new ArrayList<Integer>();
		for(int i=0;i<length;i++)
			genes.add(r.nextInt(bound));
	}
	
	public void shuffleGenes()
	{
		Collections.shuffle(genes);
	}
	
	public int getLength()
	{
		return length;
	}
	public int getBound()
	{
		return bound;
	}
	public ArrayList<Integer> getGenes()
	{
		return genes;
	}
	
	public void resetFitness()
	{
		fitness = 0;
	}
	
	public double getFitness() throws FileNotFoundException
	{
		if(fitness==0)
			fitness = FitnessCalc.calcFitness(this);
		return fitness;
	}

	@Override
	public int compareTo(Chromosome o)
	{
		// TODO 自动生成的方法存根
		int result =0;
		try
		{
			if(this.getFitness()>o.getFitness()) result = 1;
			else result = -1;
		}
		catch (FileNotFoundException e)
		{
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return result;
	}
	
	public String toString()
	{
		return genes.toString();
	}
}
