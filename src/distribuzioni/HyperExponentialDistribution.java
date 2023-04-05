package distribuzioni;
import java.util.*;

public class HyperExponentialDistribution extends Distribution {
	private double alfa[];
	
	private ExponentialDistribution distExp[];

	private boolean equals( double d1, double d2 ){
		return Math.abs(d1-d2)<=1.0E-14;
	}//equals
	
	public HyperExponentialDistribution( Random r, double []alfa, double []lambda ){
		super(r);
		if( alfa.length!=lambda.length || alfa.length<0 )
			throw new IllegalArgumentException();
		double sommaAlfa=0;
		for( int i=0; i<alfa.length; i++ ) sommaAlfa += alfa[i];
		if( !equals(sommaAlfa, 1D) ) 
			throw new IllegalArgumentException("Somma coeff alfa non unitaria");
		this.alfa=new double[alfa.length];
		System.arraycopy( alfa, 0, this.alfa, 0, alfa.length );
		this.distExp=new ExponentialDistribution[lambda.length];
		for( int i=0; i<lambda.length; i++ ){
			if( lambda[i]<=0 ) throw new IllegalArgumentException();
			distExp[i] = 
				new ExponentialDistribution( getRandomGenerator(), lambda[i] );
		}
	}
	
	public double[] getAlfa(){
		double[] a = new double[alfa.length];
		System.arraycopy( alfa, 0, a, 0, alfa.length );
		return a;
	}//getAlfa
	
	public double[] getLambda(){
		double[] lda = new double[alfa.length];
		for( int i=0; i<alfa.length; i++ )
			lda[i] = distExp[i].getLambda();
		return lda;
	}//getLambda
	
	public double nextSample(){
		double u = this.getRandomGenerator().nextDouble();
		return nextSample( u );
	}
	
	public double nextSample( double u ){
		double tacca=0;
		int i=0;
		for( ; i<alfa.length; i++ )
			if( u>=tacca && u<tacca+alfa[i] ) break;
			else tacca=tacca+alfa[i];
		return distExp[i].nextSample(); 
	}	
	
	public String toString(){
		String sa="[", sl="[";
		for( int i=0; i<alfa.length; i++ ){
			sa+=alfa[i];
			sa+=(i<alfa.length-1)? ", " : "";
			sl+=distExp[i].getLambda();
			sl+=(i<alfa.length-1)? ", " : "";
		}
		sa+="]"; sl+="]";
		return "HYPER-EXP( alfa="+sa+", lambda="+sl+" )";
	}//toString
	
}//HyperExponentialDistribution
