package sma;

import java.util.Scanner;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.gui.GuiEvent;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import sma.agents.RestaurantAgent;

public class RestaurantContainer extends Application {
	private ObservableList<String> listRestaurant;
	private RestaurantAgent restaurantAgent;
	private AgentContainer agentContainer;

	public static void main(String[] args) {
		launch(RestaurantContainer.class);
	}

	public void startConatiner() {
		try {
			Runtime runtime = Runtime.instance();
			Profile profile = new ProfileImpl(false);
			profile.setParameter(Profile.MAIN_HOST, "localhost");
			agentContainer = runtime.createAgentContainer(profile);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
//.setStyle("-fx-background-color: #a61414;"); 
//.setTextFill(Color.web("#ffffff"));
	@Override
	public void start(Stage primaryStage) throws Exception {
		startConatiner();
		primaryStage.setTitle("Restaurant");
		
		
		// diviser en 5 partie
		BorderPane borderPane = new BorderPane();

		// Définir le fond
		//borderPane.setBackground(new Background(new BackgroundFill(Color.IVORY, null, null)));

		// Créer une image
		Image image = new Image("C:\\Users\\pc\\eclipse-workspace\\PROJET_SMA_LAST\\src\\images\\logo.png");
		ImageView imageView = new ImageView(image);
		imageView.setFitWidth(120); // ajuster la largeur de l'image
		imageView.setPreserveRatio(true); // conserver les proportions de l'image
      
		// Créer un label
		Label label = new Label(" Restaurant ");
		
		label.setFont(new Font("Arial", 15)); // ajuster la police du label
		
		// Créer une boîte horizontale pour l'image et le label
		HBox hboxTop = new HBox();
		hboxTop.setPadding(new Insets(10, 10, 10, 10));
		hboxTop.setSpacing(10);
		hboxTop.getChildren().addAll(imageView, label);

		// Ajouter la boîte horizontale en haut de l'interface
		borderPane.setTop(hboxTop);
		
		Label Restaurant = new Label(" Nom de Restaurant ");
		Restaurant.setTextFill(Color.DARKCYAN);
		borderPane.setStyle("-fx-background-color: ABE5EB;");
		Restaurant.setFont(new Font("Arial", 14)); // ajuster la police du label
		// Create a horizontal box for the text field and the button
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(10, 10, 10, 10));
		hbox.setSpacing(10);
		TextField textFieldRestaurant = new TextField();
		Button btnRestaurant = new Button("  Add agent ");
		
		
		hbox.getChildren().add(Restaurant);
		hbox.getChildren().add(textFieldRestaurant);
		hbox.getChildren().add(btnRestaurant);
		
		
		
		
		// Create a vertical box for the list of restaurants and the horizontal box
		VBox vBox = new VBox();
		GridPane gp = new GridPane();
		listRestaurant = FXCollections.observableArrayList();
	
		ListView<String> lvM = new ListView<String>(listRestaurant);

		
		
		//gp.add(lvM, 0, 0);
		
		
		vBox.getChildren().addAll(lvM, hbox);
		vBox.setPadding(new Insets(10));
		vBox.setSpacing(10);
		
		
		
		
		// Add the horizontal box with the image and the label at the top of the interface
		borderPane.setTop(hboxTop);
		// Add the vertical box with the list of restaurants and the horizontal box at the center of the interface
		borderPane.setCenter(vBox);

		Scene s = new Scene(borderPane, 450, 450);
		primaryStage.setScene(s);
		primaryStage.show();
		
		
		
		/*
		borderPane.setTop(hBox);
		borderPane.setStyle("-fx-background-color: #575451;");
		VBox vBox = new VBox();
		GridPane gridPane = new GridPane();
		ListView<String> listView = new ListView<>(listRestaurant);
		listView.setMinSize(450, 600);
		gridPane.add(listView, 0, 0);
		vBox.getChildren().add(gridPane);
		borderPane.setCenter(vBox);		
		
		vBox.setPadding(new Insets(10));
		vBox.setSpacing(10);
		Scene scene = new Scene(borderPane,500, 700);
		primaryStage.setScene(scene);
		primaryStage.show();
		*/
		btnRestaurant.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String agentName = textFieldRestaurant.getText();
				AgentController agentController;
				try {
					agentController = agentContainer.createNewAgent(agentName, "sma.agents.RestaurantAgent",
							new Object[] { RestaurantContainer.this });
					agentController.start();
				} catch (StaleProxyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

	public void insertMessage(GuiEvent guiEvent) {
		String message = guiEvent.getParameter(0).toString();
		System.out.println("insert message" + message);
		listRestaurant.add(message);
	}

	public RestaurantAgent getRestaurantAgent() {
		return restaurantAgent;
	}

	public void setRestaurantAgent(RestaurantAgent restaurantAgent) {
		this.restaurantAgent = restaurantAgent;
	}

}
