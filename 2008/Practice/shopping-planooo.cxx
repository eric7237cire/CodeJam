#include <vector>
#include <iostream>
#include <queue>
#include <algorithm>
#include <ctime>
#include <set>
#include <sstream>
#include <boost/algorithm/string.hpp>
#include <boost/tuple/tuple.hpp>
#include <cmath>
#include <stdio.h>

using namespace std;
using namespace boost;

int numCalls;
int numAllCalls;

struct GroceryItem
{
  bool perishable;
  string name;

  GroceryItem(bool p, string n) :
    perishable(p), name(n)
  {
  }

  GroceryItem(string fullName)
  {
    if (fullName[fullName.length()-1] == '!') {
      perishable = true;
      name = string(fullName, 0, fullName.length() - 1);
    }
    else {
			perishable = false;
			name = fullName;
    }

    //cout << "Fullname " << fullName << perishable << name << endl;
  }

  static vector<GroceryItem> createGrocList(string inputLine) {
    vector<GroceryItem> ret;

    vector<string> items;

    boost::split(items, inputLine, boost::is_any_of("\t "));

    for(vector<string>::const_iterator it = items.begin(); it != items.end(); ++it)
    {
      ret.push_back(GroceryItem(*it));
    }

    return ret;
  }

  bool operator==(const GroceryItem& other)
  {
    return name == other.name;
  }

};

typedef vector<GroceryItem> GrocList_t;

struct Location
{
  double x, y;

  Location(double _x, double _y) : x(_x), y(_y)
  {

  }

  Location()
  {}
};

static const int MAX_ITEMS = 15;
static const int MAX_LOCATIONS = 50;

struct Store: public Location
{
  int items;
  vector<int> itemCosts;

  Store() : items(0) {};

  Store(double x, double y, const vector<int>& iCosts, int _items) :
  Location(x, y), itemCosts(iCosts), items(_items)
  {

  }

  Store(string inputLine, GrocList_t grocList) : itemCosts(grocList.size(), 0), items(0)
  {
		istringstream is(inputLine);

		is >> x;
		is >> y;
		is.ignore();

    while (!is == 0) //for(int i = 2; i < storeInput.size(); ++i)
    {
      string name;
      getline(is, name, ':');
      int cost;
      is >> cost;
      is.ignore();

      int grocItemIdx = distance(grocList.begin(), find(grocList.begin(), grocList.end(), name));
    	itemCosts[grocItemIdx] = cost;
    	//cout << "Name " << name << " costs " << cost << " grocItemIdx " << grocItemIdx << endl;
    }

    for(int idx = 0; idx < grocList.size(); ++idx)
		{
			if (itemCosts[idx] != 0)
				items |= (1 << idx);
		}

    //cout << "Items is " << items << endl;


  }

};

//location, groc, cost
typedef tuple<int, int, double> QueueKey_t;

struct KeyCompare_t
{
  inline bool operator()(const QueueKey_t& k1, const QueueKey_t& k2) const
  {
    if (k1.get<2>() == k2.get<2>()) {
      if (k1.get<0>() == k2.get<0>()) {
        return k1.get<1>() < k2.get<1>();
      } else {
        return k1.get<0>() < k2.get<0>();
      }
    } else {
      return k1.get<2>() < k2.get<2>();
    }
  }
};

struct KeyCompare2_t
{
  inline bool operator()(QueueKey_t* k1, QueueKey_t* k2) const
  {
    return k1->get<2>() > k2->get<2>();
  }
};

struct PathFinder
{
  set<QueueKey_t, KeyCompare_t> q;
  vector<QueueKey_t*> q2;
  priority_queue<QueueKey_t*, vector<QueueKey_t*>, KeyCompare2_t> q3;
  double gasPrice;

  const vector<Store> storeList;
  const GrocList_t grocVector;

  vector<int> storeItems;
  vector< vector< double> > costs;

  static const int MAX_STORES = 50;
  static const int MAX_ITEMS = 15;

