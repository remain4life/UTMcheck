package utmcheck.model;

import utmcheck.utils.ParserUtil;
import utmcheck.enums.Status;

import java.io.IOException;
import java.net.*;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Model {
    private ModelData modelData = new ModelData();

    public ModelData getModelData() {
        return modelData;
    }

    public void loadData(Path path) throws Exception {
        modelData.loadSocketList(path);
    }

    public Map.Entry<Shop, Status> checkIP(Shop shop) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) shop.getIP().openConnection(); //устанавливаем соединение
        connection.setRequestMethod("HEAD");
        boolean result = connection.getResponseCode() == HttpURLConnection.HTTP_OK;
        if (result) {
            //if response code OK
            modelData.getResultMap().put(shop, Status.OK);
            return new HashMap.SimpleEntry<>(shop, Status.OK);
        } else {
            //if something wrong with UTM services
            modelData.getResultMap().put(shop, Status.UTM_WRONG_STATUS);
            modelData.getNotConnectedShops().put(shop, Status.UTM_WRONG_STATUS);
            return new HashMap.SimpleEntry<>(shop, Status.UTM_WRONG_STATUS);
        }

    }

    public Map.Entry<Shop, Status> ping(Shop shop) throws IOException {
        //getting IP address from url
        String IP = ParserUtil.socketToIP(shop.getIP());
        InetAddress inet = InetAddress.getByName(IP);
        //if host reachable - we check connect with socket
        if (inet.isReachable(3000)) {
            Map.Entry<Shop, Status> entry;
            try {
                entry = checkIP(shop);
            } catch (IOException e) {
                //if we have exception in connection establishment time
                modelData.getResultMap().put(shop, Status.NO_UTM_CONNECT);
                modelData.getNotConnectedShops().put(shop, Status.NO_UTM_CONNECT);
                return new HashMap.SimpleEntry<>(shop, Status.NO_UTM_CONNECT);
            }
            return entry;

            //if host isn't reachable - it doesn't make sense to check socket
        } else {
            modelData.getResultMap().put(shop, Status.NO_HOST_CONNECT);
            modelData.getNotConnectedShops().put(shop, Status.NO_HOST_CONNECT);
            return new HashMap.SimpleEntry<>(shop, Status.NO_HOST_CONNECT);
        }

    }
}
