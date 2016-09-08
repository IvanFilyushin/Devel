package ru.stqa.pft.soap;

import net.webservicex.GeoIP;
import org.testng.Assert;
import org.testng.annotations.Test;
import net.webservicex.GeoIPService;

/**
 * Created by DELL on 08.09.16.
 */
public class GeoIpServiceTests {

    @Test

    public void myIp() {

        GeoIP geoIP = new GeoIPService().getGeoIPServiceSoap12().getGeoIP("188.32.206.97");
        Assert.assertEquals(geoIP.getCountryCode(), "RUS");
    }

}