  //double cache[MAX_STORES+1][1 << MAX_ITEMS];
  vector< vector< double> > cache;

  PathFinder(const vector<Store>& storeList_, const GrocList_t& grocVec, double gasPrice_) :
	  gasPrice(gasPrice_), grocVector(grocVec), storeList(storeList_),
	  storeItems(storeList_.size(), 0), costs(storeList_.size())
  {

    //cout << "Cost size " << costs.size() << endl;
    //cout << "Store list size " << storeList.size() << endl;

    for (int s = 0; s < storeList.size(); ++s)
    {
      storeItems[s] = storeList[s].items;
      //cout << storeItems[s] << " is the mask " << endl;
      costs[s].resize(1 << grocVector.size(), 0);

      //cout << "Costs sd size " << costs[s].size() << endl;

      //want to add cost data for each combination of items
      //itemsMask = 10011 for items 1, 4, 5
      const int itemsMask = storeItems[s];
      for(int permNum = itemsMask; permNum > 0; permNum = (permNum - 1) & itemsMask)
      {
        double cost = 0;

        for (int itemIdx = 0; itemIdx < grocVector.size(); ++itemIdx) {
          if (permNum & 1 << itemIdx) {
            cost += storeList[s].itemCosts[itemIdx];
          }
        }

        //cout << s << " -- " << permNum << " " << cost << endl;
        costs[s][permNum] = cost;

        //10011, 10010 & 10011 = 10010, 10001, 01111 & 10011 =00011 magic!
      }
    }

    cache.resize(storeList.size() + 1, vector<double>(1 << grocVector.size(), 1E100));
    //memset(d)
  }

	inline void addQ (double newCost, int locIndex, int groc)
	{

		if (newCost < cache[locIndex][groc]) {
		  //q.erase(make_tuple(locIndex, groc, cache[locIndex][groc]));
		  //cout << "ADDQ Cost: " << newCost << " Groc: " << groc << " loc " << locIndex << endl;
			cache[locIndex][groc] = newCost;
			//q.insert(make_tuple(locIndex, groc, newCost));
			//q2.push_back(make_tuple(locIndex, groc, newCost));

			//q2.push_back(new QueueKey_t(locIndex, groc, newCost));
			//push_heap(q2.begin(), q2.end(), KeyCompare2_t());

			q3.push(new QueueKey_t(locIndex, groc, newCost));

			//printQ();
		}
  }

//	void printQ()
//	{
//	  cout << "Queue " << endl;
//	  for (vector<QueueKey_t>::const_iterator it = q2.begin(); it != q2.end(); ++it)
//	  {
//	    QueueKey_t qkey = *it;
//	    cout << "Loc " << qkey.get<0>() << " groc " << qkey.get<1>() << " cost " << qkey.get<2>() << endl;
//	  }
//	}

	double square(double x)
	{
	  return x * x;
	}

