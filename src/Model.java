import java.io.IOException;
import java.net.*;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Model {
    private ModelData modelData = new ModelData();

    public ModelData getModelData() {
        return modelData;
    }

    public void loadData() throws IOException {
        modelData.loadSocketList(Paths.get("c:/shops/shopList.txt"));
    }

    public Map.Entry<URL, Status> checkIP(URL urlSocket) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) urlSocket.openConnection(); //устанавливаем соединение
        connection.setRequestMethod("HEAD");
        boolean result = connection.getResponseCode() == HttpURLConnection.HTTP_OK;
        if (result) {
            //if response code OK
            modelData.getResultMap().put(urlSocket, Status.OK);
            return new HashMap.SimpleEntry<>(urlSocket, Status.OK);
        } else {
            //if something wrong with UTM services
            modelData.getResultMap().put(urlSocket, Status.UTM_WRONG_STATUS);
            return new HashMap.SimpleEntry<>(urlSocket, Status.UTM_WRONG_STATUS);
        }

    }

    public Map.Entry<URL, Status> ping(URL urlSocket) throws IOException {
        //getting IP address from url
        String IP = ParserUtil.socketToIP(urlSocket);
        InetAddress inet = InetAddress.getByName(IP);
        //if host reachable - we check connect with socket
        if (inet.isReachable(2000)) {
            Map.Entry<URL, Status> entry;
            try {
                entry = checkIP(urlSocket);
            } catch (IOException e) {
                //if we have exception in connection establishment time
                modelData.getResultMap().put(urlSocket, Status.NO_UTM_CONNECT);
                return new HashMap.SimpleEntry<>(urlSocket, Status.NO_UTM_CONNECT);
            }
            return entry;

            //if host isn't reachable - it doesn't make sense to check socket
        } else {
            modelData.getResultMap().put(urlSocket, Status.NO_HOST_CONNECT);
            modelData.getNotConnectedIPList().add(IP);
            return new HashMap.SimpleEntry<>(urlSocket, Status.NO_HOST_CONNECT);
        }

    }
}
