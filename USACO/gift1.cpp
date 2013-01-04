/*
ID: eric7231
PROG: gift1
LANG: C++
*/
#include <iostream>
#include <fstream>
#include <string>
#include <map>
#include <vector>
using namespace std;

int main() {
    ofstream fout ("gift1.out");
    ifstream fin ("gift1.in");
    int NP;
    fin >> NP;
    
    int money[10];
    map<string, int> nameIndex;
    string indexName[10];
    for(int i = 0; i < NP; ++i) 
    {
        string name;
        fin >> name;
        nameIndex[name] = i;
        indexName[i] = name;
        money[i] = 0;
    }
    
    cout << "s1" << endl;
    
    for(int i = 0; i < NP; ++i)
    {
        string name;
        int giveAmt;
        int recipCount;
        fin >> name >> giveAmt >> recipCount;
        cout << "Name " << name << " Giv amt " << giveAmt << endl;
        for(int r = 0; r < recipCount; ++r)
        {
            string recipName;
            fin >> recipName;
            money[nameIndex[recipName]] += giveAmt / recipCount;
            money[nameIndex[name]] -= giveAmt / recipCount;
        }
        
        
    }
    	   
    for(int i = 0; i < NP; ++i)
    {
        fout << indexName[i]  << " " << money[i]   << endl;
    }
    return 0;
}
