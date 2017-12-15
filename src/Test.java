import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;

import app.Pathfinder;
import app.Pathfinder.AlgorithmType;
import app.RoadParser;
import models.Road;
import models.RoadPoint;


public class Test {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException {
		List<Road> roads = RoadParser.parseRoads(new File("test.txt"));
		Map<RoadPoint, List<RoadPoint>> roadMap = RoadParser.buildRoadPointGraph(roads);

		double bfsAccum = 0;
		double astarAccum = 0;
		int numTests = 0;
		Random ran = new Random();
		ran.nextInt(roads.size()-1);

		long bfsTime;
		long astarTime;
		long startTime;
		long elapsedMicroSec;
		RoadPoint start;
		RoadPoint end;
		Map<RoadPoint, Double> heuristics;
		List<Road> optPath;
		int numAstarFaster = 0;
		for(int i = 0; i < 5000; i++) {
			start = roads.get(ran.nextInt(roads.size()-1)).getStartPoint();
			end = roads.get(ran.nextInt(roads.size()-1)).getStartPoint();
			heuristics = Pathfinder.calculateHeuristics(roads, end);
			
			// Test BFS
			startTime = System.nanoTime();
			optPath = Pathfinder.getOptimalPath(roads, roadMap, heuristics, start, end, AlgorithmType.BFS);

			// If path doesn't exist, skip over this case
			if(optPath == null || optPath.size() == 0)
				continue;
			
			bfsTime = (System.nanoTime() - startTime) / 1000;
				
			// Test A*
			startTime = System.nanoTime();
			optPath = Pathfinder.getOptimalPath(roads, roadMap, heuristics, start, end, AlgorithmType.ASTAR);

			// If path doesn't exist, skip over this case
			if(optPath == null || optPath.size() == 0)
				continue;

			astarTime = (System.nanoTime() - startTime) / 1000;

			// Update # test counter
			bfsAccum += bfsTime;
			astarAccum += astarTime;
			if(astarTime < bfsTime) {
				numAstarFaster++;
			}
			numTests++;
		}
		
		double avgBfs = bfsAccum / numTests;
		double avgAstar = astarAccum / numTests;
		System.out.printf("# tests: %d|BFS: %f|A*: %f|Astar is: %f%% slower\n",numTests, avgBfs, avgAstar, (avgAstar/avgBfs)*100);
		System.out.println("Astar faster: " + numAstarFaster);
	}

}
