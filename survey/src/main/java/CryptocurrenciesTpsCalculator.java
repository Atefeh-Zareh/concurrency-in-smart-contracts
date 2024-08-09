import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Data is extracted from:
 * https://blockchair.com/dash/charts/transactions-per-second
 * https://blockchair.com/bitcoin/charts/transactions-per-second
 */
public class CryptocurrenciesTpsCalculator {
    public static void main(String[] args) {
        try {
            // Define the path to the resource folder
            Path resourcePath = Paths.get("src/main/resources/TPS");

            // Get all .tsv files in the TPS folder
            try (Stream<Path> paths = Files.walk(resourcePath)) {
                paths.filter(Files::isRegularFile)
                        .filter(path -> path.getFileName().toString().startsWith("data-"))
                        .filter(path -> path.toString().endsWith(".tsv"))
                        .forEach(CryptocurrenciesTpsCalculator::processFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processFile(Path filePath) {
        String fileName = filePath.getFileName().toString();
        String identifier = fileName.substring(5, fileName.lastIndexOf('.'));
        List<Double> tpsValues = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath.toFile()))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue; // Skip the first line
                }

                String[] parts = line.split("\t");
                if (parts.length == 2) {
                    try {
                        double tps = Double.parseDouble(parts[1]);
                        tpsValues.add(tps);
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid TPS value: " + parts[1]);
                    }
                }
            }

            if (!tpsValues.isEmpty()) {
                double sum = tpsValues.stream().mapToDouble(Double::doubleValue).sum();
                double average = sum / tpsValues.size();
                double min = tpsValues.stream().mapToDouble(Double::doubleValue).min().orElse(Double.NaN);
                double max = tpsValues.stream().mapToDouble(Double::doubleValue).max().orElse(Double.NaN);

                System.out.printf("%s, average=%.2f, min=%.2f, max=%.2f%n", identifier, average, min, max);
            } else {
                System.out.printf("%s, no valid TPS values found%n", identifier);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

