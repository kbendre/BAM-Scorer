/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bam;

import java.time.LocalDate;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import bam.GetTab;
import javafx.scene.layout.GridPane;
/**
 *
 * @author lenovo
 */
public class BAM extends Application {
    public static String nameOfEvent;
    public static int tbls;
    public boolean byetable = false;
    public static LocalDate date;
    public int rnds = 14; 
    public static String[] teamnames = new String[30];
    public static TabPane tabPane = new TabPane();
    public static Tab namestab = new Tab();
    public static Tab settab = new Tab();
    public static Tab scoretab = new Tab();
    
    
    @Override
    public void start(Stage primaryStage) {
 //       TabPane tabPane = new TabPane();
        
        Tab gettab = new Tab();
        gettab.setText("Get");
        gettab.setClosable(false);
        gettab.setContent(GetTab.GetTab());
        tabPane.getTabs().add(gettab);  
       
        //Tab namestab = new Tab();
        namestab.setText("Names");
        namestab.setClosable(false);
       // namestab.setContent(NamesTab.NamesTab());
       // tabPane.getTabs().add(namestab);
        settab.setText("Start");
        settab.setClosable(false);
        
        scoretab.setText("Score");
        scoretab.setClosable(false);
        
        
        
        Scene scene = new Scene(tabPane, 800, 600);
        primaryStage.setScene(scene);
            primaryStage.setTitle("BaM Scorer");
            //primaryStage.setScene(scene);
            primaryStage.show();
        
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
