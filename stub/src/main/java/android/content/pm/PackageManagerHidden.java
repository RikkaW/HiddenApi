package android.content.pm;

import java.util.List;

import androidx.annotation.RequiresApi;

import dev.rikka.tools.refine.RefineAs;

@RefineAs(PackageManager.class)
public class PackageManagerHidden {

    public static int MATCH_UNINSTALLED_PACKAGES;
    public static int INSTALL_REPLACE_EXISTING;
    public static int INSTALL_ALLOW_TEST;

    @RequiresApi(34)
    public static int INSTALL_BYPASS_LOW_TARGET_SDK_BLOCK;

    public ApplicationInfo getApplicationInfoAsUser(String packageName, int flags, int userId) throws PackageManager.NameNotFoundException {
        throw new RuntimeException();
    }

    public PackageInfo getPackageInfoAsUser(String packageName, int flags, int userId) throws PackageManager.NameNotFoundException {
        throw new RuntimeException();
    }

    public List<ApplicationInfo> getInstalledApplicationsAsUser(int flags, int userId) {
        throw new RuntimeException();
    }

    public List<PackageInfo> getInstalledPackagesAsUser(int flags, int userId) {
        throw new RuntimeException();
    }
}
