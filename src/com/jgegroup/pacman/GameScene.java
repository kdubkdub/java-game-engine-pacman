package com.jgegroup.pacman;

import com.jgegroup.pacman.objects.Map;

import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;


public class GameScene {
  public static final int TILE_SIZE = 32;
  public static final int NUMBER_OF_TILE_WIDTH = 20;
  public static final int NUMBER_OF_TILE_LENGTH = 28;

  public static final int RESOLUTION_HORIZONTAL = TILE_SIZE * NUMBER_OF_TILE_WIDTH; // 768
  public static final int RESOLUTION_VERTICAL = TILE_SIZE * NUMBER_OF_TILE_LENGTH; // 1024
  Scene gameScene;
  StackPane stackPane;

  GameScene() {
    stackPane = new StackPane();
    gameScene = new Scene(stackPane, RESOLUTION_HORIZONTAL, RESOLUTION_VERTICAL, Color.BLACK);
    Map map = Map.getMapInstance();
    stackPane.getChildren().add(map.getCanvas());



  }
 }
