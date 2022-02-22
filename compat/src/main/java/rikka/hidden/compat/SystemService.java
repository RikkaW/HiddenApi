package rikka.hidden.compat;

import android.annotation.SuppressLint;
import android.app.ActivityManagerHidden;
import android.app.ActivityManagerNative;
import android.app.AppOpsManagerHidden;
import android.app.ContentProviderHolder;
import android.app.IActivityManager;
import android.app.IActivityManager23;
import android.app.IApplicationThread;
import android.app.IProcessObserver;
import android.app.IUidObserver;
import android.app.ProfilerInfo;
import android.content.IContentProvider;
import android.content.IIntentReceiver;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.content.pm.PackageInfo;
import android.content.pm.ParceledListSlice;
import android.content.pm.ProviderInfo;
import android.content.pm.UserInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IUserManager;
import android.os.RemoteException;
import android.permission.IPermissionManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.collection.ArraySet;
import androidx.core.os.BuildCompat;

import com.android.internal.app.IAppOpsService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import dev.rikka.tools.refine.Refine;

@SuppressWarnings({"unused", "unchecked"})
@BuildCompat.PrereleaseSdkCheck
public class SystemService {

    private SystemService() {
    }

    private static final SystemServiceBinder<IActivityManager> activityManager = new SystemServiceBinder<>("activity", binder -> {
        if (Build.VERSION.SDK_INT >= 26) {
            return IActivityManager.Stub.asInterface(binder);
        } else {
            return ActivityManagerNative.asInterface(binder);
        }
    });

    private static final SystemServiceBinder<IPackageManager> packageManager = new SystemServiceBinder<>("package", IPackageManager.Stub::asInterface);

    private static final SystemServiceBinder<IUserManager> userManager = new SystemServiceBinder<>("user", IUserManager.Stub::asInterface);

    private static final SystemServiceBinder<IAppOpsService> appOps = new SystemServiceBinder<>("appops", IAppOpsService.Stub::asInterface);

    @RequiresApi(30)
    private static final SystemServiceBinder<IPermissionManager> permissionManager = new SystemServiceBinder<>("permissionmgr", IPermissionManager.Stub::asInterface);

    public static int checkPermission(@Nullable String permission, int pid, int uid) throws RemoteException {
        return activityManager.get().checkPermission(permission, pid, uid);
    }

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

    public static int checkPermission(@Nullable String permName, int uid) throws RemoteException {
        if (Build.VERSION.SDK_INT != 30) {
            return packageManager.get().checkUidPermission(permName, uid);
        } else {
            return permissionManager.get().checkUidPermission(permName, uid);
        }
    }

    public static void registerProcessObserver(@Nullable IProcessObserver processObserver) throws RemoteException {
        activityManager.get().registerProcessObserver(processObserver);
    }

