test = """7 6 4 2 1
1 2 7 8 9
9 7 6 2 1
1 3 2 4 5
8 6 4 4 1
1 3 6 7 9"""


def parse_reports(input):
    lines = [line for line in input.split("\n")]
    reports = []
    for line in lines:
        levels = [int(x) for x in line.split()]
        if levels:
            reports.append({"levels": levels})
    return reports


def is_strictly_increasing(levels):
    for i in range(len(levels) - 1):
        current = levels[i]
        next = levels[i + 1]
        if current >= next:
            return False
    return True


def is_strictly_decreasing(levels):
    for i in range(len(levels) - 1):
        current = levels[i]
        next = levels[i + 1]
        if current <= next:
            return False
    return True


def is_strictly_monotonic(levels):
    return is_strictly_increasing(levels) or is_strictly_decreasing(levels)


def is_difference_valid(levels):
    for i in range(len(levels) - 1):
        current = levels[i]
        next = levels[i + 1]
        if abs(current - next) > 3 or abs(current - next) < 1:
            return False
    return True


def check_reports_safety(reports):
    for report in reports:
        levels = report["levels"]
        report["is_monotonic"] = is_strictly_monotonic(levels)
        report["is_difference_valid"] = is_difference_valid(levels)
        report["is_safe"] = report["is_monotonic"] and report["is_difference_valid"]

        print(
            f"Levels: {levels}\nMonotonic: {report['is_monotonic']}\nDifference: {report['is_difference_valid']}\nSafe: {report['is_safe']}\n\n"
        )
    return reports


def part1(input):
    reports = parse_reports(input)
    reports = check_reports_safety(reports)
    safe_reports = [report for report in reports if report["is_safe"]]
    print(len(safe_reports))


with open("input.txt", "r") as file:
    input = file.read()
    part1(input)