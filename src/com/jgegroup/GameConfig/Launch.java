package com.jgegroup.GameConfig;

import com.google.gson.Gson;
import com.jgegroup.GameConfig.config.Config;
import com.jgegroup.GameConfig.config.ConfigBuilder;
import com.jgegroup.GameConfig.config.Settings;
import com.jgegroup.pacman.GameLaunch;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Slider;

import java.io.*;

public class Launch extends Application {

    public GameLaunch gameLaunch;
    public String newMapName; // For it to work with lambda ex
    public int horizontal_length; // For it to work with lambda ex
    public int vertical_length; // For it to work with lambda ex
    public static Map current_map;
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {
        // Creating tab pane, tabs, then adding tabs to tab pane
        TabPane tabPane = new TabPane();
        tabPane.setSide(Side.TOP);
        Tab tab1 = new Tab("Game Settings");
        Tab tab2 = new Tab("Map Writer");
        tab1.setClosable(false);
        tab2.setClosable(false);
        tabPane.getTabs().addAll(tab1, tab2);
        // CONTENT ADDED TO TABS HAVE TO BE NODES: SINGLE CONTROL, GROUP OF CONTROLS WRAPPED IN PANE OBJECT, ETC.
        tab1.setContent(GameSettingsContent());
        tab2.setContent(MapWriterContent());

        // Set up
        Group root = new Group(); // No longer used - Gabriel Sison
        Scene scene = new Scene(tabPane, 1000, 800);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setTitle("JGE Settings");
        stage.centerOnScreen();

        stage.show();
    }


