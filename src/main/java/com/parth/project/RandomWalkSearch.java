package com.parth.project;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.RandomWalkVertexIterator;

import java.util.Iterator;

class RandomWalkSearch extends GraphSearchTemplate implements SearchStrategy {

    RandomWalkSearch(Graph<String, DefaultEdge> graph) {
        super(graph);
    }

    private GraphManager.Path randomPathIterator(String src, String dst) {
        Iterator<String> iterator;
        GraphManager.Path path;
        int iteration = 1;

        // Check if some path exists between nodes and then execute random walk till a path is found
        iterator = new BreadthFirstIterator<>(this.graph, src);
        path = constructPath(iterator, src, dst, false);
        if (path != null) {
            do {
                System.out.println("Random Walk Iteration "+ iteration++);
                iterator = new RandomWalkVertexIterator<>(this.graph, src);
                path = constructPath(iterator, src, dst, true);
                if(path != null) System.out.println("Path found!\n");
                else System.out.println("Path not found\n");
            } while(path == null);
        } else return null;
        return path;
    }
    @Override
    public GraphManager.Path search(String src, String dst) {
        return randomPathIterator(src, dst);
    }
}