# + are bishops
# x are rooks

# 0 4
# 3 1

# https://math.stackexchange.com/questions/383321/rotating-x-y-points-45-degrees
# 45 rotation, x+y, y-x

# Adapted from https://stackoverflow.com/questions/42318343/avoid-duplicates-in-n-queen-iterative-solutions-no-recursion-allowed
class Board:
    def __init__(self, size):
        self.N = size
        self.queens = [] # list of columns, where the index represents the row
        self.rooks = []
        self.bishops = []


    def is_queen_safe(self, row, col):
        for r, c in enumerate(self.queens):
            if r == row or c == col or abs(row - r) == abs(col - c):
                return False
        return True


    def is_bishop_safe(self, row, col):
        for r, c in enumerate(self.bishops):
            if abs(row - r) == abs(col - c):
                return False
        return True


    def is_rook_safe(self, row, col):
        for r, c in enumerate(self.rooks):
            if r == row or c == col :
                return False
        return True

    def print_the_board(self):
        print ("solution:")
        for row in range(self.N):
            line = ['.'] * self.N

            if self.bishops[row] == self.rooks[row]:
                line[self.bishops[row]] = 'o'
            else:
                line[self.bishops[row]] = '+'
                line[self.rooks[row]] = 'x'

            print(''.join(line))

    def solution(self, is_rooks):

        if is_rooks:
            piece_array = self.rooks
            safe_fn = self.is_rook_safe
        else:
            piece_array = self.bishops
            safe_fn = self.is_bishop_safe
        piece_array.clear()
        col = row = 0
        while True:
            while col < self.N and not safe_fn(row, col):
                col += 1
            if col < self.N:
                piece_array.append(col)
                if row + 1 >= self.N:
                    #self.print_the_board()
                    return
                    piece_array.pop()
                    col = self.N
                else:
                    row += 1
                    col = 0
            if col >= self.N:
                # not possible to place a queen in this row anymore
                if row == 0:
                    return # all combinations were tried
                col = piece_array.pop() + 1
                row -= 1

q = Board(5)
q.solution(is_rooks = True)
q.solution(is_rooks = False)

q.print_the_board()