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
import org.jgrapht.traverse.DepthFirstIterator;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class GraphManager {

    public class Path {
        ArrayList<String> nodes;

        Path() {
            nodes = new ArrayList<>();
        }

        public void addNode(String node) {
            this.nodes.add(node);
        }

        public boolean containsNode(String searchNode) {
            for (String node : nodes) {
                if (searchNode.equals(node)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public String toString() {
            String output = "";
            for(int i=0; i<nodes.size()-1; i++) {
                output += nodes.get(i) + " -> ";
            }
            output += nodes.get(nodes.size()-1);
            return output;
        }
    }

    private Graph<String, DefaultEdge> graph = new SimpleDirectedGraph<>(DefaultEdge.class);

    public void parseGraph(String filePath) throws Exception {
        String fileContent = null;
        try {
            fileContent = Files.readString(Paths.get(filePath));
        } catch (IOException e) {
            throw new Exception("Error while reading file", e);
        }

        try {
            DOTImporter<String, DefaultEdge> dotImporter = new DOTImporter<>();
            dotImporter.setVertexFactory(label -> label);
            dotImporter.importGraph(graph, new StringReader(fileContent));
            System.out.println("Graph successfully parsed!");
        } catch (Exception e) {
            throw new Exception("Error while parsing graph", e);
        }

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

    @Override
    public String toString() {
        String output = "";
        try {
            output = constructOutputString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;

    }

    public void outputGraph(String filePath) throws Exception {
        String output = constructOutputString();
        try {
            Files.write(Paths.get(filePath), output.getBytes());
            System.out.println("Successfully wrote graph information to " + filePath);
        } catch (IOException e) {
            throw new Exception("Unable to write graph infostring to file", e);
        }
    }

    public int nodeSize() {
        return graph.vertexSet().size();
    }

    public int edgeSize() {
        return graph.edgeSet().size();
    }

    public boolean containsNode(String label) {
        return graph.containsVertex(label);
    }

    public boolean containsEdge(String srcLabel, String dstLabel) {
        return graph.containsEdge(srcLabel, dstLabel);
    }



    public void addNode(String label) throws Exception {
        if (!graph.containsVertex(label)) {
            try {
                graph.addVertex(label);
            } catch (Exception e) {
                throw new Exception("Error while adding node", e);
            }
        }
    }

    public boolean removeNode(String label) throws Exception {
        if (graph.containsVertex(label)) {
            try {
                graph.removeVertex(label);
                return true;
            } catch (Exception e) {
                throw new Exception("Error while removing node", e);
            }
        } else {
            return false;
        }
    }

    public void addNodes(String[] labels) throws Exception {
        for (String label : labels) {
            addNode(label);
        }
    }

    public boolean removeNodes(String[] labels) throws Exception {
        boolean check = true;
        for (String label : labels) {
            if (!removeNode(label)) {
                System.out.println("Failed to remove node " + label);
                check = false;
            }
        }
        return check;
    }

    public boolean addEdge(String srcLabel, String dstLabel) throws Exception {
        try {
            if (graph.containsEdge(srcLabel, dstLabel)) {
                return false;
            } else {
                if (!graph.containsVertex(srcLabel)) graph.addVertex(srcLabel);
                if (!graph.containsVertex(dstLabel)) graph.addVertex(dstLabel);
                graph.addEdge(srcLabel, dstLabel);
                return true;
            }
        } catch (Exception e) {
            throw new Exception("Error while adding edge", e);
        }

    }

    public boolean removeEdge(String srcLabel, String dstLabel) throws Exception {
        try {
            if (graph.containsEdge(srcLabel, dstLabel)) {
                graph.removeEdge(srcLabel, dstLabel);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new Exception("Error while removing edge", e);
        }

    }

    public void outputDOTGraph(String filePath) throws Exception {
        DOTExporter<String, DefaultEdge> exporter = new DOTExporter<>();
        StringWriter writer = new StringWriter();
        String dotString;
        try {

            exporter.setVertexIdProvider(v -> v);
            exporter.exportGraph(graph, writer);
            dotString = writer.toString();
        } catch (Exception e) {
            throw new Exception("Error while generating dotstring", e);
        }
        try {
            Files.write(Paths.get(filePath), dotString.getBytes());
        } catch (IOException e) {
            throw new IOException("Error while exporting to DOT graph", e);
        }

    }

    public void outputGraphics(String filePath) throws Exception {
        JGraphXAdapter<String, DefaultEdge> graphAdapter = new JGraphXAdapter<String, DefaultEdge>(graph);
        mxIGraphLayout layout = new mxCircleLayout(graphAdapter);
        try {
            layout.execute(graphAdapter.getDefaultParent());
        } catch (Exception e) {
            throw new Exception("Error while converting graph to image", e);
        }

        BufferedImage image = mxCellRenderer.createBufferedImage(graphAdapter, null, 2, Color.WHITE, true, null);
        File imgFile = new File(filePath);
        try {
            ImageIO.write(image, "PNG", imgFile);
        } catch (IOException e) {
            throw new Exception("Error while writing image to file", e);
        }
    }

    public Path GraphSearch(String src, String dst) {
        if (!graph.containsVertex(src) || !graph.containsVertex(dst)) {
            return null;
        }
        Iterator<String> iterator = new DepthFirstIterator<>(graph, src);
        Path path = new Path();
        while(iterator.hasNext()) {
            String node = iterator.next();
            path.addNode(node);
            if(node.equals(dst)) {
                break;
            }
        }
        if (path.containsNode(dst)) {
            return path;
        } else {
            return null;
        }
    }

}
