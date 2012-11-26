#include <iostream>
 
using namespace std;

int reverse(int num);

int main() {
	int t;
	cin >> t;
	while(t--) {
		int a, b;
		cin >> a >> b;
		a = reverse(a);
		b = reverse(b);
		
		int c = a + b;
		c = reverse(c);
		cout << c << endl;
	}
 
	return 0;	
}

int reverse(int num) {
	int result = 0;

	while(num != 0) 
	{
		result *= 10;
		result += num % 10;
		num /= 10;
	}
	return result;
}