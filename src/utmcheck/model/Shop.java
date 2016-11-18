package utmcheck.model;

import utmcheck.enums.Region;

import java.net.URL;

public class Shop implements Comparable{
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
        return name + ", " + region + ", " + IP;
    }

    //for sorting shops

    @Override
    public int compareTo(Object o) {
        return this.name.compareToIgnoreCase(((Shop)o).name);
    }
}
