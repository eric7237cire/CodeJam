#include <cstdio>
#include <cstdlib>
#include <iostream>

using namespace std;

int DNA[10];
char r[4]={' ','.','x','W',};
int a[2][42];

void clear()
{
	for(int i=0;i<2;i++)
		for(int j=0;j<42;j++)
			a[i][j]=0;
}
void automata()
{
	a[0][20]=1;
	for(int i=0;i<50;i++){
		for(int j=1;j<=40;j++){
			a[(i+1)%2][j]=DNA[a[i%2][j-1]+a[i%2][j]+a[i%2][j+1]];
			cout<<r[a[i%2][j]];
		}
		cout<<endl;
	}
}

int main()
{
	int n;
	cin >> n;
	for(int i=0;i<n;i++){
		for(int j=0;j<10;j++) cin >> DNA[j];
		if(i) cout << endl;
		clear();
		automata();
	}
	return 0;
}