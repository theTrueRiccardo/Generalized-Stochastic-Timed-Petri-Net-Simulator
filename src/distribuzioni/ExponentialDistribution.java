package distribuzioni;
import java.util.*;

public class ExponentialDistribution extends Distribution {
	private double lambda;
	public ExponentialDistribution( Random r, double lambda ){
		super(r);
		if( lambda<=0 ) throw new IllegalArgumentException();
		this.lambda=lambda;
	}
	public double getLambda(){ return lambda; }
	public double nextSample(){
		double u = this.getRandomGenerator().nextDouble();
		return -(1/lambda)*Math.log(u); //o 1-u
	}
	public double nextSample( double u ){
		return -(1/lambda)*Math.log(u); //o 1-u
	}
	public String toString(){
		return "EXP( "+lambda+" )";
	}
	public static void main( String[] args ){
		ExponentialDistribution de=
			new ExponentialDistribution( new Random( System.currentTimeMillis()), 0.2 );
		for( int i=0; i<10000; i++ )
			System.out.println( de.nextSample() );
	}
}//DistribuzioneEsponenziale
