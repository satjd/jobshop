package jobshop.algorithm.metaheuristic;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import jobshop.calctime.Scheduler;

public class PSOalgorithm
{

	public static void main(String[] args) throws FileNotFoundException
	{
		// TODO 自动生成的方法存根
		
		int maxIteration = 1000;   //迭代次数
		int swarmSize = 30;       //微粒个数
		double c1 = 1.0, c2 = 1.0; //加速因子
		
		ArrayList<Particle> swarm = new ArrayList<Particle>();
		ArrayList<Particle> pbestList = new ArrayList<Particle>();
		for(int i=1;i<=swarmSize;i++)
		{
			Particle newParticle = Scheduler.getParticleFromMachineSet();
			swarm.add(newParticle);         //随机生成初代微粒，位置随机
			pbestList.add(newParticle);     //pbest初始化为自身
		}
		
		double gbestFitness = 0;
		Particle gbest = null;
		for(int iter=1;iter<=maxIteration;iter++) //开始迭代
		{
			for(int index=0;index<swarmSize;index++) //寻找pbest存入pbestList
			{
				Particle p = swarm.get(index);
				double curFitness = p.getFitness();
				double pbestFitness = pbestList.get(index).getFitness();
				if(curFitness>pbestFitness)
				{
					pbestFitness = curFitness;
					pbestList.set(index, p);
				}
			}
			for(Particle p:pbestList) //在pbestList中寻找gbest
			{
				if(p.getFitness()>gbestFitness)
				{
					gbestFitness = p.getFitness();
					gbest = p;
				}
			}
			
			//------PRINT------
			System.out.println(iter+": gbest="+gbestFitness+"  time="+(1.0/gbestFitness));
			
			for(int i=0;i<swarm.size();i++)
			{
				Particle p = swarm.get(i);
				Particle pbest = pbestList.get(i);
				p.fly(pbest, gbest, 2.0, 2.0);
				ArrayList<ArrayList<Double>> al = p.getWArray();
			}
		}
	}

}
