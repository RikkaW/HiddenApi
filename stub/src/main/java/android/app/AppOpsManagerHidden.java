package android.app;

import android.util.LongSparseLongArray;

import java.util.List;

import dev.rikka.tools.refine.RefineAs;

@RefineAs(AppOpsManager.class)
public class AppOpsManagerHidden {

    public static int permissionToOpCode(String permission) {
        throw new RuntimeException("STUB");
    }

    public static class PackageOps {

        public String getPackageName() {
            throw new RuntimeException("STUB");
        }

        public int getUid() {
            throw new RuntimeException("STUB");
        }

        public List<OpEntry> getOps() {
            throw new RuntimeException("STUB");
        }
    }

    public static class OpEntry {

        public int getOp() {
            throw new RuntimeException("STUB");
        }

        public int getMode() {
            throw new RuntimeException("STUB");
        }

        public long getTime() {
            throw new RuntimeException("STUB");
        }

        public long getRejectTime() {
            throw new RuntimeException("STUB");
        }

        public boolean isRunning() {
            throw new RuntimeException("STUB");
        }

        public int getDuration() {
            throw new RuntimeException("STUB");
        }

        public int getProxyUid() {
            throw new RuntimeException("STUB");
        }

        public String getProxyPackageName() {
            throw new RuntimeException("STUB");
        }
    }

    public static final class HistoricalOps {

        public long getBeginTimeMillis() {
            throw new RuntimeException("STUB");
        }

        public long getEndTimeMillis() {
            throw new RuntimeException("STUB");
        }

        public int getUidCount() {
            throw new RuntimeException("STUB");
        }

        public HistoricalUidOps getUidOpsAt(int index) {
            throw new RuntimeException("STUB");
        }
    }

    public static final class HistoricalUidOps {

        public int getUid() {
            throw new RuntimeException("STUB");
        }

        public int getPackageCount() {
            throw new RuntimeException("STUB");
        }

        public HistoricalPackageOps getPackageOpsAt(int index) {
            throw new RuntimeException("STUB");
        }
    }

    public static final class HistoricalPackageOps {

        public String getPackageName() {
            throw new RuntimeException("STUB");
        }

        public int getOpCount() {
            throw new RuntimeException("STUB");
        }

        public HistoricalOp getOpAt(int index) {
            throw new RuntimeException("STUB");
        }
    }

    public static final class HistoricalOp {

        public int getOpCode() {
            throw new RuntimeException("STUB");
        }

        private LongSparseLongArray getOrCreateAccessCount() {
            throw new RuntimeException("STUB");
        }

        private LongSparseLongArray getOrCreateRejectCount() {
            throw new RuntimeException("STUB");
        }

        private LongSparseLongArray getOrCreateAccessDuration() {
            throw new RuntimeException("STUB");
        }
    }
}
