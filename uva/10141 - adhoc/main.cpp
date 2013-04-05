/* @JUDGE_ID: 13160EW 10141 C++ */
// 02/07/06 18:10:43

//@BEGIN_OF_SOURCE_CODE 

#include <iostream>
#include <string>
        
using namespace std;

int main()
{
        int n,p;
        int rep = 1;
        string buf;
        while(1) 
        {
                cin >> n >> p;
        
                if( n==0 || p==0) return 0;
                if( rep > 1 ) cout << endl;     //newline

                getline(cin, buf);
                for(int j=0;j<n;j++) {
                        getline(cin, buf);
                        //cout << "getline:" << buf << endl;
                }
                
                string name, best_name("");
                float d,best_d  = 0.0;
                int r,best_r = -1;
                for(int i=0;i<p;i++) {
                        getline(cin, name);
                        cin >> d >> r;
                        //cout << name << " " << d << " " << r << endl;
                        getline(cin, buf);

                        //getline(cin, buf);
                        for(int j=0;j<r;j++) {
                                getline(cin, buf);
                        //      cout << "getline:" << buf << endl;
                        }
                                
                        if( best_r == -1 ) {
                                // initialize
                                best_name = name;
                                best_d = d;
                                best_r = r;
                        } else {
                                if( r > best_r 
                                || (r == best_r && d < best_d) ) {
                                        best_name = name;
                                        best_d = d;
                                        best_r = r;
                                } // end if
                        } // end if 
                } // end for
                
                cout << "RFP #" << rep++ << endl;
                cout << best_name << endl;
        }


        return 0;
}