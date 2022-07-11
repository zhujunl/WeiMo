package android.serialport.api;

import android.os.SystemClock;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author ZJL
 * @date 2022/6/22 9:30
 * @des
 * @updateAuthor
 * @updateDes
 */
public class SerialPortManager {
    private final String TAG="SerialPortManager";

    //串口路径
    private String path = "/dev/ttyMT0";
    //设置波特率
    private int baudrate = 115200;
    public boolean serialPortStatus = false; //是否打开串口标志
    public byte[] data=null;
    public boolean threadStatus; //线程状态，为了安全终止线程
    public int Size=0;

    public SerialPort serialPort = null;
    public InputStream inputStream = null;
    public OutputStream outputStream = null;
    public double timeout=1.00;


    /**
     * 打开串口
     * @return serialPort串口对象
     */
    public SerialPort openSerialPort(String path){
        try {
            serialPort = new SerialPort(path, 115200);
            this.serialPortStatus = true;
            threadStatus = false; //线程状态
            outputStream = new FileOutputStream(serialPort.getFd());
            Log.e(TAG, "openSerialPort: 打开串口");
            return serialPort;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 关闭串口
     */
    public void closeSerialPort(){
        try {
            if (inputStream!=null)
                inputStream.close();
            if (outputStream!=null)
                outputStream.close();

            this.serialPortStatus = false;
            this.threadStatus = true; //线程状态
            if (serialPort!=null)
                serialPort.close();
        } catch (IOException e) {
            Log.e(TAG, "closeSerialPort: 关闭串口异常："+e.toString());
            return;
        }
        Log.e(TAG, "closeSerialPort: 关闭串口成功");
    }

    /**
     * 发送串口指令
     * @param senddata String数据指令
     */
    public int sendSerialPort(byte[] senddata,byte[] recvdata){
        try {
            inputStream = new FileInputStream(serialPort.getFd());
            if (senddata.length > 0) {
                Log.e(TAG, "send:" +zzStringTrans.hex2str(senddata) );
                outputStream.flush();
                outputStream.write(senddata);
                outputStream.flush();
                int size; //读取数据的大小
                int count=0;
                double time=0D;
                try {
                    while (count==0){
                        count=inputStream.available();
                        SystemClock.sleep(100);
                        time=time+100*0.001;
                        if (time>timeout){
                            return ConStant.ERRCODE_TIMEOUT;
                        }
                    }
                    size = inputStream.read(recvdata);
//                    Log.e(TAG, "size = " + size);
                    if (size > 0){
                        Log.e(TAG, "buffer:" + zzStringTrans.hex2str(recvdata) );
                    }
                    return size;
                } catch (IOException e) {
                    Log.e(TAG, "run: 数据读取异常：" +e.toString());
                    return ConStant.ERRCODE_TRANS;
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "sendSerialPort: 串口数据发送失败："+e.toString());
            return ConStant.ERRCODE_TRANS;
        }
        return ConStant.ERRCODE_TRANS;
    }


    public int sendSerialPort(byte[] senddata,byte[] recvdata,int i){
        try {
            inputStream = new FileInputStream(serialPort.getFd());
            if (senddata.length > 0) {
                Log.e(TAG, "send:" +zzStringTrans.hex2str(senddata) );
                outputStream.flush();
                outputStream.write(senddata);
                outputStream.flush();
                int size; //读取数据的大小
                int count=0;
                double time=0D;
                try {
                    while (count==0){
                        count=inputStream.available();
                        SystemClock.sleep(100);
                        time=time+100*0.001;
                        if (time>timeout){
                            return ConStant.ERRCODE_TIMEOUT;
                        }
                    }
                    size = inputStream.read(recvdata);
                    Log.e(TAG, "size = " + size);
                    if (size > 0){
                        Log.e(TAG, "buffer:" + zzStringTrans.hex2str(recvdata) );
                    }
                    Log.e(TAG, "start_inputStream.available()==" + inputStream.available());
                    while (inputStream.available() >0){
                        byte[] bb=new byte[inputStream.available()];
                        inputStream.read(bb);
                        System.arraycopy(bb,0,recvdata,size,bb.length);
                        size+=bb.length;
                        SystemClock.sleep(100);
                    }
                    Log.e(TAG, "end_inputStream.available()==" + inputStream.available());

                    return size;
                } catch (IOException e) {
                    Log.e(TAG, "run: 数据读取异常：" +e.toString());
                    return ConStant.ERRCODE_TRANS;
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "sendSerialPort: 串口数据发送失败："+e.toString());
            return ConStant.ERRCODE_TRANS;
        }
        return ConStant.ERRCODE_TRANS;
    }
    /**
     * 单开一线程，来读数据
     */
    private class ReadThread extends Thread{
        @Override
        public void run() {
            super.run();
            //判断进程是否在运行，更安全的结束进程
            while (!threadStatus) {
                try {
                    if (inputStream.available() > 0) {
                        //当接收到数据时，sleep 500毫秒（sleep时间自己把握）
                        Thread.sleep(500);
                        //sleep过后，再读取数据，基本上都是完整的数据
                        byte[] buffer = new byte[inputStream.available()];
                        int size = inputStream.read(buffer);
                        Size=size;
                        data=buffer;
                        Log.e(TAG, "size = " + size);
                        Log.e(TAG, "buffer:" + zzStringTrans.hex2str(buffer) );
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public double getTimeout() {
        return timeout;
    }

    public void setTimeout(double timeout) {
        this.timeout = timeout;
    }
}
