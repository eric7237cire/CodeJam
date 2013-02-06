import static java.lang.Math.min;
import static java.lang.Math.sqrt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
public class Shopping {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BufferedReader input=null;
		Writer output=null;
		try {
			
			
			input =  new BufferedReader(new FileReader("D-small.in"));
			output = new BufferedWriter(new FileWriter("D-small.out"));
			String line = input.readLine(); 
			int num = Integer.parseInt(line);
			for(int i=0; i<num; i++)
			{
				Shopping x = new Shopping();
				line = input.readLine(); 
				String info[] = line.split(" ");
				
				int numStore=Integer.parseInt(info[1]);
				int priceOfGas = Integer.parseInt(info[2]);
				
				String itemList[] = input.readLine().split(" ");
				String stores[]=new String[numStore];
				for(int j=0; j<numStore; j++)
				{
					stores[j]=input.readLine();
				}
				String res = x.convert(itemList, stores, priceOfGas);
				output.write( res+"\n" );
			}
			input.close();
			output.close();
		}
		catch (IOException ex){
			ex.printStackTrace();
		}
		

	}
	static int count=1;
	String convert(String[]itemList, String[]localStores, int priceOfGas)
	{
		Arrays.sort(itemList);
		this.itemList=itemList;
		N = itemList.length;
		stores = new Store[localStores.length+1];
		cost = new double[localStores.length+1][localStores.length+1];
		this.priceOfGas = priceOfGas;
		//make the home location as a dummy store
		stores[0]=new Store();

		for (int i = 0; i < localStores.length; i++) {
			String info[] = localStores[i].split(" ");
			Store store = new Store();
			store.x=Integer.parseInt(info[0]);
			store.y=Integer.parseInt(info[1]);
			
			//list of items
			for(int j=2; j<info.length; j++)
			{
				//list of items
				String stock[] = info[j].split(":");
				if(Arrays.binarySearch(itemList, stock[0])>=0)
				{
					//non perishable
					int ind = Arrays.binarySearch(itemList, stock[0]);
					store.itemList.add(new Item(ind, Integer.parseInt(stock[1]),false, itemList[ind]));
					store.items |= (1<<ind);
				}
				else
				{
					//perishable
					int ind = Arrays.binarySearch(itemList, stock[0]+"!");
					store.itemList.add(new Item(ind, Integer.parseInt(stock[1]),true, itemList[ind]));
					store.items |= (1<<ind);
					store.perishables |= (1<<ind);
				}
			}
			//Collections.sort(store.items);
			//remeber that the first store is home
			stores[i+1]=store;
		}
		memo = new double[1<<itemList.length][stores.length];
		for (int i = 0; i < memo.length; i++) {
			Arrays.fill(memo[i], -1);
		}
		// we have nothing, start from home
		DecimalFormat myFormatter = new DecimalFormat(".0000000");
		String output = myFormatter.format(get(0, 0));

		//System.out.println(get(5, 0));
		return "Case #"+(count++)+": "+output;
		
	}
	
	/**
	 * 
	 * @param mask: after landing at this shop and buying what items do i have
	 * @param loc - index of the shop
	 * @return the answer from this state
	 */
	double get(int mask, int loc)
	{
		//System.out.println(mask+"->"+loc);
		if(mask==(1<<N)-1&&loc==0)
			return 0; // honey, i'm home
		
		if(memo[mask][loc]>-1) return memo[mask][loc];
		
		//from home, you can go only to other shops for buying
		if(loc==0)
		{
			double ret = inf;
			//dont go to home again frm home! you'll never get out of home!!
			for(int i=1; i<stores.length; i++)
			{
				ret = min(ret, get(mask, i)+cost(loc, i));
			}
			return memo[mask][loc]=ret;
		}
		double ret = inf;
		
		int possible = (~mask) & (stores[loc].items);
		for(int shop = possible; shop!=0; shop = ((shop-1)&possible))
		{
			int value=0;
			for(int i=0; i<stores[loc].itemList.size(); i++)
			{
				if(((1<<stores[loc].itemList.get(i).ind)&shop)>0)
				{
					value+=stores[loc].itemList.get(i).price;
				}
			}
			
			
			if((shop&stores[loc].perishables)>0)
			{
				ret = min(ret, get(mask|shop, 0)+value+cost(loc, 0));
			}
			else
			{
				for(int j=0; j<stores.length; j++)
				{
					ret = min(ret, get(mask|shop, j)+value+cost(loc, j));
				}
			}
		}
		return memo[mask][loc]=ret;
	}
	
	//the cost to travel from from to to
	double cost(int from, int to)
	{
		return cost[from][to]>0?cost[from][to]:(cost[from][to]= cost[to][from]= sqrt(1.0*(stores[from].x-stores[to].x)*(stores[from].x-stores[to].x) + 1.0*(stores[from].y-stores[to].y)*(stores[from].y-stores[to].y))*priceOfGas);
	}
	double[][]memo;
	double[][]cost;
	int inf = 100000000;
	Store[] stores;
	String[] itemList;
	int N;
	int priceOfGas;
	class Store
	{
		int x, y;
		ArrayList<Item> itemList = new ArrayList<Item>();
		int items;
		int perishables;
		Item get(int i)
		{
			for (int j = 0; j < itemList.size(); j++) {
				if(itemList.get(j).ind==i)
					return itemList.get(j);
			}
			return null;
		}
	}
	class Item implements Comparable<Item>
	{
		int ind;//index in itemList
		int price=inf;
		boolean perishable;
		String name;
		Item(int ind, int price, boolean perishable, String name)
		{
			this.ind=ind;this.price=price;this.perishable=perishable;this.name=name;
		}
		public int compareTo(Item o) {
			if(perishable)return 1;
			if(o.perishable) return -1;
			return 0;
		}
		
	}

}


