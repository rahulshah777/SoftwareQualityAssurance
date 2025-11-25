package org.zorba;

import org.apache.commons.io.FileUtils;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {
    private static final Pattern WORD_PATTERN = Pattern.compile("[^\\p{L}0-9]+");
    private static final int TOP_N_WORDS = 50;
    private static final int HISTOGRAM_WIDTH = 1200;
    private static final int HISTOGRAM_HEIGHT = 800;

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java -jar your-jar-file.jar <input-file> [output-dir]");
            System.exit(1);
        }

        String inputFile = args[0];
        String outputDir = args.length > 1 ? args[1] : ".";

        try {
            // Read and process the input file
            System.out.println("Reading input file: " + inputFile);
            String content = FileUtils.readFileToString(new File(inputFile), StandardCharsets.UTF_8);
            
            // Count word frequencies
            Map<String, Integer> wordFrequencies = countWordFrequencies(content);
            
            // Sort by frequency in descending order
            List<Map.Entry<String, Integer>> sortedFrequencies = wordFrequencies.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .collect(Collectors.toList());

            // Print top N words
            System.out.println("\nTop " + TOP_N_WORDS + " most frequent words:");
            System.out.println("Rank\tWord\tFrequency");
            System.out.println("----\t----\t--------");
            
            int rank = 1;
            for (Map.Entry<String, Integer> entry : sortedFrequencies.stream().limit(TOP_N_WORDS).collect(Collectors.toList())) {
                System.out.printf("%d\t%s\t%d%n", rank++, entry.getKey(), entry.getValue());
            }

            // Create and save histogram
            createAndSaveHistogram(sortedFrequencies, outputDir + "/word_frequencies.png");
            
            // Analyze Zipf's law
            analyzeZipfsLaw(sortedFrequencies, outputDir);
            
            System.out.println("\nAnalysis complete. Check the output directory for charts and results.");
            
        } catch (IOException e) {
            System.err.println("Error processing file: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    // Package-private for testing
    static Map<String, Integer> countWordFrequencies(String text) {
        Map<String, Integer> frequencies = new HashMap<>();
        String[] words = WORD_PATTERN.split(text.toLowerCase());
        
        for (String word : words) {
            if (!word.trim().isEmpty()) {
                frequencies.put(word, frequencies.getOrDefault(word, 0) + 1);
            }
        }
        
        return frequencies;
    }

    private static void createAndSaveHistogram(List<Map.Entry<String, Integer>> frequencies, String outputPath) throws IOException {
        XYSeries series = new XYSeries("Word Frequencies");
        
        int rank = 1;
        for (Map.Entry<String, Integer> entry : frequencies) {
            series.add(rank++, entry.getValue());
            if (rank > 100) break; // Limit to top 100 words for better visualization
        }
        
        XYSeriesCollection dataset = new XYSeriesCollection(series);
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Word Frequency Distribution (Log-Log Scale)",
                "Rank (log scale)",
                "Frequency (log scale)",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false
        );
        
        // Save the chart as a PNG file
        File chartFile = new File(outputPath);
        ChartUtils.saveChartAsPNG(chartFile, chart, HISTOGRAM_WIDTH, HISTOGRAM_HEIGHT);
        System.out.println("\nHistogram saved to: " + chartFile.getAbsolutePath());
    }

    private static void analyzeZipfsLaw(List<Map.Entry<String, Integer>> frequencies, String outputDir) throws IOException {
        // Take log of ranks and frequencies for linear regression
        SimpleRegression regression = new SimpleRegression();
        
        // We'll use the top 1000 words for better statistical significance
        int maxWords = Math.min(1000, frequencies.size());
        
        for (int i = 0; i < maxWords; i++) {
            double rank = i + 1;
            double frequency = frequencies.get(i).getValue();
            
            // Skip zeros to avoid log(0)
            if (frequency > 0) {
                regression.addData(Math.log(rank), Math.log(frequency));
            }
        }
        
        // Get the slope and intercept of the regression line
        double slope = regression.getSlope();
        double intercept = regression.getIntercept();
        double rSquared = regression.getRSquare();
        
        // Save the results to a file
        File resultsFile = new File(outputDir + "/zipf_analysis.txt");
        String results = String.format(
                "Zipf's Law Analysis\n" +
                "==================\n" +
                "- Number of unique words analyzed: %d\n" +
                "- Slope (should be close to -1 for perfect Zipf's law): %.4f\n" +
                "- Intercept: %.4f\n" +
                "- R² (goodness of fit, 1.0 is perfect): %.4f\n" +
                "\nInterpretation:\n" +
                "- A slope close to -1 suggests the distribution follows Zipf's law.\n" +
                "- The higher the R² value (closer to 1.0), the better the fit to a power law.",
                maxWords, slope, intercept, rSquared
        );
        
        FileUtils.writeStringToFile(resultsFile, results, StandardCharsets.UTF_8);
        System.out.println("\nZipf's law analysis saved to: " + resultsFile.getAbsolutePath());
    }
}