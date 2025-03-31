package de.akitoro.graphit.io;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.akitoro.graphit.core.AdjacencyGraph;
import de.akitoro.graphit.core.Edge;
import de.akitoro.graphit.core.Graph;
import de.akitoro.graphit.core.Vertex;

public class GraphMLLoader implements GraphLoader{

	@Override
	public Graph load(String path) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        Document document = builder.parse(path);
	        
	        AdjacencyGraph graph = new AdjacencyGraph();
	        
	        NodeList vertexList = document.getElementsByTagName("node");
	        NodeList edgeList = document.getElementsByTagName("edge");
	        
	        for (int i = 0; i < vertexList.getLength(); i++) {
	            Node node = vertexList.item(i);
	            NamedNodeMap attributes = node.getAttributes();
	            
	            for (int j = 0; j < attributes.getLength(); j++) {
	            	Node attribute = attributes.item(j);
	            	String value = attribute.getNodeValue();
	            	
	            	graph.add(new Vertex(value));
	            }
	        }
	        
	        for (int i = 0; i < edgeList.getLength(); i++) {
	            Node node = edgeList.item(i);
	            NamedNodeMap attributes = node.getAttributes();
	            
	            Node id = attributes.getNamedItem("id");
	            Node source = attributes.getNamedItem("source");
	            Node target = attributes.getNamedItem("target");
	            
	            Vertex sourceVertex = new Vertex(source.getNodeValue());
	            Vertex targetVertex = new Vertex(target.getNodeValue());
	            
	            graph.connect(sourceVertex, targetVertex);
	        }
	        return graph;
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

	@Override
	public void save(Graph graph) {
		// TODO Auto-generated method stub
	}
}
