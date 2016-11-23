package utmcheck.view;

import utmcheck.Controller;
import utmcheck.enums.Region;
import utmcheck.enums.Status;
import utmcheck.model.ModelData;
import utmcheck.model.Shop;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Map;

public class ConsoleView implements View{
    private Controller controller;

    @Override
    public void refreshAll(Map<Shop, Status> resultMap) {
        for (Map.Entry<Shop, Status> entry: resultMap.entrySet()) {
            refresh(entry);
        }
    }

    @Override
    public void refresh(Map.Entry<Shop, Status> entry) {
        System.out.println(entry.getKey() + " - " + entry.getValue());
    }

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void viewIPs() {
        try {
            controller.checkRegionIP();
        } catch (IOException e) {
            System.out.println("Something wrong!");
        }
    }


    @Override
    public void loadModelData(String stringPath) throws Exception {
        controller.loadModelData(Paths.get(stringPath));
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
    public void emptyBaseMessage() {
        System.out.println("База адресов пуста, загрузите данные!\n");
    }

    @Override
    public void emptyRegionMessage() {
        System.out.println("В базе нет магазинов по данному региону!\n");
    }

    @Override
    public void cashedShopsShown() {
        System.out.println("Обработанные магазины по данному региону выведены!\n");
    }

    @Override
    public void regionProblemShopsMailSent(Region r) {
        System.out.println("Письмо для региона "+r+" отправлено!\n");
    }
}
