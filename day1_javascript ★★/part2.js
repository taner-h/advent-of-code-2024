const test = `3   4
4   3
2   5
1   3
3   9
3   3`;

function parseIntoPairs(input) {
  const left = [];
  const right = [];

  const lines = input.split("\n");
  lines.forEach((line) => {
    const regex = /\d+/g;
    const numbers = line.match(regex);

    if (!numbers) return;

    left.push(parseInt(numbers[0]));
    right.push(parseInt(numbers[1]));
  });

  return { left, right };
}

function sortArray(array) {
  return array.sort((a, b) => a - b);
}

function calculateDistances(left, right) {
  let sum = 0;
  left.forEach((leftValue, index) => {
    const count = countOccurences(right, leftValue);
    sum += count * leftValue;
  });
  return sum;
}

function countOccurences(array, element) {
  let count = 0;
  array.forEach((value) => {
    if (value === element) {
      count++;
    }
  });

  return count;
}

function part2(input) {
  const { left, right } = parseIntoPairs(input);
  const sortedLeft = sortArray(left);
  const sortedRight = sortArray(right);

  const sum = calculateDistances(sortedLeft, sortedRight);
  console.log(sum);
}

const fs = require("fs");

const input = fs.readFileSync("./input.txt").toString();

console.log(input);

part2(input);
