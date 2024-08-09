import java.io.*;

public class ReferencesComparison {
    /**
     *
     *
     * @param args
     */
    public static void main(String[] args) {
//        Note: replace NBPS with a space after copying the paper names from excell file to papers.txt
//        compare("/papers.txt", "/references.txt", "results.txt");
//        compare("hit.txt", "smarter-query.txt" , "results-smart.txt");
//        compare("hit.txt", "title.txt", "results-title.txt");
        compare("hit-final.txt", "ref-final.txt", "result-final.txt");
    }

    public static void compare(String fromFile, String toFile, String resultFile){
        try {
            BufferedReader papersReader =
                    new BufferedReader(new InputStreamReader(ReferencesComparison.class.getResourceAsStream(fromFile)));
            BufferedReader referencesReader =
                    new BufferedReader(new InputStreamReader(ReferencesComparison.class.getResourceAsStream(toFile)));
            BufferedWriter resultsWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(
                    "src/main/resources/"+resultFile))));

            // Read references file and remove all newline characters and spaces, convert to lowercase
            StringBuilder referencesContent = new StringBuilder();
            String referencesLine;
            while ((referencesLine = referencesReader.readLine()) != null) {
                referencesContent.append(referencesLine.replaceAll("\\s+", "")
                        .replaceAll("-", "")
                        .toLowerCase());
            }
            referencesReader.close();

            String referencesText = referencesContent.toString();


            String line;
            while ((line = papersReader.readLine()) != null) {
                String[] parts = line.split("\\s+", 2);
                String content = parts[1].replaceAll("\\s+", "").replaceAll("-","").toLowerCase();  // Remove all spaces and convert to lowercase

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
