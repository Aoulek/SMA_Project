package sma;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.gui.GuiEvent;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
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
import sma.agents.ClientAgent;

public class ClientContainer extends Application {
	private ObservableList<String> listLivre;
	private ClientAgent clientAgent;

	public static void main(String[] args) {
		launch(ClientContainer.class);
	}

	public void startConatiner() {
		try {
			Runtime runtime = Runtime.instance();
			Profile profile = new ProfileImpl(false);
			profile.setParameter(Profile.MAIN_HOST, "localhost");
			AgentContainer agentContainer = runtime.createAgentContainer(profile);
			AgentController agentController = agentContainer.createNewAgent("Client", "sma.agents.ClientAgent",
					new Object[] { this });
			agentController.start();

		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	@Override
	public void start(Stage stage1) throws Exception {
		startConatiner();
		
		// TODO Auto-generated method stub
				stage1.setTitle("Agent Client");

				// diviser en 5 partie
				BorderPane borderPane = new BorderPane();

				// Définir le fond
				borderPane.setBackground(new Background(new BackgroundFill(Color.ANTIQUEWHITE, null, null)));

				// Créer une image
				Image image = new Image("C:\\Users\\HP\\eclipse-workspace\\PROJET_SMA_LAST\\src\\images\\client.png");
				ImageView imageView = new ImageView(image);
				imageView.setFitWidth(120); // ajuster la largeur de l'image
				imageView.setPreserveRatio(true); // conserver les proportions de l'image
		      
				// Créer un label
				Label label = new Label(" Client");
				Label labe2 = new Label("Je veux faire une demande de réservation ");
				label.setFont(new Font("Arial", 15)); // ajuster la police du label
				labe2.setFont(new Font("Arial", 15));
				// Créer une boîte horizontale pour l'image et le label
				HBox hboxTop = new HBox();
				hboxTop.setPadding(new Insets(10, 10, 10, 10));
				hboxTop.setSpacing(10);
				hboxTop.getChildren().addAll(imageView, label);

				// Ajouter la boîte horizontale en haut de l'interface
				borderPane.setTop(hboxTop);
				
                
				Label restaurantLabel = new Label(" Nom de Restaurant ");
				restaurantLabel.setFont(new Font("Arial", 14)); // ajuster la police du label
				// Create a horizontal box for the text field and the button
				HBox hbox = new HBox();
				hbox.setPadding(new Insets(10, 10, 10, 10));
				hbox.setSpacing(10);
				TextField t = new TextField();
				Button Consulter = new Button("Ajouter ");
				hbox.getChildren().addAll(restaurantLabel, t, Consulter);

				// Create a vertical box for the list of restaurants and the horizontal box
				VBox vBox = new VBox();
				//GridPane gp = new GridPane();
				listLivre = FXCollections.observableArrayList();
				
				ListView<String> lvM = new ListView<String>(listLivre);
				//lvM.resize(50,60);
				//gp.add(lvM, 0, 0);
				
				vBox.getChildren().addAll(lvM,hbox);
				
				vBox.setPadding(new Insets(10));
				vBox.setSpacing(10);

				// Add the horizontal box with the image and the label at the top of the interface
				borderPane.setTop(hboxTop);

				// Add the vertical box with the list of restaurants and the horizontal box at the center of the interface
				borderPane.setCenter(vBox);

				Scene s = new Scene(borderPane, 400, 400);
				stage1.setScene(s);
				stage1.show();
				
				
				
		
		
		Consulter.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				String livre = t.getText();
				GuiEvent guiEvent = new GuiEvent(this, 1);
				guiEvent.addParameter(livre);
				clientAgent.onGuiEvent(guiEvent);

			}
			
		});
		

	}

	public void insertMessage(GuiEvent guiEvent) {
		String lmsg = guiEvent.getParameter(0).toString();
		System.out.println("livre" + lmsg);
		listLivre.add(lmsg);
	}

	public ClientAgent getClienAgent() {
		return clientAgent;
	}

	public void setClienAgent(ClientAgent clientAgent) {
		this.clientAgent = clientAgent;
	}

}
