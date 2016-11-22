package utmcheck;

import utmcheck.enums.Region;
import utmcheck.enums.Status;
import utmcheck.model.Model;
import utmcheck.model.Shop;
import utmcheck.view.GuiView;
import utmcheck.view.View;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.*;

import static utmcheck.enums.Region.*;

public class Controller {
    private View view;
    private Model model;

    //variable for region processing
    private volatile Region region = ALL; //default value

    //variable for one thread control
    private volatile boolean isThreadRunning = false;
    //flag for work processing control
    private volatile boolean workDone = false;
    //our thread for checking shops
    private volatile Thread workThread = null;

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public void setView(View view) {
        this.view = view;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public void init() {
        //create new model and org.view
        Model model = new Model();
        //View view = new ConsoleView();
        View view = new GuiView();


        //bind together
        view.setController(this);
        this.setModel(model);
        this.setView(view);
    }

    public static void main(String[] args) {
        Controller controller = new Controller();
        controller.init();
        //controller.getView().viewAllIP();
        //controller.getView().viewRegionIP(Region.SIMFEROPOL);
    }

    //parsing file to address list
    public void loadModelData(Path path) throws Exception {
        model.loadData(path);
    }

    public void checkRegionIP() throws IOException {
        if (model.getModelData().isEmpty()) {
            view.emptyBaseMessage();
            return;
        }

        List<Shop> selectedShops = getNeededRegionShops(model.getModelData().getShopList());

        if (selectedShops.isEmpty()) {
            view.emptyRegionMessage();
            return;
        }
        startWorkThread(selectedShops);
        System.gc();
    }

    //getting shop list based on main region town
    private List<Shop> getNeededRegionShops(List<Shop> allShops) {
        List<Shop> shops = new ArrayList<>();
        switch (region) {
            case ALL:
                return allShops;
            default:
                for (Shop shop: allShops) {
                    switch (region) {
                        case SIMFEROPOL:
                            if (shop.getRegion() == SIMFEROPOL ||
                                    shop.getRegion() == BELOGORSK ||
                                    shop.getRegion() == BAKHCHISARAI) {
                                shops.add(shop);
                            }
                            break;
                        case SEVASTOPOL:
                            if (shop.getRegion() == region) {
                                shops.add(shop);
                            }
                            break;
                        case YEVPATORIA:
                            if (shop.getRegion() == region ||
                                    shop.getRegion() == SAKI) {
                                shops.add(shop);
                            }
                            break;
                        case JANKOI:
                            if (shop.getRegion() == JANKOI ||
                                    shop.getRegion() == KRASNOGVARDEYSK ||
                                    shop.getRegion() == NIZHNEGORSK) {
                                shops.add(shop);
                            }
                            break;
                        case ALUSHTA:
                            if (shop.getRegion() == ALUSHTA ||
                                    shop.getRegion() == YALTA) {
                                shops.add(shop);
                            }
                            break;
                        case KRASNOPEREKOPSK:
                            if (shop.getRegion() == ARMYANSK ||
                                    shop.getRegion() == KRASNOPEREKOPSK) {
                                shops.add(shop);
                            }
                            break;
                        case FEODOSIYA:
                            if (shop.getRegion() == SUDAK ||
                                    shop.getRegion() == FEODOSIYA) {
                                shops.add(shop);
                            }
                            break;
                        case KERCH:
                            if (shop.getRegion() == region) {
                                shops.add(shop);
                            }
                            break;
                    }
                }
                return shops;
        }
    }

    //list processing start
    public void startWorkThread(final List<Shop> list) {
        if (!isThreadRunning){
            workThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    workDone = false;
                    isThreadRunning = true;
                    int i = 0;
                    //using while loop for quick safe interrupting
                    while(workThread!= null && !workThread.isInterrupted() && i < list.size()) {
                        try {
                            view.refresh(model.ping(list.get(i)));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        i++;
                        //work is done when all list elements processed
                        if (i == list.size()) {
                            workDone = true;
                        }

                    }
                    isThreadRunning = false;

                    //if thread was interrupted
                    if (workThread == null || workThread.isInterrupted()){
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                view.interruptDone();
                            }
                        });
                    }

                    //final message when work done
                    if (workDone) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                view.doneMessage();
                            }
                        });
                    }
                }
            });
            workThread.start();
        }
    }

    public void workInterrupt() {
        if (workThread!=null && workThread.isAlive()) {
            while (workThread!=null && !workThread.isInterrupted()) {
                workThread.interrupt();
                workThread = null;
                //garbage collector for waste thread
                System.gc();
            }
        } else {
            view.nothingToInterrupt();
        }
    }

    public void getShops() {
        if (region == ALL) {
            view.refreshAll(model.getModelData().getResultMap());
            return;
        }
        //getting shops according region
        List<Shop> neededShops = getNeededRegionShops(model.getModelData().getShopList());
        //new map for output
        Map<Shop, Status> regionMap = new TreeMap<>();
        for (Map.Entry<Shop, Status> entry: model.getModelData().getResultMap().entrySet()) {
            if (neededShops.contains(entry.getKey())) {
                regionMap.put(entry.getKey(), entry.getValue());
            }
        }
        if (regionMap.isEmpty()) {
            view.emptyRegionMessage();
            return;
        }
        view.refreshAll(regionMap);
    }

    public void getProblemShops() {
        //getting ALL problem shops
        Map<Shop,Status> allProblemShops =  model.getModelData().getNotConnectedShops();

        if (region != ALL) {

            //getting ALL region shops
            List<Shop> allRegionShops = getNeededRegionShops(model.getModelData().getShopList());

            //needed REGION problem shops
            Map<Shop,Status> problemRegionShops = new TreeMap<>();

            for (Map.Entry<Shop, Status> problemShopEntry : allProblemShops.entrySet()) {
                if (allRegionShops.contains(problemShopEntry.getKey()))
                    problemRegionShops.put(problemShopEntry.getKey(), problemShopEntry.getValue());

            }

            if (problemRegionShops.isEmpty()) {
                view.emptyRegionMessage();
                return;
            }

            view.refreshAll(problemRegionShops);

        } else {
            if (allProblemShops.isEmpty()) {
                view.emptyRegionMessage();
                return;
            }
            //getting ALL
            view.refreshAll(allProblemShops);
        }


    }
}
