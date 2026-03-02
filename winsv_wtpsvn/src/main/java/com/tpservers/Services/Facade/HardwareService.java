package com.tpservers.Services.Facade;

import com.google.gson.JsonObject;
import com.google.gson.Gson;
import tungtt.HardwareProfile.Modules.WindowsHardwareProfile;
import tungtt.Security.Contexts.HwidProfileContext;
import tungtt.Security.Modules.HwidGenerator;
import tungtt.HardwareProfile.Contexts.HardwareSnapshot;
import tungtt.HardwareProfile.Contexts.OsContext;
import oshi.SystemInfo;

public final class HardwareService {

    private HardwareService() {}

    static class Holder {

        static final WindowsHardwareProfile HW_PROFILE =
            new WindowsHardwareProfile(new OsContext(new SystemInfo()));

        static final HwidProfileContext HW_PROFILE_CONTEXT =
            new HwidProfileContext(
                Holder.HW_PROFILE.hwCpuId(),
                Holder.HW_PROFILE.hwDiskSerial(),
                Holder.HW_PROFILE.hwRdp());

        static final HwidGenerator HW_HWID_GEN =
            new HwidGenerator(HW_PROFILE_CONTEXT);
    }

    /* ===================== COLLECT ===================== */

    public static JsonObject collect() {

        HardwareSnapshot hw = Holder.HW_PROFILE.snapshot();
        JsonObject root = new JsonObject();

        Gson gson = new Gson();

        root.addProperty("rdp", hw.rdp());
        root.addProperty("ip", hw.ip());
        root.add("dns", gson.toJsonTree(hw.dns()));
        root.add("macs", gson.toJsonTree(hw.macs()));
        root.add("cpus", gson.toJsonTree(hw.cpus()));
        root.add("rams", gson.toJsonTree(hw.rams()));
        root.add("gpus", gson.toJsonTree(hw.gpus()));
        root.add("motherboards", gson.toJsonTree(hw.motherboards()));
        root.add("disks", gson.toJsonTree(hw.disks()));
        root.add("osInfo", gson.toJsonTree(hw.osInfo()));

        return root;
    }

    /* ===================== GETTER ===================== */

    public static String hwRdp() {

        return Holder.HW_PROFILE.hwRdp();
    }

    public static String hwDiskSerial() {

        return Holder.HW_PROFILE.hwDiskSerial();
    }

    public static String hwCpuId() {

        return Holder.HW_PROFILE.hwCpuId();
    }

    public static String hwHwid() {

        return Holder.HW_HWID_GEN.build();
    }
}
