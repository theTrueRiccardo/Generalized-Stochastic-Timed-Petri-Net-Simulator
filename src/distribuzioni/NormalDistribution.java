package distribuzioni;
import java.util.*;

public class NormalDistribution extends Distribution{
	
	/* mean is the mean value of the normal distribution
	 * std is the standard deviation, i.e. the sqrt of the variance
	 * (sigma^2) of the normal distribution.
	 */
	
	private double mean, std;
	public NormalDistribution( Random r, double mean, double std ){ 	
		super(r);
		this.mean=mean; this.std=std;
	}
	
	public double getMean(){ return mean; }
	public double getStd(){ return std; }
	
	public double nextSample(){
	     double v1, v2, s, rnn1, y;
	     Random r=getRandomGenerator();
	     for(;;){
	        v1=2.0*r.nextDouble()-1.0;
	        v2=2.0*r.nextDouble()-1.0;
	        s=v1*v1-v2*v2;
	        if( s<1.0 ) break;
	     }
	     rnn1=v1*Math.sqrt( -2.0*Math.log(s)/s );
	     y=mean+rnn1*std;
	     return y;
	}//nextSample
	
	public double nextSample( double u ){
		return nextSample();
	}//nextSample
	
}//NormalDistribution
