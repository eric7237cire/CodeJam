import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.StringTokenizer;

public class D13 {
	class Shop {
		double x;
		double y;
		int[] prices;
		double[] minAmount;
		double[] minPersAmount;
		double[] route;
		
		public Shop(double x, double y, int itemCount) {
			this.x = x;
			this.y = y;
			prices = new int[itemCount];
			minAmount = new double[1 << itemCount];
			minPersAmount = new double[1 << itemCount];
			Arrays.fill(minAmount, Double.MAX_VALUE);
			Arrays.fill(minPersAmount, Double.MAX_VALUE);
		}

		public void process(boolean pers, int mask, double amount) {
			if (amount != getAmounts(pers)[mask]) {
				return;
			}
			for (int i = 0; i < prices.length; i++) {
				if (prices[i] != 0 && !isSet(mask, i)) {
					addEvent(pers || isSet(persMask, i), set(mask, i), amount + prices[i]);
				}
			}
			
			if (!pers) {
				for (int i = 0; i < shops.length; i++) {
					goTo(i, mask, amount);
				}
			} else {
				goTo(shops.length - 1, mask, amount);
			}
		}

		private void goTo(int shop, int mask, double amount) {
			shops[shop].addEvent(false, mask, amount + route[shop]);
		}
		
		public void addEvent(boolean pers, int mask, double amount) {
			double[] amounts = getAmounts(pers);
			if (amount < amounts[mask] - 1e-10) {
				amounts[mask] = amount;
				events.add(new Event(this, pers, mask, amount));
			}
		}

		private double[] getAmounts(boolean pers) {
			return pers ? minPersAmount : minAmount;
		}
	}
	
	static class Event implements Comparable<Event> {
		boolean pers;
		double amount; 
		Shop shop;
		int mask;
		
		public Event(Shop shop, boolean pers, int mask, double amount) {
			this.pers = pers;
			this.shop = shop;
			this.mask = mask;
			this.amount = amount;
		}

		@Override
		public int compareTo(Event that) {
			return Double.compare(this.amount, that.amount);
		}
		
		private void process() {
			shop.process(pers, mask, amount);
		}
	}
	
	private int set(int mask, int i) {
		return mask | (1 << i);
	}

	private boolean isSet(int mask, int i) {
		return ((mask >> i) & 1) != 0;
	}

	Shop[] shops;
	int persMask;
	Queue<Event> events;
	
	public void run() {
		int itemCount = in.nextInt();
		int shopCount = in.nextInt();
		double gasPrice = in.nextDouble();
					
		List<String> items = new ArrayList<String>();
		for (int i = 0; i < itemCount; i++) {
			String item = in.next();
			if (item.endsWith("!")) {
				persMask = set(persMask, i);
				item = item.substring(0, item.length() - 1);
			}
			items.add(item);
		}
		
		shops = new Shop[shopCount + 1];
		for (int i = 0; i < shopCount; i++) {
			double x = in.nextDouble();
			double y = in.nextDouble(); 
			Shop shop = new Shop(x, y, itemCount);
			StringTokenizer t = new StringTokenizer(in.nextLine(), " :");
			while (t.hasMoreTokens()) {
				String item = t.nextToken();
				int itemIndex = items.indexOf(item);
				shop.prices[itemIndex] = Integer.parseInt(t.nextToken());
			}
			shops[i] = shop;
		}
		
		Shop home = new Shop(0, 0, itemCount);
		shops[shopCount] = home;
		
		for (int i = 0; i < shopCount + 1; i++) {
			double[] route = new double[shopCount + 1];
			for (int j = 0; j < shopCount + 1; j++) {
				route[j] = gasPrice * Math.hypot(shops[i].x - shops[j].x, shops[i].y - shops[j].y); 
			}
			shops[i].route = route;
		}
		
		events = new PriorityQueue<Event>();
		home.addEvent(false, 0, 0);
		while (!events.isEmpty()) {
			Event event = events.poll();
			event.process();
		}
		
		int allItems = (1 << itemCount) - 1;
		double result = home.minAmount[allItems];
		System.out.format("Case #%d: %.7f\n", test, result);
	}
		
	// --------------------- Stub ------------------------------
	
	private final Scanner in;
	private final int test;
	
	public D13(Scanner in, int test) {
		this.in = in;
		this.test = test;
	}

	public static void main(String[] args) {
		Locale.setDefault(Locale.US);
		Scanner in = new Scanner(System.in);
		
		int tests = in.nextInt();
		
		for (int test = 0; test < tests; test++) {
			new D13(in, test + 1).run();
		}
	}
}
