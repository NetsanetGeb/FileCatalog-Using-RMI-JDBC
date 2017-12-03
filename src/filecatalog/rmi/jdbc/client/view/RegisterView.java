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

public class RegisterView {

	private Scene scene;

	public RegisterView(ClientViewController viewManager) {
            
                BorderPane border = new BorderPane();
		GridPane root = new GridPane();
                
                
                
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
		root.setPadding(new Insets(25, 25, 25, 25));

		Label registerLabel = new Label("Register In Catalog");
                registerLabel.setFont(new Font("Arial", 23));
                registerLabel.setTextFill(Color.web("#0076a3"));
		root.add(registerLabel, 1, 0);

		Label username = new Label("UserName: ");
                username.setFont(new Font("Arial", 15));
                username.setTextFill(Color.web("#0076a3"));
		root.add(username, 0, 1);

		TextField usernameField = new TextField();
		root.add(usernameField, 1, 1);

		Label password = new Label("Password: ");
                password.setFont(new Font("Arial", 15));
                password.setTextFill(Color.web("#0076a3"));
		root.add(password, 0, 2);

		PasswordField passwordValue = new PasswordField();
		root.add(passwordValue, 1, 2);

                Image imageRegister = new Image(getClass().getResourceAsStream("register.png"), 40, 40, true, false);
		Button register = new Button(" Register ", new ImageView(imageRegister));
                register.setFont(new Font("Arial", 15));
                register.setTextFill(Color.web("#0076a3"));
		root.add(register, 1, 3);
		register.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				try {
					String username = usernameField.getText();
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
						if (viewManager.getServer().register(viewManager.getServerReader(), username, passwordHashed)) {
							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("Register success");
							alert.setHeaderText(null);
							alert.setContentText("You are now registered.\nYou can login.");
							alert.showAndWait();
							RegisterdView registerdView = new RegisterdView(viewManager);
							Scene registerdScene = registerdView.getScene();
							viewManager.getStage().setScene(registerdScene);
						} else {
							Alert alert = new Alert(AlertType.ERROR);
							alert.setTitle("Register failure");
							alert.setHeaderText(null);
							alert.setContentText(
									"Impossible to register.\nThe username/password is empty or the username already exists, please choose another one.\n Make sure that you are not already logged.");
							alert.showAndWait();
							usernameField.setText("");
							passwordValue.setText("");
                                                        Login login = new Login(viewManager);
	                                                Scene loginScene = login.getScene();
                                                        viewManager.getStage().setScene(loginScene);
						}
					}
				} catch (Exception exception) {
					System.err.println("Register request failed.");
				}
			}
		});

		

		this.scene = new Scene(border, 400, 400);
		this.scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

	}

	public Scene getScene() {
		return this.scene;
	}

}
