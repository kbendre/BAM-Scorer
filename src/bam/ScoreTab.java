/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bam;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
public class ScoreTab {
    //public static int travellers[][] = new int[BAM.tbls*2][7]; 
    public static int xmax = BAM.bds + 4;
    public static int ymax = BAM.tbls + 2;
    public static int recap[][] = new int[ymax][xmax];
    public static int id[]; 
    public static int level[];    
    public static int pairns[];    
    public static int pairew[];    
    public static String contract[];
    public static String strain[];
    public static String made[];
    public static String decl[];
    public static String dblstr[];
    public static int board[];
    public static int round[];
    public static int nsscore[];
    public static int nsmp[];
    public static float adjustedtotal[] = new float[BAM.tbls+2];
    //public static String compar[]; //for debugging
    
    //public static int noofScores = BAM.tbls * 28;
    public static Text namesText;    
     
    public static GridPane ScoreTab () {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_LEFT);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        
        Text scenetitle = new Text("Score and create reports:");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);      
        
        Button retbtn = new Button();
        retbtn.setText("Retrieve from BM");
        grid.add(retbtn, 0, 1);
        
        Text retText = new Text();
        retText.setText("0 scores retrieved.");
        grid.add(retText, 0, 2);
        
        namesText = new Text();
        int namesentered = 0;
        for(int i = 0; i < BAM.tbls; i++) if ( BAM.teamnames[i] != null) namesentered++;
        namesText.setText(namesentered + "/" + BAM.tbls + " names entered.");
        grid.add(namesText, 0, 3);
        
        Button scorebtn = new Button();
        scorebtn.setText("Score");
        scorebtn.setDisable(true);
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(scorebtn);
        grid.add(hbBtn, 0,5 );
        
        Text scoreText = new Text();
        scoreText.setText("Not scored.");
        grid.add(scoreText, 0, 6);
        
        retbtn.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                int ret = 0;
                try{
                ret = retrieveScores();
                ret = ret - removeDuplicates();                
                retText.setText(ret + " scores retrieved.");   
                scoreText.setText("Not scored.");
                if(ret > 0) scorebtn.setDisable(false);
                }
                catch (Exception e) {
                    e.printStackTrace();
                } 
            }
        });
          
        
        scorebtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try{                            
                    createRecap();           
                    createReports();        
                    scoreText.setText("Scoring Complete");
                    scorebtn.setDisable(true);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return grid;
        
        
    }    
    
    public static int retrieveScores() throws ClassNotFoundException, SQLException{
        
      //  File f1;//debug
      //  f1=new File("c:\\BAM\\BAM_debug_" + BAM.date + ".csv");//debug  
      //  String str1 = "";//debug
        
        int size = 0;
        try{
            Connection conn =DriverManager.getConnection("jdbc:ucanaccess://" + BAM.dbpath + ";memory=true");
            ResultSet rs;
            PreparedStatement ps = null;
            Statement st =conn.createStatement();
            String query;
            String str[];                       
            
            query = "Select count(*) from ReceivedData";
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while(rs.next()) {
            size = Integer.parseInt(rs.getString(1));
            }
            query = "Select ID, Board, PairNS, PairEW, Contract, [NS/EW], Result from ReceivedData";
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            
            id = new int[size];
            pairns = new int[size];
            pairew = new int[size];
            board = new int[size];
            level = new int[size];
            nsscore = new int[size];
            nsmp = new int[size];
            round = new int[size];
            contract = new String[size];
            decl = new String[size];
            dblstr = new String[size];
            made = new String[size];
            strain = new String[size];
                           
            
            int i = 0;
            while (rs.next()){
                id[i] = rs.getInt("ID");
                board[i] = rs.getInt("Board");
                pairns[i] = rs.getInt("PairNS");
                pairew[i] = rs.getInt("PairEW");
                contract[i] = rs.getString("Contract");
                decl[i] = rs.getString("NS/EW"); 
                made[i] = rs.getString("Result");                
                i++;
            }
            
            for(i = 0; i < size; i++){
                str = contract[i].split(" ");
                if (str.length == 1){
                    level[i] = 0;
                    strain[i] = "";
                    dblstr[i] = "";
                }
                else if (str.length == 2) {
                    level[i] = Integer.parseInt(str[0]);
                    strain[i] = str[1];
                    dblstr[i] = ""; 
                }
                else {
                    level[i] = Integer.parseInt(str[0]);
                    strain[i] = str[1];
                    dblstr[i] = str[2];
                }          
                
                if(level[i] == 0) nsscore[i] = 0; // all pass
                else nsscore[i] = calculateRawScore(level[i], strain[i], made[i], decl[i], dblstr[i], board[i]); 
            //    str1 += level[i] + " " + strain[i] + " " + made[i] + " " + nsscore[i];//debug
             //   str1 += "\n";//debug
            }           
            
          //  FileWriter fr1 = new FileWriter(f1);//debug
          //  fr1.write(str1);//debug
          //  fr1.close();//debug
            
            conn.close();            
        }
        catch (Exception e) {                        
			e.printStackTrace();
	}

        return size;
    }
    
    public static void createRecap(){
        int size = nsscore.length;
        int diff = 0;
        for(int i = 0; i < ymax; i++)  for(int j = 0; j< xmax; j++)  recap[i][j] = -1;
    
       // compar = new String[size];
        
        for(int i=0; i< size; i++){
            int j = i+1;
            
            if (nsmp[i] == 0 && pairns[i] > 0) {
                while(j < size && (pairns[i] != pairew[j] || board[i] != board[j] || pairns[j] < 0) ) j++;
                if (j < size){
                    diff = nsscore[i] - nsscore[j];
                  //  compar[i] =  id[i] + "~" + id[j] + "  " + diff; // debug
                  //  compar[j] =  id[j] + "~" + id[i] + "  " + diff*-1; // debug
                    if (diff < -190){
                        nsmp[i] = 0;
                        nsmp[j] = 6;
                    }
                    else if (diff < -50) {
                        nsmp[i] = 1;
                        nsmp[j] = 5;
                    }
                    else if (diff < -10) {
                        nsmp[i] = 2;
                        nsmp[j] = 4;
                    }
                    else if (diff < 20) {
                        nsmp[i] = 3;
                        nsmp[j] = 3;
                    }
                    else if (diff < 60) {
                        nsmp[i] = 4;
                        nsmp[j] = 2;
                    }
                    else if (diff < 200) {
                        nsmp[i] = 5;
                        nsmp[j] = 1;
                    }
                    else {
                        nsmp[i] = 6;
                        nsmp[j] = 0;
                    }
                    recap[pairns[i]][board[i]] = nsmp[i];
                    recap[pairns[j]][board[j]] = nsmp[j];
                }
            }
            //System.out.println(id[i]);
            
        }
        
        int total;
        int totaly = BAM.bds + 2;
        int rank;
        int ranky = totaly + 1;
        int playedboards = 0;
        //int namey = totaly - 1;
        for(int i = 1; i <= BAM.tbls ; i++){
            total = 0;
            for(int j = 1; j <= xmax - 4 ; j++){
                if(recap[i][j] != -1) {
                    total += recap[i][j];
                    playedboards += 1;
                }
            }
            recap[i][0] = i;            
            recap[i][totaly] = total;
            adjustedtotal[i] = (playedboards == 26)? (float)total * 28 / 26 : total;
            playedboards = 0;
            //recap[i][namey] = BAM.teamnames[i];            
        }
        for(int i = 1; i <= BAM.tbls; i++){
            rank = 1;
            for (int j = 1; j <= BAM.tbls; j++){
               // if(recap[i][totaly] < recap[j][totaly]) rank++ ;
                if(adjustedtotal[i] < adjustedtotal[j]) rank++ ;
            }
            recap[i][ranky] = rank;
        }      
    }
    
    public static void createReports() throws IOException {
        String td = "</td><td>";
        String row = "<tr><td>";
        String rowend = "</td></tr>";
        File f;
        f = new File("c:\\BAM\\BAM_results_" + BAM.date + ".htm");
        String str;
        File f1;
        f1 = new File("c:\\BAM\\BAM_results_" + BAM.date + ".csv");
        String str1;
        str = "<html>\n<style>\n table, th, td { \n"
                + "border-collapse: collapse;\n"
                + " border: 1px solid black; \n"
                + " padding: 8px;\n"
                + "font-family: arial, sans-serif;\n"
                + " font-size: 80%;\n"
                + " }  </style> ";

        str += "<h3>" + BAM.nameOfEvent + "</h3> \n\n";
        str += "<h4>" + BAM.date + "</h4>\n\n";

        if (BAM.tbls % 2 == 0) {
            str += "<table><tr><th>No.</th><th>Name</th><th>Adj Score</th><th>Rank</th><th>Raw Score</th>";
            str1 = "No.,Name,Adj Score,Rank, Raw Score";
        } else {
            str += "<table><tr><th>No.</th><th>Name</th><th>Score</th><th>Rank</th>";
            str1 = "No.,Name,Score,Rank";
        }
        
       
        for(int i = 1; i <= BAM.bds ; i++){
            str += "<th>" + i + "</th>";
            str1 += "," + i;
        }
        str += "</tr>";
        str1 += "\n";
        //int max_x = nsscore.length;
        int max_y = BAM.bds;
        int max_x = BAM.tbls;
        
        try {
            f.createNewFile();
            f1.createNewFile();
            boolean match = false;
            int counter = 1;
            int rank = 1;
            while (counter <= max_x) {//All these loops in order to sort ouput by rank
                match = false;
                for (int i = 1; i <= max_x; i++) {
                    if (recap[i][max_y + 3] == rank) {
                        if (BAM.tbls % 2 == 0) {
                            str += row + i + td + BAM.teamnames[i] + td + adjustedtotal[i] + td + recap[i][max_y + 3] + td + recap[i][max_y + 2];
                            str1 += i + "," + BAM.teamnames[i] + "," + adjustedtotal[i] + "," + recap[i][max_y + 3] + "," + recap[i][max_y + 2];
                        } else {
                            str += row + i + td + BAM.teamnames[i] + td + recap[i][max_y + 2] + td + recap[i][max_y + 3];
                            str1 += i + "," + BAM.teamnames[i] + "," + recap[i][max_y + 2] + "," + recap[i][max_y + 3];
                        }

                        for (int j = 1; j <= max_y; j++) {
                            if (recap[i][j] == -1) {
                                str += td + "";
                                str1 += ",";
                            } else {
                                str += td + recap[i][j];
                                str1 += "," + recap[i][j];
                            }
                        }
                        //str += row + id[i] + td + pairns[i] + td + pairew[i] + td + nsscore[i] + td + nsmp[i] + td + compar[i];
                        str += rowend;
                        str1 += "\n";
                        match = true;
                        recap[i][max_y + 3] = -1;
                        counter++;
                    }
                }
                if (match == false) {
                    rank++;
                }
            }
            
            
            str += "</table></html>";
            FileWriter fr = new FileWriter(f);
            fr.write(str);
            FileWriter fr1 = new FileWriter(f1);
            fr1.write(str1);

            fr.close();
            fr1.close();
        }
        
        catch (Exception e){
	  e.printStackTrace();
        }
        
        
    }    
    
    public static int removeDuplicates(){
        int dups = 0;
        int size = nsscore.length;
        
        for(int i = 0; i < size; i++){
            for(int j = i+1; j < size; j++){
                if(pairns[i] > 0 && pairns[i] == pairns[j] && pairew[i] == pairew[j] && board[i] == board[j]){
                    if(id[i] > id[j]){ // keep only the latest entry
                        pairns[j] = -1;
                        pairew[j] = -1; 
                        board[j] = -1;
                        dups += 1;
                    }
                    else{
                        pairns[i] = -1;
                        pairew[i] = -1;
                        board[i] = -1;
                        dups += 1;
                        j = size; //exit inside 'for' loop
                    }
                }
            }
        }
       
        return dups;
    }
    
    public static int calculateRawScore(int level, String strain, String made, String decl, String dblstr, int board){
        
        int[] vultable = {1,0,2,1,3,2,1,3,0,1,3,0,2,3,0,2,1};
        int denom;
        int dbl = 0;
        int vul;
        int NSScoreconverter;
        int ot;
        int score = 0;
        board = board%16;
        
       //////// dbl        
        if ("x".equals(dblstr)) dbl = 1;
        else if ("xx".equals(dblstr)) dbl = 2; 
        
        
      ///////// declarer, vul  
        if("N".equals(decl) || "S".equals(decl)){
            if (vultable[board] == 0 || vultable[board] == 1) vul = 0;
            else vul = 1;            
            NSScoreconverter = 1;
        }
        else{
            if (vultable[board] == 0 || vultable[board] == 2) vul = 0;
            else vul = 1;            
            NSScoreconverter = -1;
        }
        ////////// overtricks
       // if (made.length() == 0) return 0; // passed out
       //if("".equals(made)) return 0; // passed out 
       
       
        if (made.startsWith("-")) ot = Integer.parseInt(made.substring(1)) * -1;
        else if (made.startsWith("=")) ot = 0;
        else ot = Integer.parseInt(made.substring(1));
        
        /////////// contract going down
        if (ot < 0){            
            if (dbl == 0) return (vul == 0) ? ot * 50 * NSScoreconverter : ot * 100 * NSScoreconverter ;
            
            if (vul == 0) score = (ot < -3) ? ((ot + 3) * 300) - 500 : (ot * 200) + 100;
            else score = ot * 300 + 100;
            score *= NSScoreconverter;
            return (dbl == 1) ? score : score * 2;           
        }        
        
        /////////// contract made
        
        if ("C".equals(strain) || "D".equals(strain)) denom = 1;
        else if ("H".equals(strain) || "S".equals(strain)) denom = 3;
        else denom = 5;        
       
        int unit = 30; // majors + NT
        if (denom < 3) unit = 20; //minors

        score = unit * level; 
        if (denom == 5) score += 10; //NT

        if(dbl == 1) score = score * 2;
        else if(dbl == 2) score = score * 4;

        if (score < 100) score += 50; //partial
        else {
             if (vul==1) score += 500;
             else score += 300;

             if (level == 6){
                 if (vul==1)score += 750;
                 else score += 500;
             }
             if (level == 7){
                 if (vul==1)score += 1500;
                 else score += 1000;
             }
        }

        if(dbl==0)score += ot * unit;
        else if(dbl == 1){
                score += ot * 2 * (1+vul) * 50;
                score += 50;
        }
        else if(dbl == 2){
                score += ot * 4 * (1+vul) * 50;
                score += 100;	
        }

        return score*NSScoreconverter;
   }
}
