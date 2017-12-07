package models;

import java.awt.Point;

public class RoadPoint {
	private final Point point;
	
	public RoadPoint(Point p) {
		this.point = p;
	}

	public Point getPoint() {
		return point;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((point == null) ? 0 : point.hashCode());
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
		if (point == null) {
			if (other.point != null)
				return false;
		} else if (!point.equals(other.point))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RoadPoint [point=" + point + "]";
	}
	
	
}
