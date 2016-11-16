import javax.swing.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class Controller {
    private View view;
    private Model model;

    //test variable for loop stop
    public static volatile boolean isStopped = false;

    //variable for one thread control
    private volatile boolean isThreadRunning = false;
    //flag for work processing control
    private volatile boolean workDone = false;

    public boolean isThreadRunning() {
        return isThreadRunning;
    }

    public void setThreadRunning(boolean threadRunning) {
        isThreadRunning = threadRunning;
    }

    public boolean isWorkDone() {
        return workDone;
    }

    public void setWorkDone(boolean workDone) {
        this.workDone = workDone;
    }

    private volatile Thread workThread = null;

    public void setView(View view) {
        this.view = view;
    }

    public View getView() {
        return view;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public void init() {
        //create new model and view
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

        //checking number of active threads
        ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
        while(!isStopped) {
            System.out.println(threadGroup.activeCount());
            //creating empty array
            Thread[] list = new Thread[threadGroup.activeCount()];
            //filling it by data from thread group
            threadGroup.enumerate(list);
            //output info to console
            for (Thread t: list) {
                try {
                    System.out.println(t.getName() + " " + t.getPriority() + " " + t.getState());
                } catch (Exception ignored) {
                }
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException ignored) {

            }
        }

    }

    //parsing file to address list
    public void loadModelData() throws IOException {
        model.loadData();
    }

    public void checkIP() throws IOException {
        if (model.getModelData().isEmpty()) {
            view.emptyMessage();
        }
        startWorkThread(model.getModelData().getSocketList());
    }

    public void checkRegionIP(Region region) throws IOException {
       // model.checkIP(region);
       // view.refresh(model.getModelData());
    }

    //list processing start
    public void startWorkThread(final List<URL> list) {
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
                        view.doneMessage();
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
}
