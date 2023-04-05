package distribuzioni;
import java.util.*;

public class UniformDistribution extends Distribution {
	private double a, b;
	public UniformDistribution( Random r, double a, double b ){
		super(r);
		if( a<0 || b<0 || a>b ) throw new IllegalArgumentException();
		this.a=a; this.b=b;
	}
	public double getLowerBound(){ return a; }
	public double getUpperBound(){ return b; }
	public double nextSample(){
		return a+(b-a)*( getRandomGenerator().nextDouble() );
	}
	public double nextSample( double u ){
		return a+(b-a)*u;
	}
	public String toString(){
		return "U["+a+","+b+"]";
	}
}//UniformDistribution
