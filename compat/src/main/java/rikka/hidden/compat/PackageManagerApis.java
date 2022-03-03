package rikka.hidden.compat;

import static rikka.hidden.compat.Services.packageManager;

import android.annotation.SuppressLint;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.content.pm.PackageInfo;
import android.content.pm.ParceledListSlice;
import android.content.pm.ProviderInfo;
import android.os.Build;
import android.os.RemoteException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.os.BuildCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@BuildCompat.PrereleaseSdkCheck
@SuppressWarnings("unused")
public class PackageManagerApis {

    @SuppressLint("NewApi")
    @Nullable
    public static PackageInfo getPackageInfo(@Nullable String packageName, long flags, int userId) throws RemoteException {
        if (BuildCompat.isAtLeastT()) {
            return packageManager.get().getPackageInfo(packageName, flags, userId);
        } else {
            return packageManager.get().getPackageInfo(packageName, (int) flags, userId);
        }
    }

    @SuppressLint("NewApi")
    @Nullable
    public static ApplicationInfo getApplicationInfo(@Nullable String packageName, long flags, int userId) throws RemoteException {
        if (BuildCompat.isAtLeastT()) {
            return packageManager.get().getApplicationInfo(packageName, flags, userId);
        } else {
            return packageManager.get().getApplicationInfo(packageName, (int) flags, userId);
        }
    }

    @Nullable
    public static PackageInfo getPackageInfoNoThrow(@Nullable String packageName, long flags, int userId) {
        try {
            return getPackageInfo(packageName, flags, userId);
        } catch (Throwable ignored) {
            return null;
        }
    }

    @Nullable
    public static ApplicationInfo getApplicationInfoNoThrow(@Nullable String packageName, long flags, int userId) {
        try {
            return getApplicationInfo(packageName, flags, userId);
        } catch (Throwable ignored) {
            return null;
        }
    }

    @Nullable
    public static String[] getPackagesForUid(int uid) throws RemoteException {
        return packageManager.get().getPackagesForUid(uid);
    }

    @SuppressLint("NewApi")
    @Nullable
    public static ParceledListSlice<ApplicationInfo> getInstalledApplications(long flags, int userId) throws RemoteException {
        if (BuildCompat.isAtLeastT()) {
            //noinspection unchecked
            return packageManager.get().getInstalledApplications(flags, userId);
        } else {
            //noinspection unchecked
            return packageManager.get().getInstalledApplications((int) flags, userId);
        }
    }

    @SuppressLint("NewApi")
    @Nullable
    public static ParceledListSlice<PackageInfo> getInstalledPackages(long flags, int userId) throws RemoteException {
        if (BuildCompat.isAtLeastT()) {
            //noinspection unchecked
            return packageManager.get().getInstalledPackages(flags, userId);
        } else {
            //noinspection unchecked
            return packageManager.get().getInstalledPackages((int) flags, userId);
        }
    }


    @NonNull
    public static List<PackageInfo> getInstalledPackagesNoThrow(long flags, int userId) {
        try {
            ParceledListSlice<PackageInfo> parceledListSlice = getInstalledPackages(flags, userId);
            if (parceledListSlice != null && parceledListSlice.getList() != null) {
                return parceledListSlice.getList();
            }
        } catch (Throwable ignored) {
        }
        return Collections.emptyList();
    }

    @NonNull
    public static List<ApplicationInfo> getInstalledApplicationsNoThrow(long flags, int userId) {
        try {
            ParceledListSlice<ApplicationInfo> parceledListSlice = getInstalledApplications(flags, userId);
            if (parceledListSlice != null && parceledListSlice.getList() != null) {
                return parceledListSlice.getList();
            }
        } catch (Throwable ignored) {
        }
        return Collections.emptyList();
    }

    @NonNull
    public static List<String> getPackagesForUidNoThrow(int uid) {
        ArrayList<String> packages = new ArrayList<>();

        try {
            String[] packagesArray = getPackagesForUid(uid);
            if (packagesArray != null) {
                for (String packageName : packagesArray) {
                    if (packageName != null) {
                        packages.add(packageName);
                    }
                }
            }
        } catch (Throwable ignored) {
        }

        return packages;
    }

    @SuppressLint("NewApi")
    public static int getPackageUid(String packageName, long flags, int userId) throws RemoteException {
        IPackageManager pm = packageManager.get();
        if (BuildCompat.isAtLeastT()) {
            return pm.getPackageUid(packageName, flags, userId);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return pm.getPackageUid(packageName, (int) flags, userId);
        } else {
            return pm.getPackageUid(packageName, userId);
        }
    }


    public static int getUidForSharedUser(String sharedUserName) throws RemoteException {
        return packageManager.get().getUidForSharedUser(sharedUserName);
    }

    public static int getUidForSharedUserNoThrow(String sharedUserName) {
        try {
            return getUidForSharedUser(sharedUserName);
        } catch (Throwable tr) {
            return -1;
        }
    }

    @SuppressLint("NewApi")
    @Nullable
    public static ProviderInfo resolveContentProvider(String name, long flags, int userId) throws RemoteException {
        if (BuildCompat.isAtLeastT()) {
            return packageManager.get().resolveContentProvider(name, flags, userId);
        } else {
            return packageManager.get().resolveContentProvider(name, (int) flags, userId);
        }
    }

    public static boolean getApplicationHiddenSettingAsUser(String packageName, int userId) throws RemoteException {
        return packageManager.get().getApplicationHiddenSettingAsUser(packageName, userId);
    }

    public static boolean getApplicationHiddenSettingAsUserNoThrow(String packageName, int userId) {
        try {
            return getApplicationHiddenSettingAsUser(packageName, userId);
        } catch (Throwable tr) {
            return true;
        }
    }
}
