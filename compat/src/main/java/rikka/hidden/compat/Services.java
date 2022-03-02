package rikka.hidden.compat;

import android.app.ActivityManagerNative;
import android.app.IActivityManager;
import android.content.pm.IPackageManager;
import android.os.Build;
import android.os.IUserManager;
import android.permission.IPermissionManager;

import com.android.internal.app.IAppOpsService;

import rikka.hidden.compat.util.SystemServiceBinder;

class Services {

    protected static final SystemServiceBinder<IAppOpsService> appOps;
    protected static final SystemServiceBinder<IActivityManager> activityManager;
    protected static final SystemServiceBinder<IUserManager> userManager;
    protected static final SystemServiceBinder<IPackageManager> packageManager;
    protected static final SystemServiceBinder<IPermissionManager> permissionManager;

    static {
        appOps = new SystemServiceBinder<>(
                "appops", IAppOpsService.Stub::asInterface);

        activityManager = new SystemServiceBinder<>("activity", binder -> {
            if (Build.VERSION.SDK_INT >= 26) {
                return IActivityManager.Stub.asInterface(binder);
            } else {
                return ActivityManagerNative.asInterface(binder);
            }
        });

        userManager = new SystemServiceBinder<>(
                "user", IUserManager.Stub::asInterface);

        packageManager = new SystemServiceBinder<>(
                "package", IPackageManager.Stub::asInterface);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            permissionManager = new SystemServiceBinder<>(
                    "permissionmgr", IPermissionManager.Stub::asInterface);
        } else {
            permissionManager = null;
        }
    }
}
