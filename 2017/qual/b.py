"""

"""

# Brute force solver
def solve_brute_force(n:str) -> int:

    num = int(n)
    for i in range(num, 0, -1):
        if is_tidy(i):
            return i

# Part of brute force
def is_tidy(n):
    number = [int(n) for n in str(n)]

    max_digit = 0
    for pos in range(0, len(number)):

        digit = number[pos]

        if digit >= max_digit:
            max_digit = digit
            continue

        return False

    return True


def solve(n_str:str) -> int:
    """
    Basically, if we have a non decreasing number

    132  we need to find the left most max and decrease it
    and make everything else 9s

    so
    1[3]2 ==> 129

    222222[5]1 ==> 1999999
    """
    number = [int(n) for n in n_str]

    max_digit = 0
    # Keep track of first instance of max digit
    max_digit_pos = -1
    for pos in range(0, len(number)):

        digit = number[pos]

        if digit > max_digit:
            max_digit_pos = pos

        if digit >= max_digit:
            max_digit = digit
            continue

        number[max_digit_pos] -= 1
        for j in range(max_digit_pos+1, len(number)):
            number[j] = 9


    return int("".join([str(n) for n in number]))

def main():


    file_base = "small"
    file_base = "large"
    input_file_name = f"B-{file_base}-practice.in"
    output_file_name = f"B-{file_base}-practice.out"

    with open(output_file_name, "w") as output_file, open(input_file_name) as input_file:

        n_cases = int(input_file.readline())

        for i in range(n_cases):

            number = input_file.readline().strip()


            output_file.write(f"Case #{i+1}: ")

            tidy = solve( number )
            #tidy_bf = solve_brute_force(number)

            #if tidy != tidy_bf:
             #   raise Exception(f"Not ok {number}. Correct is {tidy_bf} but got {tidy}")

            output_file.write(str(tidy))

            output_file.write("\n")
if __name__ == "__main__":
    main()