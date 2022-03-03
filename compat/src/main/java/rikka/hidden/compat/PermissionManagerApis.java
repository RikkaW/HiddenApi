package rikka.hidden.compat;

import static rikka.hidden.compat.Services.packageManager;
import static rikka.hidden.compat.Services.permissionManager;

import android.content.pm.IPackageManager;
import android.os.Build;
import android.os.RemoteException;
import android.permission.IPermissionManager;

import androidx.annotation.Nullable;

import java.util.Objects;

@SuppressWarnings("unused")
public class PermissionManagerApis {

    public static int checkPermission(@Nullable String permName, int uid) throws RemoteException {
        if (Build.VERSION.SDK_INT != 30) {
            return packageManager.get().checkUidPermission(permName, uid);
        } else {
            return permissionManager.get().checkUidPermission(permName, uid);
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


    public static int checkPermission(String permName, String pkgName, int userId) throws RemoteException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return packageManager.get().checkPermission(permName, pkgName, userId);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return permissionManager.get().checkPermission(permName, pkgName, userId);
        } else {
            return packageManager.get().checkPermission(permName, pkgName, userId);
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
}
