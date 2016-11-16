import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.util.Map;

public class GuiView extends JFrame implements View {
    private Controller controller;
    //field for view processing
    private JTextArea logText = new JTextArea();

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
        setMinimumSize(new Dimension(600,580));
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
                    controller.loadModelData();
                    logText.append("Данные загружены успешно, можно обрабатывать." + System.lineSeparator());
                } catch (IOException e1) {
                    logText.append("Ошибка при загрузке файла!" + System.lineSeparator());
                }
            }
        });
        btnPanel1.add(button11);
        allBtnPanel.add(btnPanel1, BorderLayout.NORTH);

        //creating buttons panel-2
        JPanel btnPanel2 = new JPanel();
        //processing button
        JButton button21 = new JButton("Запустить проверку магазинов");
        button21.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //here controller starts new thread for real-time logging
                viewAllIP();
            }
        });
        //interrupt button
        JButton button22 = new JButton("Прервать выполнение");
        button22.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                workInterrupt();
            }
        });
        //clear button
        JButton button23 = new JButton("Очистить вывод");
        button23.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearLogView();
            }
        });
        //exit button
        JButton button24 = new JButton("Выход");
        button24.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Controller.isStopped = true;
                dispose();
            }
        });
        btnPanel2.add(button21);
        btnPanel2.add(button22);
        btnPanel2.add(button23);
        btnPanel2.add(button24);

        allBtnPanel.add(btnPanel2, BorderLayout.SOUTH);

        //add processing view field
        JPanel textPanel = new JPanel();
        textPanel.setBorder(BorderFactory.createTitledBorder("Лог обработки: "));
        logText.setLineWrap(true);
        logText.setWrapStyleWord(true);
        JScrollPane logScrollPane = new JScrollPane(logText);
        logScrollPane.setPreferredSize(new Dimension(580, 400));
        textPanel.add(logScrollPane);
        panel.add(textPanel, BorderLayout.SOUTH);

        pack();
    }

    @Override
    public void refreshAll(ModelData modelData) {
        for (Map.Entry<URL, Status> result: modelData.getResultMap().entrySet()) {
            logText.append(result.getKey() + " - " + result.getValue());
        }
    }

    @Override
    public void refresh(Map.Entry<URL, Status> entry) {
        logText.append(" "+ entry.getKey() + " - " + entry.getValue() + System.lineSeparator());
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
            logText.append("Ошибка обработки!");
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

    //when all list processing done
    @Override
    public void doneMessage() {
        logText.append("Все точки обработаны!" + System.lineSeparator());
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
        logText.append("Процесс не запущен, нечего прерывать." + System.lineSeparator());
    }

    @Override
    public void interruptDone() {
        logText.append("Обработка прервана, данные кэшированы." + System.lineSeparator());
    }

    @Override
    public void emptyMessage() {
        logText.append("База адресов пуста, загрузите данные!" + System.lineSeparator());
    }
}
