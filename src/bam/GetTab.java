/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bam;

import java.io.File;
import java.time.LocalDate;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 *
 * @author lenovo
 */
public class GetTab {
    public static GridPane GetTab () {
    GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_LEFT);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        
        Text scenetitle = new Text("Set Parameters:");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label nameOfEventlabel = new Label("Name of Event:");
        grid.add(nameOfEventlabel, 0, 1);

        TextField nameOfEventTextField = new TextField("Deccan Club Hirabaug BAM");
        nameOfEventTextField.setPrefWidth(250);
        grid.add(nameOfEventTextField, 1, 1);
        
        Label datelabel = new Label("Date:");
        grid.add(datelabel, 0, 2);
        
        final DatePicker datePicker = new DatePicker(LocalDate.now());
        grid.add(datePicker, 1, 2);
        
        Label noOfTeamslabel = new Label("Number of Teams:");
        grid.add(noOfTeamslabel, 0, 3);

        Spinner<Integer> noofteamsSpinner = new Spinner(15,25,20);
        grid.add(noofteamsSpinner, 1, 3);
        

        Label pathOfdblabel = new Label("Path of BMPro.exe :");
        grid.add(pathOfdblabel, 0, 4);

        Text pathTextField = new Text("C:\\Program Files (x86)\\Bridgemate Pro");
        //pathTextField.setPrefWidth(300);
        grid.add(pathTextField, 1, 4);
        Button pathbtn = new Button();
        pathbtn.setText("Change");
        grid.add(pathbtn, 2, 4);
        pathbtn.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
              DirectoryChooser dir = new DirectoryChooser();  
              
              dir.setTitle("Choose path of BMPro.exe");
              File selectedDirectory = 
                        dir.showDialog(null);
                 
                if(selectedDirectory == null){
                    pathTextField.setText("No Directory selected");
                }
                else{
                    pathTextField.setText(selectedDirectory.getAbsolutePath());
                }
            }
        });
        //
        

        Button btn = new Button();
        btn.setText("Set Parameters");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 2, 7);
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                BAM.nameOfEvent = nameOfEventTextField.getText();
                BAM.date = datePicker.getValue();
                //tbls = Integer.parseInt(noofteamsTextField.getText());
                BAM.tbls = noofteamsSpinner.getValue();
                BAM.namestab.setContent(NamesTab.NamesTab());
                BAM.tabPane.getTabs().add(BAM.namestab);
                //System.out.println(BAM.tbls);
                BAM.settab.setContent(SetTab.SetTab());
                BAM.tabPane.getTabs().add(BAM.settab);
                
                BAM.scoretab.setContent(ScoreTab.ScoreTab());
                BAM.tabPane.getTabs().add(BAM.scoretab);
            }
        });
        return grid;
        
            //StackPane root = new StackPane();
            //root.getChildren().add(btn);

            //Scene scene = new Scene(root, 300, 250);
}
}
