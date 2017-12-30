
from typing import Tuple
import numpy as np
from concurrent.futures import ProcessPoolExecutor, as_completed


def solve(n:int, k:int) -> Tuple[int,int]:

    if n == k:
        return 0,0

    # Find how big a balanced binary tree we need
    # Tree of height 1 has 1 node
    # h=2 = 3 nodes
    # h=3 = 7 nodes == 2^3 - 1
    for s in range(1, k+1):
        if 2**s - 1 >= k:
            tree_height = s
            break

    # Find out how many holes taken by the tree up to the last row

    #holes_left = n - 2**(tree_size-1) + 1

    #Place k-1 folks
    holes_left = n - (k-1)
    width_tree = 2**(tree_height-1)
    min_hole_size = holes_left // width_tree
    an_extra = holes_left % width_tree > 0

    if an_extra:
        hole_size = min_hole_size + 1
    else:
        hole_size = min_hole_size

    min_dist = (hole_size-1) // 2

    if hole_size % 2 == 1:
        return min_dist, min_dist
    else:
        return  min_dist + 1, min_dist

def solve_brute_force(n:int, k:int, p:bool = False) -> Tuple[int,int]:

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

        if p:
            print(stalls)

    return max_max, max_min

def main():

    if False:
        n = 7
        k = 4
        a1 = solve_brute_force(n, k, p=True)
        a2 = solve(n, k)
        print(f"Answer {a1} or {a2}")
        #return

        for n in range(1, 100):
            for k in range(1, n+1):
                print(f"N={n} K={k}")
                #a1=a2=1
                a1=solve_brute_force(n,k)
                a2=solve(n, k)

                print(f"Answer {a1} or {a2}")

                if a1 != a2:
                    raise Exception("ruh")
        return

    #return
    file_base = "small"
    ext = "-2"
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

            #results.append(executor.submit(solve_brute_force, int(n), int(k)))
            results.append(executor.submit(solve, int(n), int(k)))

        for i in range( 0, len(results) ):
            output_file.write(f"Case #{i+1}: ")

            mx, mn = results[i].result()
            output_file.write( f"{mx} {mn}" )

            output_file.write("\n")
            output_file.flush()
if __name__ == "__main__":
    main()