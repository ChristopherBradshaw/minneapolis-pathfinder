package app;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import models.Road;

public class RoadParser {

	/**
	 * Get a list of Road objects from the file. Each non-empty line in the file
	 * should be of the form:
	 * 
	 * roadType srcX srcY dstX dstY
	 * 
	 * where roadType is 1 or 2 (one-way and two-way respectively) and the
	 * others are integers
	 * 
	 * @param inFile
	 *            file containing road entries
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static final List<Road> parseRoads(File inFile)
			throws FileNotFoundException, IOException {
		final List<Road> roads = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(inFile))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] tokens = line.split(" ");
				Road.RoadType roadType = null;
				Point src;
				Point dst;

				// Parse road type
				int roadTypeN = Integer.parseInt(tokens[0]); 
				if(roadTypeN == 1) {
					roadType = Road.RoadType.ONE_WAY;
				} else if(roadTypeN == 2) {
					roadType = Road.RoadType.TWO_WAY;
				} else {
					throw new RuntimeException("Unsupported roadtype: " + roadTypeN);
				}
				
				// Parse src point
				src = new Point(Integer.parseInt(tokens[1]),Integer.parseInt(tokens[2]));
				
				// Parse dst point
				dst = new Point(Integer.parseInt(tokens[3]),Integer.parseInt(tokens[4]));

				// Construct road
				roads.add(new Road(roadType, src, dst));
			}
		}
		
		return roads;
	}
}
