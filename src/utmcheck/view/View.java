package utmcheck.view;

import utmcheck.Controller;
import utmcheck.enums.Region;
import utmcheck.enums.Status;
import utmcheck.model.ModelData;
import utmcheck.model.Shop;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Map;

public interface View {
    void refresh(Map.Entry<Shop, Status> entry);
    void refreshAll(Map<Shop, Status> resultMap);
    void setController(Controller controller);

    void loadModelData(String stringPath) throws Exception;
    void viewIPs();
    void workInterrupt();

    void doneMessage();
    void emptyBaseMessage();
    void emptyRegionMessage();
    void nothingToInterrupt();
    void interruptDone();

    void cashedShopsShown();
}
