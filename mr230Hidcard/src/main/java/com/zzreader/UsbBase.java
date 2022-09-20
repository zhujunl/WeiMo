package com.zzreader;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.HashMap;

public class UsbBase
{
    public static final int ERRCODE_SUCCESS = 0;
    public static final int ERRCODE_NODEVICE = -100;
    public static final int ERRCODE_MEMORY_OVER = -101;
    public static final int ERRCODE_NO_PERMISION = -1000;
    public static final int ERRCODE_NO_CONTEXT = -1001;
    public static final int SHOW_MSG = 255;
    private int m_iSendPackageSize;
    private int m_iRecvPackageSize;
    private UsbDevice m_usbDevice;
    private UsbInterface m_usbInterface;
    private UsbEndpoint m_inEndpoint;
    private UsbEndpoint m_outEndpoint;
    private UsbDeviceConnection m_connection;
    private Context m_ctx;
    private Handler m_fHandler;
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private final BroadcastReceiver mUsbReceiver;
//    byte[] bRecvBufTmp = new byte[512];
    
    public void SendMsg(final String obj) {
        if (ConStant.DEBUG) {
            final Message message = new Message();
            message.what = ConStant.SHOW_MSG;
            message.obj = obj;
            message.arg1 = 0;
            if (this.m_fHandler != null) {
                this.m_fHandler.sendMessage(message);
            }
        }
    }
    
