#include <cstdio>

#include <vector>

#include <utility>

#include <algorithm>

 

using namespace std;

 

typedef pair <int, int> ii;

typedef vector <int> vi;

vi id; vi sz;

 

void initSet(int n)

{

        id.assign(n, 0);

        sz.assign(n, 1);

        for(int i = 0; i < n; ++i) id[i] = i;

}

 

int findSet(int i)

{

        return (i == id[i]) ? i : id[i] = findSet(id[i]);

}

 

bool isSameSet(int p, int q)

{

        return findSet(p) == findSet(q);

}

 

void unionSet(int p, int q)

{

        int i = findSet(p);

        int j = findSet(q);

        if(sz[i] > sz[j])

        {

                id[j] = i;

                sz[i] += sz[j];

        }

        else

        {

                id[i] = j;

                sz[j] += sz[i];

        }

}

 

int main()

{

        int m; int n;

        while(true)

        {

                scanf("%d%d", &m, &n);

                if(m == 0 && n == 0) break;

                vector < pair <int, ii> > edgeList;

                initSet(m);

                int totalCost = 0;

                for(int i = 0; i < n; ++i)

                {

                        int x, y, weight;

                        scanf("%d%d%d", &x, &y, &weight);

                        edgeList.push_back(make_pair(weight, ii(x, y)));

                        totalCost += weight;

                }

                sort(edgeList.begin(), edgeList.end());

                int mst_cost = 0;

                for(int i = 0; i < n; ++i)

                {

                        pair <int, ii> front = edgeList[i];

                        if(!isSameSet(front.second.first, front.second.second))

                        {

                                mst_cost += front.first;

                                unionSet(front.second.first, front.second.second);

                        }

                }

                printf("%d\n", (totalCost-mst_cost));

        }

        return 0;

}