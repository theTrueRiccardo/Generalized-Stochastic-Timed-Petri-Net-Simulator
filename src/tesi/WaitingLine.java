package tesi;
import java.util.*;
import distribuzioni.ExponentialDistribution;
import statistiche.*;
public class WaitingLine {

	public static void main(String[] args) {
		
		Engine engine = new ModelEngine();
		
		Path path = new Path();
		
		engine.setPath(path);
		
		Random sharedSeed = new Random(31415926);
		
		PetriNet waitingLine = new PetriNet(3,2,3,2,0,1,engine);
		
		waitingLine.setMarking(0, 1);
		
		waitingLine.setDistribution(0, new ExponentialDistribution(sharedSeed, 1));
		
		waitingLine.setDistribution(1, new ExponentialDistribution(sharedSeed, 1.5));
		
		waitingLine.setBackwardArc(1, 0, 0);
		waitingLine.setForwardArc(1, 0, 0);
		waitingLine.setForwardArc(1, 1, 0);
		
		waitingLine.setBackwardArc(1, 1, 1);
		
		waitingLine.setForwardArc(1, 2, 1);
		
		int marking[] = {1,0,0};
		waitingLine.initialize(marking);
		
		engine.startTimedSimulation(waitingLine, 50000);
		
		System.out.println(path.L(50000));

	}

}
