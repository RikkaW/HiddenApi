package rikka.hidden.compat;

import static rikka.hidden.compat.Services.activityManager;

import android.annotation.SuppressLint;
import android.app.ActivityManagerHidden;
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
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import dev.rikka.tools.refine.Refine;
import rikka.hidden.compat.adapter.IntentReceiver;

@SuppressWarnings("unused")
public class ActivityService {

    public static int checkPermission(@Nullable String permission, int pid, int uid) throws RemoteException {
        return activityManager.get().checkPermission(permission, pid, uid);
    }

    public static void registerProcessObserver(@Nullable IProcessObserver processObserver) throws RemoteException {
        activityManager.get().registerProcessObserver(processObserver);
    }

    public static void unregisterProcessObserver(@Nullable IProcessObserver observer) throws RemoteException {
        activityManager.get().unregisterProcessObserver(observer);
    }

    public static void registerUidObserver(@Nullable IUidObserver observer, int which, int cutpoint, @Nullable String callingPackage) throws RemoteException {
        activityManager.get().registerUidObserver(observer, which, cutpoint, callingPackage);
    }

    public static void unregisterUidObserver(@Nullable IUidObserver observer) throws RemoteException {
        activityManager.get().unregisterUidObserver(observer);
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

    @SuppressLint("UnsafeOptInUsageError")
    public static int getPackageProcessState(String pkg, int userId, String callingPackage) throws RemoteException {
        IActivityManager am = activityManager.get();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return am.getUidProcessState(PackageService.getPackageUid(pkg, 0, userId), callingPackage);
        } else {
            if (userId != 0) {
                // getPackageProcessState missing user id, always treat them as PROCESS_STATE_TOP
                return ActivityManagerHidden.PROCESS_STATE_TOP;
            } else {
                return am.getPackageProcessState(pkg, callingPackage);
            }
        }
    }

    public static void forceStopPackage(String packageName, int userId) throws RemoteException {
        activityManager.get().forceStopPackage(packageName, userId);
    }

}
