import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import app.RoadParser;


public class Test {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException {
		RoadParser.parseRoads(new File("test.txt"));
	}

}
