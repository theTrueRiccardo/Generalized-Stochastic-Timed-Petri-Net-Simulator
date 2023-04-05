package tesi;
import distribuzioni.ExponentialDistribution;
import statistiche.*;
import java.util.*;
public class WaitingLineGSPN {
	public static void main(String[] args) {
		Engine engine = new ModelEngine();
		PetriNet waitingLine = new PetriNet(3,2,0,1,engine); 
		String file = "C:\\Users\\Riccardo\\Desktop\\wl.txt";
		waitingLine.takeFromFile(file);
		
		double tEnd=50000;
		int marking[] = {1,0,0};
		
		for(int i = 0; i < 30; i++) {
			Path path = new Path();
			engine.setPath(path);
			engine.startTimedSimulation(waitingLine, tEnd);
			double L = path.L(tEnd);
			System.out.println("Run #" + i+ " Numero medio di utenti in coda ="+L);
			engine.reset();
			waitingLine.initialize(marking);
		}
	}
}
