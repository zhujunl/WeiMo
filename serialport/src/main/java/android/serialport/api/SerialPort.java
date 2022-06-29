package android.serialport.api;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;

public class SerialPort {

    static {
        System.loadLibrary("SerialPort");
    }

    private static final String TAG = SerialPort.class.getSimpleName();
    FileDescriptor mFd;

    /**
     * @param path 串口文件路径
     * @param baudrate 波特率，不同设备波特率有区别
     * */
    public SerialPort(String path, int baudrate) throws SecurityException, IOException {
        File device = new File(path);
        if(!device.canRead() || !device.canWrite()) {
            try {
                Process su = Runtime.getRuntime().exec("/system/bin/su");
                String cmd = "chmod 777 " + device.getAbsolutePath() + "\n"
                        + "exit\n";
                su.getOutputStream().write(cmd.getBytes());
                if ((su.waitFor() != 0) || !device.canRead()
                        || !device.canWrite()) {
                    throw new SecurityException();
                }
            } catch (Exception e) {
                e.getMessage();
            }
        }
        mFd = open(device.getAbsolutePath(), baudrate,0);
        if (mFd == null) {
            throw new IOException();
        }
    }

    public FileDescriptor getFd() {
        return mFd;
    }

    // 打开串口
    public native FileDescriptor open(String path, int baudRate, int flags);

    // 关闭串口
    public native void close();
}
