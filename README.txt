# TaskManager

## Overview
This repository contains a Java program designed to implement a task management and execution system.

## Features
### Key Features
- **Task Management**: Efficiently manage tasks using the `TaskManager` class, including task creation, prioritization, and execution.
- **Data Structures**: Implements `LinkedList` for dynamic task sequencing and `HashMap` for quick task retrieval.
- **Error Handling**: Includes robust error detection and user-friendly feedback for task management operations.
- **Modularity**: Designed for easy extension with additional features or custom workflows.


## Installation
### Steps to Install
1. Clone the repository:
   ```
   git clone https://github.com/snikmas/TaskManager.git
   ```
2. Navigate to the project directory:
   ```
   cd TaskManager
   ```
3. Compile the project:
   - Using Java:
     ```
     javac -d out src/*.java
     ```
4. Run the program:
   - Using raw Java:
     ```
     java
     ```

## Usage
### How to Use
- Launch the program as described above.
- Follow the on-screen instructions, which include:
  - Entering task details via the `TaskManager` interface.
  - Viewing task sequences;
  - Executing tasks through a defined workflow;

## Project Structure
### Directory Layout
```
your-repo/
├── src/
│   ├── TaskManager.java  # Core task management class
│   ├── LinkedList.java   # Custom LinkedList implementation
│   ├── HashMap.java      # Custom HashMap implementation
│   ├── Main.java         # Entry point of the application
├── README.md            # This file
```