    public static void registerUidObserver(@Nullable IUidObserver observer, int which, int cutpoint, @Nullable String callingPackage) throws RemoteException {
        activityManager.get().registerUidObserver(observer, which, cutpoint, callingPackage);

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

    @Nullable
    public static IContentProvider getContentProviderExternal(@Nullable String name, int userId, @Nullable IBinder token, @Nullable String tag) throws RemoteException {
        IActivityManager am = activityManager.get();
        ContentProviderHolder contentProviderHolder;
        IContentProvider provider;
        if (Build.VERSION.SDK_INT >= 29) {
            contentProviderHolder = am.getContentProviderExternal(name, userId, token, tag);
            provider = contentProviderHolder != null ? contentProviderHolder.provider : null;
        } else if (Build.VERSION.SDK_INT >= 26) {
            contentProviderHolder = am.getContentProviderExternal(name, userId, token);
            provider = contentProviderHolder != null ? contentProviderHolder.provider : null;
        } else {
            provider = Refine.<IActivityManager23>unsafeCast(am).getContentProviderExternal(name, userId, token).provider;
        }

        return provider;
    }

    public static void removeContentProviderExternal(@Nullable String name, @Nullable IBinder token) throws RemoteException {
        activityManager.get().removeContentProviderExternal(name, token);
    }

    @SuppressLint("NewApi")
    @NonNull
    public static List<UserInfo> getUsers(boolean excludePartial, boolean excludeDying, boolean excludePreCreated) throws RemoteException {
        IUserManager um = userManager.get();
        List<UserInfo> list;
        if (Build.VERSION.SDK_INT >= 30) {
            list = um.getUsers(excludePartial, excludeDying, excludePreCreated);
        } else {
            try {
                list = um.getUsers(excludeDying);
            } catch (NoSuchMethodError e) {
                list = um.getUsers(excludePartial, excludeDying, excludePreCreated);
            }
        }
        return list;
    }

    public static List<UserInfo> getUsersNoThrow(boolean excludePartial, boolean excludeDying, boolean excludePreCreated) {
        List<UserInfo> result = new ArrayList<>();
        try {
            result.addAll(getUsers(excludePartial, excludeDying, excludePreCreated));
        } catch (Throwable ignored) {
        }
        return result;
    }

    public static boolean isUserUnlocked(int userId) throws RemoteException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return userManager.get().isUserUnlocked(userId);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return activityManager.get().isUserRunning(userId, ActivityManagerHidden.FLAG_AND_UNLOCKED);
        } else {
            return true;
        }
    }

    public static boolean isUserStorageAvailable(int userId) throws RemoteException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return userManager.get().isUserRunning(userId)
                    && userManager.get().isUserUnlocked(userId);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return activityManager.get().isUserRunning(userId, ActivityManagerHidden.FLAG_AND_UNLOCKED);
        } else {
            return true;
        }
    }

    @NonNull
    public static UserInfo getUserInfo(int userId) {
        IUserManager um = userManager.get();
        return um.getUserInfo(userId);
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

    @NonNull
    public static Collection<Integer> getUserIdsNoThrow() {
        return getUserIdsNoThrow(true, true, true);
    }

    public static Collection<Integer> getUserIdsNoThrow(boolean excludePartial, boolean excludeDying, boolean excludePreCreated) {
        Set<Integer> result = new ArraySet<>();
        try {
            for (UserInfo it : getUsers(excludePartial, excludeDying, excludePreCreated)) {
                result.add(it.id);
            }
        } catch (Throwable ignored) {
            result.add(0);
        }
        return result;
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

    public static void forceStopPackageNoThrow(@Nullable String packageName, int userId) {
        try {
            activityManager.get().forceStopPackage(packageName, userId);
        } catch (Exception ignored) {
        }
    }

    public static void startActivity(@Nullable Intent intent, @Nullable String mimeType, int userId) throws RemoteException {
        activityManager.get().startActivityAsUser((IApplicationThread) null, android.system.Os.getuid() == 2000 ? "com.android.shell" : null, intent, mimeType, (IBinder) null, (String) null, 0, 0, (ProfilerInfo) null, (Bundle) null, userId);
    }

    public static void startActivityNoThrow(@NonNull Intent intent, @Nullable String mimeType, int userId) {
        try {
            startActivity(intent, mimeType, userId);
        } catch (Throwable ignored) {
        }
    }

    public static void grantRuntimePermission(@Nullable String packageName, @Nullable String permissionName, int userId) throws RemoteException {
        if (Build.VERSION.SDK_INT >= 30) {
            IPermissionManager perm = permissionManager.get();
            Objects.requireNonNull(perm);
            perm.grantRuntimePermission(packageName, permissionName, userId);
        } else {
            IPackageManager pm = packageManager.get();
            Objects.requireNonNull(pm);
            pm.grantRuntimePermission(packageName, permissionName, userId);
        }
    }

    public static void revokeRuntimePermission(@Nullable String packageName, @Nullable String permissionName, int userId) throws RemoteException {
        if (Build.VERSION.SDK_INT >= 30) {
            IPermissionManager perm = permissionManager.get();
            Objects.requireNonNull(perm);

            try {
                perm.revokeRuntimePermission(packageName, permissionName, userId, (String) null);
            } catch (NoSuchMethodError e) {
                perm.revokeRuntimePermission(packageName, permissionName, userId);
            }
        } else {
            IPackageManager pm = packageManager.get();
            Objects.requireNonNull(pm);
            pm.revokeRuntimePermission(packageName, permissionName, userId);
        }
    }

    public static void broadcastIntent(
            Intent intent,
            String resolvedType, IIntentReceiver resultTo, int resultCode,
            String resultData, Bundle map, String[] requiredPermissions,
            int appOp, Bundle options, boolean serialized, boolean sticky, int userId) throws RemoteException {
        activityManager.get().broadcastIntent(null, intent, null, resultTo, 0, null, null,
                null, -1, null, true, sticky, userId);
    }

    public static void broadcastIntent(Intent intent, int userId) throws RemoteException {
        boolean sticky = intent.getComponent() == null;
        broadcastIntent(intent, null, new IntentReceiver(), 0, null, null,
                null, -1, null, true, sticky, userId);
    }

    public static int getPackageProcessState(String pkg, int userId, String callingPackage) throws RemoteException {
        IActivityManager am = activityManager.get();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return am.getUidProcessState(getPackageUid(pkg, 0, userId), callingPackage);
        } else {
            if (userId != 0) {
                // getPackageProcessState missing user id, always treat them as PROCESS_STATE_TOP
                return ActivityManagerHidden.PROCESS_STATE_TOP;
            } else {
                return am.getPackageProcessState(pkg, callingPackage);
            }
        }
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

    public static void forceStopPackage(String packageName, int userId) throws RemoteException {
        activityManager.get().forceStopPackage(packageName, userId);
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

    public static int checkPermission(String permName, String pkgName, int userId) throws RemoteException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return packageManager.get().checkPermission(permName, pkgName, userId);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return permissionManager.get().checkPermission(permName, pkgName, userId);
        } else {
            return packageManager.get().checkPermission(permName, pkgName, userId);
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

    public static int getPermissionFlags(String permissionName, String packageName, int userId) throws RemoteException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return permissionManager.get().getPermissionFlags(packageName, permissionName, userId);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return permissionManager.get().getPermissionFlags(permissionName, packageName, userId);
        } else {
            return packageManager.get().getPermissionFlags(permissionName, packageName, userId);
        }
    }

    public static void updatePermissionFlags(String permissionName, String packageName, int flagMask, int flagValues, boolean checkAdjustPolicyFlagPermission, int userId) throws RemoteException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissionManager.get().updatePermissionFlags(packageName, permissionName, flagMask, flagValues, checkAdjustPolicyFlagPermission, userId);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            permissionManager.get().updatePermissionFlags(permissionName, packageName, flagMask, flagValues, checkAdjustPolicyFlagPermission, userId);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            packageManager.get().updatePermissionFlags(permissionName, packageName, flagMask, flagValues, checkAdjustPolicyFlagPermission, userId);
        } else {
            packageManager.get().updatePermissionFlags(permissionName, packageName, flagMask, flagValues, userId);
        }
    }

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
}
