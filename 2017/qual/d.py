# + are bishops
# x are rooks

# 0 4
# 3 1

# https://math.stackexchange.com/questions/383321/rotating-x-y-points-45-degrees
import sys
import numpy as np

# Adapted from https://stackoverflow.com/questions/42318343/avoid-duplicates-in-n-queen-iterative-solutions-no-recursion-allowed
class Board:

    INVALID = "#"
    EMPTY = "."
    def __init__(self, size):
        self.N = size
        self.queens = [] # list of columns, where the index represents the row
        self.rooks = []
        self.bishops = []
        self.blanks = []

    def convert_to_tilted_board_coords(self, row, col):
        return  row+col  , col - row + self.N

    def convert_to_board_coords(self, row, col):
        return int((row-col)/2 + self.N / 2), int((row+col) / 2 - self.N / 2)

        # 0, 0 => 0,0
        # 1, 1 => 0, 1
        # 2, 2 => 0, 2
        # 1, -1 => 1, 0
        # 2, 0 => 1, 1
        # 3, 1 => 1, 2

    def unpivot_board(self):
        self.board = np.empty(shape=(self.N, self.N))

        for row in range(0, self.tilted_board.shape[0]):
            for col in range(0, self.tilted_board.shape[1]):
                if self.tilted_board[row, col] == Board.INVALID:
                    continue
                coords = self.convert_to_board_coords(row, col)
                print(f"{row},{col} {self.tilted_board[row][col]}==> {coords}")
                self.board[coords[0], coords[1]] = self.tilted_board[row,col]



    def pivot_board(self):

        self.tilted_board = np.full( shape=(2*self.N, 2*self.N),
                                     fill_value = Board.INVALID,
                                     dtype=np.chararray)

        for row in range(0, self.N):
            for col in range(0, self.N):
                # 45 rotation, x+y, y-x
                # and a translation up N to avoid nulls
                coords = self.convert_to_tilted_board_coords(row, col)
                self.tilted_board[coords[0], coords[1]] = col + row * self.N

                check_coords = self.convert_to_board_coords(*coords)
                if (row,col) != check_coords:
                    raise Exception("problem")

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

q.pivot_board()

print(q.tilted_board)

q.unpivot_board()

print(q.board)

sys.exit(0)

q.solution(is_rooks = True)
q.solution(is_rooks = False)

q.print_the_board()