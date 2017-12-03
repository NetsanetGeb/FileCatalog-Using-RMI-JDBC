package filecatalog.rmi.jdbc.client.view;

import filecatalog.rmi.jdbc.client.controller.ClientViewController;
import java.security.MessageDigest;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Login {

	private Scene scene;

	public Login(ClientViewController viewManager) {
		
                
                BorderPane border = new BorderPane();
                GridPane root = new GridPane();
                //root.setStyle("-fx-border: 2px solid; -fx-border-color: red;");
                HBox tbox= new HBox();
                HBox lbox= new HBox();
                HBox bbox= new HBox();
                VBox rbox= new VBox();
                        tbox.setPadding(new Insets(15, 12, 15, 12));
                        lbox.setPadding(new Insets(15, 12, 15, 12));
                        bbox.setPadding(new Insets(15, 12, 15, 12));
                        rbox.setPadding(new Insets(15, 12, 15, 12)); 
                        tbox.setStyle("-fx-background-color: #336699;");                
                        lbox.setStyle("-fx-background-color: #336699;");
                        bbox.setStyle("-fx-background-color: #336699;");  
                        rbox.setStyle("-fx-background-color: #336699;");   
                border.setTop(tbox);
                border.setLeft(lbox);
                border.setBottom(bbox);
                border.setRight(rbox);
                
                border.setCenter(root);
		root.setAlignment(Pos.CENTER);
		root.setHgap(10);
		root.setVgap(10);
		//root.setPadding(new Insets(25, 25, 25, 25));
                // root.setPadding(new Insets(0, 10, 0, 10));
                
		Label loginLabel = new Label("Catalog Login");
                loginLabel.setFont(new Font("Arial", 30));
                loginLabel.setTextFill(Color.web("#0076a3"));
		root.add(loginLabel, 1, 0);

		Label username = new Label("UserName: ");
                username.setFont(new Font("Arial", 15));
                username.setTextFill(Color.web("#0076a3"));
		root.add(username, 0, 1);

		TextField usernameValue = new TextField();
		root.add(usernameValue, 1, 1);

		Label password = new Label("Password: ");
                password.setFont(new Font("Arial", 15));
                password.setTextFill(Color.web("#0076a3"));
		root.add(password, 0, 2);

		PasswordField passwordValue = new PasswordField();
		root.add(passwordValue, 1, 2);

		Button login = new Button("Login");
                login.setFont(new Font("Arial", 15));
                login.setTextFill(Color.web("#0076a3"));
		root.add(login, 1, 3);
		login.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				try {
					String username = usernameValue.getText();
					String password = passwordValue.getText();
					String passwordHashed = null;
					if (!username.equals("") && !password.equals("")) {
						try {
							MessageDigest md = MessageDigest.getInstance("MD5");
							md.update(password.getBytes());
							byte[] bytes = md.digest();
							StringBuilder sb = new StringBuilder();
							for (int i = 0; i < bytes.length; i++) {
								sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
							}
							passwordHashed = sb.toString();
						} catch (Exception exception) {
							System.err.println("Error while hashing password.");
						}
						if (viewManager.getServer().login(viewManager.getServerReader(), username, passwordHashed)) {
							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("Successfull Login");
							alert.setHeaderText(null);
							alert.setContentText(" Successfull Login...");
							alert.showAndWait();
							LoggedInView loggedin = new LoggedInView(viewManager);
							Scene loggedinScene = loggedin.getScene();
							viewManager.getStage().setScene(loggedinScene);
						} else {
							Alert alert = new Alert(AlertType.ERROR);
							alert.setTitle("Login failure");
							alert.setHeaderText(null);
							alert.setContentText(
									"Login Failure.\nVerify the username and password are correct.\nTo login you  need to register first.\nTry Again !!!");
							alert.showAndWait();
							usernameValue.setText("");
							passwordValue.setText("");
						}
					}
				} catch (Exception exception) {
					System.err.println("Login request failed.");
				}
			}
		});

		Image imageRegister = new Image(getClass().getResourceAsStream("register.png"), 40, 40, true, false);
		Button registerButton = new Button(" Register First ", new ImageView(imageRegister));
                registerButton.setFont(new Font("Arial", 15));
                registerButton.setTextFill(Color.web("#0076a3"));
		root.add(registerButton, 1, 6);
		registerButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				RegisterView register = new RegisterView(viewManager);
				Scene registerScene = register.getScene();
				viewManager.getStage().setScene(registerScene);
			}
		});

		this.scene = new Scene(border, 400, 400);
		this.scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

	}

	public Scene getScene() {
		return this.scene;
	}

}
