/* @JUDGE_ID: 13160EW 661 C++ */
// 06/14/03 12:27:35

//@BEGIN_OF_SOURCE_CODE

#include <stdio.h>

bool OnOff[30];
int bottle[30];

int main()
{ 
        int n,m,c;
        int i,which;
        int large,now;
        bool flag;
        int sec = 1;
        while(1) {
                scanf("%d %d %d",&n,&m,&c);
                if( n == 0 ) break;
                printf("Sequence %d\n",sec++);
                for(i=0;i<n;i++) {
                        scanf("%d",&bottle[i]);
                        OnOff[i] = false;
                }
                large = now = 0;
                flag = true;
                for( i = 0 ; i<m ; i++) {
                        scanf("%d" , &which );
                        which -=1;
                        if( flag ) {
                                if( OnOff[which] ) {
                                        OnOff[which] = false;
                                        now -= bottle[which];
                                } else {
                                        OnOff[which] = true;
                                        now += bottle[which];
                                        if( large < now ) large = now;
                                        if( now > c ) flag = false;
                                }
                        } // end if
                } // end for
                if( flag ) {
                        printf("Fuse was not blown.\n");
                        printf("Maximal power consumption was %d amperes.\n",large);
                } else
                        printf("Fuse was blown.\n");
                printf("\n");
        }

        return 0;
}

//@END_OF_SOURCE_CODE