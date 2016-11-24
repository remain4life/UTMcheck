package utmcheck;

import utmcheck.enums.Region;
import utmcheck.enums.Status;
import utmcheck.model.Model;
import utmcheck.model.ModelData;
import utmcheck.model.Shop;
import utmcheck.utils.SendEmailUtil;
import utmcheck.view.GuiView;
import utmcheck.view.View;

import javax.mail.MessagingException;
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

    public void onCachedShops() {
        if (region == ALL) {
            Map<Shop, Status> allRegionMap = model.getModelData().getResultMap();
            if (!allRegionMap.isEmpty())
                view.refreshAll(allRegionMap);
            else
                view.emptyRegionMessage();
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

    public Map<Shop,Status> getProblemShops() {
        //getting ALL problem shops
        Map<Shop,Status> allProblemShops =  model.getModelData().getNotConnectedShops();
        //needed REGION problem shops
        Map<Shop,Status> problemRegionShops = new TreeMap<>();

        if (region != ALL) {

            //getting ALL region shops
            List<Shop> allRegionShops = getNeededRegionShops(model.getModelData().getShopList());

            for (Map.Entry<Shop, Status> problemShopEntry : allProblemShops.entrySet()) {
                if (allRegionShops.contains(problemShopEntry.getKey()))
                    problemRegionShops.put(problemShopEntry.getKey(), problemShopEntry.getValue());

            }


        } else {
            return allProblemShops;
        }

        return problemRegionShops;
    }

    public boolean sendProblemShops() throws MessagingException, IOException, CloneNotSupportedException {
        //creating copy of problem shops map - we don't need to change regions in cached map
        Map<Shop, Status> mapToSend = cloneShopMap(getProblemShops());

        if (!mapToSend.isEmpty()) {
            //if user selected specific region
            if (region!=ALL) {
                String textToSend = SendEmailUtil.resultMapToText(mapToSend);
                SendEmailUtil.sendEmail(textToSend, region);
                return true;
            } else {
                //changing region to main
                changeRegionToMain(mapToSend);

                //set for checking - what regions we have in map
                Set<Region> regions = new HashSet<>();
                for (Map.Entry<Shop, Status> entry: mapToSend.entrySet()) {
                    regions.add(entry.getKey().getRegion());
                }


                for (Region r: regions) {
                    //creating new map for each region in set
                    Map<Shop, Status> tempMapToSend = new TreeMap<>();
                    for (Map.Entry<Shop, Status> entry: mapToSend.entrySet()) {
                        if (entry.getKey().getRegion() == r)
                            tempMapToSend.put(entry.getKey(), entry.getValue());
                    }
                    //sending map
                    String textToSend = SendEmailUtil.resultMapToText(tempMapToSend);
                    SendEmailUtil.sendEmail(textToSend, r);
                    //success message
                    view.regionProblemShopsMailSent(r);
                }
                return true;
            }

        }
        return false;
    }

    private Map<Shop,Status> cloneShopMap(Map<Shop, Status> mapToClone) throws CloneNotSupportedException {
        Map<Shop,Status> clonedMap = new TreeMap<>();
        for (Map.Entry<Shop, Status> entry : mapToClone.entrySet()) {
            //our Shop class overrides default Object method
            Shop clonedShop = (Shop)entry.getKey().clone();
            Status status = entry.getValue();

            clonedMap.put(clonedShop, status);
        }
        return clonedMap;
    }

    private void changeRegionToMain(Map<Shop, Status> mapToSend) {
        for (Map.Entry<Shop, Status> entry : mapToSend.entrySet()) {
            Shop shop = entry.getKey();
            Region shopRegion = shop.getRegion();
            switch (shopRegion) {
                case BELOGORSK:
                case BAKHCHISARAI:
                    shop.setRegion(SIMFEROPOL);
                    break;
                case SAKI:
                    shop.setRegion(YEVPATORIA);
                    break;
                case KRASNOGVARDEYSK:
                case NIZHNEGORSK:
                    shop.setRegion(JANKOI);
                    break;
                case YALTA:
                    shop.setRegion(SIMFEROPOL);
                    break;
                case ARMYANSK:
                    shop.setRegion(KRASNOPEREKOPSK);
                    break;
                case SUDAK:
                    shop.setRegion(SUDAK);
                    break;
            }
        }
    }

    public void onProblemShops() {
        Map<Shop, Status> neededProblemShops = getProblemShops();
        if (neededProblemShops!= null && !neededProblemShops.isEmpty()) {
            view.refreshAll(neededProblemShops);
        } else {
            view.emptyRegionMessage();
        }

    }

    public void onClearModelData() {
        model.clearModelData();
    }
}
