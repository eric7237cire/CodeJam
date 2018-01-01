import numpy as np
import math

def solve(N, P, R, Q) -> int:

    print("\nStarting solve")
    events = []
    for i in range(0, N):
        required_amount = R[i]

        for p in range(0, P):
            package_size = Q[i][p]

            # problem is floating point
            min_servings = math.ceil(package_size / (1.1 * required_amount) )
            max_servings = math.floor( package_size / (.9 * required_amount) )


            max_servings = (10 * package_size) // (9 * required_amount)
            min_servings = (10 * package_size + 11 * required_amount - 1) // (11 * required_amount)

            print(f"For ingredient {i}, package # {p}. "
                  f" Required per serving = {required_amount} "
                  f"Package size = {package_size} "
                  f"Min = {min_servings} Max = {max_servings}")

            if min_servings == 0: min_servings = 1

            if min_servings > max_servings:
                continue

            events.append((min_servings, False, i, package_size))
            events.append((max_servings, True, i, package_size))

    # Code based on https://www.go-hero.net/jam/17/name/Nore
    events.sort()
    cnt = 0
    counts = [[] for _ in range(N)]
    remv = [0] * N
    for (boundary, is_upper_bound,
         ingredient_index, package_size) in events:
        #print(f"Saw event Boundary={boundary} {is_upper_bound} ingredient={ingredient_index} package={package_size}")
        #print(counts, remv)
        if is_upper_bound:
            if remv[ingredient_index] > 0:
                remv[ingredient_index] -= 1
            # elif yy in counts[i]:
            else:
                counts[ingredient_index].remove(package_size)
        else:
            counts[ingredient_index].append(package_size)
            if all(counts):
                cnt += 1
                for ii in range(N):
                    counts[ii].remove(min(counts[ii]))
                    remv[ii] += 1
    return cnt


def main():

    file_base = "small"
    ext = ""
    file_base = "large"
    input_file_name = f"B-{file_base}-practice{ext}.in"
    output_file_name = f"B-{file_base}-practice{ext}.out"

    with open(output_file_name, "w") as output_file,open(input_file_name) as input_file:

        n_cases = int(input_file.readline())

        for i in range( n_cases):

            N, P = map(int, input_file.readline().split(" "))

            R = list(map(int, input_file.readline().split(" ")))

            Q = []

            for _ in range(0, N):
                Q.append(list(map(int, input_file.readline().split(" "))))

            max_kits = solve(N, P, R, Q)

            output_file.write(f"Case #{i+1}: {max_kits}\n")

if __name__ == "__main__":
    main()