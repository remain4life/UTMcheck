package utmcheck.view;

import utmcheck.Controller;
import utmcheck.enums.Region;
import utmcheck.enums.Status;
import utmcheck.model.ModelData;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Map;

public interface View {
    void refresh(Map.Entry<URL, Status> entry);
    void refreshAll(ModelData modelData);
    void setController(Controller controller);

    void loadModelData(String stringPath) throws IOException;
    void viewAllIP();
    void viewRegionIP(Region region);
    void workInterrupt();

    void doneMessage();
    void emptyMessage();
    void nothingToInterrupt();
    void interruptDone();
}
