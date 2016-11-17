package utmcheck.view;

import utmcheck.ColorPane;
import utmcheck.Controller;
import utmcheck.enums.Region;
import utmcheck.enums.Status;
import utmcheck.exceptions.NotCorrectFileException;
import utmcheck.model.ModelData;
import utmcheck.model.Shop;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class GuiView extends JFrame implements View {
    private Controller controller;
    //field for view processing
    //private volatile JTextArea logText = new JTextArea(); //old field with plain text
    private volatile ColorPane logText = new ColorPane();
    //field for path
    private volatile JTextField pathField = new JTextField("C:/shops/shopList2.txt", 30);

    public GuiView() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            initGui();
        } catch (ClassNotFoundException | InstantiationException |
                IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    private void initGui() {
        //configure our window
        setVisible(true);
        setMinimumSize(new Dimension(600,600));
        setTitle("Проверка доступа к УТМ на магазинах");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //creating parent panel
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        setContentPane(panel);

        //output your IP
        String IP;
        String host = "";
        try {
            IP = InetAddress.getLocalHost().getHostAddress();
            host = InetAddress.getLocalHost().getHostName();
        }
        catch (Exception e) {
            IP = "Не хватает получения IP-адреса";
        }
        JPanel IPPanel = new JPanel();
        IPPanel.setBorder(BorderFactory.createTitledBorder("Ваш IP-адрес и имя компьютера: "));
        IPPanel.add(new JLabel("<html><b>" + IP + ", </b>"));
        IPPanel.add(new JLabel("<html><b>" + host + "</b>"));
        panel.add(IPPanel, BorderLayout.NORTH);

        //creating common button panel
        JPanel allBtnPanel = new JPanel();
        allBtnPanel.setLayout(new BorderLayout());
        panel.add(allBtnPanel, BorderLayout.CENTER);

        //creating buttons panel-1
        JPanel btnPanel1 = new JPanel();
        //loading list button
        JButton button11 = new JButton("Загрузить список магазинов");
        button11.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //here we parsing file to list we'll work with
                try {
                    loadModelData(pathField.getText());
                    //DarkGreen
                    logText.append(new Color(0, 100, 0), "Данные загружены успешно, можно обрабатывать." + System.lineSeparator());
                } catch (NotCorrectFileException e1) {
                    logText.append(Color.RED, "Некорректный синтаксис в файле!" + System.lineSeparator());
                } catch (Exception e2) {
                    logText.append(Color.RED, "Ошибка при загрузке файла!" + System.lineSeparator());
                }
            }
        });
        //adding pathField near loading button
        btnPanel1.add(pathField);

        btnPanel1.add(button11);
        allBtnPanel.add(btnPanel1, BorderLayout.NORTH);

        //creating buttons panel-2 - with region list selection
        JPanel btnPanel2 = new JPanel();
        btnPanel2.add(new JLabel("<html><b>Выберите регион для обработки: </b>"));
        JComboBox<String> regionBox = new JComboBox<>(addRegionList());
        //adding listener for processing region xelection
        regionBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox comboBox = (JComboBox) e.getSource();
                String selectedItem = (String) (comboBox).getSelectedItem();
                controller.setRegion(getMainRegionsFromString(selectedItem));
            }
        });

        btnPanel2.add(regionBox);

        allBtnPanel.add(btnPanel2, BorderLayout.CENTER);



        //creating buttons panel-2
        JPanel btnPanel3 = new JPanel();
        //processing button
        JButton button31 = new JButton("Запустить проверку магазинов");
        button31.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //here controller starts new thread for real-time logging
                viewIPs();
            }
        });
        //interrupt button
        JButton button32 = new JButton("Прервать выполнение");
        button32.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                workInterrupt();
            }
        });
        //clear button
        JButton button33 = new JButton("Очистить вывод");
        button33.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearLogView();
            }
        });
        //exit button
        JButton button34 = new JButton("Выход");
        button34.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        btnPanel3.add(button31);
        btnPanel3.add(button32);
        btnPanel3.add(button33);
        btnPanel3.add(button34);

        allBtnPanel.add(btnPanel3, BorderLayout.SOUTH);

        //add processing org.view field
        JPanel textPanel = new JPanel();
        textPanel.setBorder(BorderFactory.createTitledBorder("Лог обработки: "));
        //--logText.setLineWrap(true);
        //--logText.setWrapStyleWord(true);
        JScrollPane logScrollPane = new JScrollPane(logText);
        logScrollPane.setPreferredSize(new Dimension(580, 400));
        textPanel.add(logScrollPane);
        panel.add(textPanel, BorderLayout.SOUTH);

        pack();
    }

    private String[] addRegionList() {
        return new String[]{" Все ",
                " Симферополь / Белогорск / Бахчисарай ",
                " Севастополь ",
                " Джанкой / Красногвардейск / Нижнегорск ",
                " Феодосия / Судак ",
                " Керчь ",
                " Евпатория / Саки ",
                " Ялта / Алушта ",
                " Армянск / Красноперекопск "};
    }

    private Region getMainRegionsFromString(String item) {
        Region region;
        switch (item) {
            case " Все ":
                region = Region.ALL;
                break;
            case " Симферополь / Белогорск / Бахчисарай ":
                region = Region.SIMFEROPOL;
                break;
            case " Севастополь ":
                region = Region.SEVASTOPOL;
                break;
            case " Джанкой / Красногвардейск / Нижнегорск ":
                region = Region.JANKOI;
                break;
            case " Феодосия / Судак ":
                region = Region.FEODOSIYA;
                break;
            case " Керчь ":
                region = Region.KERCH;
                break;
            case " Евпатория / Саки ":
                region = Region.YEVPATORIA;
                break;
            case " Ялта / Алушта ":
                region = Region.ALUSHTA;
                break;
            case " Армянск / Красноперекопск ":
                region = Region.KRASNOPEREKOPSK;
                break;
            default:
                region = Region.OTHER;
                break;
        }
        return region;
    }

    @Override
    public void refreshAll(ModelData modelData) {
        for (Map.Entry<Shop, Status> result: modelData.getResultMap().entrySet()) {
            refresh(result);
        }
    }

    @Override
    public void refresh(Map.Entry<Shop, Status> entry) {
        String regionOutput;
        String status;
        Color colorStatus;

        Shop shop = entry.getKey();
        switch (shop.getRegion()) {
            case SIMFEROPOL:
                regionOutput = ", Симферополь, ";
                break;
            case BAKHCHISARAI:
                regionOutput = ", Бахчисарай, ";
                break;
            case BELOGORSK:
                regionOutput = ", Белогорск, ";
                break;
            case SEVASTOPOL:
                regionOutput = ", Севастополь, ";
                break;
            case YALTA:
                regionOutput = ", Ялта, ";
                break;
            case ALUSHTA:
                regionOutput = ", Алушта, ";
                break;
            case ARMYANSK:
                regionOutput = ", Армянск, ";
                break;
            case KRASNOPEREKOPSK:
                regionOutput = ", Красноперекопск, ";
                break;
            case KRASNOGVARDEYSK:
                regionOutput = ", Красногвардейск, ";
                break;
            case NIZHNEGORSK:
                regionOutput = ", Нижнегорск, ";
                break;
            case KERCH:
                regionOutput = ", Керчь, ";
                break;
            case FEODOSIYA:
                regionOutput = ", Феодосия, ";
                break;
            case SUDAK:
                regionOutput = ", Судак, ";
                break;
            case YEVPATORIA:
                regionOutput = ", Евпатория, ";
                break;
            case SAKI:
                regionOutput = ", Саки, ";
                break;
            case JANKOI:
                regionOutput = ", Джанкой, ";
                break;
            default:
                regionOutput = ", Неизвестный регион, ";
                break;
        }


        switch (entry.getValue()) {
            case OK:
                status = "Всё ОК!";
                colorStatus = new Color(46, 139, 87);
                break;
            case NO_HOST_CONNECT:
                status = "Нет связи с компьютером!";
                colorStatus = Color.RED;
                break;
            case NO_UTM_CONNECT:
                status = "Компьютер доступен, нет связи с УТМ!";
                colorStatus = Color.ORANGE;
                break;
            case UTM_WRONG_STATUS:
                status = "Компьютер доступен, проблема со службами УТМ!";
                colorStatus = Color.ORANGE;
                break;
            default:
                status = "Не обработан!";
                colorStatus = Color.MAGENTA;
                break;
        }

        //logText.append(" "+ shopOutput + " ==> " + status + System.lineSeparator());
        logText.append(Color.BLACK, " " + shop.getName(), true);
        logText.append(regionOutput);
        logText.append(Color.BLUE, shop.getIP().toString());
        logText.append(" ==> ");
        logText.append(colorStatus, status + System.lineSeparator(), false);

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
            logText.append(Color.RED, "Ошибка обработки!");
        }
    }

    //loading file with urls
    @Override
    public void loadModelData(String stringPath) throws Exception {
        controller.loadModelData(Paths.get(stringPath));
    }

    //when all list processing done
    @Override
    public void doneMessage() {
        logText.append(new Color(0, 100, 0), "Все точки обработаны и кэшированы!" + System.lineSeparator());
    }

    @Override
    public void workInterrupt() {
        controller.workInterrupt();
    }

    public void clearLogView() {
        logText.setText("");
    }

    @Override
    public void nothingToInterrupt(){
        logText.append(Color.RED, "Процесс не запущен, нечего прерывать." + System.lineSeparator());
    }

    @Override
    public void interruptDone() {
        logText.append(Color.RED, "Обработка прервана, данные кэшированы." + System.lineSeparator());
    }

    @Override
    public void emptyBaseMessage() {
        logText.append(Color.RED, "База адресов пуста, загрузите данные!" + System.lineSeparator());
    }

    @Override
    public void emptyRegionMessage() {
        logText.append(Color.RED, "В базе нет магазинов по данному региону!" + System.lineSeparator());
    }
}
