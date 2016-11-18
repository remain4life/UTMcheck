package utmcheck.model;

import utmcheck.ParserUtil;
import utmcheck.enums.Status;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class ModelData {
    private List<Shop> shopList = new ArrayList<>();
    private List<Shop> notConnectedShops = new ArrayList<>();
    private Map<Shop, Status> resultMap = new TreeMap<>();

    public void loadSocketList(Path path) throws Exception {
        shopList = ParserUtil.parseFile(path);
    }


    public List<Shop> getShopList() {
        return shopList;
    }

    public void setShopList(List<Shop> shopList) {
        this.shopList = shopList;
    }

    public Map<Shop, Status> getResultMap() {
        return resultMap;
    }

    public void setResultMap(Map<Shop, Status> resultMap) {
        this.resultMap = resultMap;
    }

    public List<Shop> getNotConnectedShops() {
        return notConnectedShops;
    }

    public void setNotConnectedShops(List<Shop> notConnectedShops) {
        this.notConnectedShops = notConnectedShops;
    }

    public boolean isEmpty() {
        return shopList.isEmpty();
    }
}
