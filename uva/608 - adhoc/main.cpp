#include <iostream>
#include <vector>
#include <string>
#include <cstdlib>

using namespace std;

int main(void)
{
	int		N;

	cin >> N;

	for (int n = 0; n < N; n++) {
		vector < int >		rec(12, 0);
		vector < bool >		possible(12, true);

		for (int j = 0; j < 3; j++) {
			string		left, right, balance;

			cin >> left >> right >> balance;
			for (int i = 0; i < left.length(); i++) {
				if (balance == "even") {
					possible[left[i]  - 'A'] = false;
					possible[right[i] - 'A'] = false;
				}
				else if (balance == "up") {
					rec[left[i]  - 'A']++;
					rec[right[i] - 'A']--;
				}
				else {
					rec[left[i]  - 'A']--;
					rec[right[i] - 'A']++;
				}
			}
		}

		int		min = 0, idx = 0;

		for (int i = 0; i < 12; i++)
			if ((possible[i] == true) && (abs(rec[i]) > min)) {
				min = abs(rec[i]);
				idx = i;
			}

		cout << char(idx + 'A') << " is the counterfeit coin and it is " <<
		     string((rec[idx] < 0) ? "light." : "heavy.") << endl;
	}

	return 0;
}