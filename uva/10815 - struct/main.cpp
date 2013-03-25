#include<iostream>
#include<cctype>
#include<sstream>
using namespace std;

struct node
{
	string item;
	node *next;
	node(string x, node *t){item = x; next = t;}
};

node *head;

void insert(string x)
{
	node *t,*u;
	for(t=head;t->next&&x>=t->next->item;t=t->next);
	if(t->item!=x)t->next=new node(x,t->next);
}

void print()
{
	for(node *t=head->next;t;t=t->next)cout<<t->item<<endl;
}


int main()
{
	string s;
	head = new node("",0);
	while(getline(cin,s)){
		string low;
		for(int i=0;i<s.size();i++)
			if(isalpha(s[i]))
				low.push_back(tolower(s[i]));
			else
				low.push_back(' ');
		stringstream ss;
		ss.str(low);
		while(ss>>s)insert(s);
	}
	print();
	return 0;
}