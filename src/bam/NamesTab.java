
package bam;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 *
 * @author Kaustubh Bendre
 */
public class NamesTab {
     public static GridPane NamesTab () {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_LEFT);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        
        Text scenetitle = new Text("Team Names:");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);
        
        int i, j, k;
        String str1;
        
        k = BAM.tbls;
        j = (k%2 == 0) ? k/2 : k/2 + 1;
        TextField[] fields = new TextField[k+1];
        
        for (i=1; i <= j; i++){
            str1 = "Team " + i;
                        
            Label label = new Label(str1);
            grid.add(label, 0, i);

            TextField field = new TextField();
            grid.add(field, 1, i);
            fields[i] = field;
            field.setText(BAM.teamnames[i]);
        }
        for (i=j+1; i <= k; i++){
            str1 = "Team " + i;           
            
            Label label = new Label(str1);
            grid.add(label, 3, i-j);

            TextField field = new TextField();
            grid.add(field, 4, i-j);
            fields[i] = field;
            field.setText(BAM.teamnames[i]);

        }
        Button btn = new Button();
        btn.setText("OK");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 4, j+1);
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                for (int i = 1; i <= BAM.tbls; i++){
                    BAM.teamnames[i] = fields[i].getText();
                }
                int namesentered = 0;
                for(int i = 1; i <= BAM.tbls; i++) if ( BAM.teamnames[i] != null) namesentered++;
                ScoreTab.namesText.setText(namesentered + "/" + BAM.tbls + " names entered.");
            }
        });
        return grid;
}
        
     }
