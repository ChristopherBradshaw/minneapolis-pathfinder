package models;

import java.awt.Point;

public class Road {
	public enum RoadType {
		ONE_WAY, TWO_WAY
	}

	private final RoadType roadType;
	private final Point startPoint;
	private final Point endPoint;

	public Road(RoadType roadType, Point startPoint, Point endPoint) {
		this.roadType = roadType;
		this.startPoint = startPoint;
		this.endPoint = endPoint;
	}

	public RoadType getRoadType() {
		return roadType;
	}

	public Point getStartPoint() {
		return startPoint;
	}

	public Point getEndPoint() {
		return endPoint;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((endPoint == null) ? 0 : endPoint.hashCode());
		result = prime * result + ((roadType == null) ? 0 : roadType.hashCode());
		result = prime * result + ((startPoint == null) ? 0 : startPoint.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Road other = (Road) obj;
		if (endPoint == null) {
			if (other.endPoint != null)
				return false;
		} else if (!endPoint.equals(other.endPoint))
			return false;
		if (roadType != other.roadType)
			return false;
		if (startPoint == null) {
			if (other.startPoint != null)
				return false;
		} else if (!startPoint.equals(other.startPoint))
			return false;
		return true;
	}

	public String toString() {
		return "Road [roadType=" + roadType + ", startPoint=" + startPoint + ", endPoint=" + endPoint + "]";
	}

}