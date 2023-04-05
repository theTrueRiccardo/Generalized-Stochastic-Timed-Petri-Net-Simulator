package tesi;
import statistiche.*;
public interface Engine {
	
	void startTimedSimulation(Model model, double endSimulation);
	
	void startEventSimulation(Model model, int events);
	
	void reset();
		
	void simulationEnd();
	
	void schedule(Event event);
	
	void deschedule(Event event);
	
	double now();
	
	void setStatistic(Statistic statistic);
	
	void setPath(Path path);
	
	void visualizeSchedule();

}
