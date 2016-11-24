package utmcheck.model;

import utmcheck.enums.Region;

import java.net.URL;

public class Shop implements Comparable,Cloneable{
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Shop)) return false;

        Shop shop = (Shop) o;

        if (!getName().equals(shop.getName())) return false;
        if (getRegion() != shop.getRegion()) return false;
        return getIP().equals(shop.getIP());

    }

    @Override
    public int hashCode() {
        int result = getName().hashCode();
        result = 31 * result + getRegion().hashCode();
        result = 31 * result + getIP().hashCode();
        return result;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        super.clone();
        String name = this.getName();
        Region region = this.getRegion();
        URL IP = this.getIP();
        return new Shop(name, region, IP);
    }
}
