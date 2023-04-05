package statistiche;

public class Path {
	private int N = 0;
	private int n = 0;
	private double A=0, t=0;
	public void up( double t ){ A += n*(t-this.t); n++; N++; this.t=t; };
	public void down( double t ){ A += n*(t-this.t); n--; this.t=t; };
	public double L( double tEnd ){ return (A)/tEnd; };
	public double W() { return A/N; }
}
