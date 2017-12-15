package app;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import models.Road;
import models.RoadPoint;

public class MapApp extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	private final int X_DIM = 1300;
	private final int Y_DIM = 800;
	private final int X_PADDING = 100;
	private final int Y_PADDING = 100;
	private RoadPoint activeStart = null;
	private RoadPoint activeEnd = null;

	@Override
	public void start(Stage primaryStage) throws FileNotFoundException, IOException {
		primaryStage.setTitle("Minneapolis Pathfinder");
		Group root = new Group();
		Canvas canvas = new Canvas(X_DIM, Y_DIM);
		GraphicsContext gc = canvas.getGraphicsContext2D();

		List<Road> roads = RoadParser.parseRoads(new File("test.txt"));
		scaleRoads(roads);
		drawRoads(roads, gc);
		Map<RoadPoint, List<RoadPoint>> roadMap = RoadParser.buildRoadPointGraph(roads);

		root.getChildren().add(canvas);
		Button bfs = new Button("BFS");
		bfs.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				System.out.println("Go BFS");
				redraw(roads, gc);
				List<Road> path = Pathfinder.getOptimalPath(roads, roadMap, null, activeStart, activeEnd,
						Pathfinder.AlgorithmType.BFS);
				drawOptimalPath(path, gc);
			}
		});

		Button astar = new Button("A*");
		astar.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				System.out.println("Go A*");
				redraw(roads,gc);
				Map<RoadPoint, Double> heuristics = Pathfinder.calculateHeuristics(roads, activeEnd);
				List<Road> path = Pathfinder.getOptimalPath(roads, roadMap, heuristics, activeStart, activeEnd,
						Pathfinder.AlgorithmType.ASTAR);
				drawOptimalPath(path, gc);
			}
		});

		Button idastar = new Button("IDA*");
		idastar.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				System.out.println("Go IDA*");
				redraw(roads, gc);
				List<Road> path = Pathfinder.getOptimalPath(roads, roadMap, null, activeStart, activeEnd,
						Pathfinder.AlgorithmType.IDASTAR);
				drawOptimalPath(path, gc);
			}
		});

		bfs.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
		astar.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
		idastar.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent;");

		TilePane tileButtons = new TilePane(Orientation.HORIZONTAL);
		tileButtons.setPadding(new Insets(10, 5, 10, 0));
		tileButtons.setHgap(10.0);
		tileButtons.getChildren().addAll(bfs, astar, idastar);
		root.getChildren().add(tileButtons);
		canvas.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				RoadPoint p = getRoadPointAtOrAround(roads, new Point((int) event.getX(), (int) event.getY()));

				// If user clicks empty part on map
				if (p == null) {
					return;
				}

				// Left click - pick start point
				if (event.isPrimaryButtonDown()) {
					activeStart = p;
				}

				// Right click - pick end point
				if (event.isSecondaryButtonDown()) {
					activeEnd = p;
				}

				// Reset pane, redraw to show selected point
				gc.clearRect(0, 0, X_DIM, Y_DIM);
				drawRoads(roads, gc);
			}

		});

		primaryStage.setScene(new Scene(root));
		primaryStage.show();
	}

	private void redraw(List<Road> roads, GraphicsContext gc) {
		// Reset pane, redraw to show selected point
		gc.clearRect(0, 0, X_DIM, Y_DIM);
		drawRoads(roads, gc);
	}

	/**
	 * Returns road start or end point closest to point p
	 * 
	 * @param roads
	 * @param p
	 * @return road start or end point, or null if no close point was pressed
	 */
	private RoadPoint getRoadPointAtOrAround(List<Road> roads, Point p) {
		final int MAX_ERROR_DISTANCE = 10;
		double minDistance = Double.MAX_VALUE;
		RoadPoint closestPoint = null;

		// Loop through all the roads, try to find which road (and start/end point) was
		// clicked
		for (Road r : roads) {
			final Point startPoint = r.getStartPoint().getPoint();
			final Point endPoint = r.getEndPoint().getPoint();

			if (p.distance(startPoint) < minDistance) {
				closestPoint = new RoadPoint(startPoint);
				minDistance = p.distance(startPoint);
			}

			if (p.distance(endPoint) < minDistance) {
				closestPoint = new RoadPoint(endPoint);
				minDistance = p.distance(endPoint);
			}
		}

		if (minDistance > MAX_ERROR_DISTANCE) {
			return null;
		}

		return closestPoint;
	}

	private void scaleRoads(List<Road> roads) {
		// First compute positioning
		double minX = Integer.MAX_VALUE;
		double minY = Integer.MAX_VALUE;
		double maxX = 0;
		double maxY = 0;

		for (Road r : roads) {
			Point start = r.getStartPoint().getPoint();
			Point end = r.getEndPoint().getPoint();

			// Update minX
			minX = Math.min(minX, start.x);
			minX = Math.min(minX, end.x);

			// Update minY
			minY = Math.min(minY, start.y);
			minY = Math.min(minY, end.y);

			// Update maxX
			maxX = Math.max(maxX, start.x);
			maxX = Math.max(maxX, end.x);

			// Update maxY
			maxY = Math.max(maxY, start.y);
			maxY = Math.max(maxY, end.y);
		}

		minX -= X_PADDING;
		maxX += X_PADDING;
		minY -= Y_PADDING;
		maxY += Y_PADDING;
		final double xScale = (maxX - minX) / X_DIM;
		final double yScale = (maxY - minY) / Y_DIM;

		// Subtract off, scale, add padding to x/y values
		for (Road r : roads) {
			Point start = r.getStartPoint().getPoint();
			Point end = r.getEndPoint().getPoint();
			start.x -= minX;
			start.y -= minY;

			end.x -= minX;
			end.y -= minY;

			start.x /= xScale;
			start.y /= yScale;

			end.x /= xScale;
			end.y /= yScale;

			int sYDiff = start.y - (Y_DIM / 2);
			int eYDiff = end.y - (Y_DIM / 2);

			start.y = (Y_DIM / 2) + (-1) * (sYDiff);
			end.y = (Y_DIM / 2) + (-1) * (eYDiff);

		}
	}

	private void drawOptimalPath(List<Road> path, GraphicsContext gc) {
		if (path == null || gc == null)
			return;

		gc.setStroke(Color.BLUE);
		gc.setLineWidth(3);

		for (Road r : path) {
			Point start = r.getStartPoint().getPoint();
			Point end = r.getEndPoint().getPoint();
			gc.strokeLine(start.getX(), start.getY(), end.getX(), end.getY());
		}
	}

	private void drawRoads(List<Road> roads, GraphicsContext gc) {
		final Color lineColor = Color.BLACK;
		final Color defaultColor = Color.LIGHTGRAY;
		final Color startColor = Color.GREEN;
		final Color endColor = Color.RED;

		gc.setStroke(lineColor);
		gc.setLineWidth(1);

		// Paint roads now
		for (Road r : roads) {
			Point start = r.getStartPoint().getPoint();
			Point end = r.getEndPoint().getPoint();
			gc.strokeLine(start.getX(), start.getY(), end.getX(), end.getY());

			final int REC_OFFSET = 5;
			final int REC_DIM = 8;

			// Color start point
			gc.setFill(defaultColor);
			if (activeStart != null && activeStart.equals(r.getStartPoint())) {
				gc.setFill(startColor);
			}

			if (activeEnd != null && activeEnd.equals(r.getStartPoint())) {
				gc.setFill(endColor);
			}

			gc.fillRect(start.x - REC_OFFSET, start.y - REC_OFFSET, REC_DIM, REC_DIM);

			// Color end point
			gc.setFill(defaultColor);
			if (activeStart != null && activeStart.equals(r.getEndPoint())) {
				gc.setFill(startColor);
			}

			if (activeEnd != null && activeEnd.equals(r.getEndPoint())) {
				gc.setFill(endColor);
			}

			gc.fillRect(end.x - REC_OFFSET, end.y - REC_OFFSET, REC_DIM, REC_DIM);
		}
	}
}