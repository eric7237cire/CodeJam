using System;
using System.Globalization;
using System.Collections.Generic;

public class shopping
{
    static Store[] store;
    static Item[] list;
    static int price_of_gas;
    static bool[] itemdone;
    static bool[, ,] done;
    static double[, ,] mem;
    static double[,] disthome, dist;

    static double rec(int numdone, int s, bool perishable)
    {
        if (numdone == list.Length)
            return s == -1 ? 0 : price_of_gas * store[s].HomeDist;

        int a = 0;
        for (int i = 0; i < list.Length; i++)
            if (itemdone[i])
                a |= 1 << i;
        int b = s == -1 ? store.Length : s;
        int c = perishable ? 1 : 0;
        if (done[a, b, c])
            return mem[a, b, c];
        done[a, b, c] = true;
        mem[a, b, c] = double.MaxValue;

        for (int j = 0; j < list.Length; j++)
        {
            if (!itemdone[j])
            {
                itemdone[j] = true;
                for (int i = 0; i < store.Length; i++)
                {
                    if (!store[i].HasItem(j))
                        continue;

                    int itemprice = store[i].ItemPrice(j);
                    if (s == i)
                    { // Stay at same store
                        mem[a, b, c] = Math.Min(mem[a, b, c], itemprice + rec(numdone + 1, i, perishable || list[j].perishable));
                    }
                    else
                    { // Go to another store
                        if (perishable)
                        { // First go home
                            mem[a, b, c] = Math.Min(mem[a, b, c], itemprice + disthome[s, i] + rec(numdone + 1, i, list[j].perishable));
                        }
                        else
                        { // Take shortest route to other store
                            double cost;
                            if (s != -1)
                                cost = dist[i, s];
                            else
                                cost = price_of_gas * store[i].HomeDist;
                            mem[a, b, c] = Math.Min(mem[a, b, c], itemprice + cost + rec(numdone + 1, i, list[j].perishable));
                        }
                    }
                }
                itemdone[j] = false;
            }
        }
        return mem[a, b, c];
    }

    public static void Main(string[] args)
    {
        int N = int.Parse(Console.ReadLine());
        long begin = DateTime.Now.Ticks;
        for (int i = 0; i < N; i++)
        {
            long now = DateTime.Now.Ticks;
            string[] parts = Console.ReadLine().Split();
            int num_items = int.Parse(parts[0]);
            int num_stores = int.Parse(parts[1]);
            price_of_gas = int.Parse(parts[2]);
            list = new Item[num_items];
            parts = Console.ReadLine().Split();
            for (int j = 0; j < num_items; j++)
                list[j] = new Item(parts[j]);
            store = new Store[num_stores];
            for (int j = 0; j < num_stores; j++)
                store[j] = new Store(Console.ReadLine(), list);

            done = new bool[1 << num_items, store.Length + 1, 2];
            mem = new double[1 << num_items, store.Length + 1, 2];
            itemdone = new bool[num_items + 1];
            disthome = new double[store.Length, store.Length];
            dist = new double[store.Length, store.Length];
            for (int j = 0; j < store.Length; j++)
            {
                for (int k = 0; k < store.Length; k++)
                {
                    disthome[j, k] = price_of_gas * store[j].HomeDist + price_of_gas * store[k].HomeDist;
                    int dx = store[j].X - store[k].X;
                    int dy = store[j].Y - store[k].Y;
                    dist[j, k] = price_of_gas * Math.Sqrt(dx * dx + dy * dy);
                }
            }


            double minPrice = rec(0, -1, false);
            Console.WriteLine("Case #" + (i + 1) + ": " + Math.Round(minPrice, 7).ToString("#.0000000", CultureInfo.InvariantCulture));
            Console.Error.WriteLine(((DateTime.Now.Ticks - now) / TimeSpan.TicksPerMillisecond) + " ms");
        }
        Console.Error.WriteLine("Total: " + ((DateTime.Now.Ticks - begin) / TimeSpan.TicksPerMillisecond) + " ms");
    }

    public class Item
    {
        public string item;
        public bool perishable;

        public Item(string s)
        {
            if (s.EndsWith("!"))
            {
                item = s.Substring(0, s.Length - 1);
                perishable = true;
            }
            else
            {
                item = s;
                perishable = false;
            }
        }
    }

    public class Store
    {
        public int X, Y;
        public double HomeDist;
        bool[] items;
        int[] price;

        public Store(string input, Item[] list)
        {
            string[] parts = input.Split(" :".ToCharArray());
            X = int.Parse(parts[0]);
            Y = int.Parse(parts[1]);
            HomeDist = Math.Sqrt(X * X + Y * Y);
            items = new bool[list.Length];
            price = new int[list.Length];
            for (int i = 2; i < parts.Length; i += 2)
            {
                int j = 0;
                while (list[j].item != parts[i])
                    j++;
                items[j] = true;
                price[j] = int.Parse(parts[i + 1]);
            }
        }

        public bool HasItem(int item)
        {
            return items[item];
        }

        public int ItemPrice(int item)
        {
            return price[item];
        }
    }
}