package distribuzioni;
import java.util.*;

public class ErlangDistribution extends Distribution{
	/*
	 * It is the sum of n exponential distributions each one with
	 * the same rate lambda.
	 * The rate of the Erlang is: lambda/n
	 */
	private double lambda;
	private int n;
	
	public ErlangDistribution( Random r, int n, double lambda ){
		super(r);
		if( n<1 || lambda<=0 ) throw new IllegalArgumentException();
		this.n=n;
		this.lambda=lambda;
	}
	
	public double getLambda(){ return lambda; }
	
	public int getN(){ return n; }
	
	public double nextSample(){
		double u=1;
		Random r=getRandomGenerator();
		for( int i=0; i<n; i++ ){
			double ui = r.nextDouble();
			u *= ui; //o 1-ui
		}
		return -(1/lambda)*Math.log( u );
	}
	
	public double nextSample( double u ){
		return nextSample();
	}
	
	public String toString(){
		return "ERLANG( n="+n+", lambda="+lambda+" )";
	}	
	
	public static void main(String[] args) {
		ErlangDistribution e = new ErlangDistribution(new Random(), 16, 0.6);
		for(int i = 0; i <99; i++)System.out.println(e.nextSample());
	}
}//ErlangDistribution
