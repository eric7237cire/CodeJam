
import numpy as np
from concurrent.futures import ProcessPoolExecutor, as_completed

class Board:

    def __init__(self, size):
        self.N = size
        self.rooks = []
        self.bishops = []

        self.existing_bishops = []
        self.existing_rooks = []

        self.board = np.empty(shape = (self.N, self.N))

    def convert_to_tilted_board_coords(self, row, col):
        # https://math.stackexchange.com/questions/383321/rotating-x-y-points-45-degrees
        return  row+col  , col - row + self.N

    def convert_to_board_coords(self, row, col):
        # Kind of guessed this one, looks the translation needs to be spread around too
        return int((row-col)/2 + self.N / 2), int((row+col) / 2 - self.N / 2)

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
                assert (row,col) == check_coords

        self.board = self.pivot_board


    def write_solution_lines(self, out_file):

        n_rows, n_cols = self.board.shape
        for row in range(0, n_rows):
            for col in range(0, n_cols):
                coord = (row,col)
                if coord in self.bishops and \
                        (coord in self.rooks or coord in self.existing_rooks):
                    out_file.write(f"o {row+1} {col+1}\n")
                elif coord in self.rooks and coord in self.existing_bishops:
                    out_file.write(f"o {row+1} {col+1}\n")
                elif coord in self.rooks:
                    out_file.write(f"x {row+1} {col+1}\n")
                elif coord in self.bishops:
                    out_file.write(f"+ {row+1} {col+1}\n")

    def solve(self):
        self.solution(is_rooks = True)
        self.solution(is_rooks = False)

        return self

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


def main():

    #return
    file_base = "small"
    ext = ""
    #file_base = "large"
    input_file_name = f"D-{file_base}-practice{ext}.in"
    output_file_name = f"D-{file_base}-practice{ext}.out"

    with open(output_file_name, "w") as output_file, \
            ProcessPoolExecutor(max_workers = 7) as executor, \
            open(input_file_name) as input_file:

        n_cases = int(input_file.readline())
        results = []
        for i in range( n_cases):

            n_str, m_str = input_file.readline().split(" ")

            b = Board(size=int(n_str))

            # + are bishops
            # x are rooks

            for m in range(0, int(m_str)):
                m_type, row_str, col_str = input_file.readline().split(" ")

                if m_type in ['o', 'x']:
                    b.existing_rooks.append((int(row_str)-1, int(col_str)-1))
                if m_type in ['o', '+']:
                    b.existing_bishops.append((int(row_str)-1, int(col_str)-1))

            results.append(executor.submit(b.solve))

        for i in range(0, len(results)):
            b = results[i].result()

            print(f"Done with {i} ")

            score = len(b.existing_bishops) + len(b.bishops) + \
                len(b.existing_rooks) + len(b.rooks)
            added_pieces = len(set(b.rooks + b.bishops))
            output_file.write(f"Case #{i+1}: {score} {added_pieces}\n")

            b.write_solution_lines(out_file = output_file)
if __name__ == "__main__":
    main()