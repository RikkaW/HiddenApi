package rikka.hidden.compat;

import android.content.pm.IOnAppsChangedListener;
import android.os.RemoteException;

import rikka.buildcompat.BuildCompat;

public class LauncherAppsApis {

    public static void addOnAppsChangedListener(String callingPackage, IOnAppsChangedListener listener) throws RemoteException {
        if (BuildCompat.isAtLeastN()) {
            Services.launcherApps.get().addOnAppsChangedListener(callingPackage, listener);
        } else {
            Services.launcherApps.get().addOnAppsChangedListener(listener);
        }
    }

    public static void removeOnAppsChangedListener(IOnAppsChangedListener listener) throws RemoteException {
            Services.launcherApps.get().removeOnAppsChangedListener(listener);
    }
}
