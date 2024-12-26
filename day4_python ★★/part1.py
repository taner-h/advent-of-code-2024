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
    print(chars)
    return chars

def detect_x_positions(grid):
    x_positions = []
    row_count = len(grid)
    column_count = len(grid[0])

    for row in range(row_count):
        for column in range(column_count):
            if grid[row][column] == "X":
                x_positions.append((row, column))

    return x_positions


def get_movement_changes(direction):
    movements = [(0, 1), (1, 1), (1, 0), (1, -1), (0, -1), (-1, -1), (-1, 0), (-1, 1)]
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


def check_move_destination(grid, position, movement, char_found):
    chars = ["M", "A", "S"]
    char_to_search = chars[char_found]

    x, y = position
    new_x = x + movement[0]
    new_y = y + movement[1]

    row_count = len(grid)
    column_count = len(grid[0])

    if check_if_out_of_bound((x, y), movement, column_count, row_count):
        return False

    if grid[new_x][new_y] == char_to_search:
        char_found += 1
        if char_found == len(chars):
            return True
        return check_move_destination(grid, (new_x, new_y), movement, char_found)
    return False


def solve_puzzle(grid, x_positions):
    row_count = len(grid)
    column_count = len(grid[0])

    solved = 0
    solved_positions = []

    for x, y in x_positions:
        char_found = 0
        for direction in range(8):
            movement = get_movement_changes(direction)
            if check_move_destination(grid, (x, y), movement, char_found):
                solved += 1
                solved_positions.append((x, y))

    return solved


def part1(input):
    grid = parse_input(input)
    x_positions = detect_x_positions(grid)
    solution = solve_puzzle(grid, x_positions)
    print(solution)

with open("input.txt", "r") as file:
    input = file.read()
    part1(input)