package rikka.hidden.compat;

import static rikka.hidden.compat.Services.displayManager;

import android.hardware.display.IDisplayManagerCallback;
import android.os.RemoteException;
import android.view.DisplayInfo;

public class DisplayManagerApis {

    public static DisplayInfo getDisplayInfo(int displayId) throws RemoteException {
        return displayManager.get().getDisplayInfo(displayId);
    }

    public static void registerCallback(IDisplayManagerCallback callback) throws RemoteException {
        displayManager.get().registerCallback(callback);
    }
}
