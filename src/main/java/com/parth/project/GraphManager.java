package com.parth.project;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.util.mxCellRenderer;
import org.jgrapht.Graph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.nio.dot.DOTExporter;
import org.jgrapht.nio.dot.DOTImporter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
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

    @Override
    public String toString() {
        String output = constructOutputString();
        return output;
    }

    public void outputGraph(String filePath) {
        String output = constructOutputString();
        try {
            Files.write(Paths.get(filePath), output.getBytes());
            System.out.println("Successfully wrote graph information to " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addNode(String label) {
        if (graph.containsVertex(label)) {
            System.out.println("Node " + label + " already exists in the graph");
        } else {
            graph.addVertex(label);
            System.out.println("Node " + label + " added successfully");
        }
    }

    public void removeNode(String label) {
        if (graph.containsVertex(label)) {
            graph.removeVertex(label);
            System.out.println("Node " + label + " removed successfully");
        } else {
            System.out.println("Node " + label + " not present in graph");
        }
    }

    public void addNodes(String[] labels) {
        for (String label : labels) {
            addNode(label);
        }
    }

    public void removeNodes(String[] labels) {
        for (String label : labels) {
            removeNode(label);
        }
    }

    public void addEdge(String srcLabel, String dstLabel) {
        if (graph.containsEdge(srcLabel, dstLabel)) {
            System.out.println("Edge already exists from " + srcLabel + " to " + dstLabel);
        } else {
            graph.addEdge(srcLabel, dstLabel);
            System.out.println("Edge successfully added from " + srcLabel + " to " + dstLabel);
        }
    }

    public void removeEdge(String srcLabel, String dstLabel) {
        if (graph.containsEdge(srcLabel, dstLabel)) {
            graph.removeEdge(srcLabel, dstLabel);
            System.out.println("Edge successfully removed from " + srcLabel + " to " + dstLabel);
        } else {
            System.out.println("Edge does not exist from " + srcLabel + " to " + dstLabel);
        }
    }

    public void outputDOTGraph(String filePath) {
        DOTExporter<String, DefaultEdge> exporter = new DOTExporter<>();
        StringWriter writer = new StringWriter();
        exporter.setVertexIdProvider(v -> v);
        exporter.exportGraph(graph, writer);
        String dotString = writer.toString();
        try {
            Files.write(Paths.get(filePath), dotString.getBytes());
            System.out.print("Successfully exported graph to DOT format at "+ filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void outputGraphics(String filePath) {
        JGraphXAdapter<String, DefaultEdge> graphAdapter = new JGraphXAdapter<String, DefaultEdge>(graph);
        mxIGraphLayout layout = new mxCircleLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());

        BufferedImage image = mxCellRenderer.createBufferedImage(graphAdapter, null, 2, Color.WHITE, true, null);
        File imgFile = new File(filePath);
        try {
            ImageIO.write(image, "PNG", imgFile);
            System.out.println("Successfully saved image of graph to " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        GraphManager g = new GraphManager();
        g.parseGraph("src/test.dot");
        g.outputString();
        g.outputGraph("src/graphinfo.txt");
        g.addNode("e");
        g.outputString();
        g.removeNode("e");
        g.outputString();
        String[] nodesToAdd = {"e","f","g"};
        g.addNodes(nodesToAdd);
        g.outputString();
        String[] nodesToRemove = {"e","g"};
        g.removeNodes(nodesToRemove);
        g.outputString();
        g.addEdge("a","f");
        g.outputString();
        g.addEdge("a","f");
        g.removeEdge("a","b");
        g.outputString();
        g.removeEdge("a","b");
        g.outputDOTGraph("src/modified.dot");
        g.outputGraphics("src/modified.png");
    }
}
