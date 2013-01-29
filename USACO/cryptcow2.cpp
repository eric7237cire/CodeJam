/*
ID: eric7231
PROG: cryptcow
LANG: C++
*/
#include <fstream>
#include <string>
#include <cstring>
using namespace std;

struct suftrie{
	struct item{
		item():leaf(false){
			memset(s,0,sizeof(s));
		}
		item* s[256];
		bool leaf;
	}root;
	item* now;
	suftrie(string s){
		for(long i=0;i<s.size();++i){
			zero();
			for(long j=i;j<s.size();++j)
				append(s[j]);
			now->leaf=true;
		}
		zero();
	}
	void append(char c){
		if(now->s[c]==0)now->s[c]=new item;
		now=now->s[c];
	}
	void zero(){
		now=&root;
	}
	bool find(char c){
		if(now->s[c]==0){
			zero();
			return false;
		}
		now=now->s[c];
		return true;
	}
	bool leaf(){
		return now->leaf;
	}
};
struct item{
	item(long _num,item* _next):num(_num),next(_next){}
	long num;
	item* next;
};

string S;
string R("Begin the Escape execution at the Break of Dawn");

item *_S=0;

suftrie tr(R);

const long MAXH=600000;
bool hash[MAXH];

void input(){
	ifstream cin("cryptcow.in");
	getline(cin,S);
}
void Append(item*& i,long n){
	i=new item(n,i);
}
void build(){
	for(long i=S.size()-1;i>=0;--i){
		if(S[i]=='C')Append(_S,-1);
		else if(S[i]=='O')Append(_S,-2);
		else if(S[i]=='W')Append(_S,-3);
		else Append(_S,S[i]);
	}
	Append(_S,-4);
}
long valid(){
	long stat[128];
	memset(stat,0,sizeof(stat));
	long l=S.size();
	for(long i=0;i<l;++i)
		++stat[S[i]];
	l=R.size();
	for(long i=0;i<l;++i)
		--stat[R[i]];
	if(stat['C']!=stat['O']||stat['O']!=stat['W'])return -1;
	for(long c=0;c<128;++c){
		if(c=='C'||c=='O'||c=='W')continue;
		if(stat[c]!=0)return -1;
	}
	if(S[0]!='C'&&S[0]!='B')return -1;
	build();
	return stat['C'];
}
bool cut(){
	item* p=_S->next;
	for(long i=0;p->num==R[i];p=p->next,++i);
	if(p->num!=-1)return true;
	item* last;
	for(;;){
		for(;p&&p->num<0;p=p->next)last=p;
		tr.zero();
		for(;p&&p->num>=0;p=p->next)
			if(!tr.find(p->num))return true;
		if(!p)break;
	}
	if(last->num!=-3)return true;
	if(last->next)
		if(!tr.leaf())return true;
	p=_S->next;
	long h=0;
	for(;p;p=p->next){
		h*=3651;
		h^=p->num;
	}
	h%=MAXH;
	if(h<0)h+=MAXH;
	if(hash[h])return true;
	hash[h]=true;
	return false;
}
bool Suc(){
	item *s=_S->next;
	long i=0;
	for(;s;s=s->next,++i)
		if(s->num!=R[i])return false;
	return true;
}
bool search(long time){
	if(!time)return Suc();
	if(cut())return false;
	for(item *pre_j=_S,*j=_S->next;j;pre_j=pre_j->next,j=j->next){
		if(j->num!=-2)continue;
		for(item *pre_i=_S,*i=_S->next;i!=j;pre_i=pre_i->next,i=i->next){
			if(i->num!=-1)continue;
			for(item *pre_k=j,*k=j->next;k;pre_k=pre_k->next,k=k->next){
				if(k->num!=-3)continue;
				if(pre_j==i&&pre_k!=j){
					pre_k->next=k->next;
					pre_i->next=j->next;
				}
				else if(pre_k==j&&pre_j!=i){
					pre_j->next=k->next;
					pre_i->next=i->next;
				}
				else if(pre_j==i&&pre_k==j){
					pre_i->next=k->next;
				}
				else{
					pre_k->next=i->next;
					pre_j->next=k->next;
					pre_i->next=j->next;
				}
				if(search(time-1))return true;
				pre_i->next=i;
				pre_j->next=j;
				pre_k->next=k;
			}
		}
	}
	return false;
}
void output(long res){
	ofstream cout("cryptcow.out");
	if(res!=-1)
		cout<<"1 "<<res<<endl;
	else cout<<"0 0\n";
}
int main(){
	input();
	long res=valid();
	if(res!=-1)
		if(!search(res))res=-1;
	output(res);
}
