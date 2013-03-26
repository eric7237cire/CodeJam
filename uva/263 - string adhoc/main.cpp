#include<cstdio>
#include<set>

int main(){
    long n, large, small, chain;
    std::set<long>numbers;
    std::pair< std::set<long>::iterator, bool>mypair;
    char temp[15], i, j;
    
    while(std::scanf("%ld", &n)!=EOF && n>0){
        chain=1;
        std::printf("Original number was %ld\n", n);
        while(true){
            std::sprintf(temp, "%ld", n);
            
            for(i=0;temp[i+1];i++){
                for(j=i+1;temp[j];j++){
                    if(temp[i]<temp[j])
                        temp[i]^=temp[j]^=temp[i]^=temp[j];
                }
            }
            
            std::sscanf(temp, "%ld", &large);
            for(small=0;i>=0;i--) small=10*small+temp[i]-'0';
            
            n=large-small;
            std::printf("%ld - %ld = %ld\n", large, small, n);
            mypair=numbers.insert(n);
            if(mypair.second==false) break;
            chain++;
        }
        
        std::printf("Chain length %ld\n\n", chain);
        numbers.clear();
    }
    
    return 0;
}