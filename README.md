# File System Organization

File System Organization is a Java program designed to automatically organize and structure documents on a server based on their metadata. The program uses a hierarchical tree structure to sort documents dynamically by their tags and access frequencies, ensuring efficient retrieval and organization.

---

## Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Requirements](#requirements)
- [Setup](#setup)
- [Commands](#commands)
- [Examples](#examples)
- [Notes](#notes)

---

## Overview
The goal of the program is to:
- Dynamically organize documents into a tree-like folder structure.
- Optimize access to frequently used documents by adjusting their placement in the structure.
- Use metadata such as tags, document types, and access frequencies to determine the structure.

The program processes metadata provided in a text file and outputs a structured tree hierarchy based on the calculated information gain for each tag.

---

## Features
- **Document Metadata Parsing**: Processes input files with metadata including tags, document types, and access counts.
- **Hierarchical Tree Building**: Dynamically creates a directory tree structure using tags and access frequencies.
- **Custom Tag Handling**:
    - Converts numerical tags like `size` and `length` into more meaningful categories (e.g., `ImageSize`, `AudioLength`).
    - Supports binary, multivalued, and numerical tags.
- **Entropy Calculation**: Uses information-theoretic entropy to determine the optimal tag for splitting documents.
- **Command-Line Interaction**: Provides commands for loading files, running the algorithm, modifying access counts, and quitting.

---

## Requirements
- Java SE 17.

---

## Setup
1. Clone or download the project.
2. Compile the source code:
    ```bash
    javac *.java
    ```
3. Run the program:
    ```bash
    java SavySorter
    ```

---

## Commands

### `load <path>`
Loads an input file containing document metadata.
- **Input format**: The file should contain comma-separated values for document path, type, access count, and tags.
- **Example**:
  ```plaintext
  musik/nggyu.mp3,audio,30,genre=pop,author=Rick Astley,fun
  dokumente/Abschlussaufgabe1,program,5,author=me
  ```
- **Output**:
  ```plaintext
  Loaded <path> with id: <id>
  <file content>
  ```

### `change <id> <file> <number>`
Modifies the access count for a specific document.
- **Example**:
  ```plaintext
  change 0 dokumente/Abschlussaufgabe1 10
  ```
- **Output**:
  ```plaintext
  Change 5 to 10 for dokumente/Abschlussaufgabe1
  ```

### `run <id>`
Runs the hierarchical structuring algorithm on the loaded dataset and outputs the resulting tree structure.
- **Example**:
  ```plaintext
  run 0
  ```
- **Output**: Displays information gain calculations and the generated directory structure.

### `quit`
Exits the program.

---

## Examples

### Example Interaction
```plaintext
%> java SavySorter
Use one of the following commands: load <path>, run <id>, change <id> <file> <number>, quit
> load input/example.txt
Loaded input/example.txt with id: 0
musik/nggyu.mp3,audio,30,genre=pop,author=Rick Astley,fun
dokumente/Abschlussaufgabe1,program,5,author=me
> run 0
/author=1.49
/audiogenre=1.00
/executable=0.89
/fun=0.64
/family=0.12
---
/author=Rick Astley/"musik/nggyu.mp3"
/author=me/"dokumente/Abschlussaufgabe1"
> quit
```

### Error Handling Example
```plaintext
> load invalid/file.txt
ERROR: File invalid/file.txt does not exist!
> change 0 unknown_file 15
ERROR: Document unknown_file not found!
```

---

## Notes
- Ensure the input file follows the specified format.
- The program calculates entropy and information gain for each tag to determine the optimal hierarchical structure.
- The hierarchy is built until the information gain falls below a defined threshold (epsilon).
- Follow Checkstyle rules and Java best practices for extending the code.

---

## License
This program was developed for educational purposes as part of a KIT programming course. Redistribution or plagiarism is prohibited.
