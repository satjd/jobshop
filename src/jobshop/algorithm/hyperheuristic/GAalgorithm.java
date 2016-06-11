package jobshop.algorithm.hyperheuristic;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;

import jobshop.output.xls.OutputXls;

/*
 * GAalgorithm.java
 * 
 * 	GAalgorithm ʵ�ֺͲ���GA�㷨
 * 		void select(Population p)                     ��һ��population����ѡ�����
 * 		void mutate(Chromosome cm)                    ��һ��chromosome���б������
 * 		void crossover(Chromosome cm1,Chromosome cm2) ������chromosome���н������
 * 		void main()                                   ����GA�㷨
 */



public class GAalgorithm
{
	public static final int    populationSize = 10;//��Ⱥ��ģ
	public static final double crossoverRate = 0.5; //������
	public static final double mutateRate = 0.01;   //������
	public static final double selectRate = 0.3;    //��̭��
	public static final int    selectTime = 10;     //���̶ĵĴ�������ֹ��Ⱥ���⣩
	
	public static Chromosome globalBestCm = null; 
	public static ArrayList<Double> size = new ArrayList<Double>();
	
	private static void select(Population p) throws FileNotFoundException   //ѡ��
	{
		Chromosome fittestCm = p.getFittest();
		globalBestCm = fittestCm.getFitness()>globalBestCm.getFitness()?fittestCm:globalBestCm;
		double sumFitness = 0;
		Random r = new Random();
		LinkedList<Chromosome> population = p.getPopulation();
		
		System.out.println("ȫ��������Ӧ��:"+globalBestCm.getFitness()+" ȫ������ʱ��:"+(1.0/globalBestCm.getFitness())+"��Ⱥ��ģ"+population.size());
		size.add((double)population.size());
		System.out.println("ȫ������Ⱦɫ��: "+globalBestCm+"===="+globalBestCm.getFitness());
		System.out.println("��һ��������Ⱦɫ��: "+fittestCm+"===="+fittestCm.getFitness());
		
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
				if(r.nextDouble()<=(cur.getFitness()/sumFitness)) //���̶�
				{
					isSaved = true;
				}
			}
			if(!isSaved) it.remove();
		}
	}
	
	private static void mutate(Chromosome cm)                    //����
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
	
	private static Chromosome crossOver(Chromosome cm1,Chromosome cm2) //����
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
		//ѡ��
		select(ori);
		
		//����(random crossover)
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
		
		
		//����
		for(Chromosome cm:origenes)
		{
			mutate(cm);
		}
		
		
		return ori;
	}
	
	public static void main(String[] args) throws IOException
	{
		long time = System.currentTimeMillis();
		// TODO �Զ����ɵķ������
		int iterations = 100;
		Population p = new Population(populationSize, FitnessCalc.mchCnt, FitnessCalc.ruleCnt);
		globalBestCm = p.getFittest();
		for(int i=1;i<=iterations;i++)
		{
			System.out.print(i+":  ");
			p = nextGeneration(p);
			System.gc();
		}
		System.out.println(System.currentTimeMillis()-time);
		OutputXls.outputResultToXLS("E:\\Java codes\\workspace\\jobshop\\result\\EXP2.xls","size",size);
	}

}
