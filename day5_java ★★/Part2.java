import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

class Rule {
  int first;
  int later;

  public Rule(int first, int later) {
    this.first = first;
    this.later = later;
  }
}

class Page {
  int pageNumber;
  int[] mustPrecede;
  int[] mustFollow;

  public Page(int pageNumber, Rule[] rules) {
    this.pageNumber = pageNumber;
    this.mustPrecede = findPrecedingRules(pageNumber, rules);
    this.mustFollow = findFollowingRules(pageNumber, rules);
  }

  private int[] findPrecedingRules(int page, Rule[] rules) {
    ArrayList<Rule> precedingRules = new ArrayList<Rule>();
    for (int i = 0; i < rules.length; i++) {
      if (rules[i].first == page) {
        precedingRules.add(rules[i]);
      }
    }
    int[] precedingRuleArray = new int[precedingRules.size()];
    for (int i = 0; i < precedingRules.size(); i++) {
      precedingRuleArray[i] = precedingRules.get(i).later;
    }
    return precedingRuleArray;
  }

  private int[] findFollowingRules(int page, Rule[] rules) {
    ArrayList<Rule> followingRules = new ArrayList<Rule>();
    for (int i = 0; i < rules.length; i++) {
      if (rules[i].later == page) {
        followingRules.add(rules[i]);
      }
    }
    int[] followingRuleArray = new int[followingRules.size()];
    for (int i = 0; i < followingRules.size(); i++) {
      followingRuleArray[i] = followingRules.get(i).first;
    }
    return followingRuleArray;
  }
}

class PrintUpdate {
  Page[] pages;
  boolean isValid = false;
  int middlePage;
  ArrayList<Rule> relevantRules;
  Page[] sortedPages;
  int invalidMiddlePage;

  public PrintUpdate(int[] pages, Rule[] rules) {
    this.pages = generatePages(pages, rules);
    this.isValid = checkValidityOfPrint();
    this.middlePage = findMiddlePage(this.pages);
    if (!this.isValid) {
      this.relevantRules = findRelevantRules(pages, rules);
      this.sortedPages = sortInvalidPages();
      this.invalidMiddlePage = findMiddlePage(this.sortedPages);

      // for (Rule rule : this.relevantRules) {
      // System.out.println(rule.first + ", " + rule.later);
      // }
      // System.out.println();

    }
  }

  private ArrayList<Rule> findRelevantRules(int[] pages, Rule[] rules) {
    ArrayList<Rule> relevantRules = new ArrayList<Rule>();

    for (Rule rule : rules) {
      boolean isFirstNumberInPages = isElementInArray(rule.first, pages);
      boolean isLaterNumberInPages = isElementInArray(rule.later, pages);

      if (isFirstNumberInPages && isLaterNumberInPages) {
        relevantRules.add(rule);
      }
    }
    return relevantRules;
  }

  private Page[] sortInvalidPages() {
    Page[] sortedPages = new Page[this.pages.length];
    int[] relevantRuleLaters = new int[this.relevantRules.size()];

    for (int i = 0; i < this.relevantRules.size(); i++) {
      relevantRuleLaters[i] = this.relevantRules.get(i).later;
    }

    for (Page page : this.pages) {
      int count = countOccurences(relevantRuleLaters, page.pageNumber);
      sortedPages[this.pages.length - count - 1] = page;
    }

    return sortedPages;
  }

  private int countOccurences(int[] array, int element) {
    int count = 0;
    for (int i = 0; i < array.length; i++) {
      if (array[i] == element) {
        count++;
      }
    }
    return count;
  }

  private boolean isElementInArray(int element, int[] array) {
    for (int i = 0; i < array.length; i++) {
      if (array[i] == element) {
        return true;
      }
    }
    return false;
  }

  private Page[] generatePages(int[] pageNumbers, Rule[] rules) {
    Page[] pages = new Page[pageNumbers.length];
    for (int i = 0; i < pageNumbers.length; i++) {
      pages[i] = new Page(pageNumbers[i], rules);
    }

    return pages;
  }

