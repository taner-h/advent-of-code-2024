const fs = require("fs");

const test2 =
  "xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))";

function findValidMuls(text: string) {
  const regex = /mul\(\d{1,3},\d{1,3}\)|do\(\)|don't\(\)/g;
  const matches = text.match(regex);
  console.log(matches);
  return matches!;
}

function findEnabledMuls(muls: RegExpMatchArray) {
  const enabledMuls: string[] = [];
  let isEnabled = true;
  for (const mul of muls) {
    if (mul === "do()") {
      isEnabled = true;
    }
    if (mul === "don't()") {
      isEnabled = false;
    }
    if (mul.includes("mul") && isEnabled) {
      enabledMuls.push(mul);
    }
  }
  return enabledMuls;
}

function performMulOperation(muls: string[]) {
  return muls.map((mul: string) => {
    console.log(mul);
    const numbers = mul.match(/\d{1,3}/g)!;
    const result = parseInt(numbers[0]) * parseInt(numbers[1]);
    return {
      mul,
      result,
    };
  });
}

function part2(input: string) {
  const muls = findValidMuls(input);
  const enabledMuls = findEnabledMuls(muls);
  const results = performMulOperation(enabledMuls);
  const sum = results.reduce((acc, curr) => acc + curr.result, 0);
  console.log(sum);
}

const file2 = fs.readFileSync("input.txt", "utf8");
part2(file2);
