/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bam;

//import java.io.File;
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
//import javafx.stage.DirectoryChooser;

/**
 *
 * @author Kaustubh Bendre
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

        TextField nameOfEventTextField = new TextField("Deccan Club BAM");
        nameOfEventTextField.setPrefWidth(250);
        grid.add(nameOfEventTextField, 1, 1);
        
        Label datelabel = new Label("Date:");
        grid.add(datelabel, 0, 2);
        
        final DatePicker datePicker = new DatePicker(LocalDate.now());
        grid.add(datePicker, 1, 2);
        
        Label noOfTeamslabel = new Label("Number of Teams:");
        grid.add(noOfTeamslabel, 0, 3);

        Spinner<Integer> noofteamsSpinner = new Spinner(14,31,15);
        grid.add(noofteamsSpinner, 1, 3);
        
        Button btn = new Button();
        btn.setText("Set Parameters");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 2, 3);
        
        Text scenetitle2 = new Text("Initialise Bridgemate Control: ");
        scenetitle2.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle2, 0, 5);
                             
         Button btn2 = new Button();
        btn2.setText("Start BCS");
        HBox hbBtn2 = new HBox(10);
        hbBtn2.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn2.getChildren().add(btn2);
        grid.add(hbBtn2, 0, 6 );
        btn2.setDisable(true);
        btn2.setOnAction(new EventHandler<ActionEvent>() {

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
        
        
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                BAM.nameOfEvent = nameOfEventTextField.getText();
                BAM.date = datePicker.getValue();
                BAM.tbls = noofteamsSpinner.getValue();
                BAM.bds = (BAM.tbls % 2 == 0) ? BAM.tbls * 2 + 2 : BAM.tbls * 2;
                //BAM.bmpropath = pathTextField.getText();
                BAM.teamnames = new String[BAM.tbls+1];
                
                
               
                btn2.setDisable(false);
                BAM.namestab.setContent(NamesTab.NamesTab());
                BAM.namestab.setDisable(false);
               // BAM.settab.setContent(SetTab.SetTab());
              //  BAM.settab.setDisable(false);                
                BAM.scoretab.setContent(ScoreTab.ScoreTab());
                BAM.scoretab.setDisable(false);        
            }
        });
        return grid;
        
            //StackPane root = new StackPane();
            //root.getChildren().add(btn);

            //Scene scene = new Scene(root, 300, 250);
}
}



           //   Label noOfRoundslabel = new Label("Rounds Played:");
     //   grid.add(noOfRoundslabel, 0, 4);
        
     //   Spinner<Integer> noofroundsSpinner = new Spinner(11,14,11);
     //   noofroundsSpinner.setDisable(true);
     //   grid.add(noofroundsSpinner, 1, 4);
        
      /*  noofteamsSpinner.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                    int tbls = newValue.intValue();
                    SpinnerValueFactory.IntegerSpinnerValueFactory intFactory =
        (SpinnerValueFactory.IntegerSpinnerValueFactory)  noofroundsSpinner.getValueFactory();
                    switch(tbls){
                        case 11:
                            intFactory.setMax(10);
                            intFactory.setMin(10);
                            noofroundsSpinner.setDisable(true);
                            break;
                        case 12:
                            intFactory.setMax(11);
                            intFactory.setMin(11);
                            noofroundsSpinner.setDisable(true);
                            break;
                        case 13:
                            intFactory.setMax(12);
                            intFactory.setMin(12);
                            noofroundsSpinner.setDisable(true);
                            break;
                        case 14:
                            intFactory.setMax(12);
                            intFactory.setMin(12);
                            noofroundsSpinner.setDisable(true);
                            break;
                        case 15:
                            intFactory.setMax(14);
                            intFactory.setMin(12);
                            intFactory.setAmountToStepBy(2);
                            noofroundsSpinner.setDisable(false);
                            break;
                        default:
                            intFactory.setMax(14);
                            intFactory.setMin(12);
                            intFactory.setAmountToStepBy(2);
                            noofroundsSpinner.setDisable(false);
                            break;
                    }
                
            }
        });*/
       // noofteamsSpinner.valueProperty().addListener(obs, 
        
       /* Label pathOfdblabel = new Label("Path of BMPro.exe :");
        grid.add(pathOfdblabel, 0, 5);

        Text pathTextField = new Text("C:\\Program Files (x86)\\Bridgemate Pro");
        //pathTextField.setPrefWidth(300);
        grid.add(pathTextField, 1, 5);
        Button pathbtn = new Button();
        pathbtn.setText("Change");
        grid.add(pathbtn, 2, 5);
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
        //*/
        