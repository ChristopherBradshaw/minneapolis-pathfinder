package models;

import java.awt.Point;

public class RoadPoint {
	private final Point point;
	private final Road road;
	
	public RoadPoint(Point p, Road road) {
		this.point = p;
		this.road = road;
	}

	public Point getPoint() {
		return point;
	}

	public Road getRoad() {
		return road;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((point == null) ? 0 : point.hashCode());
		result = prime * result + ((road == null) ? 0 : road.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RoadPoint other = (RoadPoint) obj;
		
		return other.getPoint().equals(this.getPoint());
	}

	@Override
	public String toString() {
		return "RoadPoint [point=" + point + ", road=" + road + "]";
	}
	
}
