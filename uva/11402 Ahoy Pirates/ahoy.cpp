/*
Problem link
Type: Data structure - segment tree
Algorithm:
*/
#include <iostream>
#include <cstdio>
#include <cmath>
#include <cstring>
#include <cstdlib>
#include <string>
#include <vector>
using namespace std;
typedef vector<int> vi;
const int maxn = 1024010;
//-----------------------------------
class node {
public:
	int value, len;
	char utype;
};
//-----------------------------------
node tree[5*maxn];
vi a;
//-----------------------------------
void st_build(node tree[], int vertex, int l, int r);
void change(int i);
void set_utype(int vertex, char type);
void update(int vertex, int l, int r, int i, int j, char type);
int answer(int vertex, int l, int r, int i, int j);
//-----------------------------------
int main()
{
#ifndef ONLINE_JUDGE
	freopen ("input.txt","r",stdin);
#endif

	char ch;
	int n,t,ntest,q,u,v;
	string str;
	cin >> ntest;
	for (int test=1; test<=ntest; test++)
	{
		a.clear();
		printf("Case %d:\n",test);
		cin >> n;
		for (int i=1; i<=n; i++)
		{
			cin >> t;
			getline(cin,str);
			getline(cin,str);
			for (int j=1; j<=t; j++)
				for (int k=0; k<str.length(); k++) a.push_back(int(str[k])-48);
		}
		st_build(tree,1,0,a.size()-1);
		int cnt = 0;
		cin >> q;
		for (int i=1; i<=q; i++)
		{
			cin >> ch >> u >> v;
			if (ch!='S') update(1,0,a.size()-1,u,v,ch);
			else
			{
				cnt++;
				cout << "Q" << cnt << ": " << answer(1,0,a.size()-1,u,v) << endl;
			}
		}
	}
}
//-----------------------------------
void st_build(node tree[], int vertex, int l, int r) {
	if (l==r)                       //If it's a leaf node, set its value to a[l] or a[r]
	{
		tree[vertex].value = a[l];
		tree[vertex].utype = -1;
		tree[vertex].len = 1;
	}
	else
	{
		int mid = (l+r)/2;
		st_build(tree, vertex*2, l, mid);       //Calculate its children
		st_build(tree, vertex*2+1, mid+1, r);
		tree[vertex].value = tree[vertex*2].value + tree[vertex*2+1].value; //Calculate its value
		tree[vertex].utype = -1;
		tree[vertex].len = r-l+1;
	}
}
//-----------------------------------
void change(int i) {            //Apply the appropriate cast
	switch(tree[i].utype)
	{
	case 'F': tree[i].value = tree[i].len; break;
	case 'E': tree[i].value = 0; break;
	case 'I': tree[i].value = tree[i].len-tree[i].value; break;
	default: return;
	}
	set_utype(i*2,tree[i].utype);   //After applying, push down the cast type to its childrend
	set_utype(i*2+1,tree[i].utype);
	tree[i].utype = -1;             //Its cast type is now NULL
}
//-----------------------------------
void set_utype(int vertex, char type) {
	if (type=='I')                  //Special case
	{
		switch(tree[vertex].utype)
		{
		case 'F': tree[vertex].utype = 'E'; break;
		case 'E': tree[vertex].utype = 'F'; break;
		case 'I': tree[vertex].utype = -1; break;
		case -1: tree[vertex].utype = 'I'; break;
		default: break;
		}
	}
	else tree[vertex].utype = type;
}
//-----------------------------------
void update(int vertex, int l, int r, int i, int j, char type) {
	int mid = (l+r)/2;
	if (l>=i && r<=j) set_utype(vertex,type);   //If it fits in the interval, set it cast type
	change(vertex);                             //Do the cast type
	if (l>=i && r<=j) return;
	else
		if (l>j || r<i) return;                     //If it's out the quit
		else
		{
			update(vertex*2,l,mid,i,j,type);
			update(vertex*2+1,mid+1,r,i,j,type);
			tree[vertex].value = tree[vertex*2].value+tree[vertex*2+1].value;
		}
}
//-----------------------------------
int answer(int vertex, int l, int r, int i, int j) {
	int mid = (l+r)/2;
	change(vertex);             //As traversing down, update the cast type of current index
	if (l>=i && r<=j) return(tree[vertex].value);
	if (l>j || r<i) return(0);
	int p1 = answer(vertex*2,l,mid,i,j);
	int p2 = answer(vertex*2+1,mid+1,r,i,j);
	tree[vertex].value = tree[vertex*2].value+tree[vertex*2+1].value;
	return(p1+p2);
}