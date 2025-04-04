package de.akitoro.graphit;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import de.akitoro.graphit.core.AdjacencyGraph;
import de.akitoro.graphit.core.Edge;
import de.akitoro.graphit.core.Graph;
import de.akitoro.graphit.core.Vertex;
import de.akitoro.graphit.draw.Drawable;
import de.akitoro.graphit.draw.forcedirected.EadesDrawer;
import de.akitoro.graphit.io.GraphLoader;
import de.akitoro.graphit.io.GraphMLLoader;
import de.akitoro.graphit.math.Mat2x2;
import de.akitoro.graphit.math.Vec2;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class HelloFX extends Application {
	
	double windowHeight = 480;
    double windowWidth = 640;
    
    @Override
    public void start(Stage stage) {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        Label l = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
        
        BorderPane pane = new BorderPane();
        Canvas canvas = new Canvas(640, 480);
        
        pane.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        
        GraphLoader loader = new GraphMLLoader();
    	
    	Graph graph = loader.load("src/test/resources/example.graphml");
        
        drawGraph(graph, gc);
        
        
        Scene scene = new Scene(pane, 640, 480);
        stage.setScene(scene);
        stage.show();
    }
    
    public Graph completeGraph(int n) {
    	Graph graph = new AdjacencyGraph();
    	for (int i = 0; i < n; i++) {
			for (int j = 0; j < i; j++) {
				graph.connect(new Vertex(Integer.toString(i)), new Vertex(Integer.toString(j)));
			};
		}
    	return graph;
    }
    
    public void drawGraph(Graph graph, GraphicsContext gc) {
    	Drawable eades = new EadesDrawer();
    	Map<Vertex, Vec2> mapping = eades.draw(graph);
    	
    	Collection<Vec2> positions = mapping.values();

        //Calculate Smallest Rectangle that contains all points
        double maxX = positions.stream().mapToDouble(v -> v.x).max().orElse(1);
        double minX = positions.stream().mapToDouble(v -> v.x).min().orElse(-1);
        double maxY = positions.stream().mapToDouble(v -> v.y).max().orElse(1);
        double minY = positions.stream().mapToDouble(v -> v.y).min().orElse(-1);
        
        Vec2 windowCenter = new Vec2((maxX - minX)/2, (maxY - minY)/2).add(new Vec2(minX, minY));
        
        Mat2x2 scaleMatrix = new Mat2x2(new double[][] {
        	{0.9 * windowWidth/(maxX - minX), 0},
        	{0, 0.9 * windowHeight/(maxY - minY)}
        });
        
        Vec2 lastOffset = new Vec2(windowWidth/2, windowHeight/2);
        
        Vec2 offset = windowCenter;
        
        for (Vertex v : graph.getVertices()) {
        	Vec2 pos = mapping.get(v);
        	pos = scaleMatrix.apply(pos.sub(offset));
        	pos = pos.add(lastOffset);
			gc.fillOval(pos.x - 10, pos.y - 10, 20, 20);
		}
        		
        for (Edge e : graph.getEdges()) {
        	Vec2 start = scaleMatrix.apply(mapping.get(e.getSource()).sub(offset)).add(lastOffset);
        	Vec2 end = scaleMatrix.apply(mapping.get(e.getTarget()).sub(offset)).add(lastOffset);
        	gc.setLineWidth(5);
			gc.strokeLine(start.x, start.y, end.x, end.y);
		}
    }

    public static void main(String[] args) {
        launch();
    }

}