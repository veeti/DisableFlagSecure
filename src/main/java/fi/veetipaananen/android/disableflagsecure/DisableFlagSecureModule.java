package fi.veetipaananen.android.disableflagsecure;

import android.os.Build;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class DisableFlagSecureModule implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        XposedHelpers.findAndHookMethod(Window.class, "setFlags", int.class, int.class,
                mRemoveSecureFlagHook);
        if (Build.VERSION.SDK_INT >= 17) {
            XposedHelpers.findAndHookMethod(SurfaceView.class, "setSecure", boolean.class,
                    mRemoveSetSecureHook);
        }
    }

    private final XC_MethodHook mRemoveSecureFlagHook = new XC_MethodHook() {
        @Override
        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            Integer flags = (Integer) param.args[0];
            flags &= ~WindowManager.LayoutParams.FLAG_SECURE;
            param.args[0] = flags;
        }
    };

    private final XC_MethodHook mRemoveSetSecureHook = new XC_MethodHook() {
        @Override
        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            param.args[0] = false;
        }
    };

}
