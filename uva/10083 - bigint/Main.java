import java.math.BigInteger;
import java.util.Scanner;

public class Main {
    
	public static void main(String[] args) {
		Scanner keyboard = new Scanner(System.in);
		int t, a, b;
		while(keyboard.hasNextInt()) {
			t = keyboard.nextInt();
			a = keyboard.nextInt();
			b = keyboard.nextInt();
			System.out.printf("(%d^%d-1)/(%d^%d-1) ", t, a, t, b);
			if(t == 1) {
				System.out.println("is not an integer with less than 100 digits.");
				continue;
			}
			if(a == b) {
				System.out.println("1");
				continue;
			}
			if(a%b != 0) {
				System.out.println("is not an integer with less than 100 digits.");
				continue;
			}
            
			if((a-b)*Math.log10(t) > 99) {
				System.out.println("is not an integer with less than 100 digits.");
				continue;
			}
			BigInteger X, Y, tmp;
			X = BigInteger.valueOf(t);
			Y = BigInteger.valueOf(t);
			X = X.pow(a);
			Y = Y.pow(b);
			X = X.subtract(BigInteger.valueOf(1));
			Y = Y.subtract(BigInteger.valueOf(1));
			if(Y.compareTo(BigInteger.valueOf(0)) == 0) {
				System.out.println("is not an integer with less than 100 digits.");
				continue;
			}
			tmp = X.mod(Y);
			X = X.divide(Y);
			System.out.println(X.toString());
		}
	}
}