/*
  Name: UVA 871
  Author: zoom
  Date: 23/06/11
*/
using namespace std;
#include<iostream>
#include<cstdio>
#include<cstring>
#include<algorithm>
#include<vector>
#include<limits>
#include<cmath>
#include<map>
#define LLU long long unsigned int
#define LLD long long double
#define FOR(i,N) for(int i=0;i<(N);i++)
vector<string> inp;
int SIZE,len,cnt,NO;
int xmyarr[]={-1, 0, 1,1,1,0,-1,-1};
int ymyarr[]={-1,-1,-1,0,1,1, 1, 0};
void calc();
int main()
{
    int cases;
    string str;
    NO=1;
    scanf("%d\n\n",&cases);
    while(cases--)
    {
        inp.clear();
        while(getline(cin,str))
        {
            if(str=="") break;
            inp.push_back(str);
        }
        calc();
    }
}
void fill(int x, int y)
{
    if(x<0 || x>=SIZE || y<0 || y>=len || inp[x][y]=='0') return;
    inp[x][y]='0';
    cnt++;
    FOR(i,8)
    fill(x+xmyarr[i],y+ymyarr[i]);
}
void calc()
{
    int ANS=0;
    len=inp[0].length();
    SIZE=inp.size();
    FOR(i,SIZE)
    FOR(j,len)
    {
        cnt=0;
        fill(i,j);
        ANS=max(ANS,cnt);
    }
    if(NO++>1)
    cout<<endl;
    cout<<ANS<<endl;
}