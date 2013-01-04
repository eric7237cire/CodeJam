/*
ID: eric7231
PROG: friday
LANG: C++
*/
#include <iostream>
#include <fstream>
#include <string>
#include <map>
#include <vector>
using namespace std;

int main() {
    ofstream fout ("friday.out");
    ifstream fin ("friday.in");
    int N;
    fin >> N;
    
    int y,m,d=0,s=0;  //y=years, m=months, d=day of week, s=sundays;

    //d = 0 sat , 6 friday
    
    int counts[7] = {0};
    
 for(y=1900; y<=1900+N-1; y++){
   for(m=1;m<=12;m++){
       counts[d]++;
     switch(m){

       case 1: case 3: case 5: case 7: case 8: case 10: case 12:
         d+=3;
        break;

       case 4: case 6: case 9: case 11:
         d+=2;
        break;

       case 2:
         if(y%400==0||(y%4==0&&y%100!=0))d+=1;
        break;

       default: break;}

     if(d>=7)d-=7;
     
    }
 }
    	   
    for(int i = 0; i < 7; ++i)
    {
        fout << counts[i];
        if (i != 6)
            fout << ' ';
        else
            fout << endl;
    }
    return 0;
}
