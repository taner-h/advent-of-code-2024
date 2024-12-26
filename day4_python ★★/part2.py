test = """MMMSXXMASM
MSAMXMSMSA
AMXSXMAAMM
MSAMASMSMX
XMASAMXAMM
XXAMMXXAMA
SMSMSASXSS
SAXAMASAAA
MAMMMXMMMM
MXMXAXMASX"""

def parse_input(input):
    lines = [line for line in input.split("\n")]
    chars = []
    for line in lines:
        if line:
            chars.append([char for char in line])
    return chars


def detect_a_positions(grid):
    a_positions = []
    row_count = len(grid)
    column_count = len(grid[0])

    for row in range(row_count):
        for column in range(column_count):
            if grid[row][column] == "A":
                a_positions.append((row, column))

    return a_positions


def get_movement_changes(direction):
    movements = [(1, 1), (1, -1), (-1, -1), (-1, 1)]
    return movements[direction]


def check_if_out_of_bound(position, movement, column_count, row_count):
    row, column = position
    new_row = row + movement[0]
    new_column = column + movement[1]

    if new_row < 0 or new_row >= row_count:
        return True
    if new_column < 0 or new_column >= column_count:
        return True
    return False


def perform_move(grid, position, movement):
    x, y = position
    new_x = x + movement[0]
    new_y = y + movement[1]

    row_count = len(grid)
    column_count = len(grid[0])

    if check_if_out_of_bound((x, y), movement, column_count, row_count):
        return False

    return grid[new_x][new_y]


def solve_puzzle(grid, a_positions):
    solved = 0
    solved_positions = []

    opposite_direction = [2, 3]

    for x, y in a_positions:
        pair_found = 0
        for direction in range(2):
            movement = get_movement_changes(direction)
            opposite_movement = get_movement_changes(opposite_direction[direction])

            destination_movement = perform_move(grid, (x, y), movement)
            destination_opposite = perform_move(grid, (x, y), opposite_movement)

            if (destination_movement == "S" and destination_opposite == "M") or (
                destination_movement == "M" and destination_opposite == "S"
            ):
                pair_found += 1

        if pair_found >= 2:
            solved += 1
            solved_positions.append((x, y))

    print(solved_positions)
    return solved


def part2(input):
    grid = parse_input(input)
    a_positions = detect_a_positions(grid)
    solution = solve_puzzle(grid, a_positions)
    print(solution)


with open("input.txt", "r") as file:
    input = file.read()
    part2(input)