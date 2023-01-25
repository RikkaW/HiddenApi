package rikka.hidden.compat;

import static rikka.hidden.compat.Services.batteryPropertiesRegistrar;

import android.os.BatteryProperty;
import android.os.Build;
import android.os.IBatteryPropertiesRegistrar;
import android.os.RemoteException;

import androidx.annotation.RequiresApi;

public class BatteryPropertiesRegistrarApis {

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static long queryProperty(int id) throws RemoteException {
        long ret;
        BatteryProperty prop = new BatteryProperty();
        IBatteryPropertiesRegistrar batteryPropertiesRegistrar = Services.batteryPropertiesRegistrar.get();
        if (batteryPropertiesRegistrar == null) {
            return Long.MIN_VALUE;
        }

        if (batteryPropertiesRegistrar.getProperty(id, prop) == 0) {
            ret = prop.getLong();
        } else {
            ret = Long.MIN_VALUE;
        }

        return ret;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static int getIntProperty(int id) throws RemoteException {
        long value = queryProperty(id);
        if (value == Long.MIN_VALUE) {
            return Integer.MIN_VALUE;
        }

        return (int) value;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static long getLongProperty(int id) throws RemoteException {
        return queryProperty(id);
    }

    public static void scheduleUpdate() throws RemoteException {
        batteryPropertiesRegistrar.get().scheduleUpdate();
    }
}
