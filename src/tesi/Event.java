package tesi;

public interface Event extends Comparable<Event> {

	void invalid();
	
	void setIstantaneous();
	
	boolean isValid();
	
	boolean isIstantaneous();
	
	void absolutize(double now);
	
	double getAbsoluteTime();
	
	double getRelativeTime();
	
	int ID();
}
