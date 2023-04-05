package tesi;



public class TransitionEvent implements Event {
	
	private int transitionId;
	
	private double delay;
	
	private double absoluteTime;
	
	private boolean validityFlag = true;
	
	private boolean instanstaneous=false;
	
	
	
	public TransitionEvent(int transitionId, double delay) {
		this.transitionId=transitionId;
		this.delay=delay;
	}

	
	
	public void setIstantaneous() {
		instanstaneous=true;
	}
	
	public int getTransitionId() {
		return transitionId;
	}

	public void setTransitionId(int transitionId) {
		this.transitionId = transitionId;
	}
	
	
	public double getDelay() {
		return delay;
	}

	public void setDelay(double delay) {
		this.delay = delay;
	}

	
	
	
	
	public double getAbsoluteTime() {
		return absoluteTime;
	}
	





	public boolean equals(Object o) {
		if(!(o instanceof TransitionEvent)) return false;
		if(o==this) return true;
		TransitionEvent te = (TransitionEvent)o;
		return transitionId==te.getTransitionId();
	}

	
	
	@Override
	public void invalid() {
		validityFlag=false;
	}

	@Override
	public boolean isValid() {
		return validityFlag;
	}





	@Override
	public void absolutize(double now) {
		absoluteTime=now+delay;
	}


	public double getRelativeTime() {
		return delay;
	}
	
	public int ID() {
		return transitionId;
	}
	@Override
	public int compareTo(Event event) {
		TransitionEvent te = (TransitionEvent)event;
		if(isIstantaneous() && !event.isIstantaneous()) return -1;
		if(!isIstantaneous()&& event.isIstantaneous()) return 1;
		if(absoluteTime<te.getAbsoluteTime()) return -1;
		if(absoluteTime>te.getAbsoluteTime()) return 1;
		return 0;
	}
	
	

	public String toString() {
		return "ID="+transitionId+"   FIRING_AT="+absoluteTime;
	}





	@Override
	public boolean isIstantaneous() {
		// TODO Auto-generated method stub
		return instanstaneous;
	}
}
