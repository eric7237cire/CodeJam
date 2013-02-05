//#define NDEBUG

#include <vector>
#include <iostream>
#include <queue>
#include <algorithm>
#include <ctime>
#include <set>
#include <sstream>
#include <fstream>
#include <boost/algorithm/string.hpp>
#include <boost/tuple/tuple.hpp>
#include <boost/pending/relaxed_heap.hpp>
#include <boost/pending/mutable_queue.hpp>
#include <boost/pending/fibonacci_heap.hpp>
#include <cmath>



using namespace std;
using namespace boost;

int numPopQCalls = 0;
int numAddQCalls = 0;
int numExtraCalls = 0;
int numFailedCostCheck = 0;

#define DBG_COUT true ? cout : cout

ifstream file;

#define cin file

#define RELAXED_HEAP2

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
  }
};


enum {
  QK_LOCATION,
  QK_ITEMS_BOUGHT,
  QK_COST,
  QK_HAS_PURCHASED,  
  QK_JUST_BOUGHT_PERISHABLE
};
//location, groc, cost, hasPurchased, justBoughtPerishable
typedef boost::tuple<unsigned short, unsigned short, double, int, bool> QueueKey_t;

ostream& operator<<(ostream& os, QueueKey_t* qk)
{
  os << "Location " << qk->get<QK_LOCATION>() 
  << " Items Bought " << qk->get<QK_ITEMS_BOUGHT>() 
  << " Cost " << qk->get<QK_COST>() 
  << " has purch " << qk->get<QK_HAS_PURCHASED>()
  << " jsut perish " << qk->get<QK_JUST_BOUGHT_PERISHABLE>();
  return os;
}

struct KeyCompare_t
{
  inline bool operator()(QueueKey_t* k1, QueueKey_t* k2) const
  {
    return k1->get<QK_COST>() > k2->get<QK_COST>();
  }
};


static const int MAX_ITEMS = 15;
static const int MAX_LOCATIONS = 51;

//How much items cost.  10011 matches 1st 3rd 4th tiems value is the total cost
double item_costs[MAX_LOCATIONS][1 << MAX_ITEMS];

//Represents the minimum cost currently calculated to some location, # of items purchased, and if the next node must be home (ie, some perishable item purchased)
double min_cost[MAX_LOCATIONS][1 << MAX_ITEMS][2];

//Distance between each location
double distCache[MAX_LOCATIONS][MAX_LOCATIONS];

#ifdef RELAXED_HEAP
bool in_q[MAX_LOCATIONS][1 << MAX_ITEMS];
#endif



struct PathFinder
{
  
  vector<QueueKey_t*> q;
  
  double gasPrice;

  const vector<Store> storeList;
  const GrocList_t grocVector;

  vector<int> storeItems;
  
  static const int MAX_STORES = 50;
  static const int MAX_ITEMS = 15;

  PathFinder(const vector<Store>& storeList_, const GrocList_t& grocVec, double gasPrice_) :
	  gasPrice(gasPrice_), grocVector(grocVec), storeList(storeList_),
	  storeItems(storeList_.size(), 0)
  {
    q.reserve(storeList.size() * 1 << grocVec.size());
   
    for (int s = 0; s < storeList.size(); ++s)
    {
      storeItems[s] = storeList[s].items;
      
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
        item_costs[s][permNum] = cost;

        //10011, 10010 & 10011 = 10010, 10001, 01111 & 10011 =00011 magic!
      }
    }


    for (int i = 0; i < MAX_LOCATIONS; ++i)
      for(int j = 0; j < 1 << 15; ++j)
      {
        min_cost[i][j][0] = 1E100;
        min_cost[i][j][1] = 1E100;
        
#ifdef RELAXED_HEAP
        in_q[i][j] = false;
#endif
      }

