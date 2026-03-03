package com.tpservers.Services.Facade;

import com.tpservers.Models.WakeOnLanHostMap;

public final class WakeOnLanService {

    private WakeOnLanService() {}

    public static String wakeOnLanePowerOnByHostId(String hostId) {
        return WakeOnLanHostMap.resolveIpByHostId(hostId);
    }
}