	double findPath()
	{
		vector<Location> locs(storeList.begin(), storeList.end());
		locs.push_back( Location(0,0) );

		//vector< vector <double> > distCache( storeList.size() + 1, vector<double>(storeList.size() + 1));
		double distCache[storeList.size() + 1][storeList.size() + 1];

		for (int locOuter = 0; locOuter < locs.size(); ++locOuter)
			for (int locInner = locOuter; locInner < locs.size(); ++locInner) {
				distCache[locOuter][locInner] = gasPrice * sqrt((square(locs[locOuter].x - locs[locInner].x) + square(locs[locOuter].y - locs[locInner].y)) );
				distCache[locInner][locOuter] = distCache[locOuter][locInner];
			}

		int isPerishableMask = 0;
		for (int idx = 0; idx < grocVector.size(); ++idx) {
			if (grocVector[idx].perishable)
				isPerishableMask |= (1 << idx);
		}

		int groc = (1 << grocVector.size()) - 1;
		int homeIndex = storeList.size();

		addQ(0, homeIndex, groc);

	  while (!q3.empty())
    {
			//QueueKey_t minQItem = *q.begin();

//	    QueueKey_t* minQItem = q2.front();
//	    pop_heap(q2.begin(), q2.end(), KeyCompare2_t());
//	    q2.pop_back();

	    QueueKey_t* minQItem = q3.top();
      q3.pop();

	    //printQ();
			//q.erase(q.begin());

	    double cost = minQItem->get<2>();
      int groc = minQItem->get<1>();
      int curLocIndex = minQItem->get<0>();

      //delete minQItem;

	  	//double cost = minQItem.get<2>();
	  	//int groc = minQItem.get<1>();
			//int curLocIndex = minQItem.get<0>();

      ++numAllCalls;

			if (cost > cache[curLocIndex][groc]) {
			  continue;
			}

			++numCalls;

			//cout << "Popping Cost: " << cost << " Groc: " << groc << " Cur loc " << curLocIndex << endl;

			if (groc == 0 && curLocIndex == homeIndex)
				return cost;

			if (curLocIndex == homeIndex) {
				for (int locIndex = 0; locIndex < homeIndex; ++locIndex) {
				  //cout << storeItems[locIndex] << " store " << locIndex << endl;
					if (storeItems[locIndex] & groc)
						addQ(cost + distCache[locIndex][homeIndex], locIndex, groc);
        }
			} else {
				int canBuy = groc & storeItems[curLocIndex];

				for (int buying = canBuy; buying > 0; buying = (buying - 1) & canBuy)
				{

		  	//#canBuy 1011, iterate 1011 1010 1001 1000 0111->011
		  	//#the mask basically gets you only the ones you need
		  		double buyingCost = costs[curLocIndex][buying] + cost;
					int newGroc = groc & ~buying;

					if (buyingCost < cache[curLocIndex][newGroc])
					{
						if (newGroc == 0 || (isPerishableMask & buying) ) {
							addQ(buyingCost + distCache[curLocIndex][homeIndex],
																			homeIndex,
																			newGroc);
						}
						else {
							//#Can only set this here as if there is a perishable item, it is
							//#not the same thing.  The cache only has costs of being in a particular loc/remaining items
							//#with the freedom to go elsewhere

							cache[curLocIndex][newGroc] = buyingCost;

			  			for (int locIndex = 0; locIndex < homeIndex; ++locIndex) {
			  			  //cout << storeItems[locIndex] << " store " << locIndex << endl;
			  				if (locIndex != curLocIndex &&	(storeItems[locIndex] & newGroc))
									addQ(buyingCost + distCache[curLocIndex][locIndex],
													locIndex,
													newGroc);
			  			}

						}
					}
				}
			}
    }
	}
};


int main(int argc, char** args)
{
  int testCases;
  cin >> testCases;

  std::clock_t start = std::clock();

  for(int testCase = 1; testCase <= testCases; ++testCase)
  {
    int num_items, num_stores;
    double price_of_gas;

    cin >> num_items >> num_stores >> price_of_gas;
    cin.ignore();

    //cout << "Number of items: " << num_items << endl;

    string grocListInput;

    getline(cin, grocListInput);
    //cout << "Groc list input " << grocListInput << endl;

    vector<GroceryItem> grocList = GroceryItem::createGrocList(grocListInput);

    vector<Store> stores;

    for(int i = 0; i < num_stores; ++i) {
      string storeInput;
      getline(cin, storeInput);
      //cout << "Store input " << storeInput << endl;
      stores.push_back(Store(storeInput, grocList));
    }

    PathFinder pf(stores, grocList, price_of_gas);

    double cost = pf.findPath();

    printf("Case #%i: %.7f\n", testCase, cost);

  }

  cout<< "Time taken " << ( ( std::clock() - start ) / (double)CLOCKS_PER_SEC ) <<'\n';
  printf("all total calls: %d\n", numAllCalls);
  printf("total calls: %d\n", numCalls);
  return 0;

}

