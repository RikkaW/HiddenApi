package rikka.hidden.compat;

import android.content.pm.IOnAppsChangedListener;
import android.os.Build;
import android.os.RemoteException;

public class LauncherAppsApis {

    public static void addOnAppsChangedListener(String callingPackage, IOnAppsChangedListener listener) throws RemoteException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Services.launcherApps.get().addOnAppsChangedListener(callingPackage, listener);
        } else {
            Services.launcherApps.get().addOnAppsChangedListener(listener);
        }
    }

    public static void removeOnAppsChangedListener(IOnAppsChangedListener listener) throws RemoteException {
            Services.launcherApps.get().removeOnAppsChangedListener(listener);
    }
}
