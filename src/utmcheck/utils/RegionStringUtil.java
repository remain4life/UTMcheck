package utmcheck.utils;

import utmcheck.enums.Region;
import utmcheck.enums.Status;

import java.awt.*;

public final class RegionStringUtil {
    private RegionStringUtil() {
    }

    public static Region getMainRegionsFromString(String item) {
        Region region;
        switch (item) {
            case " Все ":
                region = Region.ALL;
                break;
            case " Симферополь / Белогорск / Бахчисарай ":
                region = Region.SIMFEROPOL;
                break;
            case " Севастополь ":
                region = Region.SEVASTOPOL;
                break;
            case " Джанкой / Красногвардейск / Нижнегорск ":
                region = Region.JANKOI;
                break;
            case " Феодосия / Судак ":
                region = Region.FEODOSIYA;
                break;
            case " Керчь ":
                region = Region.KERCH;
                break;
            case " Евпатория / Саки ":
                region = Region.YEVPATORIA;
                break;
            case " Ялта / Алушта ":
                region = Region.ALUSHTA;
                break;
            case " Армянск / Красноперекопск ":
                region = Region.KRASNOPEREKOPSK;
                break;
            default:
                region = Region.OTHER;
                break;
        }
        return region;
    }

    public static String getStringFromMainRegions(Region r) {
        String region;
        switch (r) {
            case SIMFEROPOL:
                region = " Симферополь / Белогорск / Бахчисарай ";
                break;
            case SEVASTOPOL:
                region = " Севастополь ";
                break;
            case JANKOI:
                region = " Джанкой / Красногвардейск / Нижнегорск ";
                break;
            case FEODOSIYA:
                region = " Феодосия / Судак ";
                break;
            case KERCH:
                region = " Керчь ";
                break;
            case YEVPATORIA:
                region = " Евпатория / Саки ";
                break;
            case ALUSHTA:
                region = " Ялта / Алушта ";
                break;
            case KRASNOPEREKOPSK:
                region = " Армянск / Красноперекопск ";
                break;
            default:
                region = " Неизвестный регион ";
                break;
        }
        return region;
    }

    public static String getStringFromEnum(Region r) {
        String s;
        switch (r) {
            case SIMFEROPOL:
                s = ", Симферополь, ";
                break;
            case BAKHCHISARAI:
                s = ", Бахчисарай, ";
                break;
            case BELOGORSK:
                s = ", Белогорск, ";
                break;
            case SEVASTOPOL:
                s = ", Севастополь, ";
                break;
            case YALTA:
                s = ", Ялта, ";
                break;
            case ALUSHTA:
                s = ", Алушта, ";
                break;
            case ARMYANSK:
                s = ", Армянск, ";
                break;
            case KRASNOPEREKOPSK:
                s = ", Красноперекопск, ";
                break;
            case KRASNOGVARDEYSK:
                s = ", Красногвардейск, ";
                break;
            case NIZHNEGORSK:
                s = ", Нижнегорск, ";
                break;
            case KERCH:
                s = ", Керчь, ";
                break;
            case FEODOSIYA:
                s = ", Феодосия, ";
                break;
            case SUDAK:
                s = ", Судак, ";
                break;
            case YEVPATORIA:
                s = ", Евпатория, ";
                break;
            case SAKI:
                s = ", Саки, ";
                break;
            case JANKOI:
                s = ", Джанкой, ";
                break;
            default:
                s = ", Неизвестный регион, ";
                break;
        }
        return s;
    }

    public static String getStringFromEnum(Status status) {
        String s;
        switch (status) {
            case OK:
                s = "Всё ОК!";
                break;
            case NO_HOST_CONNECT:
                s = "Нет связи с компьютером!";
                break;
            case NO_UTM_CONNECT:
                s = "Компьютер доступен, нет связи с УТМ!";
                break;
            case UTM_WRONG_STATUS:
                s = "Компьютер доступен, проблема со службами УТМ!";
                break;
            default:
                s = "Не обработан!";
                break;
        }
        return s;
    }

    public static Color getColorToStatus(Status status) {
        Color color;
        switch (status) {
            case OK:
                color = new Color(46, 139, 87);
                break;
            case NO_HOST_CONNECT:
                color = Color.RED;
                break;
            case NO_UTM_CONNECT:
                color = Color.MAGENTA;
                break;
            case UTM_WRONG_STATUS:
                color = Color.MAGENTA;
                break;
            default:
                color = Color.ORANGE;
                break;
        }
        return color;
    }

    public static String[] addRegionList() {
        return new String[]{" Все ",
                " Симферополь / Белогорск / Бахчисарай ",
                " Севастополь ",
                " Джанкой / Красногвардейск / Нижнегорск ",
                " Феодосия / Судак ",
                " Керчь ",
                " Евпатория / Саки ",
                " Ялта / Алушта ",
                " Армянск / Красноперекопск "};
    }
}
