package app;

import java.util.ArrayList;
import java.util.List;

import models.Road;
import models.RoadPoint;

public class Pathfinder {
	public enum AlgorithmType {
		BFS, ASTAR, IDASTAR
	}

	/**
	 * Compute and return the optimal path from the start to the end point
	 * @param roads list of all roads
	 * @param startPoint
	 * @param endPoint
	 * @param type which search algorithm to use
	 * @return the optimal path from the start to the end point
	 */
	public static List<Road> getOptimalPath(List<Road> roads, RoadPoint startPoint, RoadPoint endPoint,
			AlgorithmType type) {
		
		List<Road> optimalPath = new ArrayList<>();

		// Dummy data
		for(Road r : roads) {
			if(Math.random() < .5) {
				optimalPath.add(r);
			}
		}
		
		/*
		switch (type) {
		case BFS:
			optimalPath = doDFSAlg(roads, startPoint, endPoint);
			break;
		case ASTAR:
			optimalPath = doAStarAlg(roads, startPoint, endPoint);
			break;
		case IDASTAR:
			optimalPath = doIDAStarAlg(roads, startPoint, endPoint);
			break;
		}
		*/
		return optimalPath;
	}

	private static List<Road> doDFSAlg(List<Road> roads, RoadPoint startPoint, RoadPoint endPoint) {
		return null;
	}

	private static List<Road> doAStarAlg(List<Road> roads, RoadPoint startPoint, RoadPoint endPoint) {
		return null;
	}

	private static List<Road> doIDAStarAlg(List<Road> roads, RoadPoint startPoint, RoadPoint endPoint) {
		return null;
	}
}
