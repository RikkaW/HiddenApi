package rikka.hidden.compat;

import static rikka.hidden.compat.Services.appOps;

import android.app.AppOpsManagerHidden;
import android.os.Build;
import android.os.RemoteException;

import androidx.annotation.RequiresApi;

import com.android.internal.app.IAppOpsActiveCallback;
import com.android.internal.app.IAppOpsNotedCallback;

import java.util.List;

@SuppressWarnings({"unused"})
public class AppOpsService {

    public static List<AppOpsManagerHidden.PackageOps> getOpsForPackage(int uid, String packageName, int[] ops) throws RemoteException {
        return appOps.get().getOpsForPackage(uid, packageName, ops);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<AppOpsManagerHidden.PackageOps> getUidOps(int uid, int[] ops) throws RemoteException {
        return appOps.get().getUidOps(uid, ops);
    }

    public static void setMode(int code, int uid, String packageName, int mode) throws RemoteException {
        appOps.get().setMode(code, uid, packageName, mode);
    }

    public static void setUidMode(int code, int uid, int mode) throws RemoteException {
        appOps.get().setUidMode(code, uid, mode);
    }

    public static void resetAllModes(int userId, String packageName) throws RemoteException {
        appOps.get().resetAllModes(userId, packageName);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public static void startWatchingActive(int[] ops, IAppOpsActiveCallback callback) throws RemoteException {
        appOps.get().startWatchingActive(ops, callback);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public static void stopWatchingActive(IAppOpsActiveCallback callback) throws RemoteException {
        appOps.get().stopWatchingActive(callback);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public static boolean isOperationActive(int code, int uid, String packageName) throws RemoteException {
        return appOps.get().isOperationActive(code, uid, packageName);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static void startWatchingNoted(int[] ops, IAppOpsNotedCallback callback) throws RemoteException {
        appOps.get().startWatchingNoted(ops, callback);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static void stopWatchingNoted(IAppOpsNotedCallback callback) throws RemoteException {
        appOps.get().stopWatchingNoted(callback);
    }
}
