/* @JUDGE_ID: 13160EW 641 C++ */
// 641 Do the Untwist
// 2003/05/28
//@BEGIN_OF_SOURCE_CODE

#include <iostream>
#include <string>
#include <cstring>
using namespace std;

int main()
{
        char buf[100];
        int p[100];
        int i,len;
        int k,b;
        while(1) {
                cin >> k;
                if( k == 0 ) break;
                cin >> buf;
                len = strlen(buf);
                for(i=0;i<len;i++) {
                        if( buf[i]=='_' ) b = 0;
                        else if( buf[i] == '.' ) b = 27;
                        else b= buf[i]-'a'+1;
                        p[k*i % len] = (b+i) % 28;
                }
                for(i=0;i<len;i++) {
                        if( p[i]==0) cout << "_";
                        else if( p[i]==27) cout << ".";
                        else cout << (char)(p[i]+'a'-1);
                }
                cout << endl;
        }
        return 0;
}
//@END_OF_SOURCE_CODE