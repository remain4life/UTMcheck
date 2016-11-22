package utmcheck.model;

import utmcheck.ParserUtil;
import utmcheck.enums.Status;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class ModelData {
    private List<Shop> shopList = new ArrayList<>();
    private Map<Shop, Status> notConnectedShops = new TreeMap<>();
    private Map<Shop, Status> resultMap = new TreeMap<>();

    public void loadSocketList(Path path) throws Exception {
        shopList = ParserUtil.parseFile(path);
    }

    public List<Shop> getShopList() {
        return shopList;
    }

    public Map<Shop, Status> getResultMap() {
        return resultMap;
    }

    public Map<Shop, Status> getNotConnectedShops() {
        return notConnectedShops;
    }

    public boolean isEmpty() {
        return shopList.isEmpty();
    }
}
