/*
ID: eric7231
PROG: wissqu
LANG: C++
*/
#include <stdio.h>
 #include <stdlib.h>
 #include <string.h>
 
 struct move
 {
     int i,j;
     char cow;
 }ans[16];
 
 char arr[4][5];
 char bak[4][5];
 int mark[4][4];
 int left[128];
 int dx[9] = {0,0,1,1,1,-1,-1,-1,0};
 int dy[9] = {1,-1,0,-1,1,1,0,-1,0};
 long long count = 0;
 
 void init()
 {
     int i;
     for(i = 0;i < 4;i ++)
         scanf("%s",arr[i]);
     left['A'] = left['B'] = left['C'] = left['E'] = left['D'] = 3;
     memcpy(bak,arr,sizeof(bak));
 }
 
 void pt()
 {
     int i;
     for(i = 0;i < 16;i ++)
         printf("%c %d %d\n",ans[i].cow,ans[i].i + 1,ans[i].j + 1);
 }
 
 int inmap(int i,int j)  //check if [i][j] in the range
 {
     if((i >= 0) && (i <= 3) && (j >= 0) && (j <= 3))    return 1;
     return 0;
 }
 
 int valid(char c,int i,int j)
 {
     if(mark[i][j] == 1)    return 0;   //[i][j] is already arranged
     int k;
     for(k = 0;k < 9;k ++)
     {
         if(inmap(i + dx[k],j + dy[k]))
         {
             if(arr[(i + dx[k])][j + dy[k]] == c)    return 0;
         }
     }
     return 1;
 }
 
 void dfs(int step)
 {
     if(step == 16)
     {
         if(count == 0)    pt();  //output the first solution
         count ++;
     }
     char c;
     int i,j;
     for(c = 'A';c <= 'E';c ++)
         if(left[(int)c] > 0)
         {
             left[(int)c] --;
             for(i = 0;i < 4;i ++)
             for(j = 0;j < 4;j ++)
                 if(valid(c,i,j) == 1)
                 {
                     arr[i][j] = c;
                     mark[i][j] = 1;
                     ans[step].cow = c;
                     ans[step].i = i;
                     ans[step].j = j;
                     dfs(step + 1);
                     mark[i][j] = 0;
                     arr[i][j] = bak[i][j];
                 }
             left[(int)c] ++;
         }
 }
 
 int main()
 {
     freopen("wissqu.in","r",stdin);
     freopen("wissqu.out","w",stdout);
     int i,j;
     init();
     ans[0].cow = 'D';
     for(i = 0;i < 4;i ++)   //move a 'D' first
     for(j = 0;j < 4;j ++)
     {
         if(valid('D',i,j) == 1)
         {
             //printf("%d %d\n",i,j);
             arr[i][j] = 'D';
             mark[i][j] = 1;
             ans[0].i = i;
             ans[0].j = j;
             dfs(1);
             mark[i][j] = 0;
             arr[i][j] = bak[i][j];
         }
     }
     printf("%lld\n",count);
     return 0;
 }
