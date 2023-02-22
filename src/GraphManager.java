import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.nio.dot.DOTImporter;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GraphManager {

    public void parseGraph(String filePath) {
        String fileContent = null;
        try {
            fileContent = Files.readString(Paths.get(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(fileContent);
        Graph<String, DefaultEdge> result = new SimpleDirectedGraph<>(DefaultEdge.class);

        DOTImporter<String, DefaultEdge> dotImporter = new DOTImporter<>();
        dotImporter.setVertexFactory(label -> label);
        dotImporter.importGraph(result, new StringReader(fileContent));

        System.out.println(result);
    }

    public void outputGraph() {}

    public void outputDOTGraph() {}

    public void outputGraphics() {}
    public static void main(String[] args) {
        GraphManager g = new GraphManager();
        g.parseGraph("src/test.dot");
    }
}
