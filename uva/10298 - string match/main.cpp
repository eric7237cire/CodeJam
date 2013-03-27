#include <iostream>
#include <sstream>
#include <vector>
#include <string>
#include <cstdio>
#include <cstring>
#include <algorithm>
#include <queue>
#include <set>
#include <stack>
#include <map>
#include <list>
#include <cmath>
#define UPL 1000006

using namespace std;

typedef pair<int,int>   ii;
typedef vector<int>     vi;
typedef vector<ii>      vii;
typedef vector<vi>     vvi;
typedef vector<vii>    vvii;
typedef map<int,int>    mii;

char ss[UPL];
vector<bool> sei(UPL,true);
vi ft;

void init(){
    // seive to create the test table
    for(int i=2; i < UPL;i++){
        if(sei[i]){
            ft.push_back(i);
            continue;
        }
        for(int j=i*i; j < UPL; j+=i){
            sei[j] = false;
        }
    }
}

void factorize(int n,mii& mp){
    int idx = 0;
    while(n > 1){
        if(n % ft[idx] == 0){
            n /= ft[idx]; mp[ft[idx]]++;
        } else{
            idx++;
        }
    }
}

void mp_mii_to_vii(mii mmp, vii& vmp){
    for(mii::iterator it = mmp.begin(); it != mmp.end(); it++){
        vmp.push_back(*it);
    }
}

void getFactors(vii mp, vi& fres,int idx,int res){
    if(idx == mp.size()){
        fres.push_back(res);
        return;
    }

    for(int i=0;i<=mp[idx].second;i++){
        getFactors(mp,fres,idx+1,res);
        res *= mp[idx].first;
    }
}

bool test_val(int n,int len){
    int slice = len / n;
    int start = 0;
    for(int i=1;i<slice;i++){
        if(strncmp(ss+start,ss+start+n,n) != 0) return false;
        start += n;
    }
    return true;
}

int main(){
    init();

    while(fgets(ss,UPL,stdin),ss[0] != '.'){
        int len = strlen(ss) - 1;   // escape the last \n
        ss[len] = 0;
        
        mii mmp; vii vmp; vi fac;
        factorize(len,mmp);
        mp_mii_to_vii(mmp, vmp);
        getFactors(vmp,fac,0,1);

        sort(fac.begin(),fac.end());
        
        int idx = 0;
        while(true){
            if(test_val(fac[idx],len)) break;
            idx++;
        }
        cout << len / fac[idx] << endl;

    }
    return 0;
}