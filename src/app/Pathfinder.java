package app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import models.Road;
import models.RoadPoint;

public class Pathfinder {
	public enum AlgorithmType {
		BFS, ASTAR, IDASTAR
	}

	/**
	 * Compute and return the optimal path from the start to the end point
	 * 
	 * @param roads
	 * @param roadsMap
	 * @param startPoint
	 * @param endPoint
	 * @param type
	 *            which search algorithm to use
	 * @return the optimal path from the start to the end point
	 */
	public static List<Road> getOptimalPath(List<Road> roads, Map<RoadPoint, List<RoadPoint>> roadsMap,
			RoadPoint startPoint, RoadPoint endPoint, AlgorithmType type) {

		List<Road> optimalPath = null;
		switch (type) {
		case BFS:
			optimalPath = doBFSAlg(roads, roadsMap, startPoint, endPoint);
			break;
		case ASTAR:
			optimalPath = doAStarAlg(roads, roadsMap, startPoint, endPoint);
			break;
		case IDASTAR:
			optimalPath = doIDAStarAlg(roads, roadsMap, startPoint, endPoint);
			break;
		}

		return optimalPath;
	}

	private static Road findRoad(List<Road> roads, RoadPoint p1, RoadPoint p2) {
		for (Road r : roads) {
			if (r.getStartPoint().equals(p1) && r.getEndPoint().equals(p2)
					|| r.getStartPoint().equals(p2) && r.getEndPoint().equals(p1))
				return r;
		}

		return null;
	}

	private static List<Road> doBFSAlg(List<Road> roads, Map<RoadPoint, List<RoadPoint>> roadsMap, RoadPoint startPoint,
			RoadPoint endPoint) {
		
		long timeStart = System.currentTimeMillis();
		LinkedList<RoadPoint> queue = new LinkedList<>();
		Set<RoadPoint> visited = new HashSet<>();

		// Keep track of which node added another node to allow path retrieval at the end
		Map<RoadPoint, RoadPoint> parentMap = new HashMap<>();

		queue.add(startPoint);
		int nodes = 0;
		int neighborsAdded = 0;
		// BFS
		while (queue.size() != 0) {
			RoadPoint cur = queue.poll();
			if (cur == null)
				break;

			visited.add(cur);
			if (cur.equals(endPoint)) {
				break;
			}
			nodes++;

			List<RoadPoint> neighbors = roadsMap.get(cur);
			if (neighbors != null) {
				for (RoadPoint p : neighbors) {
					if (!visited.contains(p)) {
						visited.add(p);
						parentMap.put(p, cur);
						queue.add(p);
						neighborsAdded++;
					}
				}
			}
		}

		// Reconstruct the optimal path with parentMap
		List<Road> optimalPath = new ArrayList<>();
		RoadPoint curNode = endPoint;
		while (curNode != null) {
			RoadPoint prevNode = parentMap.get(curNode);
			Road r = findRoad(roads, curNode, prevNode);
			if (r != null) {
				optimalPath.add(r);
			}
			curNode = prevNode;
		}

		System.out.println("Elapsed: " + (System.currentTimeMillis()-timeStart) + "ms. Visited " + nodes + " nodes.");
		return optimalPath;
	}

	// We'll use straight line distance as a heuristic
	private static Map<RoadPoint, Double> calculateHeuristics(List<Road> roads, RoadPoint endPoint) {
			Map<RoadPoint, Double> heuristics = new HashMap<>();
			for(Road r : roads) {
				heuristics.put(r.getStartPoint(), endPoint.getPoint().distance(r.getStartPoint().getPoint()));
				heuristics.put(r.getEndPoint(), endPoint.getPoint().distance(r.getEndPoint().getPoint()));
			}
			
			return heuristics;
	}
	
	private static List<Road> doAStarAlg(List<Road> roads, Map<RoadPoint, List<RoadPoint>> roadsMap,
			RoadPoint startPoint, RoadPoint endPoint) {
		Map<RoadPoint, Double> heuristics = calculateHeuristics(roads, endPoint);

		return null;
	}

	private static List<Road> doIDAStarAlg(List<Road> roads, Map<RoadPoint, List<RoadPoint>> roadsMap,
			RoadPoint startPoint, RoadPoint endPoint) {
		return null;
	}
}
