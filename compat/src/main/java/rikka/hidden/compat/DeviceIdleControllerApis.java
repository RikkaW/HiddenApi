package rikka.hidden.compat;

import static rikka.hidden.compat.Services.deviceIdleController;

import android.os.Build;

public class DeviceIdleControllerApis {

    public static void addPowerSaveTempWhitelistApp(String name, long duration, int userId, int reasonCode, String reason) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            deviceIdleController.get().addPowerSaveTempWhitelistApp(name, duration, userId, reasonCode, reason);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            deviceIdleController.get().addPowerSaveTempWhitelistApp(name, duration, userId, reason);
        }
    }
}
