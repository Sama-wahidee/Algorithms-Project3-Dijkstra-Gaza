package application;

import java.util.LinkedList;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class City {
	static double pic_xMin = 0;
	static double pic_xMax = 589;// map width
	static double pic_yMin = 0;
	static double pic_yMax = 695; // map height

	static double org_xMin = 34.1707489947603;
	static double org_xMax = 34.575060834817954;
	static double org_yMin = 31.614521165206845;
	static double org_yMax = 31.208163033163977;

	private double latitude;
	private double longitude;
	private String name;
	private Boolean isCity;
	private LinkedList<Adjacent> adjacents = new LinkedList<>();
	private Label Cname;
	private Polygon triangle; // Change to Polygon
	private double x;
	private double y;

	public City(String name, double latitude, double longitude, Boolean isCity) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.name = name;
		this.isCity = isCity;
		calculateX(longitude);
		calculateY(latitude);
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getlongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Polygon getTriangle() {
		return triangle;
	}

	public void setTriangle() {
		if (this.isCity) {
			this.triangle = createTriangle();

		}
	}

	private Polygon createTriangle() {
		double size = 10; // Adjust the size of the triangle as needed
		double height = Math.sqrt(3) * size / 2;

		Polygon triangle = new Polygon();
		triangle.getPoints().addAll(x - size / 2, y + height / 2, // Bottom-left corner
				x + size / 2, y + height / 2, // Bottom-right corner
				x, y - height / 2 // Top corner
		);

		triangle.setFill(Color.RED); // Set the fill color
		triangle.setRotate(180);
		return triangle;
	}

	public LinkedList<Adjacent> getAdjacent() {
		return adjacents;
	}

	public void setAdjacent(LinkedList<Adjacent> adjacent) {
		this.adjacents = adjacent;
	}

	public Label getCname() {
		return Cname;
	}

	public void setCname() {
		if (this.isCity == true) {
			this.Cname = new Label(this.name);
			this.Cname.setStyle("-fx-font-size: 15; -fx-text-fill: #ffffff;");
			this.Cname.setLayoutX(x - 10);
			this.Cname.setLayoutY(y);

		}
	}
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	private void calculateX(double orgLon) {
		double resultX1;
		double rightSideEquation = (orgLon - org_xMin) / (org_xMax - org_xMin);
		double calculating = rightSideEquation * (pic_xMax - pic_xMin);
		resultX1 = calculating + pic_xMin;
		this.x = resultX1;
	}

	private void calculateY(double orgLat) {
		double resultY;
		double rightSideEquation = (orgLat - org_yMin) / (org_yMax - org_yMin);
		double calculating = rightSideEquation * (pic_yMax - pic_yMin);
		resultY = calculating + pic_yMin;
		this.y = resultY;
	}
}
