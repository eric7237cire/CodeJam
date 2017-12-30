
from typing import Tuple
import numpy as np

def solve_brute_force(n:int, k:int) -> Tuple[int,int]:

    stalls = np.zeros((n+2), dtype=bool)
    stalls[0] = True
    stalls[-1] = True

    max_min = -1
    min_max = 1e100
    chosen_pos = -1

    print(f"Starting {n} {k}")
    for i in range(0, k):

        for pos in range(1, n+2):

            # Find left distance
            for lpos in range(pos-1, -1, -1):
                if stalls[lpos]:
                    left_position = lpos
                    break

            for rpos in range(pos+1, n+2):
                if stalls[rpos]:
                    right_position = rpos
                    break

            my_min = min(left_position, right_position)
            my_max = max(left_position, right_position)

            if my_min > max_min:
                chosen_pos = pos
                max_min = my_min

            elif my_min >= max_min:

                if my_max < min_max:
                    chosen_pos = pos
                    min_max = my_max

        stalls[chosen_pos] = True

        #print(stalls)

    return max_min, min_max

def main():

    #solve_brute_force(4, 2)

    #return
    file_base = "small"
    ext = "-1"
    #file_base = "large"
    input_file_name = f"C-{file_base}-practice{ext}.in"
    output_file_name = f"C-{file_base}-practice{ext}.out"

    with open(output_file_name, "w") as output_file, open(input_file_name) as input_file:

        n_cases = int(input_file.readline())

        for i in range(n_cases):

            n, k = input_file.readline().split(" ")


            output_file.write(f"Case #{i+1}: ")

            mx, mn = solve_brute_force( int(n), int(k) )
            output_file.write( f"{mx} {mn}" )

            output_file.write("\n")
            output_file.flush()
if __name__ == "__main__":
    main()