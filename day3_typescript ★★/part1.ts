const fs = require("fs");

const test1 =
  "xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))";

function findValidMuls1(text: string) {
  const regex = /mul\(\d{1,3},\d{1,3}\)/g;
  const matches = text.match(regex);
  return matches!;
}

function performMulOperation1(muls: RegExpMatchArray) {
  return muls.map((mul: string) => {
    const numbers = mul.match(/\d{1,3}/g)!;
    const result = parseInt(numbers[0]) * parseInt(numbers[1]);
    return {
      mul,
      result,
    };
  });
}

function part1(input: string) {
  const muls = findValidMuls1(input);
  const results = performMulOperation1(muls);
  const sum = results.reduce((acc, curr) => acc + curr.result, 0);
  console.log(sum);
}

const file = fs.readFileSync("input.txt", "utf8");
part1(file);
