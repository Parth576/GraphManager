# CSE 464 Project Part 1

## GraphManager

### Instructions to Run
- Download the ```GraphManager.zip``` file from this repository
- Run ```mvn package```
- This should run all tests for the project
- This command will build the project in the ```target``` folder as well

### APIs
- ```void parseGraph(String filePath)``` - import a directed graph in a dot file
- ```String toString()``` - Graph information like number of nodes, edges and their directions
- ```void outputGraph(String filePath)``` - Write the graph information to a file
- ```int nodeSize()``` - Returns number of nodes in the graph
- ```int edgeSize()``` - Returns the number of edges in the graph
- ```boolean containsNode(String label)``` - Returns true if the graph contains the node and false otherwise
- ```boolean containsEdge(String srcLabel, String dstLabel)``` - Returns true if edge exists in graph and false otherwise
- ```void addNode(String label)``` - Adds a new node to the graph with the given label if it does not exist
- ```boolean removeNode(String label)``` - Returns false if node does not exist or returns true if node is removed
- ```void addNodes(String[] labels)``` - Add multiple nodes to the graph
- ```boolean removeNodes(String[] labels)``` - Returns true if all nodes are removed successfully otherwise returns false if even one node exists
- ```boolean addEdge(String srcLabel, String dstLabel)``` - Returns true if edge is added otherwise returns false if edge exists
- ```boolean removeEdge(String srcLabel, String dstLabel)``` - Returns true if edge is removed successfully otherwise returns false if edge does not exist in the graph
- ```void outputDOTGraph(String filePath)``` - Outputs the modified graph in DOT format to the specified file
- ```void outputGraphics(String filePath)``` - Output the modified graph to a PNG file (Graph Visualization)

### Example Code
- Creating a new GraphManager object
```java
GraphManager g = new GraphManager();
```
- Parsing the graph and printing information
```java
g.parseGraph("src/test.dot");
System.out.println(g.toString());
g.outputGraph("src/graphinfo.txt");
```
- Adding and Removing nodes
```java
g.addNode("e");
g.removeNode("e");
String[] nodesToAdd = {"e","f","g"};
g.addNodes(nodesToAdd);
String[] nodesToRemove = {"e","g"};
g.removeNodes(nodesToRemove);
```
- Adding and removing edges
```java
g.addEdge("a","f");
g.removeEdge("a","b");
```
- Output graph as DOT and PNG
```java
g.outputDOTGraph("src/modified.dot");
g.outputGraphics("src/modified.png");
```