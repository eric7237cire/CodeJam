#include <fstream>
#include <iostream>
#include <iomanip>
#include <vector>
#include <numeric>
#include <algorithm>
#include <limits>
#include <map>
#include <set>
#include <sstream>
#include <deque>
#include <bitset>

#include <stdio.h>
#include <time.h>
#include <assert.h>
#include <stdlib.h>

#include "util.h"
#include <cstring>


using namespace std;


void do_test_case(int, ifstream& input);




int main(int argc, char** args)
{
    
    
    
    //LOG_OFF();
    
  if (argc < 2) {

    cerr << "Usage: <exe> input_file" << endl;
    return -1;
  }

  ifstream input;
  input.open(args[1]);

  int T;
  input >> T;

  string dummy;
  getline(input, dummy);
  SHOW_TIME_BEGIN(g)



  for(int test_case = 0; test_case < T; ++test_case)
  {

    do_test_case(test_case, input);
  }

  SHOW_TIME_END(g)
}


//tree ::= (weight [feature tree tree])
class Node {
public :
    
    string feature;
    Node* hasFeature;
    Node* notHasFeature;
    float weight;
    
    Node(float weight) : hasFeature(0), notHasFeature(0), weight(weight) {
    }
};

void printTree( Node * tree ) {
    cout << "Tree " << tree->weight << endl;
    
    if (tree->feature.length() > 0) {
        cout << "Has feature " << tree->feature << endl;
        printTree(tree->hasFeature);
        cout << endl;
        cout << "Not has feature " << tree->feature << endl;
        printTree(tree->notHasFeature);
        cout << endl;
    }
}

double getProb( Node * tree, double p, const vector<string>& features) {
    p *= tree->weight;
    
    if (tree->feature.length() > 0) {
        if ( std::find(features.begin(), features.end(), tree->feature) != features.end() ) {
            return getProb(tree->hasFeature, p, features);
        } else {
            return getProb(tree->notHasFeature, p, features);
        }
    }
    
    return p;
}

Node* parseTree( std::stringstream& ss ) {
    
    char c;    
    ss >> skipws >> c;
    
    if (c != '(') {
        cout << "prob 1" << endl;
        LOG(c);
        return NULL;
    }
    
    float weight;
    ss >> weight;
    
    LOG(weight);
    ss >> c;
    LOG(c);
    if (c == ')') {
        //cout << "node " << weight << endl;
        return new Node(weight);   
    }

    ss.unget();
    //<< c;
    //ss << "nth nts boseuth";
    
    string feature;
    ss >> feature;
    
    LOG(feature);
   
    Node* left = parseTree(ss);
    Node* right = parseTree(ss);
    
    ss >> c;
    LOG(c);
    if (c != ')') {
        LOG(c);
        cout << "Prob 2" << endl;
        
    }
    
    Node* ret = new Node(weight);
     ret->feature = feature;
    ret->hasFeature = left;
    ret->notHasFeature = right;
    return ret;
    /*
    for(int i = 0; i < treeDef.length(); ++i) {
        cout << "Char " << i << " " << treeDef[i] << endl;   
        if (treeDef[i] == ' ' || treeDef[i] == '\n') {
            continue;
        }
    }*/
}

void do_test_case(const int test_case, ifstream& input)
{
    SHOW_TIME_BEGIN(tc);
   //LOG_ON();
    LOG_OFF();
    
    /*
    Each test case description will start with a line that contains an integer L -- the number of lines that describe a decision tree. The next L lines will contain a decision tree in the format described above. The line after that will contain A -- the number of animals. The next A lines will each contain the description of one animal in the following format.
    */
    
    int L;
    input >> L;
    LOG(L);
    string line;
    getline(input, line);
    
    string treeDef;
    
    for(int i = 0; i < L; ++i) {
        getline(input, line);
        LOG(i);
        treeDef += line;
        
    }
   
    LOG(treeDef);
    //tree ::= (weight [feature tree tree])
    stringstream ss(treeDef);
    Node * tree = parseTree( ss );
    //printTree(tree);
    
    int A;
    input >> A;
    
    cout << "Case #" << (1+test_case) << ": " << endl;
    
    for(int i = 0; i < A; ++i) {
        string animal;
        int features;
        
        input >> animal;
        input >> features;
        LOG(animal);
        LOG(features);
        vector<string> listFeatures;
        for(int j = 0; j < features; ++j) {
            string feature;
            input >> feature;
            listFeatures.push_back(feature);
            LOG(feature);
        }
        
        cout << std::setprecision (7) << fixed << getProb(tree, 1.0, listFeatures) << endl;
        
    }
    
    

}



