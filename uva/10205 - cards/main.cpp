/* 
 * A solution for the "Stack 'em Up" problem.
 * 
 * Problem description:
 * http://icpcres.ecs.baylor.edu/onlinejudge/external/102/10205.html
 * 
 * Author: Joana Matos Fonseca da Trindade
 * Date: 2008.04.05
 * UVa ID: 10205
 */
#include <iostream>


#define NVALUES 13
#define NSUITS 4
#define NCARDS 52
#define NSHUFFLES 100
#define WSIZE 9


using namespace std;


char values[NVALUES][WSIZE] = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};
char suits[NSUITS][WSIZE] = {"Clubs", "Diamonds", "Hearts", "Spades"};
int shuffles[NSHUFFLES][NCARDS];
int deck[NCARDS];


/* read all dealer shuffles */
void read_shuffles(int n_shuff) {
        for(int i=0; i<n_shuff; i++) {
                for (int j=0; j<NCARDS; j++) {
                        cin >> shuffles[i][j];
                }
        }
}


/* shuffle the deck with one of the known shuffles */
void shuffle_deck(int s_id) {
        int tmpdeck[NCARDS];
        for (int i=0; i<NCARDS; i++) {
                tmpdeck[i] = deck[shuffles[s_id][i] - 1];
        }
        for (int i=0; i<NCARDS;i++) {
                deck[i] = tmpdeck[i];
        }
}


/* main */
int main (int argc, const char *argv[]) {
        int nc; /* number of cases */
        int ns; /* number of shuffles */
        int s; /* current shuffle */
                
        cin >> nc;
                
        for (int i=0; i<nc; i++) {      
                cin >> ns;
                        
                /* initialize deck */
                for (int p=0; p<NCARDS; p++) {
                        deck[p] = p;
                }
                
                /* read list of known shuffles */
                read_shuffles(ns);
                
                /* shuffle deck */
                for (int j=0; j<ns; j++) {
                        cin >> s;
                        shuffle_deck(s - 1);
                }


                /* print deck */
                for (int k=0; k<NCARDS; k++) {
                        cout << values[deck[k] % NVALUES] << " of " << suits[deck[k] / NVALUES] << endl;
                }
                if (i < (nc - 1)) {
                        cout << endl;
                }
        }
        
}