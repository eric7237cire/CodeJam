#include <iostream>
#include <algorithm>
#include <cstdio>
#include <cstdlib>

using namespace std;

typedef unsigned long long ULL;
typedef long long LL;

int main() {
    string str;

    while(cin >> str) {
        for(int i=0; i<str.length(); i++) {
            str[i] = toupper(str[i]);
            if (str[i] == '-' || str[i] == '1' || str[i] == '0')
                cout << str[i];
            else {
                if (str[i] >= 'A' && str[i] <='C')
                    cout << "2";
                else if (str[i] >= 'D' && str[i] <= 'F')
                    cout << "3";
                else if (str[i] >= 'G' && str[i] <= 'I')
                    cout << "4";
                else if (str[i] >= 'J' && str[i] <= 'L')
                    cout << "5";
                else if (str[i] >= 'M' && str[i] <= 'O')
                    cout << "6";
                else if (str[i] >= 'P' && str[i] <= 'S')
                    cout << "7";
                else if (str[i] >= 'T' && str[i] <= 'V')
                    cout << "8";
                else if (str[i] >= 'W' && str[i] <= 'Z')
                    cout << "9";
            }
        }
        cout << endl;
    }
    return 0;
}