  private boolean checkValidityOfPage(Page page, int index) {
    int pageNumber = page.pageNumber;
    int[] mustPrecede = page.mustPrecede;
    int[] mustFollow = page.mustFollow;

    for (int i = 0; i < mustPrecede.length; i++) {
      int mustPrecedeIndex = findPage(mustPrecede[i]);
      if (mustPrecedeIndex != -1) {
        if (mustPrecedeIndex < index) {
          return false;
        }
      }
    }

    for (int i = 0; i < mustFollow.length; i++) {
      int mustFollowIndex = findPage(mustFollow[i]);
      if (mustFollowIndex != -1) {
        if (mustFollowIndex > index) {
          return false;
        }
      }
    }

    return true;
  }

  private boolean checkValidityOfPrint() {
    for (int i = 0; i < pages.length; i++) {
      if (!checkValidityOfPage(pages[i], i)) {
        return false;
      }
    }
    return true;
  }

  private int findPage(int page) {
    for (int i = 0; i < pages.length; i++) {
      if (pages[i].pageNumber == page) {
        return i;
      }
    }
    return -1;
  }

  private int findMiddlePage(Page[] pages) {
    int middleIndex = pages.length / 2;
    return pages[middleIndex].pageNumber;
  }
}

class Manual {
  String text;
  Rule[] rules;
  PrintUpdate[] printUpdates;
  int sum = 0;
  int invalidSum = 0;

  public Manual(String text) {
    this.text = text;
    parseManual(text);
    calculateSum();
    calculateInvalidSum();
    printSums();
  }

  private void printSums() {
    System.out.println("valid sum: " + this.sum);
    System.out.println("invalid sum: " + this.invalidSum);
  }

  private void calculateSum() {
    for (int i = 0; i < this.printUpdates.length; i++) {
      if (this.printUpdates[i].isValid) {
        this.sum += this.printUpdates[i].middlePage;
      }
    }
  }

  private void calculateInvalidSum() {
    for (int i = 0; i < this.printUpdates.length; i++) {
      if (!this.printUpdates[i].isValid) {
        this.invalidSum += this.printUpdates[i].invalidMiddlePage;
      }
    }
  }

  private void parseManual(String text) {
    String[] parts = text.split("\n\n");
    String rulesString = parts[0];
    String printUpdatesString = parts[1];

    parseRules(rulesString);
    parsePrintUpdates(printUpdatesString);
  }

  private void parseRules(String string) {
    String[] lines = string.split("\n");
    Rule[] rules = new Rule[lines.length];
    ;

    for (int i = 0; i < lines.length; i++) {
      String line = lines[i];
      String[] numbers = line.split("\\|");
      int first = Integer.parseInt(numbers[0]);
      int later = Integer.parseInt(numbers[1]);

      rules[i] = new Rule(first, later);
    }
    this.rules = rules;
  }

  private void parsePrintUpdates(String string) {
    String[] lines = string.split("\n");
    PrintUpdate[] printUpdates = new PrintUpdate[lines.length];
    ;

    for (int i = 0; i < lines.length; i++) {
      String line = lines[i];
      String[] numbersStrings = line.split(",");
      int[] numbers = new int[numbersStrings.length];

      for (int j = 0; j < numbersStrings.length; j++) {
        numbers[j] = Integer.parseInt(numbersStrings[j]);
      }
      printUpdates[i] = new PrintUpdate(numbers, this.rules);
    }
    this.printUpdates = printUpdates;
  }
}

public class Part2 {
  public static void main(String[] args) {

    String test = "47|53\n97|13\n97|61\n97|47\n75|29\n61|13\n75|53\n29|13\n97|29\n53|29\n61|53\n97|53\n61|29\n47|13\n75|47\n97|75\n47|61\n75|61\n47|29\n75|13\n53|13\n\n75,47,61,53,29\n97,61,53,29,13\n75,29,13\n75,97,47,61,53\n61,13,29\n97,13,75,29,47";

    // Manual manual = new Manual(test);

    try {
      String input = Files.readString(Paths.get("./input.txt"));

      Manual manual = new Manual(input);
    } catch (IOException e) {
      System.err.println("Error reading file: " + e.getMessage());
    }
  }

}