import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Database db = new Database();
        System.out.println("Program is ready and waiting for user command");

        while (true) {
            String input = reader.readLine().toLowerCase().replace(" ", "");

            Pattern pattern = Pattern.compile("(createindexonproject2dataset)|" +
                    "(select\\*fromproject2datasetwhererandomv!=?(\\d+))|" +
                    "(select\\*fromproject2datasetwhererandomv=?(\\d+))|" +
                    "(select\\*fromproject2datasetwhererandomv>(\\d+)andrandomv<(\\d+))|" +
                    "(quit|exit)");
            Matcher matcher = pattern.matcher(input);

            if (matcher.find()) {
                if (matcher.group(1) != null) {
                    db.createIndexes();
                } else if (matcher.group(3) != null) {
                    int value = Integer.parseInt(matcher.group(3));
                    db.search(2, value, 0);
                } else if (matcher.group(5) != null) {
                    int value = Integer.parseInt(matcher.group(5));
                    db.search(0, value, 0);
                } else if (matcher.group(7) != null && matcher.group(8) != null) {
                    int firstValue = Integer.parseInt(matcher.group(7));
                    int secondValue = Integer.parseInt(matcher.group(8));
                    db.search(1, firstValue, secondValue);
                } else if (matcher.group(9) != null) {
                    System.out.println("Exiting");
                    return;
                }
            } else {
                System.out.println("Unrecognized input, please try again");
            }
        }
    }
}