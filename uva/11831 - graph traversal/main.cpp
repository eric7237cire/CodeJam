#include <iostream>
#include <cstring>


using namespace std;

char graph[110][110];
int pos_n;
int pos_m;
int face;
int total;

void initial() {
    memset(graph, 0, sizeof(graph));
    pos_n = pos_m = face = total = 0;
}

bool is_start(char x, int i, int j) {
    if (x == 'N') {
        face = 0;
    } else if (x == 'S') {
        face = 2;
    } else if (x == 'L') {
        face = 3;
    } else if (x == 'O') {
        face = 1;
    } else {
        return false;
    }

    pos_n = i;
    pos_m = j;

    return true;
}

void move(char ins) {
    if (ins == 'D') { // right
        face = (face - 1 + 4) % 4;
    } else if (ins == 'E') { // left
        face = (face + 1) % 4;
    } else {
        int new_n = pos_n;
        int new_m = pos_m;
        if (face == 0) {
            new_n--;
        } else if (face == 1) {
            new_m--;
        } else if (face == 2) {
            new_n++;
        } else if (face == 3) {
            new_m++;
        }

        if (graph[new_n][new_m] && graph[new_n][new_m] != '#') {
            pos_n = new_n;
            pos_m = new_m;
            if (graph[new_n][new_m] == '*') {
                graph[new_n][new_m] = '.';
                total++;
            }

        }
    }
}


int main() {
    int n, m, s;

    while (cin >> n >> m >> s && (n || m || s)) {
        initial();
        cin.ignore(100, '\n');
        for (int i = 1; i <= n; ++i) {
            for (int j = 1; j <= m; ++j) {
                cin >> graph[i][j];
                is_start(graph[i][j], i, j);
            }
            cin.ignore(100, '\n');
        }

        while (s--) {
            char x;
            cin >> x;
            move(x);
        }

        cout << total << endl;
    }

    return 0;
}