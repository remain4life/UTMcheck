package utmcheck.view;

import utmcheck.Controller;
import utmcheck.enums.Region;
import utmcheck.enums.Status;
import utmcheck.model.Shop;

import java.io.File;
import java.util.Map;

public interface View {
    void refresh(Map.Entry<Shop, Status> entry);
    void refreshAll(Map<Shop, Status> resultMap);
    void setController(Controller controller);

    void loadModelData(String stringPath) throws Exception;
    void viewIPs();
    void workInterrupt();
    String getPathToLoad();
    void setPathToLoad(String s);

    void doneMessage();
    void emptyBaseMessage();
    void emptyRegionMessage();
    void nothingToInterrupt();
    void interruptDone();

    void cashedShopsShown();
    void regionProblemShopsMailSent(Region r);
    void showAbout();

    void saveViewLog(File currentFile);

    String getFolderToLoad();

    void saveAllCache(File currentFile);

    void regionCachedDataSaved(Region r);
}
