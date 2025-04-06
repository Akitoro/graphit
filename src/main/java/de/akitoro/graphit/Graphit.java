package de.akitoro.graphit;

import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import de.akitoro.graphit.core.AdjacencyGraph;
import de.akitoro.graphit.core.Construction;
import de.akitoro.graphit.core.Edge;
import de.akitoro.graphit.core.Graph;
import de.akitoro.graphit.core.Vertex;
import de.akitoro.graphit.draw.Drawable;
import de.akitoro.graphit.draw.circular.CircularDrawer;
import de.akitoro.graphit.draw.forcedirected.EadesDrawer;
import de.akitoro.graphit.io.GraphLoader;
import de.akitoro.graphit.io.GraphMLLoader;
import de.akitoro.graphit.math.Mat2x2;
import de.akitoro.graphit.math.Ray;
import de.akitoro.graphit.math.Vec2;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;


public class Graphit extends Application {
	
	Property<Circle> currentCircle = new SimpleObjectProperty<>();
	
    @Override
    public void start(Stage stage) {
        GraphLoader loader = new GraphMLLoader();

    	Graph graph = loader.load("src/test/resources/example.graphml");
        //Graph graph = Construction.completeGraph(10);
        
        VBox vbox = new VBox();
        Pane pane = new Pane();
        Scene scene = new Scene(vbox, 200, 200);
        
        pane.prefHeightProperty().bind(scene.heightProperty());
        pane.prefWidthProperty().bind(scene.widthProperty());
        
        pane.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, null, null)));
        
        MenuItem exportMenuItem = new MenuItem("Export");
        MenuItem importMenuItem = new MenuItem("Import");
        Menu fileMenu = new Menu("File");
        
        fileMenu.getItems().addAll(exportMenuItem, importMenuItem);
        MenuBar menu = new MenuBar(fileMenu);
        
        
        Group root = drawGraph(graph, pane);
        root.getChildren().addAll(drawBackground(pane).getChildren());
        
        vbox.getChildren().addAll(menu, pane);
        pane.getChildren().addAll(root);
        
        pane.widthProperty().addListener(c -> {
        	pane.getChildren().clear();
        	pane.getChildren().addAll(drawGraph(graph, pane), drawBackground(pane));
        });
        pane.heightProperty().addListener(c -> {
        	pane.getChildren().clear();
        	pane.getChildren().addAll(drawGraph(graph, pane), drawBackground(pane));
        });
        
        stage.setTitle("Graphit - Viewing Graph");
        stage.setScene(scene);
        stage.show();
        
        pane.setOnMouseDragged(e -> {
        	Circle selectedCircle = currentCircle.getValue();
        	if (selectedCircle != null) {
        		
        		Bounds bounds = pane.getLayoutBounds();
        		if (bounds.contains(e.getX(), e.getY())) {
        			selectedCircle.setCenterX(e.getX());
                	selectedCircle.setCenterY(e.getY());
        		}
        	}
        });
        
    }
    
    public Group drawBackground(Pane scene) {
    	Group g = new Group();
    	double windowWidth = scene.getWidth();
    	double windowHeight = scene.getHeight();

    	double wFix = 30;
    	double hFix = 30;
    	
    	for (int x = 0; x*wFix < windowWidth; x++) {
			Line line = new Line();
			
			line.setFill(Color.GREEN);
			line.setStrokeWidth(0.1);
			line.setViewOrder(-1);
			line.setStartX(x*wFix);
			line.setStartY(0);
			line.setEndX(x*wFix);
			line.setEndY(windowHeight);
			
			g.getChildren().add(line);
		}
    	
    	for (int y = 0; y*hFix < windowHeight; y++) {
			Line line = new Line();
			
			line.setFill(Color.GREEN);
			line.setStrokeWidth(0.1);
			line.setViewOrder(-1);
			line.setStartX(0);
			line.setStartY(y*hFix);
			line.setEndX(windowWidth);
			line.setEndY(y*hFix);
			
			g.getChildren().add(line);
		}
    	return g;
    }
    
    public Group drawGraph(Graph graph, Pane scene) {
    	double windowWidth = scene.getWidth();
    	double windowHeight = scene.getHeight();
    	
    	Group g = new Group();
    	
    	Drawable eades = new EadesDrawer();
    	Drawable circular = new CircularDrawer(20, -2*Math.PI/5);
    	
    	Map<Vertex, Vec2> mapping = eades.draw(graph);
    	//Map<Vertex, Vec2> mapping = circular.draw(graph);
    	Map<Vertex, Circle> finalMapping = new HashMap<>();
    	
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
        	
        	Circle circle = new Circle(pos.x, pos.y, 10, Color.RED);
        	finalMapping.put(v, circle);
        	g.getChildren().add(circle);
        	
        	circle.setOnMousePressed(e -> {
        		currentCircle.setValue(circle);
        	});
        	
        	circle.setOnMouseReleased(e -> {
        		currentCircle.setValue(null);
        	});
        	
		}
        		
        for (Edge e : graph.getEdges()) {
        	Vec2 start = scaleMatrix.apply(mapping.get(e.getSource()).sub(offset)).add(lastOffset);
        	Vec2 end = scaleMatrix.apply(mapping.get(e.getTarget()).sub(offset)).add(lastOffset);
        	
        	if (e.getIsDirected()) {
            	Vec2 base = end.sub(start);
            	Vec2 real = end.sub(base.normalize().mul(20));
            	
            	Ray r = new Ray(real, base.orthogonal().normalize());
            	
            	Vec2 a = r.get(-4);
            	Vec2 b = r.get(4);
            	
            	Polygon arrow = new Polygon();
            	arrow.getPoints().addAll(new Double[]{ 
            				end.x, end.y,
            				a.x, a.y,
            				b.x, b.y
            	        });
        	}
        	Line line = new Line();
        	
        	line.startXProperty().bind(finalMapping.get(e.getSource()).centerXProperty());
        	line.startYProperty().bind(finalMapping.get(e.getSource()).centerYProperty());
        	line.endXProperty().bind(finalMapping.get(e.getTarget()).centerXProperty());
        	line.endYProperty().bind(finalMapping.get(e.getTarget()).centerYProperty());
        	line.setStrokeWidth(2);
        	
        	line.setOnMousePressed( (event) -> line.setStrokeWidth(4));
        	line.setOnMouseReleased( (event) -> line.setStrokeWidth(2));
        	g.getChildren().addAll(line);
		}
        
        return g;
    }

    public static void main(String[] args) {
        launch();
    }

}