    public Pane GameSettingsContent() {
        Pane root = new Pane();
        Label label = new Label ("Game Settings");
        label.relocate(150, 10);

        // Lives for PacMan
        Slider pacManLivesSlider = new Slider(1, 10, 3);
        pacManLivesSlider.setPrefWidth(250);
        pacManLivesSlider.relocate(125, 100);
        pacManLivesSlider.setBlockIncrement(1);
        pacManLivesSlider.setMajorTickUnit(1);
        pacManLivesSlider.setMinorTickCount(0);
        pacManLivesSlider.setShowTickMarks(true);
        pacManLivesSlider.setShowTickLabels(true);
        Label pacManLives = new Label ("Pacman Lives");
        pacManLives.relocate(25, 100);

        // Speed for Ghost
        Slider ghostLivesSlider = new Slider(1, 5, 3);
        ghostLivesSlider.setPrefWidth(250);
        ghostLivesSlider.relocate(125, 150);
        ghostLivesSlider.setBlockIncrement(1);
        ghostLivesSlider.setMajorTickUnit(1);
        ghostLivesSlider.setMinorTickCount(0);
        ghostLivesSlider.setShowTickMarks(true);
        ghostLivesSlider.setShowTickLabels(true);
        Label ghostSpeed = new Label ("Ghost Speed");
        ghostSpeed.relocate(25, 150);

        // PacMan Speed default
        Slider pacManSpeedSlider = new Slider(1, 5, 3);
        pacManSpeedSlider.setPrefWidth(250);
        pacManSpeedSlider.relocate(125, 200);
        pacManSpeedSlider.setBlockIncrement(1);
        pacManSpeedSlider.setMajorTickUnit(1);
        pacManSpeedSlider.setMinorTickCount(0);
        pacManSpeedSlider.setShowTickMarks(true);
        pacManSpeedSlider.setShowTickLabels(true);
        Label pacManSpeed = new Label ("Pacman Speed");
        pacManSpeed.relocate(25, 200);

        Button button = new Button ("Game Launch");
        button.relocate(300,300);
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                System.out.println("button pressed");
                int pacmanLives = (int) (pacManLivesSlider.getValue() + 0.5);
                int ghostSpeed = (int) (ghostLivesSlider.getValue() + 0.5) * 2;
                int pacmanSpeed = (int) (pacManSpeedSlider.getValue() + 0.5) * 2;
                System.out.println("Our lives for pacman is " + pacmanLives);
                System.out.println("Our speed for ghost  is " + ghostSpeed);
                System.out.println("Our speed for pacman is " + pacmanSpeed);
                // How do I launch the game from here?

                Settings settings = new Settings();
                settings.setPacmanLives(pacmanLives);
                settings.setGhostSpeed(ghostSpeed);
                settings.setPacmanSpeed(pacmanSpeed);

                Gson gson = new Gson();

                String settingsJson = gson.toJson(settings);
                String[] args = new String[] {settingsJson};
                GameLaunch.main(args);

                // Exports to settings.txt in setting directory
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter("res/settings/settings.txt"));
                    writer.write("pacman lives: " + pacmanLives);
                    writer.write("\nghost speed : " + ghostSpeed);
                    writer.write("\npacman speed: " + pacmanSpeed);
                    writer.close();
                } catch (IOException c) {
                    throw new RuntimeException(c);
                }


            }
        };

        // Create print stream


        // when button is pressed
        button.setOnAction(event);
        root.getChildren().addAll(label, button, pacManLivesSlider, ghostLivesSlider, pacManSpeedSlider,
                ghostSpeed, pacManSpeed, pacManLives);
        return root;
    }

    public Pane MapWriterContent() {
      Pane root = new Pane();
      Canvas mapWriterCanvas = new Canvas(400, 400);
      GraphicsContext mapPainter = mapWriterCanvas.getGraphicsContext2D();
      root.getChildren().add(mapWriterCanvas);

      // Save map button
      Button saveMapButton = new Button("Save Map");
      saveMapButton.relocate(840, 0);
      saveMapButton.setOnAction(event -> {
        try {
          current_map.saveMap();
        } catch (NullPointerException e) {
          Text errText = new Text();
          errText.setText("Not working with a map!");
          errText.relocate(920, 20);
          root.getChildren().add(errText);
        }
      });


      // Show Map button
      Button showMapButton = new Button("Show map");
      showMapButton.relocate(765, 0);
      Text map_text = new Text();
      showMapButton.setOnAction(event -> {
        try {
          String content = current_map.showMapContent();
          root.getChildren().remove(map_text);
          map_text.setText(content);
          map_text.relocate(5, 50);
          root.getChildren().add(map_text);
        } catch (NullPointerException e) {
          Text errText = new Text();
          errText.setText("Not working with a map!");
          errText.relocate(300, 30);
          root.getChildren().add(errText);
        }
      });

      // Create new map button
      Button createNewMapButton = new Button("Create New Map");
      createNewMapButton.relocate(0, 0);
      createNewMapButton.setOnAction(event -> {
        getMapInfo();
      });


      root.getChildren().addAll(saveMapButton, showMapButton, createNewMapButton);
      addMapDropBox(root);


      return root;
    }

  public void addMapDropBox(Pane root) {
    ComboBox<String> comboBox = new ComboBox<>();
    comboBox.relocate(910, 0);
    File directory = new File("res/maps");
    File[] maps = directory.listFiles();
    if (maps != null) {
      for (File map : maps) {
          comboBox.getItems().add(map.getName());
      }
    }
    comboBox.setOnAction(event -> {
      String selectedMap = comboBox.getValue();
      if (selectedMap != null) {
        System.out.println("selecting map: " + selectedMap);
        try {
          // Change the currently working map.
          current_map = new Map(selectedMap);

        } catch (FileNotFoundException e) {
          throw new RuntimeException(e);
        }
      }
    });
    root.getChildren().addAll(comboBox);
  }

    private Config initialize(Scene scene) {
        ConfigBuilder cb = new ConfigBuilder();
        return cb.build();
    }

    public void getMapInfo() {
      String map_name;
      TextInputDialog nameDialog = new TextInputDialog();
      nameDialog.setTitle("Create New Map");
      nameDialog.setHeaderText(null);
      nameDialog.setContentText("Enter Map Name:");

      nameDialog.showAndWait().ifPresent(name -> {

        this.newMapName = name;

        TextInputDialog horizontaLengthDialog = new TextInputDialog();
        horizontaLengthDialog.setTitle("Create New Map");
        horizontaLengthDialog.setHeaderText(null);
        horizontaLengthDialog.setContentText("Enter number of tile horizontal");
        horizontaLengthDialog.showAndWait().ifPresent(horizontal_length -> {
          try {

          this.horizontal_length = Integer.parseInt(horizontal_length);

          TextInputDialog verticalLengthDialog = new TextInputDialog();
          verticalLengthDialog.setTitle("Create New Map");
          verticalLengthDialog.setHeaderText(null);
          verticalLengthDialog.setContentText("Enter number of tile vertical");
          verticalLengthDialog.showAndWait().ifPresent(vertical_length -> {
            try {
              this.vertical_length = Integer.parseInt(vertical_length);

            } catch (NumberFormatException e) {
              System.out.println("Invalid number");
            }
          });
          } catch (NumberFormatException e) {
            System.out.println("Invalid number");
          }
        });
      });
      // change the currently working map.
      current_map = new Map(newMapName,vertical_length, horizontal_length);
    }
}
