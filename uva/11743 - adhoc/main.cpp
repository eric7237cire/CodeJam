#include <iostream>
#include <algorithm>
#include <cstdio>
#include <cstdlib>

using namespace std;

typedef unsigned long long ULL;
typedef long long LL;

int main() {
    int tcase; cin >> tcase;

    while(tcase--) {
        string num[5];
        int sum = 0;

        cin >> num[0] >> num[1] >> num[2] >> num[3];

        for(int i=0; i<4; i++) {
            for(int j=0; j<4; j++) {
                int temp=0;

                if (j % 2 == 0) {
                    temp = (2 * (num[i][j] - '0'));
                    if (temp > 9) {
                        temp = (temp / 10) + (temp % 10);
                    }
                }
                else
                    temp = num[i][j] - '0';
                sum+=temp;
            }
        }

        if (sum % 10 == 0)
            cout << "Valid";
        else
            cout << "Invalid";
        cout << endl;
    }
    return 0;
}