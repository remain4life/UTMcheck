package utmcheck.model;

import utmcheck.enums.Region;

import java.net.URL;

public class Shop {
    private String name;
    private Region region;
    private URL IP;

    public Shop(String name, Region region, URL IP) {
        this.name = name;
        this.region = region;
        this.IP = IP;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public URL getIP() {
        return IP;
    }

    public void setIP(URL IP) {
        this.IP = IP;
    }

    @Override
    public String toString() {
        return "Shop{" +
                "name='" + name + '\'' +
                ", region=" + region +
                ", IP=" + IP +
                '}';
    }
}
