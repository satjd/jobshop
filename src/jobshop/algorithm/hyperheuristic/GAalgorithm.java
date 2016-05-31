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
	public static final double crossoverRate = 0.9; //������
	public static final double mutateRate = 0.01;   //������
	public static final double selectRate = 0.3;    //��̭��
	
	private static void select(Population p) throws FileNotFoundException   //ѡ��
	{
		double fittest = p.getFittest();
		double sumFitness = 0;
		Random r = new Random();
		LinkedList<Chromosome> population = p.getPopulation();
		Collections.sort(population);
		
		System.out.println("������Ӧ��:"+fittest+" ����ʱ��:"+(1.0/fittest)+"��Ⱥ��ģ"+population.size());
		System.out.println("����Ⱦɫ��: "+population.getLast()+"===="+population.getLast().getFitness());
		Chromosome mid = population.get((int)(population.size()*selectRate));
		
		for(Chromosome cm:population)
		{
			sumFitness+=cm.getFitness();
		}
		
		for(Iterator<Chromosome> it = population.iterator();it.hasNext();)
		{
			Chromosome cur = it.next();
//			if(r.nextDouble()>=(cur.getFitness()/sumFitness)) //���̶�
//			{
//				it.remove();
//			}
			if(cur.getFitness()<mid.getFitness())
			{
				it.remove();
			}
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
		ori.resetPopulation();
		Random r = new Random();
		//ѡ��
		select(ori);
		
		//����
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
		
		//����
//		for(Chromosome cm:origenes)
//		{
//			mutate(cm);
//		}
		
		
		return ori;
	}
	
	public static void main(String[] args) throws FileNotFoundException
	{
		// TODO �Զ����ɵķ������
		int iterations = 100;
		Population p = new Population(100, FitnessCalc.mchCnt, FitnessCalc.ruleCnt);
		for(int i=1;i<=iterations;i++)
		{
			System.out.print(i+":  ");
			p = nextGeneration(p);
		}
	}

}
