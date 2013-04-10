#include <stdio.h>

#define MAXCOMPUTER 20

int getBugYear(int, int, int);

int main(){
    int numofcomputer;
    int displayyear[MAXCOMPUTER];
    int bugyear[MAXCOMPUTER];
    int realyear[MAXCOMPUTER];
    int actualyear[20];
    int tmpinterval;
    int interval;
    int factor;
    int flag;
    int flag_tmp;
    int i;
    int casenum;

    flag = 1;
    casenum = 0;
    while(flag){
        //INPUT
        scanf("%d", &numofcomputer);
        flag = numofcomputer;
        if(flag == 0)break;
        for(i = 0; i < numofcomputer; i++){
            scanf("%d %d %d", &displayyear[i], &bugyear[i], &realyear[i]);
        }

        //COMPUTE
        actualyear[casenum] = displayyear[0];
        interval = realyear[0] - bugyear[0];
        factor = 0;
        flag_tmp = 0;
        while((actualyear[casenum] + interval*factor) < 10000){
            for(i = 0; i < numofcomputer; i++){
                tmpinterval = realyear[i] - bugyear[i];
                //printf("%d\n", displayyear[i]);
                if(displayyear[i] == getBugYear(actualyear[casenum] + interval * factor, realyear[i], tmpinterval)){
                    flag_tmp = 1;
                }else{
                    flag_tmp = 0;
                    break;
                }
            }
            if(flag_tmp == 1)break;
            factor++;
        }

        actualyear[casenum] = actualyear[casenum] + interval * factor;
        casenum++;
    }
    //OUTPUT
    for(i = 0; i < casenum; i++){
        printf("Case #%d:\n", i+1);
        if(actualyear[i] < 10000){
            printf("The actual year is %d.\n", actualyear[i]);
        }else{
            printf("Unknown bugs detected.\n");
        }
        printf("\n");
    }
    return 0;
}

int getBugYear(int sumyear, int realyear, int interval){
    //printf("%d %d %d\n", sumyear, realyear, interval);
    while(sumyear >= realyear){
        sumyear -= interval;
    }
    //printf("%d\n", sumyear);
    //system("pause");
    return sumyear;
}