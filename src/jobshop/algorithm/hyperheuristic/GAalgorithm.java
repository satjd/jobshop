package jobshop.algorithm.hyperheuristic;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;

public class GAalgorithm
{
	public static final double crossoverRate = 0.9; //交叉率
	public static final double mutateRate = 0.01;   //变异率
	public static final double selectRate = 0.3;    //淘汰率
	
	private static void select(Population p) throws FileNotFoundException   //选择
	{
		double fittest = p.getFittest();
		double sumFitness = 0;
		Random r = new Random();
		LinkedList<Chromosome> population = p.getPopulation();
		Collections.sort(population);
		
		System.out.println("最优适应度:"+fittest+" 最优时间:"+(1.0/fittest)+"种群规模"+population.size());
		Chromosome mid = population.get((int)(population.size()*selectRate));
		
		for(Chromosome cm:population)
		{
			sumFitness+=cm.getFitness();
		}
		
		for(Iterator<Chromosome> it = population.iterator();it.hasNext();)
		{
			Chromosome cur = it.next();
//			if(r.nextDouble()>=(cur.getFitness()/sumFitness)) //轮盘赌
//			{
//				it.remove();
//			}
			if(cur.getFitness()<mid.getFitness())
			{
				it.remove();
			}
		}
	}
	
	private static void mutate(Chromosome cm)                    //变异
	{
		ArrayList<Integer> gene = cm.getGenes();
		Random r = new Random();
		for(Integer i:gene)
		{
			if(r.nextDouble()<=mutateRate)
			{
				i = r.nextInt(cm.getBound());
			}
		}
	}
	
	private static Chromosome crossOver(Chromosome cm1,Chromosome cm2) //交叉
	{
		Chromosome newCm = new Chromosome(cm1.getLength(), cm1.getBound());
		Random r = new Random();
		ArrayList<Integer> gene1 = cm1.getGenes();
		ArrayList<Integer> gene2 = cm2.getGenes();
		ArrayList<Integer> newGene = newCm.getGenes();
		
		for(int i=0;i<gene1.size();i++)
		{
			if(r.nextDouble()<=crossoverRate)
			{
				newGene.set(i, gene1.get(i));
			}
			else 
			{
				newGene.set(i, gene2.get(i));
			}
		}
		
		return newCm;
	}
	
	public static Population nextGeneration(Population ori) throws FileNotFoundException
	{
		Random r = new Random();
		//选择
		select(ori);
		
		//交叉
		LinkedList<Chromosome>  origenes = ori.getPopulation();
		Chromosome cm1 = null;
		Chromosome cm2 = null;
		for(ListIterator<Chromosome> it = origenes.listIterator();it.hasNext();)
		{
			Chromosome cur = it.next();
			if(r.nextDouble()<=0.333) cm1 = cur;
			if(r.nextDouble()<=0.333) cm2 = cur;
			if(cm1!=null&&cm2!=null) 
			{
				it.add(crossOver(cm1, cm2));
				cm1 = null;
				cm2 = null;
			}
		}
		
		//变异
		for(Chromosome cm:origenes)
		{
			mutate(cm);
		}
		
		return ori;
	}
	
	public static void main(String[] args) throws FileNotFoundException
	{
		// TODO 自动生成的方法存根
		int iterations = 100;
		Population p = new Population(100, 12, 5);
		for(int i=1;i<=iterations;i++)
		{
			System.out.print(i+":  ");
			p = nextGeneration(p);
		}
	}

}
