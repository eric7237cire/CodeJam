import numpy as np
import math

def solve(N, P, R, Q) -> int:

    print("\nStarting solve")
    for i in range(0, N):
        required_amount = R[i]

        for p in range(0, P):
            package_size = Q[i][p]
            min_servings = math.ceil(package_size / (1.1 * required_amount) )
            max_servings = math.floor( package_size / (.9 * required_amount) )

            print(f"For ingredient {i}, package # {p}. "
                  f" Required per serving = {required_amount} "
                  f"Package size = {package_size} "
                  f"Min = {min_servings} Max = {max_servings}")



def main():

    file_base = "small"
    ext = ""
    #file_base = "large"
    input_file_name = f"B-{file_base}-practice{ext}.in"
    output_file_name = f"B-{file_base}-practice{ext}.out"

    with open(output_file_name, "w") as output_file,open(input_file_name) as input_file:

        n_cases = int(input_file.readline())

        for i in range( n_cases):

            N, P = map(int, input_file.readline().split(" "))

            R = list(map(int, input_file.readline().split(" ")))

            Q = []

            for i in range(0, N):
                Q.append(list(map(int, input_file.readline().split(" "))))

            solve(N, P, R, Q)

if __name__ == "__main__":
    main()