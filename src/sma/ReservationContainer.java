package sma;
//--module-path C:/Users/HP/Desktop/javafx-sdk-20/lib --add-modules javafx.controls,javafx.fxml
import java.util.Iterator;

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
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
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
import sma.agents.ReservationAgent;

public class ReservationContainer extends Application {
	private ObservableList<String> liste;
	private ReservationAgent ManagerAgent;

	public static void main(String[] args) {
		launch(ReservationContainer.class);
	}

	public void startConatiner() {
		try {
			Runtime runtime = Runtime.instance();
			Profile profile = new ProfileImpl(false);
			profile.setParameter(Profile.MAIN_HOST, "localhost");
			AgentContainer agentContainer = runtime.createAgentContainer(profile);
			AgentController agentController = agentContainer.createNewAgent("Reservation", "sma.agents.ReservationAgent",
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
		stage1.setTitle("Réservation");
		BorderPane borderPane = new BorderPane();
		/***********/
		// Créer une image
		Image image = new Image("C:\\Users\\pc\\eclipse-workspace\\PROJET_SMA_LAST\\src\\images\\rese.png");
		ImageView imageView = new ImageView(image);
		imageView.setFitWidth(120); // ajuster la largeur de l'image
		imageView.setPreserveRatio(true); // conserver les proportions de l'image

		// Créer un label
		Label label = new Label("  Je suis un Agent de resérvation  :)");
		label.setFont(new Font("Arial", 15)); // ajuster la police du label

		// Créer une boîte horizontale pour l'image et le label
		HBox hboxTop = new HBox();
		hboxTop.setPadding(new Insets(10, 10, 10, 10));
		hboxTop.setSpacing(10);
		hboxTop.getChildren().addAll(imageView, label);

		// Ajouter la boîte horizontale en haut de l'interface
		borderPane.setTop(hboxTop);

		// Le reste du code reste le même

		// Définir le fond
		borderPane.setBackground(new Background(new BackgroundFill(Color.web("#ABEBC6"), null, null)));

		liste = FXCollections.observableArrayList();

		VBox vBox = new VBox();
		//GridPane gridPane = new GridPane();
		ListView<String> listView = new ListView<>(liste);
		
		//gridPane.add(listView, 0, 0);

		vBox.getChildren().add(listView);
		borderPane.setCenter(vBox);

		vBox.setPadding(new Insets(10));
		vBox.setSpacing(10);
		Scene scene = new Scene(borderPane,400, 400);
		stage1.setScene(scene);
		stage1.show();

	}

	public void insertMessage(GuiEvent guiEvent) {
		Iterator<String> it = guiEvent.getAllParameter();
		while (it.hasNext()) {

			String msg = it.next();
			liste.add(msg);
		}
	}

	public ReservationAgent getManagerAgent() {
		return ManagerAgent;
	}

	public void setManagerAgent(ReservationAgent ManagerAgent) {
		this.ManagerAgent = ManagerAgent;
	}

}