    //memset(d)
  }

	inline void addQ (double newCost, int locIndex, int groc, int hasPurchased, bool justBoughtPerishableGood)
	{
	  ++numAddQCalls;
	  
	  double& oldCost = min_cost[locIndex][groc][ justBoughtPerishableGood ? 1 : 0];
	  
		if (newCost < oldCost) // && (!justBoughtPerishableGood || newCost < min_cost[locIndex][groc][0])) {
		{
		  //q.erase(make_tuple(locIndex, groc, cache[locIndex][groc]));
		  
          
			oldCost = newCost;
			//cache[locIndex][groc] = newCost;
		  q.push_back(new QueueKey_t(locIndex, groc, newCost, hasPurchased, justBoughtPerishableGood));
		  DBG_COUT << "ADDQ: " << *q.rbegin() << endl;
		  push_heap(q.begin(), q.end(), KeyCompare_t());

		  //printQ();
		} else {
		  ++numFailedCostCheck;
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

		const int homeIndex = storeList.size();

		addQ(0, homeIndex, (1 << grocVector.size()) - 1, 0, false);

	  while (!q.empty())
    {
   
      QueueKey_t* minQItem = q.front();
      pop_heap(q.begin(), q.end(), KeyCompare_t());
      q.pop_back();

      const double cost = minQItem->get<2>();
      const int groc = minQItem->get<1>();
      const int curLocIndex = minQItem->get<0>();
      const bool justBoughtPerishableGood = minQItem->get<QK_JUST_BOUGHT_PERISHABLE>();

	    //printQ();
		
      DBG_COUT << "Popping: " << minQItem << endl;
      
      if (cost > min_cost[curLocIndex][groc][justBoughtPerishableGood ? 1 : 0]) {
        ++numExtraCalls;
        DBG_COUT << endl 
        << "Cost: " << cost << " min cost " 
        << min_cost[curLocIndex][groc][justBoughtPerishableGood ? 1 : 0] << " Node: " << minQItem << endl;
        
        //continue;
      }
	    
	    //delete minQItem;

	  	const int hasPurchased = minQItem->get<QK_HAS_PURCHASED>();

      ++numPopQCalls;
			
			if (groc == 0 && curLocIndex == homeIndex)
				return cost;

			//If we are at home or have already bought items from the current location
			if (curLocIndex == homeIndex || (hasPurchased & (1 << curLocIndex))) {
			  if (justBoughtPerishableGood || groc == 0) {
			    assert(homeIndex != curLocIndex);
			    addQ(cost + distCache[homeIndex][curLocIndex], homeIndex, groc, hasPurchased, false );
			  } else {
          for (int locIndex = 0; locIndex < homeIndex; ++locIndex) {
            if (locIndex != curLocIndex && !(hasPurchased & (1 << locIndex)) && storeItems[locIndex] & groc) {
              //Visited means purchased items from, so don't set it yet
              addQ(cost + distCache[curLocIndex][locIndex], locIndex, groc, hasPurchased, false );
            }
          }
			  }
			} else {
				int canBuy = groc & storeItems[curLocIndex];

				for (int buying = canBuy; buying; buying = (buying - 1) & canBuy)
				{				  
					addQ(item_costs[curLocIndex][buying] + cost,
													curLocIndex,
													groc & ~buying,
													hasPurchased | (1 << curLocIndex),
													(isPerishableMask & buying) );
					
				}
			}
    }
	}
};


int main(int argc, char** args)
{
  if (argc <= 1) {
    file.open("D-small-practice.in", ifstream::in);
  } else {
    DBG_COUT << "Opening " << args[1] << endl;
    file.open(args[1], ifstream::in);
  }
  
  int testCases;
  cin >> testCases;

  std::clock_t start = std::clock();

  for(int testCase = 1; testCase <= testCases; ++testCase)
  {
    int num_items, num_stores;
    double price_of_gas;

    cin >> num_items >> num_stores >> price_of_gas;
    cin.ignore();

    string grocListInput;
    getline(cin, grocListInput);
    
    vector<GroceryItem> grocList = GroceryItem::createGrocList(grocListInput);

    vector<Store> stores;

    for(int i = 0; i < num_stores; ++i) {
      string storeInput;
      getline(cin, storeInput);
      stores.push_back(Store(storeInput, grocList));
    }

    PathFinder pf(stores, grocList, price_of_gas);

    double cost = pf.findPath();
    printf("Case #%i: %.7f\n", testCase, cost);
  }

  double totalTimeSec = ( std::clock() - start ) / (double)CLOCKS_PER_SEC ;
  /*
  cout<< "Time taken " << ( ( std::clock() - start ) / (double)CLOCKS_PER_SEC ) <<'\n';
  printf("AddQ calls: %d\n", numAddQCalls);
  printf("PopQ calls: %d\n", numPopQCalls);
  printf("Extra calls: %d\n", numExtraCalls);
  printf("Failed cost check: %d\n", numFailedCostCheck);
  
  std::cout << "cost per popq " << (1000.0 * totalTimeSec / numPopQCalls) << " ms " << endl;
  std::cout << "cost per popq " << (1E6 * totalTimeSec / numPopQCalls) << " ns " << endl;

  std::cout << "cost per addq " << (1000.0 * totalTimeSec / numAddQCalls) << " ms " << endl;
  std::cout << "cost per addq " << (1E6 * totalTimeSec / numAddQCalls) << " ns " << endl;
    */
  return 0;

  //all total calls: 88,175,062
  //total calls:      2,674,652
  //Extra calls:      8,780,558
}

