using namespace std;
#include<iostream>
#include<cstdio>
#include<cstring>
#include<algorithm>
#include<vector>

int main()
{
    int A,F,cases;
	cin>>cases;
	while(cases--)
    {
        cin>>A>>F;
        for(int i=0;i<F;i++)
        {
            for(int j=0;j<A;j++)
            {
                for(int k=0;k<j;k++)
                {
                    cout<<j;
                }
                if(j)cout<<endl;
            }
            for(int j=A; j >0; --j)
            {
                for(int k=0;k<j;k++)
                {
                    cout<<j;
                }
                cout<<endl;
            }
            if(!(cases==0 && i==F-1))
            cout<<endl;
        }

    }
    return 0;
}