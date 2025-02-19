import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Viewmachin extends Application {

    // จำนวนเครื่องซักผ้า
    private static final int NUM_MACHINES = 12;

    @Override
    public void start(Stage primaryStage) {

        // สร้าง GridPane สำหรับจัดเรียงปุ่ม
        GridPane grid = new GridPane();
        
        // สร้าง Array เพื่อเก็บสถานะเครื่องซักผ้า
        BooleanProperty[] machineStatus = new BooleanProperty[NUM_MACHINES];

        // สร้างเครื่องซักผ้า 12 เครื่อง
        for (int i = 0; i < NUM_MACHINES; i++) {
            final int machineIndex = i;

            // สถานะของเครื่องซักผ้า (True = กำลังทำงาน, False = ว่าง)
            machineStatus[i] = new SimpleBooleanProperty(false);

            // สร้างปุ่มสำหรับแต่ละเครื่อง
            Button machineButton = new Button("เครื่อง " + (i + 1));
            Text statusText = new Text();
            statusText.textProperty().bind(Bindings.format("สถานะ: %s", machineStatus[i]));

            
            // เมื่อกดปุ่ม จะสลับสถานะเครื่องซักผ้า
            machineButton.setOnAction(event -> {
                boolean currentStatus = machineStatus[machineIndex].get();
                machineStatus[machineIndex].set(!currentStatus);
            });

            // วางปุ่มใน GridPane
            grid.add(machineButton, i % 3, i / 3);
            grid.add(statusText, i % 3, (i / 3) + 1);
        }

        // สร้าง Scene และตั้งค่าให้กับ Stage
        Scene scene = new Scene(grid, 300, 300);
        primaryStage.setTitle("สถานะเครื่องซักผ้า");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
