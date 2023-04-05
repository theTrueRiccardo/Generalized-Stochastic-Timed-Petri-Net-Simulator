package distribuzioni;
import java.util.*;

public abstract class Distribution {
	private Random r;
	public Distribution( Random r ){ this.r=r; }
	public Random getRandomGenerator(){ return r; }
	public abstract double nextSample();
	public abstract double nextSample( double u );
}//Distribution
