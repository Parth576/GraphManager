import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.nio.dot.DOTImporter;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

public class GraphManager {

    private Graph<String, DefaultEdge> graph = new SimpleDirectedGraph<>(DefaultEdge.class);

    public void parseGraph(String filePath) {
        String fileContent = null;
        try {
            fileContent = Files.readString(Paths.get(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try {
            DOTImporter<String, DefaultEdge> dotImporter = new DOTImporter<>();
            dotImporter.setVertexFactory(label -> label);
            dotImporter.importGraph(graph, new StringReader(fileContent));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        System.out.println("Graph successfully parsed!");
    }

    private String constructOutputString() {
        String output = "";
        Set<DefaultEdge> edgeSet = graph.edgeSet();
        Set<String> vertexSet = graph.vertexSet();
        output += "The number of nodes are: " + vertexSet.size() + "\n";
        output += "The node labels are: \n";
        for(String elem : vertexSet) {
            output += elem + "\n";
        }
        output += "The number of edges are: " + edgeSet.size() + "\n";
        output += "The nodes with the direction of edges: \n";
        for (DefaultEdge edge : edgeSet) {
            String source = graph.getEdgeSource(edge);
            String target = graph.getEdgeTarget(edge);
            output += source + " -> " + target + "\n";
        }
        return output;
    }

    public void outputString() {
        String output = constructOutputString();
        System.out.print(output);
    }

    public void outputGraph(String filePath) {
        String output = constructOutputString();
        try {
            Files.write(Paths.get(filePath), output.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void outputDOTGraph() {} //output graph object to a DOT file

    public void outputGraphics() {} //output graph object to a PNG file
    public static void main(String[] args) {
        GraphManager g = new GraphManager();
        g.parseGraph("src/test.dot");
        g.outputString();
        g.outputGraph("src/graphinfo.txt");
    }
}
