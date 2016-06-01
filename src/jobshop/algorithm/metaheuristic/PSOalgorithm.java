package jobshop.algorithm.metaheuristic;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import jobshop.calctime.Scheduler;

public class PSOalgorithm
{

	public static void main(String[] args) throws FileNotFoundException
	{
		// TODO 自动生成的方法存根
		
		int iteration = 1000; //迭代次数
		int swarmSize = 100; //微粒个数
		
		ArrayList<Particle> swarm = new ArrayList<Particle>();
		for(int i=1;i<=swarmSize;i++)
			swarm.add(Scheduler.getParticleFromMachineSet()); //随机生成初代微粒，位置随机
		
		for(int i=1;i<=iteration;i++) //开始迭代
		{
			
			for(Particle p:swarm)
			{
				
			}
		}
	}

}
