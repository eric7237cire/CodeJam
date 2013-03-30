#include <iostream>
#include <algorithm>
#include <climits>
#include <math.h>

using namespace std;

int main() {

  int n, m, q, ca = 0;
  int vet[1000];
  while (cin >> n && n != 0) {
    ca++;
    for (int i = 0; i < n; i++) {
      cin >> vet[i];
    }
    sort(vet,vet+n);
    cin >> m;
    cout << "Case " << ca << ":" << endl;
    for (int c = 0; c < m; c++) {
      cin >> q;
      int b, lb = 0x3f3f3f3f;
      int i = 0, j = n-1;
      while (i != j) {
	b = vet[i] + vet[j];
	if (vet[i] + vet[j] < q) {
	  i++;
	}
	else if (vet[i] + vet[j] > q) {
	  j--;
	}
	else {
	  lb = b;
	  break;
	}
	if (abs(b - q) < abs(lb - q)) {
	  lb = b;
	}
      }
      cout << "Closest sum to " << q << " is " << lb << "." << endl;
    }
  }
  return 0;
}