package tesi;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

import distribuzioni.*;
import statistiche.DataKeeper;
import statistiche.Statistic;
public class CSM{
	
	public static void main(String[] args) {
		
		Engine engine = new ModelEngine();
		
		/*CSM*/
		
		//#places,#transitions,beginUntimedIds,endUntimedIds,beginTimedIds,endTimedIds,engine
		PetriNet csm = new PetriNet(18,31,15,30,0,14,engine); 
		
		
		/*S0*/
		//set priority and probability of untimed t15,t16...//tid,priority,probability
		for(int i = 15; i<=24; i++) csm.setUntimedTransition(i, 1, 0.1);
		
		
		//set distrbution of timed t0,t1...			   //tid,distribution
		for(int i = 0; i<= 9; i++) csm.setDistribution(i, new ExponentialDistribution(new Random(), 0.1));
		
		//bind P0 to all the untimed
		for(int i = 15; i<=24; i++) csm.setBackwardArc(1,0, i);
		
		//bind untimed to p1,p2...
		for(int i = 15; i <=24; i++) csm.setForwardArc(1, i-14, i);
		
		//bind p1,p2..with inihbitor arcs to untimed
		for(int i =1; i<=10; i++) csm.setBackwardArc(0, i, i+14);
		
		//bind p1,p2... to timed t0,t1..
		for(int i = 1; i<=10; i++) csm.setBackwardArc(1,i, i-1);
		
		//bind timed t0,t1.. to S1
		for(int i = 0; i < 10; i++) csm.setForwardArc(1, 11, i);
		
		/*END S0*/
		
		
		/*S1*/
		
		//bind p11 to t10
		csm.setBackwardArc(1, 11, 10);
		
		//set distribution of t10
		csm.setDistribution(10, new ExponentialDistribution(new Random(), 1));
		
		//bind t10 to ROUTER
		csm.setForwardArc(1, 12, 10);
		
		/*END S1*/
		
		
		
		/*ROUTER*/
		
		//bind P12 to t25,t26,t27,t30
		csm.setBackwardArc(1, 12, 25);
		csm.setBackwardArc(1, 12, 26);
		csm.setBackwardArc(1, 12, 27);
		csm.setBackwardArc(1, 12, 30);
		
		//bind ROUTER to S0
		csm.setForwardArc(1, 0, 25);
		
		//bind ROUTER to S2
		csm.setForwardArc(1, 13, 26);
		
		//bind ROUTER to S3
		csm.setForwardArc(1, 14, 27);
		
		//bind ROUTER to S4
		csm.setForwardArc(1, 17, 30);
		
		//set priority and probability of t25,t26,t27,t30
		csm.setUntimedTransition(25, 1, 0.2);
		csm.setUntimedTransition(26, 1, 0.3);
		csm.setUntimedTransition(27, 1, 0.3);
		csm.setUntimedTransition(30, 1, 0.2);
		
		/*END ROUTER*/
		
		
		/*S2*/
		
		//bind p13 to t11
		csm.setBackwardArc(1, 13, 11);
		
		//bind t11 to S1
		csm.setForwardArc(1, 11, 11);
		
		//set distribution of timed t11
		csm.setDistribution(11, new ExponentialDistribution(new Random(), 0.8));
		
		/*END S2*/
		
		
		/*S3*/
		
		//bind p14 to t28 and t29
		csm.setBackwardArc(1, 14, 28);
		csm.setBackwardArc(1, 14, 29);
		
		//set priority and probability of t28,t29
		csm.setUntimedTransition(28, 1, 0.95);
		csm.setUntimedTransition(29, 1, 0.05);
		
		//bind t28 to p15
		csm.setForwardArc(1, 15, 28);
		
		//bind t29 to p16
		csm.setForwardArc(1, 16, 29);
		
		//bind p15 to t28,t29 with inihbitor arcs
		csm.setBackwardArc(0, 15, 28);
		csm.setBackwardArc(0, 15, 29);
		
		//bind p16 to t28,t29 with inihbitor arcs
		csm.setBackwardArc(0, 16, 28);
		csm.setBackwardArc(0, 16, 29);
		
		//bind p15 to timed t12
		csm.setBackwardArc(1, 15, 12);
		
		//bind p16 to timed t13
		csm.setBackwardArc(1, 16, 13);
		
		//set distributions of timed t12,t13
		csm.setDistribution(12, new ExponentialDistribution(new Random(), 5));
		csm.setDistribution(13, new ExponentialDistribution(new Random(), 0.5));
		
		//bind t12,t13 to S1
		csm.setForwardArc(1, 11, 12);
		csm.setForwardArc(1, 11, 13);
		
		
		/*END S3*/
		
		/*S4*/
		
		//bind p17 to t14
		csm.setBackwardArc(1, 17, 14);
		
		//bind t14 to S1
		csm.setForwardArc(1, 11, 14);
		
		//set distribution of t14
		csm.setDistribution(14, new ErlangDistribution(new Random(),16, 0.6));
		
		
		/*END S4*/
		
		
		
		//place token in p0
		csm.setMarking(0, 10);
		
		/*END CSM*/
		
		Statistic statistic = new Statistic();
		for(int i = 0; i<=30; i++) statistic.insertTransition(i, new DataKeeper());
		
		engine.setStatistic(statistic);
		
		csm.viewState();
		
		int mark[]={10,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		csm.initialize(mark);
		
		
		double tEnd= 10000000;

		engine.startTimedSimulation(csm, tEnd);
		
		int transitionIds[] = {10,11,14};
		
		String[] meanTimeOfServiceFiles= {"C:\\Users\\Riccardo\\Desktop\\Università\\terzo anno\\Tesi\\curve di performance\\t10\\tempoMedioServizio.txt",
				"C:\\Users\\Riccardo\\Desktop\\Università\\terzo anno\\Tesi\\curve di performance\\t11\\tempoMedioServizio.txt",
				"C:\\Users\\Riccardo\\Desktop\\Università\\terzo anno\\Tesi\\curve di performance\\t14\\tempoMedioServizio.txt"},
				
				throughputFiles= {"C:\\Users\\Riccardo\\Desktop\\Università\\terzo anno\\Tesi\\curve di performance\\t10\\throughput.txt",
						"C:\\Users\\Riccardo\\Desktop\\Università\\terzo anno\\Tesi\\curve di performance\\t11\\throughput.txt",
						"C:\\Users\\Riccardo\\Desktop\\Università\\terzo anno\\Tesi\\curve di performance\\t14\\throughput.txt"},
				
				utilizationFiles= {"C:\\Users\\Riccardo\\Desktop\\Università\\terzo anno\\Tesi\\curve di performance\\t10\\utilizzazione.txt",
						"C:\\Users\\Riccardo\\Desktop\\Università\\terzo anno\\Tesi\\curve di performance\\t11\\utilizzazione.txt",
						"C:\\Users\\Riccardo\\Desktop\\Università\\terzo anno\\Tesi\\curve di performance\\t14\\utilizzazione.txt"},
				
				numberOfServicesFiles={"C:\\Users\\Riccardo\\Desktop\\Università\\terzo anno\\Tesi\\curve di performance\\t10\\numeroServizi.txt",
						"C:\\Users\\Riccardo\\Desktop\\Università\\terzo anno\\Tesi\\curve di performance\\t11\\numeroServizi.txt",
						"C:\\Users\\Riccardo\\Desktop\\Università\\terzo anno\\Tesi\\curve di performance\\t14\\numeroServizi.txt"};
		
	statistic.printOnFiles(meanTimeOfServiceFiles, throughputFiles, utilizationFiles, numberOfServicesFiles, transitionIds);
		
		for(int i = 10; i<=14;) {
			System.out.println("Transizione " + i);
			System.out.println("Tempo medio di servizio = " + statistic.meanTimeOfService(i));
			System.out.println("Throughput = " + statistic.throughput(i, tEnd));
			System.out.println("Utilizzazione = " + statistic.utilization(i, tEnd));
			System.out.println("Numero servizi = " + statistic.numberOfServices(i));
			System.out.println();
			System.out.println();
			if(i==10)i++;
			else i+=3;
		}
		
	
	}
	

}
