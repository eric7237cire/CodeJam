 /*
ID: eric7231
LANG: C++
PROG: picture
*/
#include <iostream>
#include <cstdio>
#include <cstring>
#include <algorithm>
using namespace std;
int axis[10005<<1];
int *level = axis+10005;//???? 
struct segment{
    int st, ed, ht;
    bool lab;
    segment(int _st = 0, int _ed = 0, int _ht = 0, bool _lab = false):
        st(_st), ed(_ed), ht(_ht), lab(_lab){}
}hor[2*5005], ver[2*5005];
int n;
int ans;
void scan(segment *ar){
    memset(axis, 0, sizeof(axis));
    for (int i = 1; i <= n; ++i){
        if (ar[i].lab){
            for (int j = ar[i].st; j < ar[i].ed; ++j){
                ++level[j];
                if (level[j] == 1)
                    ++ans;
            }
        }
        else {
            for (int j = ar[i].st; j < ar[i].ed; ++j){
                --level[j];
                if (level[j] == 0)
                    ++ans;
            }
        }
    }
}
void solve(){
	ans = 0;
	scan(hor);
	scan(ver);
	printf("%d\n", ans);	
}
bool cmp(struct segment a, struct segment b){
	if(a.ht != b.ht) return a.ht < b.ht;
	else {
		return a.lab > b.lab;
	}
}
void input(){
    scanf("%d", &n);
    int lx, ly, rx, ry;
    for (int i = 1; i <= n; ++i)
    {
        scanf("%d%d%d%d", &lx, &ly, &rx, &ry);
        hor[(i<<1)-1] = segment(lx,rx,ly,true);
        hor[i<<1] = segment(lx,rx,ry,false);
        ver[(i<<1)-1] = segment(ly,ry,lx,true);
        ver[i<<1] = segment(ly,ry,rx,false);
    }
    n <<= 1;
    sort(&hor[1], &hor[1+n], cmp);
    sort(&ver[1], &ver[1+n], cmp);
}
int main(){
	freopen("picture.in", "r", stdin);
	freopen("picture.out", "w", stdout);
    input();
    solve();
}