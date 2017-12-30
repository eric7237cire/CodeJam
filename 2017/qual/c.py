
from typing import Tuple
import numpy as np
from concurrent.futures import ProcessPoolExecutor, as_completed


def solve_brute_force(n:int, k:int) -> Tuple[int,int]:

    stalls = np.zeros((n+2), dtype=bool)
    stalls[0] = True
    stalls[-1] = True

    if n == k:
        return 0, 0

    print(f"Starting {n} {k}")
    for i in range(0, k):

        max_min = -1
        max_max = 0
        chosen_pos = -1

        for pos in range(1, n+2):

            if stalls[pos]:
                continue

            # Find left distance
            for lpos in range(pos-1, -1, -1):
                if stalls[lpos]:
                    left_position = lpos
                    break

            for rpos in range(pos+1, n+2):
                if stalls[rpos]:
                    right_position = rpos
                    break

            my_min = min(pos-left_position-1, right_position-pos-1)
            my_max = max(pos-left_position-1, right_position-pos-1)

            if my_min > max_min:
                chosen_pos = pos
                max_min = my_min
                max_max = my_max

            elif my_min >= max_min:

                if my_max > max_max:
                    chosen_pos = pos
                    max_max = my_max

        stalls[chosen_pos] = True

        #print(stalls)

    return max_max, max_min

def main():

    #solve_brute_force(4, 2)

    #return
    file_base = "small"
    ext = "-1"
    #file_base = "large"
    input_file_name = f"C-{file_base}-practice{ext}.in"
    output_file_name = f"C-{file_base}-practice{ext}.out"

    with open(output_file_name, "w") as output_file, \
            ProcessPoolExecutor(max_workers = 7) as executor, \
            open(input_file_name) as input_file:

        n_cases = int(input_file.readline())

        results = []

        for i in range( n_cases):

            n, k = input_file.readline().split(" ")

            results.append(executor.submit(solve_brute_force, int(n), int(k)))

        for i in range( 0, len(results) ):
            output_file.write(f"Case #{i+1}: ")

            mx, mn = results[i].result()
            output_file.write( f"{mx} {mn}" )

            output_file.write("\n")
            output_file.flush()
if __name__ == "__main__":
    main()