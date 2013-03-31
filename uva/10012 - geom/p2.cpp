#include<iostream>
#include<cstdio>
#include<cstring>
#include<cmath>
#define MAXN 20
using namespace std;
int n, order[MAXN];

double arr[MAXN], minVal;
bool vis[MAXN];

struct Point{
    double x, y;
};

Point coord[MAXN]; // ?????????????

inline double getDist(Point &a, Point &b){
    return sqrt( (a.x-b.x)*(a.x-b.x)+(a.y-b.y)*(a.y-b.y));
}

inline void addCircle(int cur, int pos){
    for(int i=0; i<cur; ++i){
        double l;
        if(coord[i].y != arr[pos]){
            double h = fabs( coord[i].y - arr[pos] ); // ?
            double x = coord[i].y + arr[pos]; // ??
            l = pow( x*x-h*h, 0.5);
        }
        else 
            l = 2*arr[pos];

        Point temp; 
        temp.x = coord[i].x + l,  temp.y = arr[pos];

        if(temp.x <= coord[cur-1].x) continue;
         
        // ??????????                
        bool flag = true;
        for(int j=0; j<cur; ++j){
            if(i==j) continue;
            double t = getDist(coord[j], temp);
            if( t < coord[j].y + arr[pos]) { 
                flag=false; break;
            }
        }
        if(flag){ 
            if(temp.x < arr[pos]){ // ??????????,????????????
                temp.x = arr[pos];
            }
            coord[cur] = temp;
            return;
        }    
    }
}

void search(int cur, double len){
    if(cur==n && len<minVal){ 
        minVal = len;
        return;
    }

    for(int i=0; i<n; ++i)if(!vis[i]){
        vis[i] = true;
        if(cur==0){ 
            coord[0].x = arr[i], coord[0].y = arr[i]; 
        }
        else{
            addCircle(cur, i);
        }
        // ????
        double newLen;
        if(coord[cur].x + arr[i] > len) newLen = coord[cur].x + arr[i];
        else newLen = len;

        search(cur+1, newLen);

        vis[i] = false;
    }
}

int main(){
#ifdef LOCAL
    freopen("input.txt","r",stdin);
#endif
    int T;
    scanf("%d", &T);
    while(T--){
        scanf("%d", &n);
        for(int i=0; i<n; ++i)
            scanf("%lf", &arr[i]);

        minVal = 2147483646; // ??!????????????!????????
                             // ?????????WA???????????
        memset(vis, 0, sizeof(vis));
        search(0, 0);
        printf("%.3lf\n", minVal);
    }
    return 0;
}