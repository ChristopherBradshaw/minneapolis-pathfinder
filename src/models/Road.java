package models;

import java.awt.Point;

public class Road {
	public enum RoadType {
		ONE_WAY, TWO_WAY
	}

	private final RoadType roadType;
	private final RoadPoint startPoint;
	private final RoadPoint endPoint;

	public Road(RoadType roadType, Point startPoint, Point endPoint) {
		this.roadType = roadType;
		this.startPoint = new RoadPoint(startPoint, this);
		this.endPoint = new RoadPoint(endPoint, this);
	}

	public RoadType getRoadType() {
		return roadType;
	}

	public RoadPoint getStartPoint() {
		return startPoint;
	}

	public RoadPoint getEndPoint() {
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
		return "Road [roadType=" + roadType + ", startPoint=" + startPoint.getPoint() + ", endPoint=" + endPoint.getPoint() + "]";
	}

}