package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;

public class Main extends Application {
	private ObservableList<String> pathCities = FXCollections.observableArrayList();
	ObservableList<String> cityCB = FXCollections.observableArrayList();
	Image image = new Image(getClass().getResourceAsStream("/resources/Map.jpg"));
	private HashMap<String, Node> table = new HashMap<String, Node>();
	private static final double EARTH_RADIUS = 6371.0;
	ImageView imageView = new ImageView(image);
	LinkedList<Line> lines = new LinkedList<>();
	ComboBox<String> sCB = new ComboBox<>();
	ComboBox<String> tCB = new ComboBox<>();
	Stage primaryStage = new Stage();
	Pane contentPane = new Pane();
	TextArea dTA = new TextArea();
	TextArea pTA = new TextArea();
	File file;
	graph graph;
	String soundFile = "Gaza1.mp3";
	Media sound = new Media(getClass().getResource("/resources/Gaza1.mp3").toExternalForm());
	MediaPlayer mediaPlayer = new MediaPlayer(sound);

	public void start(Stage primaryStage) {
		start();

	}

	public void start() {
		BorderPane root = new BorderPane();
		VBox bVB = new VBox();
		Label l = new Label("Cease fire NOW");
		l.setFont(Font.font("Papyrus", 40));
		l.setStyle("-fx-text-fill: #781917;-fx-font-weight: bold;"); // Set text colo
		Button uploadBT = new Button("Upload File");
		uploadBT.setFont(Font.font("Papyrus", 30));
		uploadBT.setStyle(
				"-fx-background-color:#0000 ; -fx-border-color: #781917; -fx-border-width: 3px;-fx-text-fill: #781917");
		uploadBT.setOnAction(e -> {
			try {
				contentPane.getChildren().add(imageView);
				getCities();
				secoundScene();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		Label space = new Label("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
		bVB.getChildren().addAll(l, space, uploadBT);
		bVB.setAlignment(Pos.CENTER);
		bVB.setSpacing(50);
		root.setCenter(bVB);
		Scene scene = new Scene(root, 347, 700);
		backGround(root);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.setTitle("Dijkstras_Gaza");
		primaryStage.show();
	}

	public void secoundScene() {
		mediaPlayer.play();
		Label sL = new Label("Source");
		sL.setStyle("-fx-font-weight: bold; -fx-font-size: 20;"); // Adjust the font size as needed
		Label tL = new Label("Target");
		tL.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
		sCB.setItems(cityCB);
		sCB.setValue("None");
		tCB.setItems(cityCB);
		tCB.setValue("None");
		HBox sHB = new HBox();
		sHB.getChildren().addAll(sL, sCB);
		sHB.setSpacing(5);
		HBox tHB = new HBox();
		tHB.getChildren().addAll(tL, tCB);
		tHB.setSpacing(5);
		Button runBT = new Button("Run");
		runBT.setAlignment(Pos.CENTER);
		runBT.setStyle(
				"-fx-background-color: #808080; -fx-border-color: red; -fx-border-radius: 20; -fx-background-radius: 20;-fx-font-weight: bold; -fx-font-size: 20;-fx-text-fill: red;");
		runBT.setOnAction(m -> {
			if (sCB.getValue() != "None" && tCB.getValue() != "None") {
				if (!sCB.getValue().equals(tCB.getValue())) {
					getShortestPath(getCity(sCB.getValue()), getCity(tCB.getValue()));
					for (int i = 0; i < lines.size(); i++) {
						lines.get(i).setStrokeWidth(2);
						lines.get(i).setStroke(Color.RED); // Use setStroke to set the line color
						contentPane.getChildren().add(lines.get(i));
						getCity(sCB.getValue()).getTriangle().setFill(Color.BLUE);
						getCity(tCB.getValue()).getTriangle().setFill(Color.YELLOW);

					}
				} else {
					dialog(AlertType.ERROR, "You are already in the city");

				}

			} else {
				dialog(AlertType.ERROR, "You have to choose two cities");

			}
			sCB.setValue("None");
			tCB.setValue("None");
			System.out.println("hiii");
		});
		Label pL = new Label("Path");
		pL.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
		pTA.setStyle("-fx-control-inner-background: black; -fx-text-fill: white; -fx-font-size: 20;");
		Label dL = new Label("Distances");
		dL.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
		dTA.setStyle(
				"-fx-control-inner-background: black; -fx-text-fill: white; -fx-font-size: 20; -fx-max-height: 50;");
		VBox vb = new VBox();
		vb.getChildren().addAll(sHB, tHB, runBT, pL, pTA, dL, dTA);
		vb.setSpacing(8);
		vb.setMaxWidth(200);
		vb.setAlignment(Pos.CENTER);
		vb.setStyle("-fx-background-color: #808080;");
		BorderPane bp = new BorderPane();
		bp.setRight(vb);
		bp.setCenter(contentPane);
		contentPane.setOnMouseClicked(event -> {
			double mouseX = event.getX();
			double mouseY = event.getY();
			System.out.println("Clicked at X: " + mouseX + ", Y: " + mouseY);
		});
		Scene s = new Scene(bp, 789, 695);
		primaryStage.setFullScreen(true);
		primaryStage.setScene(s);
	}

	public void backGround(Pane p) {
		try {
			BackgroundImage bGI = new BackgroundImage(new Image(getClass().getResourceAsStream("/resources/Gaza.jpg")),
					BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
					BackgroundSize.DEFAULT);
			Background bGround = new Background(bGI);
			p.setBackground(bGround);
		} catch (Exception e) {
			dialog(AlertType.ERROR, "Sorry, there was an error while uploading the background");
		}

	}

	public void dialog(AlertType t, String s) {
		Alert alert = new Alert(t);
		alert.setTitle("Dialog");
		alert.setHeaderText("");
		alert.setContentText(s);
		alert.showAndWait();
	}

	public Boolean openFileChooser() {
		try {
			FileChooser fileChooser = new FileChooser();
			Stage fileChooserStage = new Stage();
			File file = fileChooser.showOpenDialog(fileChooserStage);
			BufferedReader br;
			br = new BufferedReader(new FileReader(file));
			String line;
			ArrayList<String> fullFile = new ArrayList<>();
			while ((line = br.readLine()) != null) {
				fullFile.add(line);
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private void getCities() throws FileNotFoundException {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select Cities File");
		File file = fileChooser.showOpenDialog(null);
		int cityCounter = 0;
		int adjCounter = 0;
		if (file != null) {
			try {

				Scanner input = new Scanner(file);
				String line = input.nextLine();
				String[] numbers = line.split(" ");
				int Cnumber = Integer.parseInt(numbers[0]);
				int Anumber = Integer.parseInt(numbers[1]);

				graph = new graph(Cnumber);
				while (cityCounter < Cnumber) {
					line = input.nextLine();
					String[] parts = line.split("\\s+");
					boolean iscity = false;
					if (parts[3].equals("T")) {
						iscity = true;
						cityCB.add(parts[0]);
					}
					City city = new City(parts[0], Double.parseDouble(parts[1]), Double.parseDouble(parts[2]), iscity);
					city.setTriangle();
					city.setCname();
					if (parts[3].equals("T")) {
						city.getTriangle().setOnMouseClicked(e -> {
							if (sCB.getValue() == "None") {
								sCB.setValue(city.getName());
							} else if (tCB.getValue() == "None") {
								tCB.setValue(city.getName());
							}
						});

						city.getTriangle().setOnMouseEntered(e -> {
							city.getTriangle().setFill(Color.GREEN);
						});
						city.getTriangle().setOnMouseExited(e -> {
							city.getTriangle().setFill(Color.RED);
						});

						contentPane.getChildren().addAll(city.getTriangle(), city.getCname());
					}
					graph.vert[cityCounter++] = city;
				}
				while (input.hasNextLine()) {
					line = input.nextLine();
					String[] parts = line.split(" ");
					System.out.println(parts[0]);
					City c1 = getCity(parts[0]);
					City c2 = getCity(parts[1]);
					Adjacent n = new Adjacent(c2, calculateDistance(c1.getLatitude(), c1.getlongitude(),
							c2.getLatitude(), c2.getlongitude()));
					getCity(parts[0]).getAdjacent().add(n);
					// Close the scanner
					adjCounter++;
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace(); // Handle the exception as needed

			}
		}

	}

	// Convert degrees to radians
	private static double toRadians(double degree) {
		return Math.toRadians(degree);
	}

	public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
		double dLat = toRadians(lat2 - lat1);
		double dLon = toRadians(lon2 - lon1);

		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(toRadians(lat1)) * Math.cos(toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);

		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		// Distance in kilometers
		double distance = EARTH_RADIUS * c;

		return distance;
	}

	private City getCity(String cityName) {

		for (int i = 0; i < graph.vert.length; i++) {
			if (graph.vert[i].getName().equalsIgnoreCase(cityName)) {
				return graph.vert[i];
			}
		}
		return null;
	}

	public int getIndexByValue(String targetValue) {
		for (int i = 0; i < graph.vert.length; i++) {
			if (graph.vert[i].getName() == targetValue) {
				return i; // Found the index
			}
		}
		return -1; // Value not found in the array
	}

	
	/*
	 * Get shortest path between to cities
	 */
	private void getShortestPath(City sourceCity, City targetCity) {
		// reset the table to start new one
		buildTable(sourceCity, targetCity);

		// remove previous lines
		for (int i = 0; i < lines.size(); i++) {
			contentPane.getChildren().remove(lines.get(i));
		}
		// clear lines Array List
		lines.clear();

		// clear the Observable List that holds all cities between the target and source
		// cities
		pathCities.clear();
		// clear th00000000000000000000000000000000000000000e list view that shows all
		// cities between the target and source
		// cities
		pTA.clear();
		// reset the total distance Text Field
		dTA.setText("0.0");

		// check if there is path
		if (table.get(targetCity.getName()).getDistance() != Double.POSITIVE_INFINITY
				&& table.get(targetCity.getName()).getDistance() != 0) {
			shortestPath(sourceCity, targetCity);
			DecimalFormat df = new DecimalFormat("###.##");
			Node t = table.get(targetCity.getName());
			dTA.setText(df.format(t.getDistance()) + "Kilometer");
			/*
			 * Add all the cities that were found between the source and target cities
			 */
			StringBuilder sb = new StringBuilder();
			sb.append(sourceCity.getName() + " → \n");
			for (int i = pathCities.size() - 1; i >= 0; i--) {
				if (i == 0) {
					sb.append(pathCities.get(i) + "");
				} else {
					sb.append(pathCities.get(i) + " → \n");
				}
			}
			pTA.setText(sb.toString());

		} else {
			dialog(AlertType.ERROR, "Ther is no path! Missing Edges!");

		}

	}

	/*
	 * A recursive method to trace the path between two cities
	 */
	private void shortestPath(City sourceCity, City targetCity) {
		pathCities.add(targetCity.getName());
		Node t = table.get(targetCity.getName());
		if (t.getSourceCity() == null) {
			return;
		}

		if (t.getSourceCity() != sourceCity) {
			shortestPath(sourceCity, t.getSourceCity());
			lines.add(
					new Line(t.getSourceCity().getX(), t.getSourceCity().getY(), targetCity.getX(), targetCity.getY()));
		}
	}

	/*
	 * Build the hash table by applying the dijkstra algorithm
	 */
	private void buildTable(City source, City targetCity) {
		table.clear();
		for (City i : graph.vert) {
			table.put(i.getName(), new Node(i, false, Double.POSITIVE_INFINITY, null));
		}
		TableCompare comp = new TableCompare();
		Queue<Node> q = new PriorityQueue<Node>(10, comp);
		Node node = table.get(source.getName());
		node.setDistance(0.0);
		node.setKnown(true);
		q.add(node);

		while (!q.isEmpty()) {
			Node temp = q.poll();
			temp.setKnown(true);
			if (temp.getCurrentCity() == targetCity) {
				break;
			}
			LinkedList<Adjacent> adj = temp.getCurrentCity().getAdjacent();

			for (Adjacent c : adj) {
				Node t = table.get(c.getCity().getName());
				if (t.isKnown()) {
					continue;
				}

				double newDis = c.getDistance() + temp.getDistance();
				if (newDis < t.getDistance()) {
					t.setSourceCity(temp.getCurrentCity());
					t.setDistance(newDis);
				}
				q.add(t);
			}

		}

	}

	public static void main(String[] args) {
		launch(args);
	}
}
