package tesi;

import java.util.*;

import distribuzioni.Distribution;
import distribuzioni.ErlangDistribution;
import distribuzioni.ExponentialDistribution;
import distribuzioni.HyperExponentialDistribution;
import distribuzioni.NormalDistribution;
import distribuzioni.PoissonDistribution;
import distribuzioni.UniformDistribution;

import java.io.*;

public class PetriNet implements Model{
	
	//0 means inihbitor arc, -1 means arc doesn't exists
	
	
	
	private int[][] backward;
	
	private int[][] forward;
	
	private int[] marking;
	
	private int beginUntimedIds,endUntimedIds,beginTimedIds,endTimedIds;
	
	private Engine engine;
	
	private boolean untimed;
	
	
	/*All id's of untimed transitions are in the first array,
	 * whith the couples priority-probability associated in the other two arrays. 
	 * if an untimed ID is in immediateTransitions[i], then its priority 
	 * is in prioritiesOfImmediateTransitions[i] and its probability is in
	 * probabilitiesOfImmediateTransitions[i]. Arrays are sorted in
	 * crescent order, according to priorities. Said that, the first transition 
	 * of immediateTransitions array is the most priority one.
	 * */
	private int[] immediateTransitions;
	private int[] prioritiesOfImmediateTransitions;
	private double[] probabilitiesOfImmediateTransitions;
	
	
	
	
	//Timed transition Ids as keys, pdf as values
	private HashMap<Integer,Distribution> distributionsOfTimedTransitions;
	
	
	
	
	
	public PetriNet(int numberOfPlaces, int numberOfTransitions, int beginUntimedIds, int endUntimedIds, int beginTimedIds,int endTimedIds, Engine engine) {
		backward = new int[numberOfPlaces][numberOfTransitions];
		forward = new int[numberOfPlaces][numberOfTransitions];
		for(int i =0; i < backward.length; i++) {
			for(int j = 0; j < backward[0].length; j++) {
				backward[i][j]=-1;
				forward[i][j] =-1;
			}
		}
		marking = new int[numberOfPlaces];
		this.beginUntimedIds=beginUntimedIds;
		this.endUntimedIds=endUntimedIds;
		this.beginTimedIds=beginTimedIds;
		this.endTimedIds=endTimedIds;
		this.engine=engine;
		distributionsOfTimedTransitions=new HashMap<Integer,Distribution>();
		//check for absence of untimed
		untimed = (endUntimedIds>beginUntimedIds) ? true : false;
		if(!untimed) return;
		//set the untimed
		int numberOfUntimed = endUntimedIds-beginUntimedIds+1;
		immediateTransitions = new int[numberOfUntimed];
		prioritiesOfImmediateTransitions = new int[numberOfUntimed];
		probabilitiesOfImmediateTransitions=new double[numberOfUntimed];
		//set all cells to -1, to indicate the absence of untimed.
		for(int i = 0; i < immediateTransitions.length; i++) {
			immediateTransitions[i]=-1;
			prioritiesOfImmediateTransitions[i]=-1;
			probabilitiesOfImmediateTransitions[i]=-1;
		}
	}
	
	
	public PetriNet(int numberOfPlaces, int numberOfTimedTransitions,int beginTimedIds, int endTimedIds,Engine engine) {
		untimed=false;
		backward = new int[numberOfPlaces][numberOfTimedTransitions];
		forward = new int[numberOfPlaces][numberOfTimedTransitions];
		for(int i =0; i < backward.length; i++) {
			for(int j = 0; j < backward[0].length; j++) {
				backward[i][j]=-1;
				forward[i][j] =-1;
			}
		}
		this.beginTimedIds=beginTimedIds;
		this.endTimedIds=endTimedIds;
		this.engine=engine;
		distributionsOfTimedTransitions=new HashMap<Integer,Distribution>();
	}
	
	
	
