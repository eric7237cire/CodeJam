import java.io.*;
import java.math.*;
import java.util.*;

class Fraction implements Comparable<Fraction> {
	static BigInteger gcd(BigInteger a, BigInteger b) {
		if (b.equals(BigInteger.ZERO))
			return a;
		return gcd(b, a.mod(b));
	}

	BigInteger n, d;

	public Fraction(long a, long b) {
		n = BigInteger.valueOf(a);
		d = BigInteger.valueOf(b);
		Normalize();
	}

	public Fraction(BigInteger a, BigInteger b) {
		n = a;
		d = b;
		Normalize();
	}

	private void Normalize() {
		if (d.compareTo(BigInteger.ZERO) < 0) {
			n = n.multiply(BigInteger.valueOf(-1));
			d = d.multiply(BigInteger.valueOf(-1));
		}
		BigInteger g = gcd(n, d);
		n = n.divide(g);
		d = d.divide(g);
	}

	public Fraction add(Fraction f) {
		return new Fraction(n.multiply(f.d).add(d.multiply(f.n)), d
				.multiply(f.d));
	}

	public Fraction sub(Fraction f) {
		return new Fraction(n.multiply(f.d).subtract(d.multiply(f.n)), d
				.multiply(f.d));
	}

	public Fraction mul(Fraction f) {
		return new Fraction(n.multiply(f.n), d.multiply(f.d));
	}

	public Fraction div(Fraction f) {
		return new Fraction(n.multiply(f.d), d.multiply(f.n));
	}

	public int compareTo(Fraction o) {

		return n.multiply(o.d).compareTo(d.multiply(o.n));
	}

	public Fraction abs() {

		return new Fraction(n.abs(), d.abs());
	}

}

class triple implements Comparable<triple> 
{
	int ind;
	Fraction val;
	String str;
	public int compareTo(triple arg) {
		if(ind!=arg.ind)
			return Integer.valueOf(ind).compareTo(arg.ind);
		return val.compareTo(arg.val);
	}
	triple(String s)
	{
		ind=0;
		str=s;
		BigInteger n=new BigInteger(s.substring(2));
		BigInteger d=BigInteger.ONE;
		for(int i=0;i<s.length()-2;i++)
			d=d.multiply(BigInteger.TEN);
		val=new Fraction(n, d);
	}
	
	
	@Override
	public String toString() {
	
		return ind+"#"+str;
	}
}

public class a {


	public static void main(String[] args) throws Exception {

		/*
		 * Scanner in = new Scanner(System.in); PrintStream out = System.out; /
		 */
		Scanner in = new Scanner(new FileInputStream("1.in"));
		PrintWriter out = new PrintWriter(new FileWriter("1.out"));
		// */
		int t=in.nextInt(),tt=1;
		
		while(t-->0)
		{
			int n=in.nextInt();
			
			Fraction trd=new Fraction(1,3);
			Fraction trd2=new Fraction(2,3);
			Fraction three=new Fraction(3,1);
			triple d[]=new triple[n];
			for (int k = 0; k < n; k++) {
				
				
				String str=in.next();
				d[k]=new triple(str);
				Fraction x=d[k].val;
				TreeSet<Fraction> s=new TreeSet<Fraction>();
				while(x.compareTo(trd) < 0 || x.compareTo(trd2)>0)
				{
					if(s.contains(x))
					{
						d[k].ind=Integer.MAX_VALUE;
						break;
					}
					s.add(x);
					d[k].ind++;
					if(x.compareTo(trd) < 0 )
					{
						x=x.mul(three);
					}
					else
					{
						x=x.sub(trd2);
						x=x.mul(three);
					}
				}
			}
			Arrays.sort(d);

			out.printf("Case #%d:\n",tt++);
			for (int k = 0; k < n; k++) 
				out.println(d[k].str);
		}
		out.close();
	}


}
