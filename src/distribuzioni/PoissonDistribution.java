package distribuzioni;
import java.util.*;

public class PoissonDistribution extends Distribution{
	private double lambda;
	
	public PoissonDistribution( Random r, double lambda ){
		super(r);
		this.lambda=lambda;
	}
	
	public double getLambda(){ return lambda; }
	
	public double nextSample(){
		Random r=getRandomGenerator();
		double x=0.0, a=Math.exp( -lambda ), s=1.0;
		for(;;){
			s=s*r.nextDouble();
			if( s<a ) break;
			x += 1.0;
		}
		return x;
	}//nextSample
	
	public double nextSample( double u ){
		return nextSample();
	}//nextSample
	
}//PoissonDistribution
