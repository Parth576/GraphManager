package com.parth.project;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.BreadthFirstIterator;

import java.util.Iterator;

class BreadthFirstSearch extends GraphSearchTemplate {

    BreadthFirstSearch(Graph<String,DefaultEdge> graph) {
        super(graph);
    }
    @Override
    public GraphManager.Path search(String src, String dst) {
        Iterator<String> iterator = new BreadthFirstIterator<>(this.graph, src);
        GraphManager.Path path = constructPath(iterator, src, dst);
        return path;
    }
}
