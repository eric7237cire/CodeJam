#include<iostream>
#include<cstdio>
#include<cstring>
using namespace std;
char a[5][5];
char s[5][5]={
	'1','1','1','1','1',
	'0','1','1','1','1',
	'0','0',' ','1','1',
	'0','0','0','0','1',
	'0','0','0','0','0',
};

int step;

int dr[8]={ 1, 2, 2, 1,-1,-2,-2,-1,};
int dc[8]={-2,-1, 1, 2, 2, 1,-1,-2,};

bool isValid(int r, int c)
{
	if(r>=5||r<0)return false;
	if(c>=5||c<0)return false;
	return true;
}

void jump(int ra,int ca,int rb,int cb)
{
	swap(a[ra][ca],a[rb][cb]);
}

void dfs(int l, int r, int c)
{
	if(l==11)return;
	if(memcmp(a,s,sizeof(a))==0){
		step = min(step,l);
		return;
	}
	for(int i=0;i<8;i++){
		int rr = r + dr[i];
		int cc = c + dc[i];
		if(isValid(rr,cc)){
			jump(r,c,rr,cc);
			dfs(l+1,rr,cc);
			jump(r,c,rr,cc);
		}
	}	
}

int main()
{
	int tc;
	cin>>tc;
	string s;
	getline(cin,s);
	for(int i=0;i<tc;i++){
		int r,c;
		for(int j=0;j<5;j++){
			getline(cin,s);
			for(int k=0;k<5;k++){
				a[j][k]=s[k];
				if(s[k]==' '){
					r = j;
					c = k;
				}
			}
		}
		step=11;
		dfs(0,r,c);
		if(step==11)
			printf("Unsolvable in less than 11 move(s).\n");
		else
			printf("Solvable in %d move(s).\n",step);
	}
	return 0;
}