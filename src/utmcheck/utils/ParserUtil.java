package utmcheck.utils;

import utmcheck.enums.Region;
import utmcheck.exceptions.NotCorrectFileException;
import utmcheck.model.Shop;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class ParserUtil {
    private ParserUtil() {
    }

    public static List<Shop> parseFile(Path path) throws Exception {

        //loading file
        /* each line of file should match this pattern
        "Shop/Point name* Region** IP***"
        * one or more space separated words
        ** one or more space separated words, started with region name
        *** IP-address or domain name, may start with http:// or not

        examples:

        "Магазин 25" "Бахчисарайский р-н"	91.142.74.58:4155
        "Market-1"     "Армянск"    hostname.domain.com
        */
        List<Shop> shops = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(path)));
        String regexp = "\\s*\"\\s*\"*\\s*";
        String s;
        while ((s = reader.readLine()) != null) {

            String[] parts = s.split(regexp);

            //adding only good strings
            if (parts.length==4 && parts[0].isEmpty()) {
                String name = parts[1];
                Region region = getRegion(parts[2]);
                URL IP = stringToURL(parts[3]);

                shops.add(new Shop(name, region, IP));
            } else if (parts.length>0) {
                throw new NotCorrectFileException(s);
            }
        }

        return shops;
    }

    private static Region getRegion(String place) {
        Region region;
        //we determine region by first 4 letters because of the least name length
        String begin = place.substring(0, 4).toUpperCase();
        switch (begin) {
            case "СИМФ":
                region = Region.SIMFEROPOL;
                break;
            case "БАХЧ":
                region = Region.BAKHCHISARAI;
                break;
            case "БЕЛО":
                region = Region.BELOGORSK;
                break;
            case "СЕВА":
                region = Region.SEVASTOPOL;
                break;
            case "ЯЛТА":
            case "ЯЛТИ":
                region = Region.YALTA;
                break;
            case "АЛУШ":
                region = Region.ALUSHTA;
                break;
            case "АРМЯ":
                region = Region.ARMYANSK;
                break;
            case "ПЕРВ":
            case "РАЗД":
                region = Region.KRASNOPEREKOPSK;
                break;
            case "КРАС":
                if (place.toUpperCase().startsWith("КРАСНОПЕР")){
                    region = Region.KRASNOPEREKOPSK;
                } else {
                    region = Region.KRASNOGVARDEYSK;
                }
                break;
            case "ЛЕНИ":
            case "КЕРЧ":
                region = Region.KERCH;
                break;
            case "ФЕОД":
                region = Region.FEODOSIYA;
                break;
            case "СУДА":
                region = Region.SUDAK;
                break;
            case "ЕВПА":
            case "ЧЕРН":
                region = Region.YEVPATORIA;
                break;
            case "САКИ":
            case "САКС":
                region = Region.SAKI;
                break;
            case "ДЖАН":
                region = Region.JANKOI;
                break;
            case "НИЖН":
                region = Region.NIZHNEGORSK;
                break;
            default:
                region = Region.OTHER;
                break;
        }
        return region;
    }

    public static String socketToIP(URL socket) {
        return socket.toString().replaceAll("^http://","").replaceAll(":.*$", "");
    }

    public static URL stringToURL(String address) throws Exception {
        //adding protocol
        if (!address.matches("^http://.*")) {
            address = "http://" + address;
        }
        return new URL(address);
    }
}