	public void setBackwardArc(int arcWeight,int placeId, int transitionId) {
		backward[placeId][transitionId] = arcWeight;
	}
	public void setForwardArc(int arcWeight,int placeId, int transitionId) {
		forward[placeId][transitionId] = arcWeight;
	}
	public void setMarking(int placeId, int numberOfToken) {
		marking[placeId] = numberOfToken;
	}
	public void setUntimedTransition(int tid, int priority, double probability) {
		//search for a free slot (i.e. == -1)
		for(int i = 0; i < immediateTransitions.length;i++) {
			if(immediateTransitions[i]==-1) {
				immediateTransitions[i]=tid;
				prioritiesOfImmediateTransitions[i]=priority;
				probabilitiesOfImmediateTransitions[i]=probability;
				break;
			}
		}
	}
	public void setDistribution(int transitionId,Distribution distribution) {
		distributionsOfTimedTransitions.put(transitionId, distribution);
	}
	public void initialize(int marking[]) {
		this.marking=Arrays.copyOf(marking, marking.length);
		if(untimed)sortProperly(immediateTransitions,prioritiesOfImmediateTransitions,probabilitiesOfImmediateTransitions);
		elect();
		propagatesConsequences();
	}
	
	
	private void sortProperly(int[] t, int[] pri, double[] pro) {
		boolean scambi=true;
		while(scambi) {
			scambi = false;
			for(int i = 0; i < t.length-1; i++) {
				if(pri[i]<pri[i+1]) {
					int tmpT=t[i],tmpPri=pri[i]; double tmpPro=pro[i];
					t[i]=t[i+1];pri[i]=pri[i+1];pro[i]=pro[i+1];
					t[i+1]=tmpT;pri[i+1]=tmpPri;pro[i+1]=tmpPro;
					scambi=true;
				}
			}
		}
	}
	
	
	
	
	
	
	private boolean isTimed(int transitionId) {
		return (transitionId<=endTimedIds&&transitionId>=beginTimedIds) ? true:false;
	}
	
	private boolean isUntimed(int transitionId) {
		return (transitionId<=endUntimedIds&&transitionId>=beginUntimedIds) ? true:false;
	}
	
	
	
