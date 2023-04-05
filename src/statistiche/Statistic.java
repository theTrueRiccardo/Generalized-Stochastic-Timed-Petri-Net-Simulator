package statistiche;
import java.util.*;
import java.io.*;
public class Statistic {
	
	private HashMap<Integer,DataKeeper> data = new HashMap<Integer,DataKeeper>();
	
	public void insertTransition(int tId,DataKeeper dataKeeper) {
		data.put(tId, dataKeeper);
	}
	
	public void setCurrentServiceTime(int tId,double delay){
		data.get(tId).signCurrentServiceTime(delay);
	}
	
	public void signFire(int tId,double now) {
		data.get(tId).signFire(now);
	}
	
	public double meanTimeOfService(int tId) {
		DataKeeper dk = data.get(tId);
		return dk.sumOfServiceTime()/dk.numberOfFires();
	}
	
	public double utilization(int tId,double totalTime) {
		DataKeeper dk = data.get(tId);
		return dk.sumOfServiceTime()/totalTime;
	}
	
	public double throughput(int tId,double totalTime) {
		DataKeeper dk = data.get(tId);
		return dk.numberOfFires()/totalTime;
	}
	
	public int numberOfServices(int tId) {
		DataKeeper dk = data.get(tId);
		return dk.numberOfFires();
	}
	
	
	
	
	/*Each transitions has its own files. There is a file for each performance curve for every transition
	 * so that e.g. to throughputFiles[i] corresponds the file of throughput curve of transition in transitionIds[i] */
	public void printOnFiles(String[] meanTimeOfServiceFiles, String[] throughputFiles, String[] utilizationFiles,String[] numberOfServicesFiles,int[] transitionIds) {
		try{
			PrintWriter pwMean[]=new PrintWriter[meanTimeOfServiceFiles.length];
			PrintWriter pwThroughput[]=new PrintWriter[throughputFiles.length];
			PrintWriter pwUtilization[]=new PrintWriter[utilizationFiles.length];
			PrintWriter pwServices[] = new PrintWriter[numberOfServicesFiles.length];
			
			for(int i = 0; i < meanTimeOfServiceFiles.length; i++) pwMean[i]=new PrintWriter(new FileWriter(meanTimeOfServiceFiles[i]));
			for(int i = 0; i < throughputFiles.length; i++) pwThroughput[i]=new PrintWriter(new FileWriter(throughputFiles[i]));
			for(int i = 0; i < utilizationFiles.length; i++) pwUtilization[i]=new PrintWriter(new FileWriter(utilizationFiles[i]));
			for(int i = 0; i < numberOfServicesFiles.length; i++) pwServices[i]=new PrintWriter(new FileWriter(numberOfServicesFiles[i]));
			
			for(int i = 0; i < transitionIds.length;i++) { //for each transition
				ArrayList<Double> x = data.get(transitionIds[i]).getX_axis(); //get its data
				ArrayList<Double> mean = data.get(transitionIds[i]).getMeanTimeOfService_axis();
				ArrayList<Double> utilization = data.get(transitionIds[i]).getUtilization_axis();
				ArrayList<Double> throughput = data.get(transitionIds[i]).getThroughput_axis();
				ArrayList<Integer> services = data.get(transitionIds[i]).getNumberOfServices_axis();
				for(int j=0; j < x.size(); j++) { //print curves on files
					String time = String.valueOf(x.get(j)).replace('.', ',');
					String meanY = String.valueOf(mean.get(j)).replace('.', ',');
					String utilizationY = String.valueOf(utilization.get(j)).replace('.', ',');
					String throughputY = String.valueOf(throughput.get(j)).replace('.', ',');
					String servicesY = String.valueOf(services.get(j));
					
					pwMean[i].println(time+"   "+meanY);
					pwUtilization[i].println(time+"   "+utilizationY);
					pwThroughput[i].println(time+"   "+throughputY);
					pwServices[i].println(time+"   "+servicesY);
				}
			}
		
			for(int i = 0; i < transitionIds.length;i++) {
				pwMean[i].close();
				pwServices[i].close();
				pwThroughput[i].close();
				pwUtilization[i].close();
			}
			
		}catch(IOException e) {
			System.out.println("Qualcosa è andato storto");
			e.printStackTrace();
		}
	}
	
	
}
