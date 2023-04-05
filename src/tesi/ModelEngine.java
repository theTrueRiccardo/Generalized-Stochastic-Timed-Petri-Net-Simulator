package tesi;
import java.util.*;

import statistiche.*;

public class ModelEngine implements Engine {

	private boolean terminated=false;
	
	private Statistic statistic;
	
	private Path path; 
	
	private double now=0;
	
	private PriorityQueue<Event> eventsQueue = new PriorityQueue<Event>();
	
	
	public void reset() {
		now=0;
		eventsQueue.clear();
		terminated=false;
	}

	public void setStatistic(Statistic statistic) {
		this.statistic=statistic;
	}
	
	public void setPath(Path path) {
		this.path=path;
	}
	
	
	public void startTimedSimulation(Model model, double endSimulation){
		while(now<=endSimulation && !terminated) {
			if(eventsQueue.isEmpty()) {
				System.out.println("Deadlock");
				break;//deadlock
			}
			Event event = eventsQueue.poll();
			if(event.isValid()) {
				now = event.getAbsoluteTime();
				model.fire(event);
				if(statistic!=null)statistic.signFire(event.ID(),now);
				if(event.ID()==0)path.up(now);
				else path.down(now);
			}
		}
	}
	
	
	
	
	
	public void startEventSimulation(Model model, int numberOfEvents) {
		while(numberOfEvents>0) {
			if(eventsQueue.isEmpty())break;
			Event event = eventsQueue.poll();
			if(event.isValid()) {
				model.fire(event);
				numberOfEvents--;
			}
		}
	}

	
	
	
	
	
	
	@Override
	public void schedule(Event event) {
		if(eventsQueue.contains(event)) return; //already scheduled
		event.absolutize(now);
		eventsQueue.add(event);
		if(statistic!=null)statistic.setCurrentServiceTime(event.ID(), event.getRelativeTime());
	}

	
	
	
	@Override
	public void deschedule(Event event) {
		invalid(event);
	}
	
	
	
	
	private void invalid(Event event) {
		for(Event e : eventsQueue) {
			if(e.equals(event)) {
				e.invalid();
				return;
			}
		}
	}
	
	
	public double now() {
		return now;
	}
	
	
	public void simulationEnd() {
		terminated=true;
	}
	
	public void visualizeSchedule() {
		System.out.println(eventsQueue.toString());
	}

}
