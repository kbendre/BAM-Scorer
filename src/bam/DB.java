
package bam;

import java.net.InetAddress;
import java.sql.*;

/**
 *
 * @author lenovo
 */
public class DB {
    
    //I've decided to not create a db after all. Too boring.
    //We'll take an existing db, empty the tables and reuse it.
    
    //String dbPath = "C:/Users/Public/newDb.bws";
    
    public static void initiateDB() throws ClassNotFoundException, SQLException{
        
        try{
            Connection conn =DriverManager.getConnection("jdbc:ucanaccess://C:\\Users\\lenovo\\Documents\\dchbam.bws;memory=true");           
            PreparedStatement ps = null;
            String query;
            
            ClearDB(conn);
            
            String ComputerName = InetAddress.getLocalHost().getHostName();
            query = "Insert into Clients (Computer) Values ('" + ComputerName + "')";
            ps = conn.prepareStatement(query);
            ps.execute();
            
            query = "Insert into Session (ID, Name) Values ('1','" + BAM.nameOfEvent + "')";
            ps = conn.prepareStatement(query);
            ps.execute();
            
            query = "Insert into Section (ID, Letter, Tables, EWMoveBeforePlay) "
                    + "Values ('1','A','" + BAM.tbls + "','2')";
            ps = conn.prepareStatement(query);
            ps.execute();
            
            query = "Insert into Tables (Section, Table) Values ";
            for (int i = 1; i < BAM.tbls; i++) query += "('1','" + i + "'),";               
            query += "('1','" + BAM.tbls + "')";      
            ps = conn.prepareStatement(query);
            ps.execute();  
            
            uploadRoundData(conn);      
            
        }
        catch (Exception e) {
			e.printStackTrace();
	}

    }
    
    
    public static void ClearDB(Connection conn) throws SQLException {
        ResultSet rs;
        PreparedStatement ps = null;
        String query;
        
        try{          
        
        query = "delete from Clients";
        ps = conn.prepareStatement(query);
        ps.execute();
        
         query = "delete from Session";
        ps = conn.prepareStatement(query);
        ps.execute();
        
         query = "delete from Section";
        ps = conn.prepareStatement(query);
        ps.execute();
        
         query = "delete from Tables";
        ps = conn.prepareStatement(query);
        ps.execute();
        
         query = "delete from RoundData";
        ps = conn.prepareStatement(query);
        ps.execute();
        
         query = "delete from ReceivedData";
        ps = conn.prepareStatement(query);
        ps.execute();
        
         query = "delete from IntermediateData";
        ps = conn.prepareStatement(query);
        ps.execute();
        
         query = "delete from PlayerNumbers";
        ps = conn.prepareStatement(query);
        ps.execute();
        
         query = "delete from PlayerNames";
        ps = conn.prepareStatement(query);
        ps.execute();
        
         query = "delete from BiddingData";
        ps = conn.prepareStatement(query);
        ps.execute();
        
         query = "delete from PlayData";
        ps = conn.prepareStatement(query);
        ps.execute();
        
         query = "delete from HandRecord";
        ps = conn.prepareStatement(query);
        ps.execute();
        
         query = "delete from HandEvaluation";
        ps = conn.prepareStatement(query);
        ps.execute();
        
         query = "delete from Settings";
        ps = conn.prepareStatement(query);
        ps.execute();
        
        
        }
        catch (Exception e) {
			e.printStackTrace();
	}
        
    }
    
