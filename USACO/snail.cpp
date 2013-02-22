
/*
ID: eric7231
PROG: snail
LANG: C++
*/

#include <fstream>
const int MAX = 120;
using namespace std;
ofstream fout("snail.out");
ifstream fin("snail.in");

struct dir{
   int row, col;
   
   dir(int r, int c) : row(r), col(c) {}
};

bool map[MAX][MAX] = {false}, run[MAX][MAX] = {false};

dir moves[] = 
{
    dir(1, 0),
    dir(-1, 0),
    dir(0, 1),
    dir(0, -1)
};


int themax = 0, N, B;
void DFS(int, int, int, int, int);
bool valid(int, int);
void ini();

int main()
{    
    fin>>N>>B;
    
    for(int i = 0; i < B; i++)
    {
    char temp;
    int tempd;
    fin>>temp>>tempd;
    map[tempd - 1][temp - 'A'] = true;
    }
    //load barriers
    
    run[0][0] = true;
    DFS(0, 0, 1, 0, 1);
    DFS(0, 0, 0, 1, 1);
    
    fout<<themax<<endl;
        
    return 0;
}

//Returns true if coordinates are in the square
bool valid(int a, int b)
{
     return a >= 0 && a < N && b >= 0 && b < N;
 }

void DFS(int nr, int nc, int dr, int dc, int depth)
{
     themax = themax > depth ? themax : depth;
     if( valid(nr + dr, nc + dc) )
     {
         //No barrier
        if( !map[nr + dr][nc + dc] )
        {
            //Square not yet visited
             if( !run[nr + dr][nc + dc] )
             {
             run[nr + dr][nc + dc] = true;
             DFS(nr + dr, nc + dc, dr, dc, depth + 1);
             run[nr + dr][nc + dc] = false;             
             }
        }
        else
        {
            //Hit a barrier
             for(int i = 0; i < 4; i++)
             {
             int newr = moves[i].row, newc = moves[i].col;
             
                 if( valid(nr + newr, nc + newc) )
                 if( !map[nr + newr][nc + newc] && !run[nr + newr][nc + newc])
                 {
                     run[nr + newr][nc + newc] = true;
                     DFS(nr + newr, nc + newc, newr, newc, depth + 1);
                     run[nr + newr][nc + newc] = false;
                 }
                 
             }
        }
        //barrier
     }
     else
     {
         //Hit a wall
             for(int i = 0; i < 4; i++)
             {
             int newr = moves[i].row, newc = moves[i].col;
             
                 if( valid(nr + newr, nc + newc) )
                 if( !map[nr + newr][nc + newc] && !run[nr + newr][nc + newc])
                 {
                     run[nr + newr][nc + newc] = true;
                     DFS(nr + newr, nc + newc, newr, newc, depth + 1);
                     run[nr + newr][nc + newc] = false;
                 }
                 
             }
     }
     //brink
}
