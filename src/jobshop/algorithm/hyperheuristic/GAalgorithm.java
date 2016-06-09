package jobshop.algorithm.hyperheuristic;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;

/*
 * GAalgorithm.java
 * 
 * 	GAalgorithm 实现和测试GA算法
 * 		void select(Population p)                     对一个population进行选择操作
 * 		void mutate(Chromosome cm)                    对一个chromosome进行变异操作
 * 		void crossover(Chromosome cm1,Chromosome cm2) 对两个chromosome进行交叉操作
 * 		void main()                                   测试GA算法
 */



public class GAalgorithm
{
	public static final int    populationSize = 10;//种群规模
	public static final double crossoverRate = 0.5; //交叉率
	public static final double mutateRate = 0.01;   //变异率
	public static final double selectRate = 0.3;    //淘汰率
	public static final int    selectTime = 10;     //轮盘赌的次数（防止种群死光）
	
	public static Chromosome globalBestCm = null; 
	
	private static void select(Population p) throws FileNotFoundException   //选择
	{
		Chromosome fittestCm = p.getFittest();
		globalBestCm = fittestCm.getFitness()>globalBestCm.getFitness()?fittestCm:globalBestCm;
		double sumFitness = 0;
		Random r = new Random();
		LinkedList<Chromosome> population = p.getPopulation();
		
		System.out.println("全局最优适应度:"+globalBestCm.getFitness()+" 全局最优时间:"+(1.0/globalBestCm.getFitness())+"种群规模"+population.size());
		
		System.out.println("全局最优染色体: "+globalBestCm+"===="+globalBestCm.getFitness());
		System.out.println("这一代的最优染色体: "+fittestCm+"===="+fittestCm.getFitness());
		
		for(Chromosome cm:population)
		{
			sumFitness+=cm.getFitness();
		}
		
		for(Iterator<Chromosome> it = population.iterator();it.hasNext();)
		{
			Chromosome cur = it.next();
			boolean isSaved = false;
			for(int i=1;i<=selectTime;i++)
			{
				if(r.nextDouble()<=(cur.getFitness()/sumFitness)) //轮盘赌
				{
					isSaved = true;
				}
			}
			if(!isSaved) it.remove();
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
			if(r.nextDouble()<=0.5)
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
		ori.resetPopulation();
		Random r = new Random();
		//选择
		select(ori);
		
		//交叉(random crossover)
		LinkedList<Chromosome>  origenes = ori.getPopulation();
		while(origenes.size()<populationSize)
		{
			Chromosome cm1 = origenes.get(r.nextInt(origenes.size()));
			Chromosome cm2 = origenes.get(r.nextInt(origenes.size()));
			origenes.add(crossOver(cm1, cm2));
		}
		
		
//		for(ListIterator<Chromosome> it = origenes.listIterator();it.hasNext();)
//		{
//			Chromosome cur = it.next();
//			if(r.nextDouble()<=1.0) cm1 = cur;
//			if(r.nextDouble()<=1.0) cm2 = cur;
//			if(cm1!=null&&cm2!=null) 
//			{
//				it.add(crossOver(cm1, cm2));
//				cm1 = null;
//				cm2 = null;
//			}
//		}
		
		
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
		Population p = new Population(populationSize, FitnessCalc.mchCnt, FitnessCalc.ruleCnt);
		globalBestCm = p.getFittest();
		for(int i=1;i<=iterations;i++)
		{
			System.out.print(i+":  ");
			p = nextGeneration(p);
			System.gc();
		}
	}

}
