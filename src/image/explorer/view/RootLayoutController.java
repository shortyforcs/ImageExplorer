package image.explorer.view;

import image.explorer.MainApp;
import image.explorer.importer.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.WindowEvent;

public class RootLayoutController {
    private MainApp mainApp;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    
    @FXML protected void menu_close(ActionEvent event) {
    	mainApp.getPrimaryStage().fireEvent(new WindowEvent(mainApp.getPrimaryStage(), WindowEvent.WINDOW_CLOSE_REQUEST));
    }
    
    @FXML protected void menu_import(ActionEvent event) {
    	imageImport(mainApp);
    }
    
    @FXML protected void menu_export(ActionEvent event) {
    	mainApp.getController().exportData();
    }
    
    public static void imageImport(MainApp mainApp) {
    	Importer importer = new Importer(mainApp.getPrimaryStage(), mainApp.getConnectionDriver());
    	importer.importImage();
    	mainApp.updateImageData(new String[]{"*"}, null, null, null);
    }
}
