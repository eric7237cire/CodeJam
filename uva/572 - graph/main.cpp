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

void reverseList(const Node* list)
{
	if (list == NULL || list->next == NULL) 
	{
		return;
	}
	Node* cur = list;
	Node* next = cur->next;
	
	Node* newHead = NULL;
	
	while(next)
	{
		if (newHead) {
			cur->next = newHead;
		} else {
			cur->next = NULL;
		}
		
		newHead = cur;
		cur = next;
		next = cur->next;
	}
	

}

int main()
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
}

int main2()
{  
	cout << reverseInt(40240) << endl;
	cout << reverseInt(123402407) << endl;
	
	Base b;
	cout << "b done" << endl;
	
	Derived d;
	cout << "d done" << endl;
	
	chop(d);
	
	cout << "chop done" << endl;
 return 0;
}