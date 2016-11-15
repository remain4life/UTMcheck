import java.net.URL;
import java.util.Map;

public interface View {
    void refresh(Map.Entry<URL, Status> entry);
    void refreshAll(ModelData modelData);
    void setController(Controller controller);

    void viewAllIP();
    void viewRegionIP(Region region);
    void workInterrupt();

    void doneMessage();
    void emptyMessage();
    void nothingToInterrupt();
    void interruptDone();
}
