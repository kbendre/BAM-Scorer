/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bam;

/**
 *
 * @author lenovo
 */
public class ScoreTab {
    public int travellers[][] = new int[BAM.tbls*2][7];
    public String recap[][] = new String[BAM.tbls + 2][BAM.tbls *2 + 4];
    public String raw[][] = new String[BAM.tbls * 14 * 2][5]; //NS, EW, Contract, By, Tricks
    
    
    public static void Score() {
        
        
    }    
    
    public static void createReports() {
        String td = "</td><td>";
        String trtd = "<tr><td>";
        String tdtr = "</td></tr>";
        
        
        
    }
    
    
    public static void removeDuplicates(){
        
    }
    public static int calculateRawScore(int level, String strain, String made, String decl, String dblstr, int board){
        int[] vultable = {1,0,2,1,3,2,1,3,0,1,3,0,2,3,0,2,1};
        int denom;
        int dbl = 0;
        int vul;
        int NSScoreconverter;
        int ot;
        int score = 0;
        
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
        if (made.length() == 0) return 0; // passed out
        
        if (made.startsWith("-")) ot = Integer.parseInt(made.substring(1)) * -1;
        else if (made.startsWith("=")) ot = 0;
        else ot = Integer.parseInt(made.substring(1));
        
        /////////// contract going down
        if (ot < 0){
            
            if (dbl == 0) return (vul == 0) ? ot * 50 * NSScoreconverter : ot * 100 * NSScoreconverter ;
            
            if (vul == 0) score = (ot < 3) ? ((ot + 3) * 300) - 500 : (ot * 200) + 100;
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

       if(dbl == 1){
               score = score * 2;
       }
       else if(dbl == 2){
               score = score * 4;
       }




       if (score < 100){
               score += 50; //partial

       }
       else {
               if (vul==1){
                       score += 500;
               }
               else {
                       score += 300;
               }
           if (level == 6){
                   if (vul==1){
                   score += 750;
                }
                        else {
                   score += 500;
                }
           }
            if (level == 7){
               if (vul==1){
               score += 1500;
               }
                    else {
               score += 1000;
               }
            }
       }




       if(dbl==0){
               score += ot * unit;
       }
       else if(dbl == 1){
               score += ot * 2 * (1+vul) * 50;
               score += 50;
       }
       else if(dbl == 2){
               score += ot * 4 * (1+vul) * 50;
               score += 100;	
       }


       return score;
   }
}
