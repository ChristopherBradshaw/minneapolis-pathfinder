package app;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
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
			Map<RoadPoint, Double> heuristics, RoadPoint startPoint, RoadPoint endPoint, AlgorithmType type) {

		List<Road> optimalPath = null;
		switch (type) {
		case BFS:
			optimalPath = doBFSAlg(roads, roadsMap, startPoint, endPoint);
			break;
		case ASTAR:
			optimalPath = doAStarAlg(roads, roadsMap, heuristics, startPoint, endPoint);
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

	// Start at endPoint, work our way back to the start using parent map
	private static List<Road> reconstructPath(List<Road> roads, Map<RoadPoint, RoadPoint> parentMap,
			RoadPoint endPoint) {
		List<Road> optimalPath = new ArrayList<>();
		RoadPoint curNode = endPoint;
		while (curNode != null) {
			RoadPoint prevNode = parentMap.get(curNode);
			Road r = findRoad(roads, curNode, prevNode);
			if (r != null) {
				optimalPath.add(r);
			}
			
			// Sanity check -- will cycle forever otherwise
			if(curNode.equals(prevNode)) {
				return null;
			}

			curNode = prevNode;
		}

		return optimalPath;
	}

	private static List<Road> doBFSAlg(List<Road> roads, Map<RoadPoint, List<RoadPoint>> roadsMap, RoadPoint startPoint,
			RoadPoint endPoint) {

		LinkedList<RoadPoint> queue = new LinkedList<>();
		Set<RoadPoint> visited = new HashSet<>();

		// Keep track of which node added another node to allow path retrieval at the
		// end
		Map<RoadPoint, RoadPoint> parentMap = new HashMap<>();

		queue.add(startPoint);

		// BFS
		while (queue.size() != 0) {
			RoadPoint cur = queue.poll();
			if (cur == null)
				break;

			visited.add(cur);
			if (cur.equals(endPoint)) {
				break;
			}

			// Get all neighbors and add them to queue if they haven't been visited already
			List<RoadPoint> neighbors = roadsMap.get(cur);
			if (neighbors != null) {
				for (RoadPoint p : neighbors) {
					if (!visited.contains(p)) {
						visited.add(p);
						parentMap.put(p, cur);
						queue.add(p);
					}
				}
			}
		}

		// Reconstruct the optimal path with parentMap
		List<Road> optimalPath = reconstructPath(roads, parentMap, endPoint);

		return optimalPath;
	}

	// We'll use straight line distance as a heuristic
	public static Map<RoadPoint, Double> calculateHeuristics(List<Road> roads, RoadPoint endPoint) {
		Map<RoadPoint, Double> heuristics = new HashMap<>();
		for (Road r : roads) {
			heuristics.put(r.getStartPoint(), endPoint.getPoint().distance(r.getStartPoint().getPoint()));
			heuristics.put(r.getEndPoint(), endPoint.getPoint().distance(r.getEndPoint().getPoint()));
		}

		return heuristics;
	}

	private static List<Road> doAStarAlg(List<Road> roads, Map<RoadPoint, List<RoadPoint>> roadsMap,
			Map<RoadPoint, Double> heuristics, RoadPoint startPoint, RoadPoint endPoint) {

		// Map a road point to its path length from the start
		Map<RoadPoint, Double> gCosts = new HashMap<>();

		// Nodes that have been visited but not expanded (i.e sucessors haven't been
		// explored yet). We store these in increasing f-cost order.
		// f(n) = g(n) + h(n) or total-cost(n) = path-cost(n) + heuristic(n)
		PriorityQueue<RoadPoint> openList = new PriorityQueue<RoadPoint>(new Comparator<RoadPoint>() {

			@Override
			public int compare(RoadPoint o1, RoadPoint o2) {
				// Nodes with low f-costs come first
				double f1Cost = gCosts.getOrDefault(o1, Double.MAX_VALUE) + heuristics.get(o1);
				double f2Cost = gCosts.getOrDefault(o2, Double.MAX_VALUE) + heuristics.get(o2);
				return Double.compare(f1Cost, f2Cost);
			}
		});

		// Nodes that have been visited and expanded
		Set<RoadPoint> closedList = new HashSet<>();

		// Keep track of which node added another node to allow path retrieval at the
		// end
		Map<RoadPoint, RoadPoint> parentMap = new HashMap<>();

		// Add start point
		gCosts.put(startPoint, 0.0);
		openList.add(startPoint);

		// Keep going until all nodes have been expanded or we find the goal
		while (!openList.isEmpty()) {

			// Get node with lowest f-cost
			RoadPoint curNode = openList.remove();

			// Stop if we found the goal
			if (curNode.equals(endPoint)) {
				break;
			}

			if (roadsMap.containsKey(curNode)) {
				// Generate each successor that comes after curNode
				for (RoadPoint neighbor : roadsMap.get(curNode)) {
					// Calculate g-cost of this neighbor assuming we use curNode's path
					double newNeighborGCost = gCosts.get(curNode) + curNode.getPoint().distance(neighbor.getPoint());
					double oldNeighborGCost = gCosts.getOrDefault(neighbor, Double.MAX_VALUE);

					if (openList.contains(neighbor)) {
						if (oldNeighborGCost <= newNeighborGCost) {

						} else {
							gCosts.put(neighbor, newNeighborGCost);
							parentMap.put(neighbor, curNode);
						}
					} else if (closedList.contains(neighbor)) {
						if (oldNeighborGCost <= newNeighborGCost) {

						} else {
							// Move neighbor from the closed list to the open list
							closedList.remove(neighbor);
							openList.add(neighbor);
							gCosts.put(neighbor, newNeighborGCost);
							parentMap.put(neighbor, curNode);
						}
					} else {
						openList.add(neighbor);
						gCosts.put(neighbor, newNeighborGCost);
						parentMap.put(neighbor, curNode);
					}

				}
			}

			closedList.add(curNode);
		}

		return reconstructPath(roads, parentMap, endPoint);
	}

	private static List<Road> doIDAStarAlg(List<Road> roads, Map<RoadPoint, List<RoadPoint>> roadsMap,
			RoadPoint startPoint, RoadPoint endPoint) {
		return null;
	}
}
