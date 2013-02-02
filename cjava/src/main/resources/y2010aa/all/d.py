# -*- coding: utf-8 -*-
import sys


truths = []
falses = []
same = []
diff = []

cantrue = []
canfalse = []

def intmin(x):
    return int(x)-1
    
def tr(seq, n):
    if len(seq) == n:
        for i, j in truths:
            if seq[i] != seq[j]:
                return
        for i, j in falses:
            if seq[i] == seq[j]:
                return
        for i, j, k in same:
            if seq[i] ^ (seq[j] == seq[k]):
                return
        
        for i, j, k in diff:
            if seq[i] ^ (seq[j] != seq[k]):
                return
        
        #print seq
        # it worked
        for i, v in enumerate(seq):
            if v:
                cantrue[i] = True
            else:
                canfalse[i] = True
        
        return
    seq.append(True)
    tr(seq, n)
    del seq[-1]
    seq.append(False)
    tr(seq, n)
    del seq[-1]
    
fin = sys.stdin
T = int(fin.readline())

for case in range(1,T+1):
    N, M = map(int, fin.readline().split())
    
    truths = []
    falses = []
    same = []
    diff = []
    cantrue = [False]*N
    canfalse = [False]*N
    
    for i in xrange(M):
        s = fin.readline().split()
        if s[1] == 'T':
            truths.append(map(intmin, [s[0], s[2]]))
        if s[1] == 'L':
            falses.append(map(intmin, [s[0], s[2]]))
        if s[1] == 'S':
            same.append(map(intmin, [s[0], s[2], s[3]]))
        if s[1] == 'D':
            diff.append(map(intmin, [s[0], s[2], s[3]]))
        
    tr([], N)
    
    result = []
    for i in xrange(N):
        if cantrue[i] and not canfalse[i]:
            result.append('T')
        elif not cantrue[i] and canfalse[i]:
            result.append('L')    
        elif cantrue[i] and canfalse[i]:
            result.append('-')
        else:
            result.append('!')
    #print counter
    print ("Case #%d: %s" % (case, ' '.join(result)))
    