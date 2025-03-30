package lab_4;

import java.util.Random;

public class RegexGenerator {
    private static final Random random = new Random();

    public static String generateFromRegex(String regex) {
        regex = regex.replaceAll("\\s", ""); // Eliminăm spațiile
        return processRegex(regex);
    }

    private static String processRegex(String regex) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < regex.length(); i++) {
            char c = regex.charAt(i);
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
}
