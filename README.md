# Word Frequency Analysis and Zipf's Law Verification

This Java application analyzes text documents to count word frequencies, visualize the distribution, and verify if the word frequencies follow Zipf's law. The project includes an Azure Pipeline for continuous integration and testing.

## Features

- Counts word frequencies in text files or Wikipedia XML dumps
- Displays top N most frequent words
- Generates a histogram of word frequencies
- Analyzes if the word distribution follows Zipf's law
- Processes Wikipedia XML dumps
- Includes automated tests
- Azure Pipeline integration for CI/CD

## Prerequisites

- Java 18 or higher
- Maven 3.6.0 or higher
- Python 3.6+ (for processing Wikipedia dumps)
- Azure DevOps account (for pipeline execution)

## Building the Project

```bash
# Build the project and create an executable JAR with dependencies
mvn clean package
```

This will create two JAR files in the `target` directory:
- `SoftwareQualityAssuranceSE-1.0-SNAPSHOT.jar` - Regular JAR
- `SoftwareQualityAssuranceSE-1.0-SNAPSHOT-jar-with-dependencies.jar` - Fat JAR with all dependencies

## Usage

### Basic Usage

```bash
# Analyze a text file and save results to 'output' directory
java -jar target/SoftwareQualityAssuranceSE-1.0-SNAPSHOT-jar-with-dependencies.jar input.txt output
```

### Processing Wikipedia Dumps

1. Download a Wikipedia XML dump from [https://dumps.wikimedia.org/backup-index.html](https://dumps.wikimedia.org/backup-index.html)
2. Use the provided Python script to process the dump:

```bash
# Process a Wikipedia XML dump
python process_wiki_dump.py path/to/wikipedia_dump.xml output
```

### Azure Pipeline

The project includes an `azure-pipelines.yml` file that defines a CI/CD pipeline with the following stages:
1. **Build**: Compiles the code and runs tests
2. **Analyze**: Processes a sample Wikipedia dump and generates analysis

To set up the pipeline:
1. Push the code to an Azure DevOps repository
2. Create a new pipeline and select the `azure-pipelines.yml` file
3. Run the pipeline

## Output Files

- `word_frequencies.png`: Histogram of word frequencies (log-log scale)
- `zipf_analysis.txt`: Analysis of Zipf's law fit with:
  - Slope (should be close to -1 for perfect Zipf's law)
  - R² value (goodness of fit, 1.0 is perfect)
  - Number of unique words analyzed

## Example Output

```
Top 50 most frequent words:
Rank    Word    Frequency
----    ----    --------
1       the     45
2       and     32
3       of      28
...
```

## Understanding Zipf's Law

Zipf's law states that in a corpus of natural language, the frequency of any word is inversely proportional to its rank in the frequency table. In practice, this means:
- The most frequent word appears approximately twice as often as the second most frequent word
- Three times as often as the third most frequent word
- And so on...

A perfect Zipf's law distribution would have a slope of -1 on a log-log plot of rank vs. frequency.

## Testing

Run the test suite with:

```bash
mvn test
```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
