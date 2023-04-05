package tesi;

public interface Model {
	
	void fire(Event event);
	void viewState();
	int getInfoOn(int element);
	void takeFromFile(String file);
}
