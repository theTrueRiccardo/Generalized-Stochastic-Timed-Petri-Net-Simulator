package statistiche;
import java.util.*;
public class DataKeeper {

	private ArrayList<Double> X_axis = new ArrayList<Double>(),
	meanTimeOfService_axis = new ArrayList<Double>(),
	utilization_axis = new ArrayList<Double>(),
	throughput_axis = new ArrayList<Double>();
	private ArrayList<Integer> numberOfServices_axis = new ArrayList<Integer>();
	
	private int firings = 0;
	
	private double sumOfServiceTime = 0;
	
	private double currentServiceTime;
	
	public void signCurrentServiceTime(double d) {
		currentServiceTime=d;
	}
	
	
	
	public void signFire(double now) {
		firings++;
		sumOfServiceTime+=currentServiceTime;
		X_axis.add(now);
		meanTimeOfService_axis.add(sumOfServiceTime/firings);
		utilization_axis.add(sumOfServiceTime/now);
		throughput_axis.add(firings/now);
		numberOfServices_axis.add(firings);
	}
	
	public int numberOfFires() {
		return firings;
	}
	
	public double sumOfServiceTime() {
		return sumOfServiceTime;
	}
	
	public ArrayList<Double> getX_axis() {
		return X_axis;
	}



	public ArrayList<Double> getMeanTimeOfService_axis() {
		return meanTimeOfService_axis;
	}



	public ArrayList<Double> getUtilization_axis() {
		return utilization_axis;
	}



	public ArrayList<Double> getThroughput_axis() {
		return throughput_axis;
	}
	
	public ArrayList<Integer> getNumberOfServices_axis() {
		return numberOfServices_axis;
	}
	
	
}
