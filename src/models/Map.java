package models;

import java.util.List;

public class Map {
	private final List<Road> roads;

	public Map(final List<Road> roads) {
		this.roads = roads;
	}

	public List<Road> getRoads() {
		return roads;
	}
}
