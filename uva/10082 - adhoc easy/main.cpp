#include <iostream>
#include <algorithm>
#include <cstdio>
#include <cstring>
#include <cstdlib>
#include <string>

using namespace std;

int main() {
    string table = "`1234567890-=QWERTYUIOP[]\\ASDFGHJKL;'ZXCVBNM,./";
    string line;
    
    while(getline(cin, line)) {
        for(int i=0; i<line.length(); i++) {

            if (line[i] == ' ') cout << ' ';
            else {
                int index = table.find(line[i]);
                cout << table[index-1];
            }
        }
        cout << endl;
    }
    return 0;
}