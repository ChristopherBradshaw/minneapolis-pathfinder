package app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import models.Road;

public class MapApp extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	private final int X_DIM = 800;
	private final int Y_DIM = 800;

	@Override
	public void start(Stage primaryStage) throws FileNotFoundException, IOException {
		primaryStage.setTitle("Minneapolis Pathfinder");
		Group root = new Group();
		Canvas canvas = new Canvas(X_DIM, Y_DIM);
		GraphicsContext gc = canvas.getGraphicsContext2D();

		List<Road> roads = RoadParser.parseRoads(new File("test.txt"));
		scaleRoads(roads);
		drawRoads(roads, gc);

		root.getChildren().add(canvas);
		canvas.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				System.out.println(event);
			}
			
		});
		
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
	}

	private void scaleRoads(List<Road> roads) {
		// First compute positioning
		double minX = Integer.MAX_VALUE;
		double minY = Integer.MAX_VALUE;
		double maxX = 0;
		double maxY = 0;

		for (Road r : roads) {
			// Update minX
			minX = Math.min(minX, r.getStartPoint().getX());
			minX = Math.min(minX, r.getEndPoint().getX());

			// Update minY
			minY = Math.min(minY, r.getStartPoint().getY());
			minY = Math.min(minY, r.getEndPoint().getY());

			// Update maxX
			maxX = Math.max(maxX, r.getStartPoint().getX());
			maxX = Math.max(maxX, r.getEndPoint().getX());

			// Update maxY
			maxY = Math.max(maxY, r.getStartPoint().getY());
			maxY = Math.max(maxY, r.getEndPoint().getY());
		}

		final double xScale = (maxX - minX) / X_DIM;
		final double yScale = (maxY - minY) / Y_DIM;

		// Subtract off, scale, add padding to x/y values
		for (Road r : roads) {
			r.getStartPoint().x -= minX;
			r.getStartPoint().y -= minY;

			r.getEndPoint().x -= minX;
			r.getEndPoint().y -= minY;

			r.getStartPoint().x /= xScale;
			r.getStartPoint().y /= yScale;

			r.getEndPoint().x /= xScale;
			r.getEndPoint().y /= yScale;

			
			int sYDiff = r.getStartPoint().y - (Y_DIM/2);
			int eYDiff = r.getEndPoint().y - (Y_DIM/2);
			
			r.getStartPoint().y = (Y_DIM/2) + (-1)*(sYDiff);
			r.getEndPoint().y = (Y_DIM/2) + (-1)*(eYDiff);

		}
	}

	private void drawRoads(List<Road> roads, GraphicsContext gc) {

		gc.setFill(Color.GREEN);
		gc.setStroke(Color.BLUE);
		gc.setLineWidth(1);

		// Paint roads now
		for (Road r : roads) {
			gc.strokeLine(r.getStartPoint().getX(), r.getStartPoint().getY(), r.getEndPoint().getX(),
					r.getEndPoint().getY());
			
			final int REC_OFFSET = 4;
			final int REC_DIM = 7;
			gc.setFill(Color.GREEN);
			gc.fillRect(r.getStartPoint().x-REC_OFFSET, r.getStartPoint().y-REC_OFFSET, REC_DIM, REC_DIM);
			
			gc.setFill(Color.RED);
			gc.fillRect(r.getEndPoint().x-REC_OFFSET, r.getEndPoint().y-REC_OFFSET, REC_DIM, REC_DIM);

		}
	}
}