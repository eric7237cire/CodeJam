/*
ID: eric7231
PROG: transform
LANG: C++
*/
#include <iostream>
#include <fstream>
#include <string>
#include <map>
#include <vector>
#include <algorithm>
#include <cassert>
using namespace std;

typedef vector<string> charMatrix;
typedef unsigned int uint; 

 void rotate90(const charMatrix& matrix, charMatrix& target)
 {
	 for(uint r = 0; r < matrix.size(); ++r)
	 {
		 for(uint c = 0; c < matrix[r].length(); ++c)
		 {
			 target[r][c] = matrix[matrix.size() - 1 - c][r];
		 }
	 }
 }

 void reflect(const charMatrix& matrix, charMatrix& target)
 {
	 for(uint r = 0; r < matrix.size(); ++r)
	 {
		 target[r] = matrix[r];
		 reverse(target[r].begin(), target[r].end());
	 }
 }
 
 void input(charMatrix& matrix, istream& in)
 {
	 for(uint i = 0; i < matrix.size(); ++i)
	 {
		 string str;
		 in >> str;
		 matrix[i] = str;
	 }
 }

  void output(const charMatrix& matrix)
 {
	 for(uint i = 0; i < matrix.size(); ++i)
	 {
		 cout << matrix[i] << endl;
	 }
	 cout << endl;
 }

int main() {
	ofstream fout ("transform.out");
    ifstream fin ("transform.in");

	assert(fin);

    int N;
    fin >> N;

	charMatrix matrix(N);
	input(matrix, fin);

	charMatrix after(N);
	input(after, fin);

	int minTrans = matrix == after ? 6 : 7;

	vector<charMatrix> xforms(7, matrix);

	reflect(matrix, xforms[3]);
	rotate90(matrix,xforms[0]);
	rotate90(xforms[0],xforms[1]);
	rotate90(xforms[1],xforms[2]);

	rotate90(xforms[3], xforms[4]);
	rotate90(xforms[4], xforms[5]);
	rotate90(xforms[5], xforms[6]);

	for(int i = 0; i < 7; ++i) 
	{
		if (xforms[i] == after)
		{
			minTrans = i < 4 ? i + 1 : 5;
			break;
		}
	}

	fout << minTrans << endl;

    return 0;
}