    public static void uploadRoundData(Connection conn) throws SQLException{
        int rnds = 14;
        int tbls = BAM.tbls;
        int bds;
        int[][] roundtable;
        int rndno,tblno,ns,ew,lo,hi,trndno;
        int row;
        String query;
        PreparedStatement ps;
        bds = tbls * 2;
    roundtable = new int[tbls*rnds+1][6];// rows start from 1, cols start from 0    
    
    if (tbls%2 == 1){
        row = 1;
        for(rndno=1; rndno < 15; rndno++){
            for(tblno=1; tblno <= tbls; tblno++){
                    // "rndno" is the actual Round number in the tournament
                    // "trndno" is Theoretical round number ( used after Round 7 - The Big Move)
                    // "Theoretical round number" is what the round number would be if there were no skip after round 7.
                    if(rndno < 8) trndno = rndno;
                    else trndno = rndno + (tbls - 15);
                    //row = (rndno-1)*14 + tblno;

                    roundtable[row][0] = rndno;
                    roundtable[row][1] = tblno;

                    //NS number (same as table number)
                    roundtable[row][2] = tblno;

                    //EW number
                    ew = tblno - (2*trndno);
                    while (ew < 1) ew = ew + tbls;

                    roundtable[row][3] = ew;

                    //Low Board, High Board
                    hi = (tblno - trndno) * 2;
                    while (hi <= 0) hi = hi + (tbls * 2);
                    while (hi > tbls*2) hi = hi - (tbls * 2);

                    roundtable[row][4] = hi - 1;
                    roundtable[row][5] = hi;
                    
                    row = row + 1;
            }
        }
    }
    else { 
        /*  for 16, skip at 4th
            for 18, skip at 5
            for 20, skip at 5
            for 22, skip at 6
            for 24, skip at 6
        */
        
        row = 1;
        int max = rnds * tbls + 1;
        int skipround = 4;
        if (tbls == 18 || tbls == 20 ) skipround = 5;
        else if (tbls == 22 || tbls == 24) skipround = 6;
        
        for(rndno=1; rndno < 8; rndno++){
            for(tblno=1; tblno <= tbls; tblno++){
                
                roundtable[row][0] = rndno;
                roundtable[max - row][0] = 15 - rndno;
                roundtable[row][1] = tblno;
              
                //NS number (same as table number)
                roundtable[row][2] = tblno;

                //EW number
                if(rndno < skipround){
                    ew = tblno - (2*rndno);
                    while (ew < 1) ew = ew + tbls;
                }
                else {
                    ew = tblno - (2*rndno) - 1;
                    while (ew < 1) ew = ew + tbls;
                }
                
                roundtable[row][3] = ew;
                
                roundtable[max-row][3] = tblno;
                roundtable[max-row][2] = ew;
                roundtable[max-row][1] = ew;

                //Low Board, High Board
                hi = (tblno - rndno) * 2;
                while (hi <= 0) hi = hi + (tbls * 2);
                while (hi > tbls*2) hi = hi - (tbls * 2);    
                    
                roundtable[row][4] = hi - 1;
                roundtable[row][5] = hi;     
                
                roundtable[max-row][4] = hi-1;
                roundtable[max-row][5] = hi;
                
                row = row + 1;
            }
         }           
    }
    try{
        query = "Insert into RoundData (Section, Table, Round, NSPair, EWPair, LowBoard, HighBoard) Values ";
        for(int i=1; i<tbls*rnds; i++){
            query += "('1','" 
                    + roundtable[i][1] + "','" + roundtable[i][0] + "','" + roundtable[i][2] + "','" 
                    + roundtable[i][3] + "','" + roundtable[i][4] + "','" + roundtable[i][5] + "'), "; //note the comma at the end
        }  
        int i = tbls*rnds; //last iteration done outside loop to remove the comma at the end.
        query += "('1','" 
                    + roundtable[i][1] + "','" + roundtable[i][0] + "','" + roundtable[i][2] + "','" 
                    + roundtable[i][3] + "','" + roundtable[i][4] + "','" + roundtable[i][5] + "')"; //no comma at the end
        
            ps = conn.prepareStatement(query);
            ps.execute();
                   
    }
    catch (Exception e) {
			e.printStackTrace();
	}
    }
    
    public static void uploadSettings(Connection conn) throws SQLException {
        
        
        try{
            
        }
        catch (Exception e) {
			e.printStackTrace();
	}
        
    }
}
    

