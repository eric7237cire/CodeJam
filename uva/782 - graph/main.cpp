#include <stdio.h>
#include <stdlib.h>
int dx[] = {1,-1,0,0};
int dy[] = {0,0,1,-1};
void visit(int i,int j,char** map)
{
    if(i>=0&&j>=0&&i<40&&j<100&&map[i][0]!='_'&&j<100&&(map[i][j]==' '||map[i][j]=='*'||!map[i][j]))
    {
        map[i][j]='#';
        visit(i+1,j,map);
        visit(i,j+1,map);
        visit(i-1,j,map);
        visit(i,j-1,map);
    }
}
int main()
{
    int c,i,j,top;
    char** temp =  (char**)malloc(40*sizeof(char*));
    char** map;
    map = (char**)malloc(40*sizeof(char*));
    for(i=0;i<40;i++)
    {
        map[i] = (char*) malloc(100);
        temp[i] =  (char*) malloc(100);
    }
    scanf("%d",&c);
    getchar();
    while(c-->0)
    {
            top=0;
            for(i=0;i<40;i++)
                for(j=0;j<100;j++)
                    temp[i][j]=map[i][j]=0;
            while(gets(map[top])&&map[top][0]!='_')
            {
                i = 0;
                while(map[top][i])
                    temp[top][i]=map[top][i++];
                top++;
            }
            i = 0;
            while(map[top][i])
                temp[top][i]=map[top][i++];
            top++;
            for(i=0;i<top;i++)
                for(j=0;map[i][j];j++)
                    if(map[i][j]=='*')
                    {
                        visit(i,j,temp);
                        map[i][j]=' ';
                    }
            for(i=0;i<top;i++)
                for(j=0;j<100;j++)
                    if(map[i][j]!='_'&&map[i][j]!=' '&&map[i][j]!='#'&&map[i][j])
                    {
                        int d;
                        for(d=0;d<4;d++)
                        {
                            int cx = i + dx[d];
                            int cy = j + dy[d];
                            if(cx>=0&&cy>=0&&cx<40&&cy<100&&temp[cx][cy]=='#')
                            {
                                map[cx][cy]='#';
                                int k;
                                for(k=0;k<cy;k++)
                                    if(!map[cx][k])
                                        map[cx][k]=' ';
                            }
                        }
                    }
            for(i=0;i<40;i++)
            {
                int max = -1;
                for(j=0;j<100;j++)
                    if(map[i][j]&&map[i][j]!=' ')
                        max = j;
                map[i][max+1]=0;
            }
 
            for(i=0;i<top;i++)
                printf("%s\n",map[i]);
        }
    return 0;
}