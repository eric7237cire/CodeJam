#include <vector>
#include <iostream>
#include <limits>
#include <queue>
#include <algorithm>
#include <ctime>
#include <set>
#include <iomanip>
#include <sstream>
#include <map>
#include <boost/algorithm/string.hpp>
#include <boost/tuple/tuple.hpp>
#include <cmath>
#include <stdio.h>

using namespace std;
using namespace boost;

#define DBG_COUT_0 true ? cout : cout
#define DBG_COUT_1 true ? cout : cout
#define DBG_COUT_2 true ? cout : cout

const int MAX_CITIES = 100;

const int MAX_ROUTE_COST = 100000000;

//Memoize results
int min_distance[MAX_CITIES][MAX_CITIES];

typedef tuple<int, int, int> RoadInfo_t;

enum {
  NEXT_CITY,
  ROAD_INDEX,
  ROAD_LENGTH
};

typedef multimap<int,RoadInfo_t> RoadMap_t;
RoadMap_t roads;

//Adds name to index if needed, returns index in cities map
int getCityIndex(vector<string>& cities, string cityName)
{
  vector<string>::iterator it = find(cities.begin(), cities.end(), cityName);

  if (it == cities.end()) {
    cities.push_back(cityName);
    return cities.size() - 1;
  } else {
    return distance(cities.begin(), it);
  }
}

inline bool isValidDistance(int startCity, int endCity)
{
  if (min_distance[startCity][endCity] >= 0 && min_distance[startCity][endCity] < MAX_ROUTE_COST ) {
    return true;
  } else {
    return false;
  }
}

//Returns min cost between 2 cities, visited is to prevent cycles
int go(int startCity, int endCity, int visited)
{
  if (startCity == endCity) {
    return 0;
  }

  int& dist = min_distance[startCity][endCity];

  //Memoize!
  if (dist > 0) {
    return dist;
  }

  DBG_COUT_1 << "go " << startCity << " " << endCity << " " << visited << endl;

  //Init to a really big value, but must be careful to avoid overflow!
  dist = MAX_ROUTE_COST;

  pair<RoadMap_t::const_iterator, RoadMap_t::const_iterator> neighbors = roads.equal_range(startCity);

  for (RoadMap_t::const_iterator it = neighbors.first; it != neighbors.second; ++it) {
    int nextCity = it->second.get<NEXT_CITY>();
    int roadDistance = it->second.get<ROAD_LENGTH>();

    DBG_COUT_2 << "Considering " << nextCity << endl;
    if (visited & (1 << nextCity)) {
      continue;
    }
    dist = min<int>(dist, roadDistance + go(nextCity, endCity, visited | (1 << nextCity)));
  }

  DBG_COUT_1 << "go " << startCity << " " << endCity << " " << visited << " returning " << dist << endl;
  return dist;
}

typedef vector<RoadInfo_t> Path_t;

struct ShortPaths
{
  int maxCost;
  int numCities;
  vector<Path_t> shortestPaths;
  int endCity;

  void shortest_paths(int startCity, int curCost, const Path_t& curPath)
  {
    //Depth first search, we hit the max so fail!
    if (curCost > maxCost) {
      return;
    }

    DBG_COUT_2 << "--Shortest paths " << startCity << " " << endCity << " " << curCost << endl;

    if (startCity == endCity) {
      //curCost should be less than maxCost
      shortestPaths.push_back(curPath);
      return;
    }

    pair<RoadMap_t::const_iterator, RoadMap_t::const_iterator> neighbors = roads.equal_range(startCity);

    for (RoadMap_t::const_iterator it = neighbors.first; it != neighbors.second; ++it) {
      int nextCity = it->second.get<NEXT_CITY>();
      int roadDistance = it->second.get<ROAD_LENGTH>();

      if (!isValidDistance(nextCity, endCity)) {
        continue;
      }

      //Check that this is on a shortest path
      int minDist = min_distance[nextCity][endCity];

      DBG_COUT_2 << "Considering " << nextCity << " " << minDist << endl;

      Path_t nextPath(curPath);
      nextPath.push_back(it->second);
      shortest_paths(nextCity, curCost + roadDistance, nextPath);
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
    int num_roads;
    string starting_city;
    vector<string> cityNames;

    roads.clear();
    memset(min_distance, -1, sizeof(min_distance));

    cin >> num_roads >> starting_city;

    cityNames.push_back(starting_city);

    min_distance[0][0] = 0;

    for (int roadNum = 0; roadNum < num_roads; ++roadNum)
    {
      string city1, city2;
      int time;
      cin >> city1 >> city2 >> time;

      int city1Idx = getCityIndex(cityNames, city1);
      int city2Idx = getCityIndex(cityNames, city2);

      RoadInfo_t roadInfo = make_tuple(city2Idx, roadNum, time);
      roads.insert(make_pair(city1Idx, roadInfo));

      DBG_COUT_2 << "City " << city1Idx << " to " << city2Idx << " takes " << time << endl;
    }

    for (int city = 1; city < cityNames.size(); ++city)
    {
      min_distance[city][city] = 0;
      go(0, city, 1);
    }

    for(int i = 0; i < cityNames.size(); ++i) {
      DBG_COUT_0 << "Index:[" << i << "] is " << cityNames[i] << endl;
    }

    for(int i = 0; i < cityNames.size(); ++i)
    {
      DBG_COUT_0 << "From city " << setw(2) << i ;

      for(int j = 0; j < cityNames.size(); ++j)
      {
        DBG_COUT_0 << setw(3);
        if ( min_distance[i][j] < 0 || min_distance[i][j] > 10000000) {
          DBG_COUT_0 << "-";
        } else {
          DBG_COUT_0 << min_distance[i][j] ;
        }
      }
      DBG_COUT_0 << endl;
    }

    //Need to know total # of reachable cities
    int numReachable = 0;
    for (int city = 1; city < cityNames.size(); ++city) {
      if (isValidDistance(0, city)) {
        numReachable++;
      }
    }

    vector<double> roadProbabilities(roads.size(), 0.0);

    for (int city = 1; city < cityNames.size(); ++city)
    {

      //ShortPaths::shortestPaths.clear();
      if (!isValidDistance(0,city)) {
        continue;
      }

      ShortPaths sp;
      sp.maxCost = min_distance[0][city];
      sp.numCities = cityNames.size();
      sp.endCity = city;
      Path_t curPath;
      sp.shortest_paths(0, 0, curPath);

      DBG_COUT_0 << "Shortest paths between " << cityNames[0] << " and " << cityNames[city] << endl;

      for (int i = 0; i < sp.shortestPaths.size(); ++i)
      {
        Path_t path = sp.shortestPaths[i];

        DBG_COUT_0 << "Path: 0 ";
        for (int pathStep = 0; pathStep < path.size(); ++pathStep)
        {
          DBG_COUT_2 << "Road index " << path[pathStep].get<ROAD_INDEX>() << " adding " << (1.0 / numReachable / sp.shortestPaths.size() ) << endl;
          roadProbabilities[path[pathStep].get<ROAD_INDEX>()] += 1.0 / numReachable / sp.shortestPaths.size();

          DBG_COUT_0 << setw(3) << path[pathStep].get<NEXT_CITY>();
        }
        DBG_COUT_0 << endl;
      }
    }

    printf("Case #%i: ", testCase);
    for(int i = 0; i < roads.size(); ++i) {
      printf("%.7f ", roadProbabilities[i]);
    }
    cout << endl;

  }

  DBG_COUT_0<< "Time taken " << ( ( std::clock() - start ) / (double)CLOCKS_PER_SEC ) <<'\n';

  return 0;

}

