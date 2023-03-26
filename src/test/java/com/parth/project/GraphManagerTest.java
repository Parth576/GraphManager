package com.parth.project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class GraphManagerTest {
    GraphManager g;

    @BeforeEach
    public void setup() throws Exception {
        g = new GraphManager();
        g.parseGraph("src/test.dot");
    }

    @Test
    public void testParseGraph() {
        assertEquals(4, g.nodeSize());
        assertEquals(4, g.edgeSize());
        assertTrue(g.containsEdge("a", "b"));
        assertTrue(g.containsEdge("b", "c"));
        assertTrue(g.containsEdge("c", "d"));
        assertTrue(g.containsEdge("d", "a"));
        System.out.println(g.toString());

        Exception exception = assertThrows(Exception.class, () -> {
            g.parseGraph("src/doesnotexist.dot");
        });

        String expectedMessage = "Error while reading file";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));


    }

    @Test
    public void testToString() throws IOException {
        String expected = Files.readString(Paths.get("src/expectedString.txt"));
        String output = g.toString();
        assertEquals(output, expected);
    }

    @Test
    public void testOutputGraph() throws Exception {
        g.outputGraph("src/testOutputGraph.txt");
        String output = Files.readString(Paths.get("src/testOutputGraph.txt"));
        String expected = Files.readString(Paths.get("src/expectedString.txt"));
        assertEquals(output, expected);
    }



    @Test
    public void testAddNode() throws Exception {
        g.addNode("e");
        System.out.println(g.toString());

        assertEquals(5, g.nodeSize());
        assertTrue(g.containsNode("e"));
    }

    @Test
    public void testAddEdge() throws Exception {
        g.addEdge("e", "a");
        System.out.println(g.toString());

        assertEquals(5, g.nodeSize());
        assertTrue(g.containsNode("e"));
        assertTrue(g.containsEdge("e", "a"));
    }

    @Test
    public void testAddNodes() throws Exception {
        String[] labels = { "a", "e", "f"};
        g.addNodes(labels);

        assertEquals(6, g.nodeSize());
        assertTrue(g.containsNode("a"));
        assertTrue(g.containsNode("e"));
        assertTrue(g.containsNode("f"));
    }


    @Test
    public void testRemoveNode() throws Exception {
        g.removeNode("a");
        System.out.println(g.toString());

        assertEquals(3, g.nodeSize());
        assertFalse(g.containsNode("a"));
    }

    @Test
    public void testRemoveNodes() throws Exception {
        g.addNode("e");
        g.addEdge("e","a");
        String[] labels = {"a","e","f"};
        assertFalse(g.removeNodes(labels));
        assertFalse(g.containsNode("a"));
        assertFalse(g.containsNode("e"));
        assertEquals(3, g.nodeSize());
        assertEquals(2, g.edgeSize());
        assertFalse(g.containsEdge("e","a"));
    }

    @Test
    public void testRemoveEdge() throws Exception {

        g.removeEdge("b", "c");
        System.out.println(g.toString());

        assertEquals(4, g.nodeSize());
        assertEquals(3, g.edgeSize());
        assertTrue(g.containsEdge("a", "b"));
        assertTrue(g.containsEdge("c", "d"));
        assertTrue(g.containsEdge("d", "a"));
    }

    @Test
    public void testOutputDOTGraph() throws Exception {
        g.addEdge("e", "f");
        String outputfile = "src/output2.dot";
        g.outputDOTGraph(outputfile);

        String output = Files.readString(Paths.get(outputfile));
        System.out.println("output: " + output);
        String expected = Files.readString(Paths.get("src/expected.txt"));
        assertEquals(expected, output);
    }

    @Test
    public void testBFS() throws Exception {
        GraphManager gm = new GraphManager();
        gm.parseGraph("src/test2.dot");
        ArrayList<String> expected = new ArrayList<>();
        expected.add("a");
        expected.add("d");
        expected.add("c");
        expected.add("b");
        expected.add("h");
        String expectedString = "a -> d -> c -> b -> h";

        GraphManager.Path result = gm.GraphSearch("a", "h", GraphManager.Algorithm.BFS);

        assertNotNull(result);
        assertEquals(expected, result.nodes);
        assertEquals(expectedString, result.toString());

        result = gm.GraphSearch("h", "a", GraphManager.Algorithm.BFS);
        assertNull(result);
    }

    @Test
    public void testDFS() throws Exception {
        GraphManager gm = new GraphManager();
        gm.parseGraph("src/test2.dot");
        ArrayList<String> expected = new ArrayList<>();
        expected.add("a");
        expected.add("b");
        expected.add("f");
        expected.add("e");
        expected.add("c");
        expected.add("g");
        expected.add("d");
        expected.add("i");
        expected.add("h");
        String expectedString = "a -> b -> f -> e -> c -> g -> d -> i -> h";

        GraphManager.Path result = gm.GraphSearch("a", "h", GraphManager.Algorithm.DFS);

        assertNotNull(result);
        assertEquals(expected, result.nodes);
        assertEquals(expectedString, result.toString());

        result = gm.GraphSearch("h", "a", GraphManager.Algorithm.DFS);
        assertNull(result);
    }

}
