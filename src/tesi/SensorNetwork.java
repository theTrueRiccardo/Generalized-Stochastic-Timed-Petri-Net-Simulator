package tesi;

import distribuzioni.UniformDistribution;
import java.util.*;
public class SensorNetwork {

	private static void sensor(PetriNet n,double ts,double swp,double nd) {
		n.setMarking(0, 1);
		n.setBackwardArc(1, 0, 13);
		n.setForwardArc(1, 1, 13);
		n.setBackwardArc(1, 1, 0);
		n.setBackwardArc(1,1,1);
		n.setForwardArc(1, 0, 1);
		n.setForwardArc(1, 2, 1);
		n.setBackwardArc(1, 2, 2);
		n.setBackwardArc(1, 2, 3);
		n.setBackwardArc(1, 3, 3);
		n.setBackwardArc(0, 3, 2);
		n.setForwardArc(1, 4, 3);
		n.setBackwardArc(1, 4, 14);
		
		n.setDistribution(13, new UniformDistribution(new Random(), ts, ts));
		n.setDistribution(14, new UniformDistribution(new Random(), nd, nd));
		n.setUntimedTransition(0, 1,1-swp);
		n.setUntimedTransition(1, 1, swp);
		//da settare le probabilità di t2 e t3
		n.setUntimedTransition(2, 1, 1);
		n.setUntimedTransition(3, 1, 1);
		n.setForwardArc(1, 5, 14);
	}
	
	
	private static void controller(PetriNet n,double tc) {
		n.setMarking(6, 1);
		n.setBackwardArc(0, 5, 4);
		n.setBackwardArc(1, 5, 5);
		n.setBackwardArc(0, 8, 5);
		n.setForwardArc(1, 8, 5);
		n.setForwardArc(1, 6, 4);
		n.setForwardArc(1, 6, 5);
		n.setBackwardArc(1, 6, 15);
		n.setForwardArc(1, 7, 15);
		n.setBackwardArc(1, 7, 4);
		n.setBackwardArc(1, 7, 5);
		
		//da settare priorità e probabilità di t4 e t5
		n.setUntimedTransition(4, 1, 1);
		n.setUntimedTransition(5, 1, 1);
		n.setDistribution(15, new UniformDistribution(new Random(), tc, tc));
		
		n.setForwardArc(1, 9, 5);
	}
	
	
	private static void rescue(PetriNet n,double tnd,double tack,double tr) {
		n.setMarking(11, 1);
		n.setBackwardArc(1, 9, 16);
		n.setForwardArc(1, 10, 16);
		n.setBackwardArc(1, 10, 17);
		n.setForwardArc(1, 12, 17);
		n.setBackwardArc(1, 11, 18);
		n.setBackwardArc(1, 12, 18);
		n.setForwardArc(1, 13, 18);
		n.setBackwardArc(1,13, 19);
		n.setForwardArc(1, 14, 19);
		n.setBackwardArc(1, 14, 20);
		
		n.setDistribution(16, new UniformDistribution(new Random(), tnd, tnd));
		n.setDistribution(17, new UniformDistribution(new Random(), tack, tack));
		n.setDistribution(18, new UniformDistribution(new Random(), tnd, tnd));
		n.setDistribution(19, new UniformDistribution(new Random(), tr, tr));
		n.setDistribution(20, new UniformDistribution(new Random(), 0.00000000001, 0.00000000001));
		
		n.setForwardArc(1, 15, 16);
		n.setBackwardArc(1, 10, 6);
		n.setForwardArc(1, 20, 20);
	}
	
	
	private static void scientist(PetriNet n, double tnd, double tdie,double shp) {
		n.setMarking(16, 1);
		n.setBackwardArc(1, 15, 7);
		n.setBackwardArc(1, 16, 7);
		n.setForwardArc(1, 17, 7);
		n.setBackwardArc(1, 17, 8);
		n.setBackwardArc(1, 17, 9);
		n.setForwardArc(1, 19, 8);
		n.setBackwardArc(1, 19, 21);
		n.setForwardArc(1, 18, 21);
		n.setBackwardArc(1, 18, 6);
		n.setForwardArc(1, 20, 8);
		n.setBackwardArc(0, 20, 20);
		n.setBackwardArc(0, 21, 8);
		n.setBackwardArc(0, 21, 20);
		n.setBackwardArc(0, 20, 22);
		n.setBackwardArc(0, 21, 22);
		n.setForwardArc(1, 21, 22);
		n.setForwardArc(1, 22, 22);
		n.setBackwardArc(1, 22, 22);
		
		
		n.setUntimedTransition(8, 1, shp);
		n.setUntimedTransition(9, 1, 1-shp);
		n.setDistribution(21, new UniformDistribution(new Random(), tnd, tnd));
		n.setDistribution(22, new UniformDistribution(new Random(), tdie, tdie));
		//da settare t6 e t7
		n.setUntimedTransition(6,1,1);
		n.setUntimedTransition(7, 1, 1);
		
		n.setBackwardArc(0, 22, 10);
	}
	
	
	private static void enviroment(PetriNet n, double te, double gop) {
		n.setMarking(25, 1);
		n.setForwardArc(1, 23, 10);
		n.setForwardArc(1, 3, 10);
		n.setForwardArc(1, 22, 10);
		n.setBackwardArc(2, 24, 12);
		n.setBackwardArc(1, 24, 10);
		n.setBackwardArc(1, 24, 11);
		n.setBackwardArc(0, 23, 11);
		n.setForwardArc(1, 24, 23);
		n.setForwardArc(1, 25, 23);
		n.setBackwardArc(1, 25, 23);
		
		n.setDistribution(23, new UniformDistribution(new Random(), te, te));
		n.setUntimedTransition(11, 1, gop);
		n.setUntimedTransition(10, 1, 1-gop);
		//da settare t12
		n.setUntimedTransition(12,1, 1);
	}
	
	
	
	
	
	public static void main(String[] args) {
		
		Engine engine = new ModelEngine();
		
		PetriNet sensorNetwork = new PetriNet(26,24,0,12,13,23,engine);
		
		int defaultMarking[]={1,0,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,0,0,0,0,1};
		
		double ts=1, tc=3, te=5, tdie=10, tnd=1, tack=2, tr=3, swp=0.99,gop=0.98,shp=0.90;
		
		double sciDie=0, experiments=5000,Ps;
		
		sensor(sensorNetwork,ts,swp,tnd);
		controller(sensorNetwork,tc);
		rescue(sensorNetwork,tnd,tack,tr);
		scientist(sensorNetwork,tnd,tdie,shp);
		enviroment(sensorNetwork,te,gop);
		
		sensorNetwork.initialize(defaultMarking);
		
		sensorNetwork.viewState();
		
		
		for(ts=1; ts <=15; ts++) {
			sensorNetwork.setDistribution(13, new UniformDistribution(new Random(), ts, ts));
			for(int i = 0; i < 5000; i++) {
				engine.startTimedSimulation(sensorNetwork, 5000); //each simulation last 5000 time units
				if(sensorNetwork.getInfoOn(21)==1)sciDie++;
				engine.reset();
				sensorNetwork.initialize(defaultMarking);
			}
			Ps=1-(sciDie/experiments);
			System.out.println(ts + " " + Ps);
			sciDie=0;
		}	
		
		
		
		
	}

}
