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

    /**
     * 文件设置最高权限 777 可读 可写 可执行
     *
     * @param file 文件
     * @return 权限修改是否成功
     */
    public boolean chmod777(File file) {
        if (null == file || !file.exists()) {
            // 文件不存在
            return false;
        }
        try {
            // 获取ROOT权限
            Process su = Runtime.getRuntime().exec("/system/bin/su");
            // 修改文件属性为 [可读 可写 可执行]
            String cmd = "chmod 777 " + file.getAbsolutePath() + "\n" + "exit\n";
            su.getOutputStream().write(cmd.getBytes());
            if (0 == su.waitFor() && file.canRead() && file.canWrite() && file.canExecute()) {
                return true;
            }
        } catch (IOException | InterruptedException e) {
            // 没有ROOT权限
            e.printStackTrace();
        }
        return false;
    }

    public FileDescriptor getFd() {
        return mFd;
    }

    // 打开串口
    public native FileDescriptor open(String path, int baudRate, int flags);

    // 关闭串口
    public native void close();
}
