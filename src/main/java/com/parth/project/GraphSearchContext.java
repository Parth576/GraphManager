package com.parth.project;

public class GraphSearchContext {
    private SearchStrategy searchStrategy;

    public void setSearchAlgorithm(SearchStrategy searchStrategy) {
        this.searchStrategy = searchStrategy;
    }

    public GraphManager.Path executeSearchAlgorithm(String src, String dst) {
        return this.searchStrategy.search(src, dst);
    }


}
