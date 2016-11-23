package utmcheck.model;

import utmcheck.utils.ParserUtil;
import utmcheck.enums.Status;

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

    public void setShopList(List<Shop> shopList) {
        this.shopList = shopList;
    }

    public void setNotConnectedShops(Map<Shop, Status> notConnectedShops) {
        this.notConnectedShops = notConnectedShops;
    }

    public void setResultMap(Map<Shop, Status> resultMap) {
        this.resultMap = resultMap;
    }

    public boolean isEmpty() {
        return shopList.isEmpty();
    }
}
