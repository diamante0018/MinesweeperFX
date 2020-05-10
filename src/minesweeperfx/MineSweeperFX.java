/**
 * Copyright (C) 2020 Edoardo Sanguineti
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package minesweeperfx;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import oldMinesweeper.*;

/**
 *
 * @author es597
 */
public class MineSweeperFX extends Application {
    private MineField mineField;
    private GridPane grid;
    private VBox root;
    private Button[][] buttons;
    private boolean[][] hasImg;
    Menu menuFlagCounter;
    private int counter;
    
    @Override
    public void start(Stage primaryStage) {
        MenuBar menuBar = new MenuBar();
        grid = new GridPane();
         
        //Standard game
        mineField = new MineField(10,10,10);
        mineField.populate();
        buttons = buttonsInit(10,10);
        hasImg = ImgInit(10,10);
        counter = 10;
        
        Menu menuSize = new Menu("Size & Difficulty");
		MenuItem easy = new MenuItem("10 * 10 Grid, 10 Mines");
		easy.setOnAction(e -> {
			mineField = new MineField(10,10,10);
                        counter = 10;
                        updateFlagCou();
                        mineField.populate();
                        root.getChildren().remove(grid);
                        grid = new GridPane();
                        buttons = buttonsInit(10,10);
                        hasImg = ImgInit(10,10);
                        root.getChildren().add(grid);
		});
		MenuItem medium = new MenuItem("18 * 18 Grid, 40 Mines");
		medium.setOnAction(e -> {
			mineField = new MineField(18,18,40);
                        counter = 40;
                        updateFlagCou();
                        mineField.populate();
                        root.getChildren().remove(grid);
                        grid = new GridPane();
                        buttons = buttonsInit(18,18);
                        hasImg = ImgInit(18,18);
                        root.getChildren().add(grid);
		});
                /**MenuItem hard = new MenuItem("18 * 18 Grid, 70 Mines");
		medium.setOnAction(e -> {
			mineField = new MineField(18,18,70);
                        mineField.populate();
                        root.getChildren().remove(grid);
                        grid = new GridPane();
                        buttons = buttonsInit(18,18);
                        hasImg = ImgInit(18,18);
                        counter = 70;
                        updateFlagCou();
                        root.getChildren().add(grid);
		});*/
                
	menuSize.getItems().addAll(easy, medium);
        
        menuFlagCounter = new Menu("Flag Counter : " + counter);
        
        menuBar.getMenus().addAll(menuSize, menuFlagCounter);
        root = new VBox(menuBar);
        root.getChildren().add(grid);
        
        Scene scene = new Scene(root);
        
        Image icon = new Image(getClass().getResourceAsStream("images/spike.jpg"));
        
        primaryStage.setTitle("Minesweeper");
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(icon);
        primaryStage.show();
    }
    
    private void refresh() {
        for(int i=0;i<buttons.length;i++) {
            for(int j=0;j<buttons[i].length;j++) {
              if(mineField.isRevealed(i, j)) {
                  buttons[i][j].setText("" + mineField.getMinedNeighbour(i,j));
              }  
            }
        }
        if(mineField.areAllMinesRevealed()) {
            Alert alert = new Alert(AlertType.CONFIRMATION, "Game Finished, Well Done!!!", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();  
            if (alert.getResult() == ButtonType.YES) 
                newGame();
            else 
                System.exit(0);
        }
    }
    
    private Button[][] buttonsInit(int x,int y) {
        Button[][] bt = new Button[x][y];      
        
        for(int i=0;i<x;i++) {
            for(int j=0;j<y;j++) {
                bt[i][j] = new Button();
                final int z=i, w=j;
                
                bt[i][j].setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                        if(!mineField.step(z, w)) {
                           Alert alert = new Alert(AlertType.CONFIRMATION, "Oh no, you stepped on a Mine! New Game ?", ButtonType.YES, ButtonType.NO);
                           alert.showAndWait();  
                           if (alert.getResult() == ButtonType.YES) {
                              newGame();
                            }  else 
                                 System.exit(0);
                        }
                        refresh();
                    }
                    if(mouseEvent.getButton().equals(MouseButton.SECONDARY)){
                        if(!mineField.isRevealed(z,w) && counter > 0 ) {
                            Image image = new Image(getClass().getResourceAsStream("images/flag.jpg"), bt[z][w].getWidth(),bt[z][w].getHeight(),false,false);
                            ImageView img = new  ImageView(image);
                            
                            if(!hasImg[z][w]) {
                               //grid.getChildren().remove(bt[z][w]);
                               bt[z][w].setGraphic(img);
                               counter--;
                               updateFlagCou();
                               //grid.add(img, z, w);
                               mineField.markTile(z, w);
                               bt[z][w].setGraphic(new  ImageView(image));
                               hasImg[z][w] = !hasImg[z][w];
                            }
                            else {
                               //grid.getChildren().remove(bt[z][w]); 
                               bt[z][w].setGraphic(null);
                               counter++; 
                               updateFlagCou();
                               hasImg[z][w] = !hasImg[z][w];
                               mineField.markTile(z, w);
                            }
                                
                        }
                    }
                }
                });
                /**bt[i][j].setOnAction(e -> {
                    if(!mineField.step(z, w)) {
                       Alert alert = new Alert(AlertType.CONFIRMATION, "Oh no, you stepped on a Mine! New Game ?", ButtonType.YES, ButtonType.NO);
                       alert.showAndWait();  
                       if (alert.getResult() == ButtonType.YES) {
                          newGame();
                       } else 
                           System.exit(0);
                    }
                    refresh();
                });*/
                grid.add(bt[i][j], i, j);
            }
        }
        
        return bt;
    }
    
    private void newGame() {
        mineField = new MineField(10,10,10);
        mineField.populate();
        root.getChildren().remove(grid);
        grid = new GridPane();
        buttons = buttonsInit(10,10);
        hasImg = ImgInit(10,10);
        counter = 10;
        updateFlagCou();
        root.getChildren().add(grid);
    }
    
    private boolean[][] ImgInit(int x,int y) {
        boolean a[][] = new boolean[x][y];
        
        for(int i=0;i<x;i++) 
            for(int j=0;j<y;j++) 
               a[i][j] = false;  
            
       return a; 
    }
    
    private void updateFlagCou() {
        menuFlagCounter.setText("Flag Counter : " + counter);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
