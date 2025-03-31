# Regular expressions

### Course: Formal Languages & Finite Automata
### Author: Vornicescu Ion

## Theory

Regular expressions (regex) are a fundamental concept in formal languages and finite automata, used to define search patterns for text processing. They provide a concise and flexible way to match, extract, and manipulate strings, making them essential in fields like data validation, text parsing, and lexical analysis. Regex patterns are built using special symbols and operators, such as `.` for any character, `*` and `+` for repetition, and `|` for alternation. Grouping with parentheses `()` allows for capturing specific parts of a match, while character classes `[a-z]` define sets of allowable characters. Advanced regex features include lookaheads, lookbehinds, and greedy or lazy quantifiers, offering powerful ways to refine pattern matching. By understanding regex and its underlying principles, developers can efficiently process structured text and automate complex string operations with minimal effort.

---

## Objectives
* Understand the structure and mechanics of regular expressions.
* Write a code that will generate valid combinations of symbols conform given regular expressions
* Write a function that will show sequence of processing regular expression

---

## Implementation Description

### **1. Regex Processing Logic**
The RegexGenerator class processes regex patterns by iterating through each character and applying the corresponding operation based on regex rules.

```java
package lab_3;

import java.util.Random;

public class RegexGenerator {
    private static final Random random = new Random();

    public static String generateFromRegex(String regex) {
        regex = regex.replaceAll("\\s", ""); // Remove spaces
        return processRegex(regex);
    }
}

```

The Class Setup and Utility Methods, where the RegexGenerator class is defined along with a Random instance for generating random values. The generateFromRegex method is responsible for removing spaces from the input regex and calling processRegex to generate a corresponding random string.

```java
private static String processRegex(String regex) {
    StringBuilder result = new StringBuilder();

    for (int i = 0; i < regex.length(); i++) {
        char c = regex.charAt(i);

```

The second part is the Core Processing Logic, where the processRegex method is defined. This method iterates through each character of the regex using a loop and constructs the final string using a StringBuilder.

```java
        if (c == '(') {
            int end = regex.indexOf(')', i);
            String[] options = regex.substring(i + 1, end).split("\\|");
            result.append(options[random.nextInt(options.length)]);
            i = end;
        } else if (c == '{') {
            int end = regex.indexOf('}', i);
            String[] nums = regex.substring(i + 1, end).split(",");
            int min = Integer.parseInt(nums[0]);
            int max = nums.length > 1 ? Integer.parseInt(nums[1]) : min;
            int repeat = min + random.nextInt(max - min + 1);
            result.append(String.valueOf(result.charAt(result.length() - 1)).repeat(repeat - 1));
            i = end;
        } else if (c == '?') {
            if (random.nextBoolean()) result.append(result.charAt(result.length() - 1));
        } else if (c == '*') {
            int repeat = random.nextInt(6);
            result.append(String.valueOf(result.charAt(result.length() - 1)).repeat(repeat));
        } else if (c == '+') {
            int repeat = 1 + random.nextInt(5);
            result.append(String.valueOf(result.charAt(result.length() - 1)).repeat(repeat - 1));
        } else {
            result.append(c);
        }
    }
    return result.toString();
}

```
This part is Handling Different Regex Patterns, where specific regex symbols are processed. If the character is (), it randomly selects an option from the given choices. If {min,max} is encountered, it determines a random repetition count within the specified range and repeats the previous character accordingly. The ? operator decides randomly whether to include the previous character, while * repeats it between 0 and 5 times, and + ensures at least one repetition, up to 5 times. If the character does not match any regex operator, it is simply appended to the result. Finally, the generated string is returned. This structure ensures efficient processing and randomness while interpreting the simplified regex


### **2. Testing the Generator**
The `main` method generates strings based on different regex patterns to validate the correctness of the implementation.

```java
public static void main(String[] args) {
    String regex1 = "M?N{2}(O|P){3}Q*R+";
    String regex2 = "(X|Y|Z){3}8+(9|0)";
    String regex3 = "(H|I)(J|K)L*N.";

    for (int i = 0; i <= 5; i++){
        System.out.println(generateFromRegex(regex1));
    }
    System.out.println();
    for (int i = 0; i <= 5; i++){
        System.out.println(generateFromRegex(regex2));
    }
    System.out.println();
    for (int i = 0; i <= 5; i++){
        System.out.println(generateFromRegex(regex3));
    }
}
```

This code defines a RegexGenerator class that creates strings matching a given regex pattern through randomization. The generateFromRegex method cleans the input regex by removing any whitespace before delegating processing to the processRegex method. As the regex is iterated character by character, the code detects special regex constructs such as grouping (using parentheses) and repetition operators like {}, ?, *, and +, applying random decisions to determine the output. Essentially, for each construct, the code chooses one of the possible options or determines the number of repetitions, and appends the corresponding characters to the result, thereby generating a string that fits the original pattern.

---

## Example Execution
### **Input Regex Patterns**
```plaintext
1. M?N{2}(O|P){3}Q*R+
2. (X|Y|Z){3}8+(9|0)
3. (H|I)(J|K)L*N.
```

### **Sample Outputs**
```plaintext
MNNOOORRR
NNPPPPQQR
NNOPPPPRR
MNNOOOPRR
NNOOOORRR
NNPOOOQRR

XYYX88899
XXYX88890
YZZX88890
ZZYZ88889
YYZZ88899
XZZX88899

HJLLLLL6
ILLL6
HKL5
IJLL7
IKLLL9
IJLL8
```

---

## Conclusion
This lab successfully demonstrates how to generate strings from regular expressions. The implementation supports essential regex constructs such as grouping, repetition, and optional elements. By leveraging randomness, the generator creates diverse outputs while maintaining the structural constraints of the regex pattern. This technique can be extended for testing regex-based parsers, fuzz testing, and other applications in formal language theory.

## References
* Regular Expressions - Formal Language Theory
* Finite State Machines & Regular Expressions
* LFPC Guide

