package jobshop.algorithm.metaheuristic;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import jobshop.calctime.Scheduler;

public class PSOalgorithm
{

	public static void main(String[] args) throws FileNotFoundException
	{
		// TODO �Զ����ɵķ������
		
		int iteration = 1000; //��������
		int swarmSize = 100; //΢������
		
		ArrayList<Particle> swarm = new ArrayList<Particle>();
		for(int i=1;i<=swarmSize;i++)
			swarm.add(Scheduler.getParticleFromMachineSet()); //������ɳ���΢����λ�����
		
		for(int i=1;i<=iteration;i++) //��ʼ����
		{
			
			for(Particle p:swarm)
			{
				
			}
		}
	}

}
