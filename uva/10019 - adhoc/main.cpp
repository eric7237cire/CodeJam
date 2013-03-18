#include <stdlib.h>
#include <iostream>

using namespace std;

int count_bits(int num, int it) {
    int store=0;

    for (int i=0; i<it; i++) {
        if (num & 1 == 1)
            store++;
        num = num >> 1;
    }

    return store;
}

int main(int argc, char** argv) {

    int n;
    int b1[1000], b2[1000];

    for(int i=0; i<1000; i++) {
        b1[i] = 0;
        b2[i] = 0;
    }

    cin>>n;

    for (int i=0; i<n; i++) {

        int number;
        cin>>number;
        int temp=number;

        for (int ii=0; ii<32; ii++)
            b1[i] = count_bits(temp, 32);

        int d1,d2,d3,d4;

        d1 = number/1000; number -= d1*1000;
        d2 = number/100 ; number -= d2*100;
        d3 = number/10  ; number -= d3*10;
        d4 = number;

        b2[i] = count_bits(d1, 4);
        b2[i] = b2[i] + count_bits(d2, 4);
        b2[i] = b2[i] + count_bits(d3, 4);
        b2[i] = b2[i] + count_bits(d4, 4);

    }

    for (int i=0; i<n; i++)
        cout<<b1[i]<<" "<<b2[i]<<endl;

    return (0);
}