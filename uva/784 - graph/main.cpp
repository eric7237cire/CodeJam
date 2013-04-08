#include<iostream>
#include<cstring>
#include<cstdio>
using namespace std;

char maze[30][81];
int n;
char buf[81];

int di[]={-1, 0,0,1,};
int dj[]={ 0,-1,1,0,};

bool valid(int i, int j)
{
	return i>=0 && i<30 && j>=0 && j<80;
}

void printMaze()
{
	for(int i=0;i<n;i++)printf("%s\n",maze[i]);
}

void paint(int i,int j)
{
	if(maze[i][j]==' '){
		maze[i][j]='#';
		for(int k=0;k<4;k++){
			int ii=i+di[k];
			int jj=j+dj[k];
			if(valid(ii,jj))paint(ii,jj);
		}
	}
}

void paintMaze()
{
	for(int i=0;i<n;i++)
		if(char *p = strchr(maze[i],'*')){
			*p = ' ';
			paint(i,p-maze[i]);
		}

}


int main()
{
	int tc;
	cin>>tc;
	getchar();
	for(int i=0;i<tc;i++){
		for(n=0;gets(buf)&&buf[0]!='_';n++)
			strcpy(maze[n],buf);
		paintMaze();
		printMaze();
		printf("%s\n",buf);
	}
	return 0;
}		