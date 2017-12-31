# + are bishops
# x are rooks

# 0 4
# 3 1

# https://math.stackexchange.com/questions/383321/rotating-x-y-points-45-degrees
import sys
import numpy as np

# Adapted from
# https://stackoverflow.com/questions/42318343/avoid-duplicates-in-n-queen-iterative-solutions-no-recursion-allowed
class Board:

    INVALID = "#"
    EMPTY = "."
    def __init__(self, size):
        self.N = size
        self.queens = [] # list of columns, where the index represents the row
        self.rooks = []
        self.bishops = []
        self.blanks = []

        self.existing_bishops = []
        self.existing_rooks = []

        self.board = np.empty(shape = (self.N, self.N))

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
        self.tilted_board = self.board
        self.board = np.empty(shape=(self.N, self.N))

        for row in range(0, self.tilted_board.shape[0]):
            for col in range(0, self.tilted_board.shape[1]):
                if self.tilted_board[row, col] == False:
                    continue
                coords = self.convert_to_board_coords(row, col)
                #print(f"{row},{col} {self.tilted_board[row][col]}==> {coords}")
                self.board[coords[0], coords[1]] = self.tilted_board[row,col]



    def create_pivot_board(self):

        self.pivot_board = np.full( shape=(2*self.N, 2*self.N),
                                     fill_value = False,
                                     dtype=np.bool)

        for row in range(0, self.N):
            for col in range(0, self.N):
                # 45 rotation, x+y, y-x
                # and a translation up N to avoid nulls
                coords = self.convert_to_tilted_board_coords(row, col)
                self.pivot_board[coords[0], coords[1]] = True

                check_coords = self.convert_to_board_coords(*coords)
                if (row,col) != check_coords:
                    raise Exception("problem")

        self.board = self.pivot_board

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
        if self.board[row, col] == Board.INVALID:
            return False

        for r, c in enumerate(self.rooks):
            if r == row or c == col :
                return False

        for r, c in self.existing_pieces:
            if r == row or c == col :
                return False

        return True

    def print_the_board(self):
        print ("solution:")
        self.board = np.full(shape = (self.N, self.N),
                             fill_value = '.',
                             dtype = np.chararray)

        for row, col in self.bishops:
            assert self.board[row,col] == '.'
            self.board[row, col]='b'

        for row, col in self.existing_bishops:
            assert self.board[row, col] == '.'
            self.board[row,col]='B'

        for row, col in self.existing_rooks:
            if self.board[row,col] != '.':
                self.board[row,col] = 'o'
            else:
                self.board[row,col] = 'R'

        for row, col in self.rooks:
            assert self.board[row, col] != 'o'
            assert self.board[row, col] != 'R'
            assert self.board[row, col] != 'r'
            if self.board[row, col] != '.':
                self.board[row, col] = 'o'
            else:
                self.board[row, col] = 'r'

        print(self.board)

    def solution(self, is_rooks):

        if is_rooks:
            piece_array = self.rooks

            self.board = np.full(shape = (self.N,  self.N),
                                       fill_value = True,
                                       dtype = np.bool)

            for row,col in self.existing_rooks:
                self.board[row] = False
                self.board[:, col] = False

        else:
            piece_array = self.bishops
            self.create_pivot_board()

            for r, c in self.existing_bishops:

                row,col = self.convert_to_tilted_board_coords(r,c)
                self.board[row] = False
                self.board[:, col] = False

        n_rows = self.board.shape[0]
        piece_array.clear()

        for i in range(0, n_rows):
            # Find row with smallest number of empty columns (value 0)
            row_sums = np.sum(self.board, axis = 1)

            # Need to make rows with no spots unattractive
            row_sums[row_sums == 0] = 3 * n_rows
            # Find first free column
            min_row = np.argmin(row_sums)

            min_col = np.argmax(self.board[min_row])

            if self.board[min_row, min_col] == False:
                break

            if is_rooks:
                piece_array.append((min_row, min_col))
            else:
                piece_array.append(self.convert_to_board_coords(min_row, min_col))

            self.board[min_row] = False
            self.board[:,min_col] = False



q = Board(5)

#q.existing_bishops.append((2,2))
#q.existing_bishops.append((2, 1))

q.existing_rooks.append( (4,1))

q.solution(is_rooks = True)
q.solution(is_rooks = False)

q.print_the_board()

#q.create_pivot_board()

#print(q.board)

#q.unpivot_board()

#print(q.board)

sys.exit(0)

q.solution(is_rooks = True)
q.solution(is_rooks = False)

q.print_the_board()