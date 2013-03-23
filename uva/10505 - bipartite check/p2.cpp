#include <stdio.h>
#include <memory.h>
#include <algorithm>
#include <queue>

using namespace std;

int N;
bool g[ 202 ][ 202 ];
int color[ 202 ];
int xd[2];
bool bfs(int s)
{
    bool ok = true;
    queue< int > q;
    q.push( s );
    color[ s ] = 0;
    while ( !q.empty() )
    {
        int v = q.front();
        xd[ color[ v ] ]++;
        q.pop();
        for (int i=1; i<=N; i++)
        {
            if ( g[v][i] )
            {
                if ( color[ i ] == -1 )
                {
                    color[ i ] = color[ v ] ? 0 : 1;
                    q.push( i );
                }
                else if ( color[ i ] == color[ v ] ) ok = false;
            }
        }
    }
    return ok;
}

int main()
{
    int test;
    int p, v;
    int cs = 1;
    
    scanf("%d",&test);
    while ( test-- )
    {
        scanf("%d",&N);
        
        memset( g, 0, sizeof g );
        for (int u=1; u<=N; u++)
        {
            scanf("%d",&p);
            while (p--)
            {
                scanf("%d",&v);
                g[u][v] = g[v][u] = true;
            }
        }
        
        int ans = 0;
        memset( color, -1, sizeof color );
        for (int i=1; i<=N; i++)
        {
            xd[ 0 ] = xd[ 1 ] = 0;
            if ( color[i] == -1 )
            {
                if ( bfs( i ) ) ans += max( xd[0], xd[1] );
            }
        }
        printf("%d\n",ans);
    }
    
    return 0;
}