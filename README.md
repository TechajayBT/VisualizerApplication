# DSA Visualizer Application

A comprehensive Visualizer Application built with Java and JavaFX to help users understand and interactively learn various Data Structures and Algorithms (DSA). This application provides visual representations, step-by-step animations, and interactive controls for a wide range of DSA topics.

## Features

- **Visualize Popular Data Structures:**  
  Includes Arrays, Linked Lists, Stacks, Queues, Trees (Binary, AVL, Heap, etc.), Graphs, Hash Tables, and more.

- **Algorithm Animations:**  
  Step-by-step visualizations for sorting algorithms (Bubble, Insertion, Merge, Quick, etc.), searching algorithms, traversals (BFS, DFS), and more.

- **Interactive Controls:**  
  Manipulate data structures in real time—add, delete, search, and modify elements. Control the speed of animations.

- **Educational Descriptions:**  
  Displays explanations, time complexity, and use-cases for each data structure and algorithm.

- **User-Friendly Interface:**  
  Built using JavaFX, ensuring a smooth, modern, and responsive user experience.

## Getting Started

### Prerequisites

- Java JDK 11 or above
- JavaFX SDK (Download from [GluonHQ](https://gluonhq.com/products/javafx/))
- Maven or Gradle (optional, for dependency management)

### Running the Application

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/TechajayBT/VisualizerApplication.git
   cd VisualizerApplication
   ```

2. **Set Up JavaFX:**  
   Make sure the JavaFX SDK is installed and configured in your IDE/build tool.

3. **Build and Run:**
   - **Using Maven:**
     ```bash
     mvn clean javafx:run
     ```
   - **Using Gradle:**
     ```bash
     gradle run
     ```
   - **Manual (IDE):**
     - Open the project in your preferred Java IDE.
     - Configure JavaFX libraries.
     - Run the `Main` class.

## Project Structure

```
dsa-visualizer/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── (Java source files)
│   │   ├── resources/
│   │   │   └── (FXML, CSS, images)
├── README.md
├── pom.xml / build.gradle
└── ...
```

## Supported Visualizations

- **Data Structures:**
  - Array
  - Linked List (Singly, Doubly, Circular)
  - Stack
  - Queue
  - Binary Tree, BST, AVL Tree, Heap
  - Graphs (Directed, Undirected)
  - Hash Table

- **Algorithms:**
  - Sorting: Bubble, Selection, Insertion, Merge, Quick, Heap Sort
  - Searching: Linear, Binary Search
  - Tree Traversals: In-order, Pre-order, Post-order, Level-order
  - Graph Traversals: BFS, DFS



## Contributing

Contributions are welcome!  
- Fork the repository
- Create a new branch (`git checkout -b feature/your-feature`)
- Commit your changes
- Open a Pull Request

## License

This project is licensed under the [MIT License](LICENSE).

## Acknowledgments

- JavaFX documentation and community
- Open-source DSA visualizer projects for inspiration

---

**Happy Learning and Visualizing!**
