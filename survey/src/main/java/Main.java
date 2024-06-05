import java.io.*;

public class Main {
    public static void main(String[] args) {
        try {
            BufferedReader papersReader = new BufferedReader(new InputStreamReader(Main.class.getResourceAsStream("/papers.txt")));
            BufferedReader referencesReader = new BufferedReader(new InputStreamReader(Main.class.getResourceAsStream("/references.txt")));
            BufferedWriter resultsWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("src/main/resources/results.txt"))));

            // Read references file and remove all newline characters and spaces, convert to lowercase
            StringBuilder referencesContent = new StringBuilder();
            String referencesLine;
            while ((referencesLine = referencesReader.readLine()) != null) {
                referencesContent.append(referencesLine.replaceAll("\\s+", "").toLowerCase());
            }
            referencesReader.close();

            String referencesText = referencesContent.toString();

            String line;
            while ((line = papersReader.readLine()) != null) {
                String[] parts = line.split("\\s+", 2);
                String content = parts[1].replaceAll("\\s+", "").toLowerCase();  // Remove all spaces and convert to lowercase

                boolean found = referencesText.contains(content);

                if (found) {
                    resultsWriter.write("available " + parts[0] + " " + parts[1].trim());
                } else {
                    resultsWriter.write("unavailable " + parts[0] + " " + parts[1].trim());
                }
                resultsWriter.newLine();
            }

            papersReader.close();
            resultsWriter.close();

            System.out.println("Results written to results.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
