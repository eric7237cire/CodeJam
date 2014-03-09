//STARTCOMMON
#include <cmath>
#include <vector>
#include "stdio.h" 
#include <limits>
#include <cassert>
#include <string>
#include <iostream>
#include <cstring>
#include <algorithm>

using namespace std;

class Contained
{
	public:
	Contained() {
		cout << "Contained constructor" << endl;
	}
	
	~Contained() {
		cout << "Contained destructor" << endl;
	}
	
	Contained(const Contained& rhs) {
		cout << "Contained copy constructor" << endl;
	}
};

class Base
{
	public:
	Base() {
		cout << "Base constructor" << endl;
	}
	
	~Base() {
		cout << "Base destructor" << endl;
	}
	
	Base(const Base& rhs) {
		cout << "Base copy constructor" << endl;
	}

	int foo() {
		cout << "Base::foo" << endl;
		return 45;
	}

	virtual int bar() {
		cout << "Base::bar" << endl;
		return 33;
	}

	private:
	
	
	
	Contained c;

};

class Derived : public Base
{
public:
	Derived() {
		cout << "Derived constructor" << endl;
	}
	
	~Derived() {
		cout << "Derived destructor" << endl;
	}
	
	Derived(const Derived& rhs) {
		cout << "Derived copy constructor" << endl;
	}

	int foo() {
		cout << "Derived::foo" << endl;
		return 45;
	}

	virtual int bar() {
		cout << "Derived::bar" << endl;
		return 33;
	}
};

void chop(Base b)
{

}

int reverseInt(int a) 
{
	int rev = 0;
	
	while(a) 
	{
		rev *= 10;
		rev += a % 10;
		a /= 10;
	}
	
	return rev;
}

class Node{
    public:
    	int data;
	Node * next;
	Node(int x)
	{
		data = x;
		next = NULL;
	}
	Node(int x, Node * y)
	{
		data = x; 
		next = y;
	}
};
	
void printList(const Node* list)
{
	cout << "List " << endl;
	while(list != NULL)
	{
		cout << list->data << endl;
		list = list->next;
	}
}

void reverseList(Node* list)
{
	if (list == NULL || list->next == NULL) 
	{
		return;
	}
	Node* cur = list;
	Node* next = cur->next;
	
	Node* newHead = NULL;
	
	while(cur)
	{
		if (newHead) {
			cur->next = newHead;
		} else {
			cur->next = NULL;
		}
		
		newHead = cur;
		cur = next;
		if (cur) next = cur->next;
	}
	

}

void reverseStr(char* str)
{
	int len = strlen(str);
	//printf("len %i", len);
	for(int i = 0; i < len / 2; ++i)
	{
		int e = len - i - 1;
		char t = str[i];
		str[i] = str[e];
		str[e] = t;


	}
}

int main2()
{
	Node* head = new Node(1);
	Node* cur = head;
	
	cur = cur->next = new Node(2);
	
	cur = cur->next = new Node(3);
	
	cur = cur->next = new Node(4);
	
	cur = cur->next = new Node(5);
	
	printList(head);
	
	reverseList(head);
	
	printList(cur);

	return 0;
}

int main()
{
	char* str = "ab"; // "a string to reverse.";
	cout << str << endl;
	reverseStr(str);
	cout << str << endl;

	return 0;

	cout << reverseInt(40240) << endl;
	cout << reverseInt(123402407) << endl;
	
	Base* b = new Base();
	cout << "b done" << endl;
	
	Derived* d = new Derived();
	cout << "d done" << endl;
	
	Base* bp = d;

	chop(*d);
	
	cout << "chop done" << endl;

	b->foo();
	d->foo();
	bp->foo();

	b->bar();
	d->bar();
	bp->bar();

	d->Base::bar();
	
 return 0;
}