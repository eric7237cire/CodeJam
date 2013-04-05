#include<iostream>
using namespace std;

char research(string s)
{
	if(s=="1"||s=="4"||s=="78")return '+';
	if(s.substr(s.size()-2,2)=="35")return '-';
	if(s.substr(0,1)=="9"&&s.substr(s.size()-1,1)=="4")return '*';
	if(s.substr(0,3)=="190")return '?';
}

int main()
{
	int n;
	cin>>n;
	string s;
	for(int i=0;i<n;i++){
		cin>>s;
		cout<<research(s)<<endl;
	}
	return 0;
}