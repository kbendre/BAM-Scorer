/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bam;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 *
 * @author Kaustubh Bendre
 */
public class SetTab {
    public static GridPane SetTab () {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_LEFT);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        
        Text scenetitle = new Text("Initialise Bridgemate Control");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);
        
        String str;
        
        str = BAM.nameOfEvent + "\n\n" +
                BAM.date + "\n\n" +
                "No. of teams: " + BAM.tbls;
               
        
        final Text settabText = new Text (25, 25, str);
        
        grid.add(settabText, 0, 1);
                
         Button btn = new Button();
        btn.setText("Start BCS");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 0, 2 );
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event)  {
                try{
                DB.initiateDB();                   
                Process process = new ProcessBuilder(BAM.bmpropath + "\\BMPro.exe","/f:[" + BAM.dbpath + "]").start();             
                }
                catch (Exception e) {
			e.printStackTrace();
                }
                
            }
        
    });
                return grid;
                }
}
