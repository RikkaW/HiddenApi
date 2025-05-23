package stub.dalvik.system;

import java.util.function.Consumer;

import dalvik.system.VMRuntime;
import dev.rikka.tools.refine.RefineAs;

@RefineAs(VMRuntime.class)
public class VMRuntimeHidden {

    public static native VMRuntimeHidden getRuntime();

    // Use `Process.is64Bit()` instead
    public native boolean is64Bit();

    public native String vmInstructionSet();

    public native void setHiddenApiExemptions(String[] signaturePrefixes);

    public native boolean isNativeDebuggable();

    public native boolean isJavaDebuggable();

    public static native String getInstructionSet(String abi);

    public static native void setNonSdkApiUsageConsumer(Consumer<String> consumer);

    public static native void setDedupeHiddenApiWarnings(boolean dedupe);
}
