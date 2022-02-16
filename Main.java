import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.*;

public class Main {
    private static final String DELIMITERS_PATTERN = "^((?://(?:\\[?[^\\[\\]]+]?)*))\\n";
    private static final String DELIMITER_PATTERN = "(?<=(?://|]))\\[?([^\\[\\]]+)*\\]?";
    private static final String INPUT_PATTERN = "(?:-?\\d+(?:(?:%s)-?\\d+)*)+$";
    private static final String NUMBER_PATTERN = "(-?)(\\d+)";
    private static final String[] DEFAULT_DELIMITERS = new String[]{",", "\\n"};
    
    static int add(String numbers) {
        System.out.println("Input: " + numbers);
        int sum = 0;
        
        Pattern pattern = Pattern.compile(DELIMITERS_PATTERN);
        Matcher matcher = pattern.matcher(numbers);
        
        ArrayList<String> customDelimiters = new ArrayList<String>();
        
        if (matcher.find()) {
            pattern = Pattern.compile(DELIMITER_PATTERN);
            matcher = pattern.matcher(matcher.group(1));
            
            while (matcher.find()) {
                if (null == matcher.group(1)) {
                    break;
                }
                
                customDelimiters.add(Pattern.quote(matcher.group(1)));
            }
        }
        
        String[] allDelimiters = Stream.concat(Arrays.stream(DEFAULT_DELIMITERS), customDelimiters.stream()).toArray(String[]::new);
        
        String delimiters = String.join("|", allDelimiters);
        String wholePattern = String.format(String.format("(?:%s)?%s", DELIMITERS_PATTERN, INPUT_PATTERN), delimiters);
        
        pattern = Pattern.compile(wholePattern);
        matcher = pattern.matcher(numbers);
        
        if (!matcher.matches()) {
            return sum;
        }
        
        pattern = Pattern.compile(NUMBER_PATTERN);
        matcher = pattern.matcher(numbers);
        
        ArrayList<String> illegalNumbers = new ArrayList<String>();

        int number;
        while (matcher.find()) {
            if (matcher.group(1).length() > 0) {
                illegalNumbers.add(matcher.group());
                continue;
            }
            
            number = Integer.parseInt(matcher.group(2));
            
            if (number > 1000) {
                continue;
            }
            
            sum += Integer.parseInt(matcher.group(2));
        }
        
        if (illegalNumbers.size() > 0) {
            throw new IllegalArgumentException("negatives not allowed: " + String.join(", ", illegalNumbers));
        }
        
        return sum;
    }

    public static void main(String[] args) {
        try {
            System.out.println("Result: " + add(""));
            System.out.println();
            System.out.println("Result: " + add("1"));
            System.out.println();
            System.out.println("Result: " + add("1,2"));
            System.out.println();
            System.out.println("Result: " + add("1\n2,3"));
            System.out.println();
            System.out.println("Result: " + add("1,\n"));
            System.out.println();
            System.out.println("Result: " + add("//;\n1;2"));
            System.out.println();
            System.out.println("Result: " + add("2,1001"));
            System.out.println();
            System.out.println("Result: " + add("//[***]\n1***2***3"));
            System.out.println();
            System.out.println("Result: " + add("//[*][%]\n1*2%3"));
            System.out.println();
            System.out.println("Result: " + add("//[***][%%%]\n1***2%%%3"));
            System.out.println();
            System.out.println("Result: " + add("//[***][%%%]\n1*2%3"));
            System.out.println();
            System.out.println("Result: " + add("1,-2,-3"));
            System.out.println();
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }
}
