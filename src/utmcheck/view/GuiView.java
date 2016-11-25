package utmcheck.view;

import utmcheck.Controller;
import utmcheck.enums.Region;
import utmcheck.enums.Status;
import utmcheck.exceptions.NotCorrectFileException;
import utmcheck.model.Shop;

import javax.mail.MessagingException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Paths;
import java.util.*;

public class GuiView extends JFrame implements View {
    private Controller controller;
    //field for view processing
    private volatile ColorPane logText = new ColorPane();
    //field for path
    private volatile JTextField pathField = new JTextField("C:/shops/shopList.txt", 30);

    public void setPathToLoad(String s) {
        pathField.setText(s);
    }

    @Override
    public String getPathToLoad() {
        return pathField.getText();
    }

    @Override
    public String getFolderToLoad() {
        File file = new File(pathField.getText());
        return file.getParent();
    }

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
        initWindow();

        //creating parent panel
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        setContentPane(panel);

        //creating common button panel
        JPanel allBtnPanel = new JPanel();
        allBtnPanel.setLayout(new BorderLayout());
        //adding button panel-1
        allBtnPanel.add(createBtnPanel1(), BorderLayout.NORTH);
        //adding button panel-2
        allBtnPanel.add(createBtnPanel2(), BorderLayout.CENTER);
        //adding button panel-3
        allBtnPanel.add(createBtnPanel3(), BorderLayout.SOUTH);

        //creating menu and IP parent panel
        JPanel menuAndIpPanel = new JPanel();
        menuAndIpPanel.setLayout(new BorderLayout());
        //adding menu
        menuAndIpPanel.add(createMenuPanel(), BorderLayout.NORTH);
        //adding IP-panel
        menuAndIpPanel.add(createIPPanel(), BorderLayout.SOUTH);

        //adding menu/IP-panel
        panel.add(menuAndIpPanel, BorderLayout.NORTH);
        //adding all buttons to parent parent
        panel.add(allBtnPanel, BorderLayout.CENTER);
        //add processing view field
        panel.add(createTxtPanel(), BorderLayout.SOUTH);

