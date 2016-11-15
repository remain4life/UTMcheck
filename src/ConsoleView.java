import java.io.IOException;
import java.net.URL;
import java.util.Map;

public class ConsoleView implements View{
    private Controller controller;

    @Override
    public void refreshAll(ModelData modelData) {
        for (Map.Entry<URL, Status> result: modelData.getResultMap().entrySet()) {
            System.out.println(result.getKey() + " - " + result.getValue());
        }
    }

    @Override
    public void refresh(Map.Entry<URL, Status> entry) {
        System.out.println(entry.getKey() + " - " + entry.getValue());
    }

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void viewAllIP() {
        try {
            controller.checkIP();
        } catch (IOException e) {
            System.out.println("Something wrong!");
        }
    }

    @Override
    public void viewRegionIP(Region region) {
        try {
            controller.checkRegionIP(region);
        } catch (IOException e) {
            System.out.println("Something wrong!");
        }
    }

    @Override
    public void doneMessage() {
        System.out.println("Все точки обработаны!\n");
    }

    @Override
    public void workInterrupt() {
        controller.workInterrupt();
    }

    @Override
    public void nothingToInterrupt() {
        System.out.println("Процесс не запущен, нечего прерывать.\n");
    }

    @Override
    public void interruptDone() {
        System.out.println("Обработка прервана, данные кэшированы.\n");
    }

    @Override
    public void emptyMessage() {
        System.out.println("База адресов пуста, загрузите данные! \n");
    }
}