package xposed.com.xposed;

import java.net.URL;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

//web.loadUrl("http://1.1.1.0", param);web.loadUrl("http://1.1.1.1/");web.postUrl("","");

//DatagramSocket.send(packet) 发送
//DatagramSocket DatagramSocket.bind DatagramSocket.createSocket 监听

//startupSocket对应多个socket构造函数  sock.connect
//ServerSocket ServerSocket.bind

//SocketChannel.open(addr)  ?SocketChannel.connect?

//outputstream.write(byte[],int,int)

//httpclient.execute(post);
//urls.openConnection(); urls.openConnection(proxy);  ?urls.setRequestProperty?


//辅助记录，避免出现调用其他包进行监听 logcat | grep xuhu >>/sdcard/a.txt

public class Test implements IXposedHookLoadPackage{

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if (!loadPackageParam.packageName.equals("com.gswifi.gshelper")) return;

        XposedBridge.log("Loaded app: " + loadPackageParam.packageName);

        XposedHelpers.findAndHookMethod(java.net.URL.class, "openConnection", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param)
                    throws Throwable {
                URL url = (URL) param.thisObject;
                XposedBridge.log("url: " + url);
                super.beforeHookedMethod(param);
            }
        });

        XposedHelpers.findAndHookMethod("java.io.OutputStream", loadPackageParam.classLoader, "write", byte[].class, int.class, int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param)
                    throws Throwable {
                byte[] d = (byte[])param.args[0];
                XposedBridge.log("socketdata: " + new String(d));
                super.beforeHookedMethod(param);
            }
        });
        XposedHelpers.findAndHookMethod("java.io.OutputStream", loadPackageParam.classLoader, "write", byte[].class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param)
                    throws Throwable {
                byte[] d = (byte[])param.args[0];
                XposedBridge.log("socketdata1: " + new String(d));
                super.beforeHookedMethod(param);
            }
        });
    }
}