	public void viewState() {
		System.out.println("Marking: " + Arrays.toString(marking));
		System.out.println("ImmediateTransitions: " + Arrays.toString(immediateTransitions));
		System.out.println("Their priorities: " + Arrays.toString(prioritiesOfImmediateTransitions));
		System.out.println("And their probabilities: " + Arrays.toString(probabilitiesOfImmediateTransitions));
		System.out.println("Timed transitions and their distributions: " + distributionsOfTimedTransitions.toString());
		for(int i =0; i< backward.length;i++) {
			for(int j=0; j < backward[0].length;j++) {
				if(backward[i][j]==0)System.out.println("Posto["+i+"]------oTransizione["+j+"]");
				else if(backward[i][j]!=-1)System.out.println("Posto["+i+"]------>Transizione["+j+"]");
				if(forward[i][j]!=-1)System.out.println("Transizione["+j+"]------>Posto["+i+"]");
			}
		}
	}
	
	
	public void takeFromFile(String nomeFile) {
		try{
			BufferedReader br = new BufferedReader(new FileReader(nomeFile));
			String linea = null;
			StringTokenizer st=null;
			ArrayList<Integer> tmp = new ArrayList<Integer>();
			ArrayList<Double> tmp2 = new ArrayList<Double>();
			ArrayList<Distribution>tmp3=new ArrayList<Distribution>();
	loop:	for(int i=0;i<5;i++) {
				if(i==1 && !untimed) {i=3; continue loop;}
				linea = br.readLine();
				st=new StringTokenizer(linea, " ");
				tmp.clear();
				while(st.hasMoreTokens()) {
					if(i!=3) {
						int el = Integer.parseInt(st.nextToken());
						tmp.add(el);
					}
					else {
						double el = Double.parseDouble(st.nextToken());
						tmp2.add(el);
					}
				}
				switch(i) {
				case 0 : {marking=new int[tmp.size()]; for(int j=0;j<tmp.size();j++)marking[j]=tmp.get(j); break;}
				case 1 : {immediateTransitions=new int[tmp.size()];for(int j=0;j<tmp.size();j++)immediateTransitions[j]=tmp.get(j); break;}
				case 2 : {prioritiesOfImmediateTransitions=new int[tmp.size()];for(int j=0;j<tmp.size();j++)prioritiesOfImmediateTransitions[j]=tmp.get(j); break;}
				case 3 : {probabilitiesOfImmediateTransitions=new double[tmp2.size()];for(int j=0;j<tmp2.size();j++)probabilitiesOfImmediateTransitions[j]=tmp2.get(j); break;}
				case 4 : {break;}
				}
			}
			/*Distributions. At this time tmp contains id of timed transitions
			 * In tmp2, for each distribution a time, there will be their parameters
			 * */
			linea = br.readLine();
			st=new StringTokenizer(linea, "|");
			int i = 0; //index for timed transitions
			while(st.hasMoreTokens()) {
				tmp2.clear();
				String distribution = st.nextToken();
				StringTokenizer st2 = new StringTokenizer(distribution," ");
				int k = 0;
				String nomeDistribuzione=null;
				while(st2.hasMoreTokens()) {
					if(k==0) {
						nomeDistribuzione = st2.nextToken();
						k++;
					}
					else {
						tmp2.add(Double.parseDouble(st2.nextToken()));
					}
				}
				switch(nomeDistribuzione) {
				case "exp" : {distributionsOfTimedTransitions.put(tmp.get(i), new ExponentialDistribution(new Random(), tmp2.get(0))); break;}
				case "hyp" : {
					int len =0;
					boolean menoUno=false;
					for(int par=0; par < tmp2.size(); par++) {
						if(tmp2.get(par)==-1)break;
						len++;
					}
					double[] alfa= new double[len];
					double[] lambda= new double[len];
					for(int par=0; par < tmp2.size(); par++) {
						if(tmp2.get(par)==-1) menoUno=true;
						if(menoUno && tmp2.get(par)!=-1)lambda[par-(len)-1]=tmp2.get(par);
						else if(tmp2.get(par)!=-1) alfa[par]=tmp2.get(par);
					}
					distributionsOfTimedTransitions.put(tmp.get(i), new HyperExponentialDistribution(new Random(), alfa, lambda)); 
					break;
				}
				case "nor" : {distributionsOfTimedTransitions.put(tmp.get(i), new NormalDistribution(new Random(), tmp2.get(0), tmp2.get(1))); break;}
				case "poi" : {distributionsOfTimedTransitions.put(tmp.get(i), new PoissonDistribution(new Random(), tmp2.get(0))); break;}
				case "uni" : {distributionsOfTimedTransitions.put(tmp.get(i), new UniformDistribution(new Random(), tmp2.get(0), tmp2.get(1))); break;}
				case "erl" : {distributionsOfTimedTransitions.put(tmp.get(i), new ErlangDistribution(new Random(), (int)Math.round(tmp2.get(0)), tmp2.get(1))); break;}
				}
				i++;
			}
			//Topology
			while(true) {
				linea=br.readLine();
				if(linea==null) break;
				int place=Integer.parseInt(linea.charAt(0)+"");
				int transition=Integer.parseInt(linea.charAt(2)+"");
				char arc=linea.charAt(1);
				int weight =Integer.parseInt(linea.charAt(4)+"");
				switch(arc) {
				case 'o' : backward[place][transition]=0; break;
				case '>' : backward[place][transition]=weight; break;
				case '<' : forward[place][transition]=weight; break;
				}
			}
			br.close();
		}catch(IOException e) {System.out.println("Errore col file");}
		elect();
		propagatesConsequences();
	}
		
	
	
	private void deposit(int numberOfTokens, int placeId) {
		marking[placeId] += numberOfTokens;
	}
	
	private void withdraw(int numberOfTokens, int placeId) {
		marking[placeId] -= numberOfTokens;
	}
	
	
	
	
	
	private boolean isEnabled(int transitionId) {
		for(int placeId=0; placeId < backward.length; placeId++) {
			int arc = backward[placeId][transitionId];
			switch(arc) {
				case 0: if(marking[placeId]!=0) return false; break; //Inihbitor arc: no tokens have to be present in preset(tId)
				case -1:break; //No arc
				default: if(marking[placeId] < arc) return false; //normal arc
			}
		}
		return true;
	}
	
	
	
	
	private void propagatesConsequences() {
		for(int timedTransitionId = beginTimedIds; timedTransitionId <= endTimedIds; timedTransitionId++) {
			if(!(isEnabled(timedTransitionId))) {
				/*Dummy couple: two transition events are equals iff they have the same Id.
				 * Such equals method allows me to ignore the timestamp of a timed transition
				 * when I want to deschedule it.
				*/
				engine.deschedule(new TransitionEvent(timedTransitionId,0)); 
			}
			else {
				double delay = distributionsOfTimedTransitions.get(timedTransitionId).nextSample();
				engine.schedule(new TransitionEvent(timedTransitionId,delay));
			}
		}
	}
	
	
	
	
	
	
	