        pack();
    }

    private JMenuBar createMenuPanel() {
        //initializing our menu bar with File and Help options
        JMenuBar mainMenu = new JMenuBar();
        mainMenu.setBorderPainted(true);
        MenuHelper.initFileMenu(this, mainMenu);
        MenuHelper.initHelpMenu(this, mainMenu);
        return mainMenu;
    }

    private JPanel createTxtPanel() {
        //creating scroll text panel for log viewing
        JPanel textPanel = new JPanel();
        textPanel.setBorder(BorderFactory.createTitledBorder("Лог обработки: "));
        JScrollPane logScrollPane = new JScrollPane(logText);
        logScrollPane.setPreferredSize(new Dimension(580, 400));
        textPanel.add(logScrollPane);
        return textPanel;
    }

    private JPanel createBtnPanel1() {
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
                    logText.append(Color.RED, "Некорректный синтаксис в файле! Строка: " + e1.getMessage() + System.lineSeparator());
                } catch (Exception e2) {
                    logText.append(Color.RED, "Ошибка при загрузке файла!" + System.lineSeparator());
                }
            }
        });
        //adding pathField near loading button
        btnPanel1.add(pathField);
        btnPanel1.add(button11);
        return btnPanel1;
    }

    private JPanel createBtnPanel2() {
        //creating buttons panel-2 - with region list selection
        JPanel btnPanel2 = new JPanel();
        btnPanel2.add(new JLabel("<html><b>Выберите регион для обработки/вывода: </b>"));
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
        return btnPanel2;
    }

    private JPanel createBtnPanel3() {
        //creating buttons panel-3
        JPanel btnPanel3 = new JPanel();
        btnPanel3.setLayout(new BorderLayout());

        JPanel subBtnPanel1 = new JPanel();
        JPanel subBtnPanel2 = new JPanel();
        JPanel subBtnPanel3 = new JPanel();
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
        //resultMap with cached data show button
        JButton button33 = new JButton("Вывести обработанные магазины");
        button33.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //here controller starts new thread for real-time logging
                controller.onCachedShops();
            }
        });
        //view not connected shops button
        JButton button34 = new JButton("Вывести проблемные магазины");
        button34.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.onProblemShops();
            }
        });
        //send email test
        JButton button35 = new JButton("Выслать письма о проблемных магазинах");
        button35.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    //flag for successful sending
                    boolean goodSend = controller.sendProblemShops();
                    if (goodSend)
                        logText.append(new Color(0, 100, 0), "Письма успешно отправлены." + System.lineSeparator());
                    else
                        logText.append(Color.RED, "Нет данных для отправки!" + System.lineSeparator());
                } catch (MessagingException | IOException | CloneNotSupportedException e1) {
                    logText.append(Color.RED, "Проблема при отправке!" + System.lineSeparator());
                    e1.printStackTrace();
                }
            }
        });
        //clear button
        JButton button36 = new JButton("Очистить вывод");
        button36.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearLogView();
            }
        });

        //clear model data button
        JButton button37 = new JButton("Очистить кэш обработки");
        button37.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.onClearModelData();
                logText.append(new Color(0, 100, 0), "База данных очищена." + System.lineSeparator());
            }
        });
        //exit button
        JButton button38 = new JButton("Выход");
        button38.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        subBtnPanel1.add(button31);
        subBtnPanel1.add(button32);
        subBtnPanel1.add(button33);
        subBtnPanel2.add(button34);
        subBtnPanel2.add(button35);
        subBtnPanel3.add(button36);
        subBtnPanel3.add(button37);
        subBtnPanel3.add(button38);
        btnPanel3.add(subBtnPanel1, BorderLayout.NORTH);
        btnPanel3.add(subBtnPanel2, BorderLayout.CENTER);
        btnPanel3.add(subBtnPanel3, BorderLayout.SOUTH);
        return btnPanel3;
    }

    private void initWindow() {
        setVisible(true);
        setResizable(false);
        //center positioning - setLocationRelativeTo(null);
        setLocation(150,100);
        setMinimumSize(new Dimension(600,710));
        setTitle("Проверка доступа к УТМ на магазинах");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private JPanel createIPPanel() {
        //output your IP
        String IP;
        String host = "";
        try {
            IP = InetAddress.getLocalHost().getHostAddress();
            host = InetAddress.getLocalHost().getHostName();
        }
        catch (Exception e) {
            IP = "Не хватает прав для получения IP-адреса";
        }
        JPanel IPPanel = new JPanel();
        IPPanel.setBorder(BorderFactory.createTitledBorder("Ваш IP-адрес и имя компьютера: "));
        IPPanel.add(new JLabel("<html><b>" + IP + ", </b>"));
        IPPanel.add(new JLabel("<html><b>" + host + "</b>"));
        return IPPanel;
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

    private String getStringFromMainRegions(Region r) {
        String region;
        switch (r) {
            case SIMFEROPOL:
                region = " Симферополь / Белогорск / Бахчисарай ";
                break;
            case SEVASTOPOL:
                region = " Севастополь ";
                break;
            case JANKOI:
                region = " Джанкой / Красногвардейск / Нижнегорск ";
                break;
            case FEODOSIYA:
                region = " Феодосия / Судак ";
                break;
            case KERCH:
                region = " Керчь ";
                break;
            case YEVPATORIA:
                region = " Евпатория / Саки ";
                break;
            case ALUSHTA:
                region = " Ялта / Алушта ";
                break;
            case KRASNOPEREKOPSK:
                region = " Армянск / Красноперекопск ";
                break;
            default:
                region = " Неизвестный регион ";
                break;
        }
        return region;
    }

    @Override
    public void refreshAll(Map<Shop, Status> resultMap) {
        for (Map.Entry<Shop, Status> result: resultMap.entrySet()) {
            refresh(result);
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                cashedShopsShown();
            }
        });

    }

    @Override
    public void refresh(Map.Entry<Shop, Status> entry) {
        final String regionOutput;
        final String status;
        final Color colorStatus;

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
                colorStatus = Color.MAGENTA;
                break;
            case UTM_WRONG_STATUS:
                status = "Компьютер доступен, проблема со службами УТМ!";
                colorStatus = Color.MAGENTA;
                break;
            default:
                status = "Не обработан!";
                colorStatus = Color.ORANGE;
                break;
        }

        final String name = shop.getName();
        final String IP = shop.getIP().toString();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                logText.append(Color.BLACK, " " + name, true);
                logText.append(regionOutput);
                logText.append(Color.BLUE, IP);
                logText.append(" ==> ");
                logText.append(colorStatus, status + System.lineSeparator(), false);
            }
        });

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

    @Override
    public void cashedShopsShown() {
        logText.append(new Color(0, 100, 0), "Обработанные магазины по выбранным регионам выведены!" + System.lineSeparator());
    }

    @Override
    public void regionProblemShopsMailSent(Region r) {
        logText.append(new Color(0, 100, 0), "Письмо для региона "+getStringFromMainRegions(r)+" отправлено!" + System.lineSeparator());
    }

    public void showAbout() {
        JOptionPane.showMessageDialog(this,"<html><b>Проверка доступа к УТМ на магазинах</b>"+System.lineSeparator()+
                "<html><i>версия 1.0</i>" + System.lineSeparator()+
                "Автор идеи и разработчик - Александр Марченко"+ System.lineSeparator()+
                "<html><i>email: </i>"+"<html><u>remain4life@gmail.com</i>",
                "Информация о программе",JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void saveViewLog(File currentFile) {
        try {
            controller.onSaveViewLog(currentFile, logText.getText());
            String fileName = currentFile.getName();
            if (!fileName.endsWith(".txt"))
                fileName+=".txt";
            logText.append(new Color(0, 100, 0), "Выведенный лог сохранён в файл "+ fileName + System.lineSeparator());
        } catch (IOException e) {
            logText.append(Color.RED, "Ошибка записи в файл!" + System.lineSeparator());
        }
    }

    @Override
    public void saveAllCache(File currentFile) {
        try {
            if (controller.onSaveAllCache(currentFile))
                logText.append(new Color(0, 100, 0), "Обработанные данные успешно сохранены." + System.lineSeparator());
        } catch (IOException | CloneNotSupportedException e) {
            logText.append(Color.RED, "Ошибка записи в директорию!" + System.lineSeparator());
        }
    }

    @Override
    public void regionCachedDataSaved(Region r) {
        logText.append(new Color(0, 100, 0), "Данные для региона "+getStringFromMainRegions(r)+
                " сохранены в файл " + r + ".txt."+ System.lineSeparator());

    }
}
