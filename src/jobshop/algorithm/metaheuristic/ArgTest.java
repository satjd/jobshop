package jobshop.algorithm.metaheuristic;

import java.io.IOException;
import java.util.ArrayList;

import jobshop.calctime.Scheduler;
import jobshop.output.xls.OutputXls;

public class ArgTest
{

	public static ArrayList<Double> gbestList = new ArrayList<Double>();  //记录每一代的gbest，便于存入Excel
	public static void main(String[] args) throws IOException
	{
		// TODO 自动生成的方法存根
		
		int maxIteration = 1000;  //迭代次数
		int swarmSize = 30;       //微粒个数
		double c1 = 2.8, c2 = 1.3;//加速因子
		double inertia = 1.0;     //惯性权重
		
		for(double d = 0.0;d<=4.1;d+=0.1)
		{
			c1 = d;
			c2 = 4.1 - d;
			
			//========================
			
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
				
				//------PRINT&SAVE------
				if(iter==maxIteration)
					System.out.println("c1="+c1+": gbest="+gbestFitness+"  time="+(1.0/gbestFitness));
				gbestList.add(1.0/gbestFitness);
				
				//---------------------
				
				
				for(int i=0;i<swarm.size();i++)
				{
					Particle p = swarm.get(i);
					Particle pbest = pbestList.get(i);
					//p.hybridFly(pbest, gbest,inertia,c1,c2,0.6);
					p.fly(pbest, gbest, inertia, c1, c2);
				}
			}
			
			//===================
		}
		
		
		//------OUTPUT------
		OutputXls.outputResultToXLS("E:\\Java codes\\workspace\\jobshop\\result\\EXP2.xls","PSOgbest",gbestList);
		
		
		System.gc();
		
	}

}
