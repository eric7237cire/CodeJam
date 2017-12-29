"""
We can't flip in the same place twice

So for the edges, if we have a - we must flip



"""
from typing import List
import numpy as np

def solve(pancake_row:List[bool], k:int) -> int:

    moves = 0
    for i in range(0, 2000):
        row_len = len(pancake_row)

        sum = np.sum(pancake_row)

        if sum == row_len:
            return moves

        if row_len < k:
            raise Exception("Impossible")

        if not pancake_row[0]:
            # We must flip
            for i in range(0, k):
                pancake_row[i] = not pancake_row[i]

            moves += 1
            pancake_row = pancake_row[1:]
            continue

        if not pancake_row[-1]:
            # We must flip
            for i in range(1, k+1):
                pancake_row[-i] = not pancake_row[-i]

            moves += 1
            pancake_row = pancake_row[:-1]
            continue

        pancake_row = pancake_row[1:]

    return moves

def main():

    file_base = "small"
    file_base = "large"
    input_file_name = f"A-{file_base}-practice.in"
    output_file_name = f"A-{file_base}-practice.out"

    with open(output_file_name, "w") as output_file, open(input_file_name) as input_file:

        n_cases = int(input_file.readline())

        for i in range(n_cases):

            pancake_row, k = input_file.readline().split(' ')
            k = int(k)

            bool_list = [ c == '+' for c in pancake_row ]

            #print(f"Pancake row {pancake_row}")

            output_file.write(f"Case #{i+1}: ")

            try:
                min = solve(bool_list, k)
                output_file.write(str(min))

            except Exception as ex:
              #  print(ex)
                output_file.write("IMPOSSIBLE")

            output_file.write("\n")
if __name__ == "__main__":
    main()