	public void fire(Event event) {
		TransitionEvent te = (TransitionEvent)event;
		int transitionId = te.getTransitionId();
		for(int placeId = 0; placeId < marking.length; placeId++) {
			int backwardArcWeight = backward[placeId][transitionId];
			if(backwardArcWeight!=-1)withdraw(backwardArcWeight,placeId);
		}
		propagatesConsequences();
		for(int placeId = 0; placeId < marking.length; placeId++) {
			int forwardArcWeight = forward[placeId][transitionId];
			if(forwardArcWeight!=-1)deposit(forwardArcWeight,placeId);
		}
		propagatesConsequences();
		elect();
	}
	
	
	
	
	public int getInfoOn(int placeId) {
		return marking[placeId];
	}
	
	
	
	private int randomSwitch(HashMap<Integer,Double> transitionsWithProbabilities) {
		double PRs = 0;
		for(Integer tId : transitionsWithProbabilities.keySet()) PRs+=transitionsWithProbabilities.get(tId);
		double u = Math.random(), r=u*PRs;
		Iterator<Integer> it = transitionsWithProbabilities.keySet().iterator();
		int tIdResult = 0;
		double inf = 0, sup=0;
		do {
			tIdResult = it.next();
			sup += transitionsWithProbabilities.get(tIdResult);
			if(r>=inf && r<sup)break;
			inf = sup;
		}while(it.hasNext());
		return tIdResult;
	}
	
	
	
	
	/*The most priority enabled untimed transiton is elected and scheduled.
	 * If there are transitions enabled with the same priority,
	 * (i.e. method's map's size > 1) randomSwitch method it's called 
	 * passing it the transitions in this map structure as argument so that
	 * it can return the selected one.
	 * If there isn't any immediate transition enabled
	 * (i.e. method's map's size==0), the method doesn't schedule anything.
	 * */
	private void elect() {
		if(!untimed) return;
		int untimedToFire;
		HashMap<Integer,Double> transitionsWithProbabilities = new HashMap<Integer,Double>();
		int maxPriority=-1;
		for(int i = 0; i < immediateTransitions.length; i++) { //for all untimed transitions
			int tId = immediateTransitions[i];
			if(isEnabled(tId)) { //check if it's enabled
				int currentPriority = prioritiesOfImmediateTransitions[i];
				if(currentPriority > maxPriority) { //It is so for the first enabled transition (if any) of immediateTransitions structure
					maxPriority = currentPriority;
					transitionsWithProbabilities.put(tId, probabilitiesOfImmediateTransitions[i]);
				}
				else if(currentPriority==maxPriority) { //if any, will be all enabled transitions that come after the first 
					transitionsWithProbabilities.put(tId, probabilitiesOfImmediateTransitions[i]);
				}
				else break; //means that the remaining transitions are less priority, so it's usless to continue the loop
			}
			else if(!isEnabled(tId) && probabilitiesOfImmediateTransitions[i] < maxPriority) break; //same as above
		}
		if(transitionsWithProbabilities.isEmpty()) untimedToFire = -2; //no enabled transition exists
		else if(transitionsWithProbabilities.size()>1) untimedToFire = randomSwitch(transitionsWithProbabilities); //chose randomically
		else { //only one transition in the map
			int tIdResult = 0;
			Iterator<Integer> iterator = transitionsWithProbabilities.keySet().iterator();
			tIdResult= iterator.next(); //chose the most priority one
			untimedToFire=tIdResult;
		}
		if(untimedToFire!=-2) {
			TransitionEvent te = new TransitionEvent(untimedToFire, 0);
			te.setIstantaneous();
			engine.schedule(te); //if present, schedule immediate transition
		}
	}
	
	
	
	
	

}
