#include <iostream>
#include <stdio.h>
#include <sstream>
#include <string>
#include <vector>
#include <assert.h>
#include <ctype.h>
#include <string.h>

using namespace std;

struct Element
{
	Element(int type, int value) : type(type), value(value) { }
	int type;
	int value;
};

typedef vector<Element>::const_iterator exp_iter;

int a[27];
bool b[27];

int GetVariable(exp_iter& iter, const exp_iter& end)
{
	assert(iter->type);
	if (iter->type == 1) {
		int type = iter->value;
		++iter;
		assert(iter->type == 2);
		b[iter->value] = true;
		return type ? --a[iter++->value] : ++a[iter++->value];
	} else {
		int var = iter->value;
		b[var] = true;
		if (++iter != end && iter->type) {
			assert(iter->type == 1);
			return iter++->value ? a[var]-- : a[var]++;
		} else {
			return a[var];
		}
	}
}

char GetBinary(exp_iter& iter, const exp_iter& end)
{
	if (iter == end) return 0;
	assert(!iter->type);
	return iter++->value ? '-' : '+';
}

void Evaluate(const vector<Element>& expression)
{
	exp_iter iter = expression.begin();
	char oper = '\0';
	while (iter != expression.end()) {
		switch (oper) {
			case '+':
				a[0] += GetVariable(iter, expression.end());
				break;
			case '-':
				a[0] -= GetVariable(iter, expression.end());
				break;
			default:
				a[0] = GetVariable(iter, expression.end());
				break;
		}
		oper = GetBinary(iter, expression.end());
	}
}

Element GetElement(char p, char c)
{
	if (isalpha(c))
		return/* new*/ Element(2, c - 0x60);
	return /*new*/ Element(p != 0 , c == '-');
}

vector<Element> GetElements(string line)
{
	stringstream ss(line);
	vector<Element> elements;
	char p = '\0', c;
	while (ss.get(c)) {
		if (isalpha(c)) {
			if (p) elements.push_back(GetElement(0, p));
			elements.push_back(GetElement(0, c));
			c = 0;
		} else {
			if (c == ' ') {
				if (p) elements.push_back(GetElement(0, p));
				c = 0;
			} else if (p) {
				if (p == c) {
					elements.push_back(GetElement(p, c));
					c = 0;
				} else {
					elements.push_back(GetElement(0, p));
				}
			}
		}
		p = c;
	}
	assert(!p);
	return elements;
}

void ReleaseElements(vector<Element>& elements)
{
//	for (int i = 0; i < elements.size(); i++)
//		delete elements[i];
	elements.clear();
}

int main()
{
	string line;
	while (getline(cin, line)) {
		printf("Expression: %s\n", line.c_str());
		memset(b, 0, sizeof(b));
		for (int i = 0; i < 27; i++) a[i] = i;
		Evaluate(GetElements(line));
		printf("    value = %d\n", a[0]);
		for (int i = 1; i < 27; i++)
			if (b[i]) printf("    %c = %d\n", i + 0x60, a[i]);
	}
	return 0;
}