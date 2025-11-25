#!/usr/bin/env python3
"""
Script to extract text from a Wikipedia XML dump and process it with the Word Frequency Analyzer.
"""
import re
import sys
import subprocess
import os
from pathlib import Path

def extract_text_from_wiki_xml(xml_file):
    """Extract plain text from a Wikipedia XML dump file."""
    with open(xml_file, 'r', encoding='utf-8') as f:
        content = f.read()
    
    # Simple regex to extract text content between <text> tags
    texts = re.findall(r'<text[^>]*>(.*?)</text>', content, re.DOTALL)
    
    # Clean up the text (remove XML tags, etc.)
    cleaned_texts = []
    for text in texts:
        # Remove XML tags
        text = re.sub(r'<[^>]+>', ' ', text)
        # Remove special markers and templates
        text = re.sub(r'\{\{.*?\}\}', ' ', text, flags=re.DOTALL)
        text = re.sub(r'\[\[.*?\|(.*?)\]\]', r'\1', text)  # Handle links: [[page|display]] -> display
        text = re.sub(r'\[\[(.*?)\]\]', r'\1', text)  # Handle simple links: [[page]] -> page
        # Remove excess whitespace
        text = ' '.join(text.split())
        cleaned_texts.append(text)
    
    return '\n\n'.join(cleaned_texts)

def main():
    if len(sys.argv) < 2:
        print(f"Usage: {sys.argv[0]} <wikipedia_xml_dump> [output_dir]")
        sys.exit(1)
    
    xml_file = sys.argv[1]
    output_dir = sys.argv[2] if len(sys.argv) > 2 else 'output'
    
    # Create output directory if it doesn't exist
    os.makedirs(output_dir, exist_ok=True)
    
    # Extract text from XML
    print(f"Extracting text from {xml_file}...")
    extracted_text = extract_text_from_wiki_xml(xml_file)
    
    # Save extracted text to a temporary file
    temp_file = os.path.join(output_dir, 'extracted_text.txt')
    with open(temp_file, 'w', encoding='utf-8') as f:
        f.write(extracted_text)
    
    print(f"Extracted text saved to {temp_file}")
    
    # Run the Java application
    jar_file = os.path.join('target', 'SoftwareQualityAssuranceSE-1.0-SNAPSHOT-jar-with-dependencies.jar')
    if not os.path.exists(jar_file):
        print("Error: JAR file not found. Please build the project with 'mvn package' first.")
        sys.exit(1)
    
    print("Running word frequency analysis...")
    result = subprocess.run(
        ['java', '-jar', jar_file, temp_file, output_dir],
        capture_output=True,
        text=True
    )
    
    # Print the output
    if result.stdout:
        print("\nOutput:")
        print(result.stdout)
    
    if result.stderr:
        print("\nErrors:")
        print(result.stderr)
    
    print(f"\nAnalysis complete. Results saved to {output_dir}")

if __name__ == "__main__":
    main()
