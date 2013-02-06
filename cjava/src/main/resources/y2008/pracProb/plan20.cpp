#include <fstream>
#include <sstream>
#include <cstring>
#include <cmath>
#include <string>
#include <set>
#include <map>

using namespace std;

const int INF = 0x3f3f3f3f;
const int NMAX = 16;
const int MMAX = 64;
const double eps = 1e-9;

typedef pair <int, int> PII;
#define x first
#define y second
#define MP make_pair

map <string, int> M;
int NI, NS, C[MMAX][NMAX];
double PG, D[MMAX][1 << NMAX], X[MMAX], Y[MMAX];
set <pair <double, PII> > H;
bool perish[NMAX];

void read(ifstream &fin) {
	int i, val;
	string s, buf;
	string :: iterator p;

	fin >> NI >> NS >> PG;

	M.clear(); memset(perish, 0x00, sizeof(perish));
	for (i = 0; i < NI; ++i) {
		fin >> s;
		if (*s.rbegin() == '!')
			s.erase( --s.end() ),
			perish[i] = true;
		M[s] = i;
	}

	fin.get();
	memset(C, 0x3f, sizeof(C));
	for (i = 1; i <= NS; ++i) {
		getline(fin, buf);
		istringstream ss(buf);

		ss >> X[i] >> Y[i];
		while (ss >> buf) {
			s.clear();
			for (p = buf.begin(); *p != ':'; ++p)
				s += *p;
			for (++p, val = 0; p != buf.end(); ++p)
				val = val * 10 + *p - '0';
			C[i][ M[s] ] = val;
		}
	}
}

void relax(PII u, double dist) {
	if (D[u.x][u.y] < eps + dist) return;
	H.erase( MP(D[u.x][u.y], u) );
	D[u.x][u.y] = dist;
	H.insert( MP(dist, u) );
}

double dist(int a, int b) {
	return PG * sqrt( (X[a] - X[b]) * (X[a] - X[b]) + (Y[a] - Y[b]) * (Y[a] - Y[b]) );
}

void dijkstra(void) {
	PII p;
	double cost, c;
	int i, d, cnf, stop;
	
	for (d = 0; d <= NS; ++d)
		for (i = 0; i < (1 << (NI+1)); ++i)
			D[d][i] = (double) INF;
	
	D[0][0] = 0.;
	H.insert( MP(0, MP(0, 0)) );

	while (!H.empty()) {
		p = H.begin()->y;
		cost = H.begin()->x;
		H.erase( H.begin() );

		for (i = 0; i < NI; ++i) {
			if (p.y & (1 << i)) continue;
			c = cost + C[p.x][i];
			cnf = p.y | (1 << i);
			if (perish[i]) cnf |= 1 << NI;

			relax(MP(p.x, cnf), c);
		}

		stop = ((p.y & (1 << NI)) ? 0 : NS);
		p.y &= ~(1 << NI);
		for (i = 0; i <= stop; ++i) {
			if (i == p.x) continue;
			c = cost + dist(p.x, i);

			relax(MP(i, p.y), c);
		}
	}
}

int main(void) {
	ifstream fin("gcj/plan.in");
	ofstream fout("gcj/plan.out");
	int CN, cn;

	fin >> CN;

	for (cn = 1; cn <= CN; ++cn) {

		read(fin);

		dijkstra();

		fout.precision(7);
		fout << "Case #" << cn << ": " << fixed << D[0][(1 << NI) - 1] << '\n';
	}

	fin.close();
	fout.close();

	return 0;
}
