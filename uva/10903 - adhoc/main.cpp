#include<iostream>
#include<cstdio>
using namespace std;

int n,k;
int w[100];
int l[100];


bool win(string x, string y)
{
	if(x=="rock"&&y=="scissors")return true;
	if(x=="scissors"&&y=="paper")return true;
	if(x=="paper"&&y=="rock")return true;
	return false;
}

int main()
{
	int tc = 0;
	while(cin>>n&&n){
		cin>>k;
		for(int i=0;i<n;i++)
			w[i]=l[i]=0;
		for(int i=0;i<k*n*(n-1)/2;i++){
			int a,b;
			string x,y;
			cin>>a>>x>>b>>y;
			a--;
			b--;
			if(win(x,y)){
				w[a]++;
				l[b]++;
			};
			if(win(y,x)){
				w[b]++;
				l[a]++;
			}
		}
		if(tc)cout<<endl;
		tc++;
		for(int i=0;i<n;i++){
			if(w[i]+l[i])printf("%.3f\n",1.0*w[i]/(w[i]+l[i]));
			else printf("-\n");
		}
	}
	return 0;
}