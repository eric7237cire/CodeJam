/*
ID: eric7231
PROG: charrec
LANG: C++
*/
 #include <stdio.h>
 #include <stdlib.h>
 #include <string.h>
 #define MAXLINE 1220
 #define FONTN 27
 #define INF 100000000
 
 char corr[] = " abcdefghijklmnopqrstuvwxyz";
 char font[FONTN][20][21];
 int N;
 char str[MAXLINE][21];
 int opt[MAXLINE];
 int prev[MAXLINE];
 char store[MAXLINE];
 int linediff[MAXLINE][FONTN][20];   //[line][fontnumber][fontline]  precalculate
 
 void readfont()
 {
     int i,j;
     freopen("font.in","r",stdin);
     scanf("%d\n",&i);
     for(i = 0;i < 27;i ++)
         for(j = 0;j < 20;j ++)
             scanf("%s\n",font[i][j]);
 }
 
 void readinput()
 {
     freopen("charrec.in","r",stdin);
     scanf("%d\n",&N);
     int i;
     for(i = 0;i < N;i ++)
         scanf("%s\n",str[i]);
 }
 
 void init()    //calculate linediff array
 {
     int i,j,k,t;
     for(i = 0;i < N;i ++)
         for(j = 0;j < FONTN;j ++)
             for(k = 0;k < 20;k ++)
             {
                 linediff[i][j][k] = 0;
                 for(t = 0;t < 20;t ++)
                     if(font[j][k][t] != str[i][t])    linediff[i][j][k] ++;
             }
 }
 
 int get_min(int a,int b)
 {
     if(a < b)    return a;
     return b;
 }
 
 int cmp_normal(int l,int f)
 {
     int d;
     int i;
     for(i = d = 0;i < 20;i ++)
         d += linediff[l ++][f][i];
     return d;
 }
 
 int cmp_missing(int l,int f)
 {
     int d,tmp;
     int missline,i,j;
     d = INF;
     for(missline = 0;missline < 20;missline ++)
     {
         tmp = 0;j = l;
         for(i = 0;i < 20;i ++)
         {
             if(missline == i)    continue;
             tmp += linediff[j ++][f][i];
         }
         d = get_min(d,tmp);
     }
     return d;
 }
 
 int cmp_dup(int l,int f)
 {
     int d,tmp;
     int dupline,i,j;
     d = INF;
     for(dupline = 0;dupline < 20;dupline ++)
     {
         tmp = 0;j = l;
         for(i = 0;i < 20;i ++)
         {
             if(i == dupline)
             {
                 tmp += get_min(linediff[j][f][i],linediff[j + 1][f][i]);
                 j += 2;
                 continue;
             }
             tmp += linediff[j ++][f][i];
         }
         d = get_min(d,tmp);
     }
     return d;
 }
 
 void dp()
 {
     int i,j;
     //i:line
     //j:alpha for test
     int tmp;
     opt[0] = 0;
     for(i = 1;i < MAXLINE;i ++)
         opt[i] = INF;
     for(i = 0;i < N;i ++)
     {
         if(opt[i] == INF)    continue;
         if(i + 19 <= N)
             for(j = 0;j < 27;j ++)
             {
                 tmp = opt[i] + cmp_missing(i,j);
                 if(tmp < opt[i + 19])
                 {
                     store[i + 19] = corr[j];
                     opt[i + 19] = tmp;
                     prev[i + 19] = i;
                 }
             }
         if(i + 20 <= N)
             for(j = 0;j < 27;j ++)
             {
                 tmp = opt[i] + cmp_normal(i,j);
                 if(tmp < opt[i + 20])
                 {
                     store[i + 20] = corr[j];
                     opt[i + 20] = tmp;
                     prev[i + 20] = i;
                 }
             }
         if(i + 21 <= N)
             for(j = 0;j < 27;j ++)
             {
                 tmp = opt[i] + cmp_dup(i,j);
                 if(tmp < opt[i + 21])
                 {
                     store[i + 21] = corr[j];
                     opt[i + 21] = tmp;
                     prev[i + 21] = i;
                 }
             }
     }
 }
 
 void pt()
 {
     int i;
     int top = 0;
     char ans[100];
     for(i = N;i > 0;i = prev[i])
         ans[top ++] = store[i];
     while(top --)
         printf("%c",ans[top]);
     printf("\n");
 }
 
 int main()
 {
     freopen("charrec.out","w",stdout);
     readfont();
     readinput();
     init();
     dp();
     pt();
     fclose(stdout);
     fclose(stdin);
     return 0;
 }