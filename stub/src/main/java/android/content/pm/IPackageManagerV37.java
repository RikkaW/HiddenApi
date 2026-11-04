package android.content.pm;

import android.os.Build;
import android.os.RemoteException;

import androidx.annotation.RequiresApi;

import dev.rikka.tools.refine.RefineAs;

@RefineAs(IPackageManager.class)
public interface IPackageManagerV37 {

    @RequiresApi(Build.VERSION_CODES.CINNAMON_BUN)
    PackageInfoList getInstalledPackages(long flags, int userId)
            throws RemoteException;
}