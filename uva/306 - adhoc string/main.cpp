#include "stdio.h"
#include <iostream> 
#include <vector>

using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)
 
typedef vector<int> vi;
typedef vector<vi> vvi;

const int MAX_KEYVAL_SIZE = 201;
int keyValues[201];
int N;



vvi cycles;

//for each key position, which cycle (index in cycles) does it belong too
vi cycleIds;
//for each key position, the offset in the cycle (index in cycles[cycleId]) does it belong too
vi cycleOffsets;

string encodeStr( const string& str)
{
	//printf("Str len %d N %d\n", str.length(), N);
	
		
	string ret(str);
	//puts("th");
	
	for(int i = 0; i < str.length(); ++i)
	{
		//printf("%d = %d\n", keyValues[i] - 1, i);
		ret[ keyValues[i]  ] = str[i];
	}
	return ret;
}

void findCycles()
{
	cycles.clear();
	cycleIds.assign(N, -1);
	cycleOffsets.assign(N, -1);
	
	for(int i = 0; i < N; ++i)
	{
		int cycleId = cycleIds[i];
		if (cycleId != -1)
			continue;
			
		cycleId = cycles.size();
		cycles.push_back( vi() );
		
		//cycles[cycleId].push_back( i );
		int cur = i;
		int start = i;
		printf("Finding cycle id=%d start i = %d\n", cycleId, i);
		while( true )
		{
			printf("Next elem %d in cycle %d  offset %d\n", cur, cycleId, cycles[cycleId].size());
			cycles[cycleId].push_back(cur);
			cycleIds[cur] = cycleId;
			cycleOffsets[cur] = cycles[cycleId].size() - 1;
			
			cur = keyValues[ cur ];
			
			if (cur == cycles[cycleId][0])
				break;
				
			
		}
	}
	
}

int main()
{
	int t = 0;
	while(scanf("%d", &N) == 1 && N)
	{
		if (t++)
			puts("");
			
		for(int i = 0; i < N; ++i)
		{
			scanf("%d", &keyValues[i]);
			//convert to 0 index
			--keyValues[i];
		}
		
		findCycles();
		
		int k;
		while(1 == scanf("%d", &k) && k)
		{
			string str;
			getline(cin, str);
			
			//Get rid of leading space
			str.erase(str.begin());
			//cout << "[" << str <<  "]" << endl;
			
			if (str.length() < N)
				str.append( N - str.length(), ' ');
			
			const string orig = str;
			
			for(int i = 0; i < str.length(); ++i)
			{	
				int cycleId = cycleIds[i];
				int cycleOffset = cycleOffsets[i];
				int posIdx = ( cycles[cycleId].size() + k + cycleOffset) % cycles[cycleId].size();
				int pos = cycles[cycleId][posIdx];
				printf("i=%d cycleId=%d offset=%d k=%d posIdx=%d pos=%d\n",
					i, cycleId, cycleOffset, k, posIdx, pos);
				str[ pos ] = orig[i];
			}
			
			cout << "String fast [" << str << "]" << endl;
			
			str = orig;
			
			
			for(int i = k-1; i >= 0; --i)
			{
				str = encodeStr(str);
				//if (str == orig)
				{
					//int cycleLen = k-i ;
					//printf("Found cycle len %d at i=%d\n", cycleLen, i);
					//i %= cycleLen; 
					//printf("i = %d\n", i);
					
				}
				//printf("k=%d str = [%s]\n", k, str.c_str());
			}
			
			//cout << str << endl;
			cout << "brute force [" << str <<  "]" << endl;
		}
		
		
	}

	return 0;
}