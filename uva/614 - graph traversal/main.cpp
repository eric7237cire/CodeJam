#include <iostream>
#include <iomanip>
#include <string>
#include <vector>

using namespace std;

struct POS {
	int		X, Y;
	POS(int x, int y) : X(x), Y(y) { }
	POS(void) { }
};

typedef vector< vector< int > > vvi;

enum { W, N, E, S };

int		diffX[] = {  0, -1, 0, 1};
int		diffY[] = { -1,  0, 1, 0};

int		height, width;
POS		start, end;

bool ispass(vvi &wall, POS p, int dir) 
{
	switch (dir) {
		case W :
			if (p.Y > 1)
				if ((wall[p.X - 1][p.Y - 2] & 1) == 0)
					return true;

			return false;
		case N:
			if (p.X > 1)
				if ((wall[p.X - 2][p.Y - 1] & 2) == 0)
					return true;

			return false;
		case E: 
			if (p.Y < width)
				if ((wall[p.X - 1][p.Y - 1] & 1) == 0)
					return true;

			return false;
		case S: 
			if (p.X < height)
				if ((wall[p.X - 1][p.Y - 1] & 2) == 0)
					return true;

			return false;
	}

	return false; 
}

void output(vvi &maze,
			vvi &wall)
{
	cout << '+';
	for (int i = 0; i < width; i++)
		cout << "---+";

	cout << endl;

	for (int i = 0; i < height; i++) {
		cout << '|';
		for (int j = 0; j < width; j++) {
			if (maze[i][j] > 0)
				cout << setw(3) << maze[i][j];
			else if (maze[i][j] == 0)
				cout << "   ";
			else
				cout << "???";

			cout << char(ispass(wall, POS(i + 1, j + 1), E) ? ' ' : '|');
		}

		cout << endl << '+';
		for (int j = 0; j < width; j++)
			cout << string(ispass(wall, POS(i + 1, j + 1), S) ? "   +": "---+");

		cout << endl;
	}

	cout << endl << endl;
}

bool travel(vvi &maze,
			vvi &wall,
			POS cur, int count)
{
	maze[cur.X - 1][cur.Y - 1] = count;
	if ((cur.X == end.X) && (cur.Y == end.Y)) {
		output(maze, wall);
		return true;
	}

	for (int i = 0; i < 4; i++) {
		if (ispass(wall, cur, i)) {
			POS		nxt = cur;

			nxt.X += diffX[i];
			nxt.Y += diffY[i];
			if ((maze[nxt.X - 1][nxt.Y - 1]) == 0)
				if (travel(maze, wall, nxt, count + 1))
					return true;
		}
	}

	maze[cur.X - 1][cur.Y - 1] = -1;
	return false;
}

int main(void) {
	int		count = 1;

	while (true) {
		cin >> height >> width;
		cin >> start.X >> start.Y;
		cin >> end.X >> end.Y;

		if (!height && !width && !start.X && !start.Y && !end.X && !end.Y)
			break;

		vvi	maze(height, vector < int > (width));
		vvi	wall(height, vector < int > (width));

		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++)
				cin >> wall[i][j];

		cout << "Maze " << count++ << endl << endl;
		travel(maze, wall, start, 1); 
	}
	return 0;
}