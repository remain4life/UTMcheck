package utmcheck.model;

import utmcheck.ParserUtil;
import utmcheck.enums.Status;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelData {
    private List<URL> socketList = new ArrayList<>();
    private List<String> notConnectedIPList = new ArrayList<>();
    private Map<URL, Status> resultMap = new HashMap<>();

    public void loadSocketList(Path path) throws IOException {
        socketList = ParserUtil.parseFile(path);
    }


    public List<URL> getSocketList() {
        return socketList;
    }

    public void setSocketList(List<URL> socketList) {
        this.socketList = socketList;
    }

    public Map<URL, Status> getResultMap() {
        return resultMap;
    }

    public void setResultMap(Map<URL, Status> resultMap) {
        this.resultMap = resultMap;
    }

    public List<String> getNotConnectedIPList() {
        return notConnectedIPList;
    }

    public void setNotConnectedIPList(List<String> notConnectedIPList) {
        this.notConnectedIPList = notConnectedIPList;
    }

    public boolean isEmpty() {
        return socketList.isEmpty();
    }
}
