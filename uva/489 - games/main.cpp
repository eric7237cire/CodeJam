#include<cstdio>
#include<cstring>
int alp[26],ans;
int gues[26];

int main(){
	//freopen("in.txt","r",stdin);
	int n,wa,guest;char w;
	while(scanf("%d",&n)==1&&n!=-1){
		ans=0;
		memset(alp,0,sizeof(alp));
		memset(gues,0,sizeof(gues));
		scanf("%c",&w);
		scanf("%c",&w);
		while(w!='\n'&&ans<26){
			if(!alp[w-'a']){++ans;alp[w-'a']=1;}
			scanf("%c",&w);
		}
		wa=0;guest=0;
		scanf("%c",&w);
		while(wa<7&&guest<ans&&w!='\n'){
			if(gues[w-'a']){scanf("%c",&w); continue;}//???????
			gues[w-'a']=1;
			if(alp[w-'a'])
				++guest;
			else
				++wa;
			scanf("%c",&w);
		}
		printf("Round %d\n",n);
		if(guest==ans) printf("You win.\n");
		else if(wa==7) printf("You lose.\n");
		else printf("You chickened out.\n");
		while(w!='\n') scanf("%c",&w);//????????
	}
	return 0;
}