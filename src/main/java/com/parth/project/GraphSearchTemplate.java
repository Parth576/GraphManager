package com.parth.project;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import java.util.ArrayList;
import java.util.Iterator;

abstract class GraphSearchTemplate {

    protected Graph<String,DefaultEdge> graph;

    public GraphSearchTemplate(Graph<String,DefaultEdge> graph) {
        this.graph = graph;
    }
    private final boolean checkSourceNode(String src) {
        return graph.containsVertex(src);
    };

    private final boolean checkDestinationNode(String dst) {
        return graph.containsVertex(dst);
    };

    public abstract GraphManager.Path search(String src, String dst);

    public final GraphManager.Path constructPath(Iterator<String> iterator, String src, String dst) {
        if (!checkSourceNode(src) || !checkDestinationNode(dst)) return null;
        GraphManager.Path path = new GraphManager().new Path();
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

    public final void printPath(GraphManager.Path path) {
        ArrayList<String> nodes = path.getNodeList();
        String output = "";
        for(int i=0; i<nodes.size()-1; i++) {
            output += nodes.get(i) + " -> ";
        }
        output += nodes.get(nodes.size()-1);
        System.out.println(output);
    }
}
