
import numpy as np
from concurrent.futures import ProcessPoolExecutor, as_completed


def solve(grid : np.array) -> None:
    #print(f"Solving {case_no}")

    def handle_col(r,c):
        nonlocal  last_value
        value = grid[r, c]

        if last_value == '?' and value != '?':
            last_value = value
            return

        if last_value != '?' and value == '?':
            grid[r, c] = last_value

    n_rows, n_cols = grid.shape

    for c in range(0, n_cols):
        last_value = '?'
        for r in range(0, n_rows):
            handle_col(r,c)

        last_value = '?'
        for r in range(n_rows-1, -1, -1):
            handle_col(r,c)

    # Now handle blank columns

    # Copy from right
    for c in range(0, n_cols-1):

        if grid[0,c] == '?':
            grid[:,c] = grid[:, c+1]

    # Copy from left
    for c in range(n_cols-1, 0, -1):

        if grid[0, c] == '?':
            grid[:, c] = grid[:, c - 1]

    return

def main():

    #return
    file_base = "small"
    ext = ""
    #file_base = "large"
    input_file_name = f"A-{file_base}-practice{ext}.in"
    output_file_name = f"A-{file_base}-practice{ext}.out"

    with open(output_file_name, "w") as output_file, \
            ProcessPoolExecutor(max_workers = 7) as executor, \
            open(input_file_name) as input_file:

        n_cases = int(input_file.readline())

        results = []
        for i in range( n_cases):

            R, C = map(int, input_file.readline().split(" "))

            grid = np.empty( shape=(R,C), dtype=np.chararray)

            for r in range(0,R):
                for col,ch in enumerate(input_file.readline().strip()):
                    grid[r][col] = ch

            print(grid)

            solve(grid)

            output_file.write(f"Case #{i+1}:\n")
            for r in range(0, R):
                output_file.write("".join(grid[r]))
                output_file.write("\n")

if __name__ == "__main__":
    main()