    public UsbBase(final Context context) {
        this.m_iSendPackageSize = 0;
        this.m_iRecvPackageSize = 0;
        this.m_usbDevice = null;
        this.m_usbInterface = null;
        this.m_inEndpoint = null;
        this.m_outEndpoint = null;
        this.m_connection = null;
        this.m_ctx = null;
        this.m_fHandler = null;
        this.mUsbReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                final String action = intent.getAction();
                if ("com.android.example.USB_PERMISSION".equals(action)) {
                    synchronized (this) {
                        final UsbDevice device = (UsbDevice)intent.getParcelableExtra("device");
                        if (!intent.getBooleanExtra("permission", false)) {
                            Log.d("MIAXIS", "permission denied for device " + device);
                        }
                    }
                }
                if ("android.hardware.usb.action.USB_DEVICE_DETACHED".equals(action)) {
                    final UsbDevice device2 = (UsbDevice)intent.getParcelableExtra("device");
                    if (device2 != null) {
                        UsbBase.this.m_connection.releaseInterface(UsbBase.this.m_usbInterface);
                        UsbBase.this.m_connection.close();
                    }
                }
            }
        };
        this.m_ctx = context;
        this.m_fHandler = null;
        this.regUsbMonitor();
    }
    
    public UsbBase(final Context context, final Handler bioHandler) {
        this.m_iSendPackageSize = 0;
        this.m_iRecvPackageSize = 0;
        this.m_usbDevice = null;
        this.m_usbInterface = null;
        this.m_inEndpoint = null;
        this.m_outEndpoint = null;
        this.m_connection = null;
        this.m_ctx = null;
        this.m_fHandler = null;
        this.mUsbReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                final String action = intent.getAction();
                if ("com.android.example.USB_PERMISSION".equals(action)) {
                    synchronized (this) {
                        final UsbDevice device = (UsbDevice)intent.getParcelableExtra("device");
                        if (!intent.getBooleanExtra("permission", false)) {
                            Log.d("MIAXIS", "permission denied for device " + device);
                        }
                    }
                }
                if ("android.hardware.usb.action.USB_DEVICE_DETACHED".equals(action)) {
                    final UsbDevice device2 = (UsbDevice)intent.getParcelableExtra("device");
                    if (device2 != null) {
                        UsbBase.this.m_connection.releaseInterface(UsbBase.this.m_usbInterface);
                        UsbBase.this.m_connection.close();
                    }
                }
            }
        };
        this.m_ctx = context;
        this.m_fHandler = bioHandler;
        this.regUsbMonitor();
    }
    
    public int getDevNum(final int vid, final int pid) {
        if (this.m_ctx == null) {
            return -1001;
        }
        int iDevNum = 0;
        final UsbManager usbManager = (UsbManager)this.m_ctx.getSystemService("usb");
        final HashMap<String, UsbDevice> map = (HashMap<String, UsbDevice>)usbManager.getDeviceList();
        for (final UsbDevice device : map.values()) {
            if (!usbManager.hasPermission(device)) {
                final PendingIntent pi = PendingIntent.getBroadcast(this.m_ctx, 0, new Intent("com.android.example.USB_PERMISSION"), 0);
                usbManager.requestPermission(device, pi);
                return -1000;
            }
            if (vid != device.getVendorId() || pid != device.getProductId()) {
                continue;
            }
            ++iDevNum;
        }
        return iDevNum;
    }
    
    public int openDev(final int vid, final int pid) {
        if (this.m_ctx == null) {
            return -1001;
        }
        final UsbManager usbManager = (UsbManager)this.m_ctx.getSystemService("usb");
        final HashMap<String, UsbDevice> map = (HashMap<String, UsbDevice>)usbManager.getDeviceList();
        for (final UsbDevice device : map.values()) {
            if (vid == device.getVendorId() && pid == device.getProductId()) {
                if (!usbManager.hasPermission(device)) {
                    final PendingIntent pi = PendingIntent.getBroadcast(this.m_ctx, 0, new Intent("com.android.example.USB_PERMISSION"), 0);
                    usbManager.requestPermission(device, pi);
                    return ConStant.ERRCODE_DEVICE;
                }
                this.m_usbDevice = device;
                this.m_usbInterface = this.m_usbDevice.getInterface(0);
                this.m_inEndpoint = this.m_usbInterface.getEndpoint(0);
                this.m_outEndpoint = this.m_usbInterface.getEndpoint(1);
                this.m_connection = usbManager.openDevice(this.m_usbDevice);
                this.m_connection.claimInterface(this.m_usbInterface, true);
                this.m_iSendPackageSize = this.m_outEndpoint.getMaxPacketSize();
                this.m_iRecvPackageSize = this.m_inEndpoint.getMaxPacketSize();
                return 0;
            }

//            if (vid == device.getVendorId() && pid == device.getProductId()) {
//                if (!usbManager.hasPermission(device)) {
//                    final PendingIntent pi = PendingIntent.getBroadcast(this.m_ctx, 0, new Intent("com.android.example.USB_PERMISSION"), 0);
//                    usbManager.requestPermission(device, pi);
//                    return ConStant.ERRCODE_DEVICE;
//                }
//                this.m_usbDevice = device;
//                this.m_usbInterface = this.m_usbDevice.getInterface(0);
//                this.m_inEndpoint = this.m_usbInterface.getEndpoint(0);
//                this.m_outEndpoint = this.m_usbInterface.getEndpoint(1);
//                this.m_connection = usbManager.openDevice(this.m_usbDevice);
//                this.m_connection.claimInterface(this.m_usbInterface, true);
//                this.m_iSendPackageSize = this.m_outEndpoint.getMaxPacketSize();
//                this.m_iRecvPackageSize = this.m_inEndpoint.getMaxPacketSize();
//                return 0;
//            }
        }
        return ConStant.ERRCODE_DEVICE;
    }
    
    public int sendPacketSize() {
        return this.m_iSendPackageSize;
    }
    
    public int recvPacketSize() {
        return this.m_iRecvPackageSize;
    }


    private final String TAG="UsbBase";
    public int sendData(final byte[] bSendBuf, final int iSendLen, final int iTimeOut) {
        int iRV = -1;
        if (iSendLen > bSendBuf.length) {
            return -101;
        }
        iRV=this.m_connection.controlTransfer( 0x21, 0x09, 0x200, 0, bSendBuf, iSendLen, iTimeOut);
//         Log.e("Usb——", "==========================================" );
//         Log.d("Usb——sendData:",zzStringTrans.hex2str(bSendBuf));
//         Log.e("Usb——sendData_iRV:",""+iRV);
        return iRV;
    }

    public int recvData(byte[] bRecvBuf, final int iRecvLen, final int iTimeOut) {
        int iRV = -1;
        if (iRecvLen > bRecvBuf.length) {
            return -101;
        }
         iRV=this.m_connection.controlTransfer( 0xA1, 0x01, 0x100, 0, bRecvBuf, iRecvLen, iTimeOut);
//         Log.d("Usb——recvData:",zzStringTrans.hex2str(bRecvBuf));
//        Log.e("Usb——recvData", "iRV==" +iRV );
//         //Log.e("iRecvLen:",""+iRecvLen);
        return iRV;
    }
    
    public int closeDev() {
        if (this.m_connection != null) {
            this.m_connection.releaseInterface(this.m_usbInterface);
            this.m_connection.close();
            this.m_connection = null;
        }
        return 0;
    }
    
    private void regUsbMonitor() {
        final IntentFilter filter = new IntentFilter("com.android.example.USB_PERMISSION");
        this.m_ctx.registerReceiver(this.mUsbReceiver, filter);
    }

    public UsbDeviceConnection getM_connection() {
        return m_connection;
    }
}
