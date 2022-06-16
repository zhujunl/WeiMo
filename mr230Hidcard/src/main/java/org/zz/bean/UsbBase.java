package org.zz.bean;

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
import android.util.Log;

import java.util.Calendar;
import java.util.HashMap;

/**
 * @author  chen.gs
 * @version V1.0.0.0 2015-02-10  封装Android USB-Host API	
 * */
public class UsbBase {
	
	private int m_iSendPackageSize = 0;
	private int m_iRecvPackageSize = 0;
	
	private UsbDevice    m_usbDevice  		     = null;
	private UsbInterface m_usbInterface 		 = null;
	private UsbEndpoint  m_inEndpoint 			 = null;        //读数据节点
	private UsbEndpoint  m_outEndpoint 			 = null;        //写数据节点
	private UsbDeviceConnection m_connection     = null;
	
	private Context m_ctx          = null;
	private Handler m_fHandler     = null;
	/**
	 * 功	能：构造函数
	 * 参	数：context - 应用上下文
	 * 返	回：
	 * */
	public UsbBase(Context context){
		m_ctx      = context;
		m_fHandler = null;	
		//注册监听
		regUsbMonitor();
	}
	
	public UsbBase(Context context, Handler bioHandler){
		m_ctx      = context;
		m_fHandler = bioHandler;	
//		MXLog.SetHandler(bioHandler);
		//注册监听
		regUsbMonitor();
	}
	
	
	/**
	 * 功	能：获取连接设备属性
	 * 参	数：
	 * 返	回： 设备属性信息
	 * */
	public String getDevAttribute(){
		if(m_ctx == null){
			return null;
		}
		int iDevNum = 0; 
		String strDevInfo = "";
		String strTmp=null;
		UsbManager usbManager = (UsbManager)m_ctx.getSystemService(Context.USB_SERVICE);
		HashMap<String, UsbDevice> map = usbManager.getDeviceList();
		for (UsbDevice device : map.values()) {	
			if (usbManager.hasPermission(device)) {
				iDevNum++;
				strTmp = "describeContents:"+device.describeContents();
				strDevInfo = strDevInfo+strTmp+"\r\n";
				strTmp = "DeviceProtocol:"+device.getDeviceProtocol();
				strDevInfo = strDevInfo+strTmp+"\r\n";
				strTmp = "DeviceClass:"+device.getDeviceClass();
				strDevInfo = strDevInfo+strTmp+"\r\n";	
				strTmp = "DeviceSubclass:"+device.getDeviceSubclass();
				strDevInfo = strDevInfo+strTmp+"\r\n";
				strTmp = "InterfaceCount:"+device.getInterfaceCount();
				strDevInfo = strDevInfo+strTmp+"\r\n";				
				strTmp = "DeviceId:"+device.getDeviceId();
				strDevInfo = strDevInfo+strTmp+"\r\n";
				strTmp = "DeviceName:"+device.getDeviceName();
				strDevInfo = strDevInfo+strTmp+"\r\n";
				strTmp = "VendorId:"+device.getVendorId();
				strDevInfo = strDevInfo+strTmp+"\r\n";
				strTmp = "ProductId:"+device.getProductId();
				strDevInfo = strDevInfo+strTmp+"\r\n";
				
				//MXLog.SendMsg("Device\r\n" + strDevInfo);
			}
			else{
				//没有权限询问用户是否授予权限
				PendingIntent pi = PendingIntent.getBroadcast(m_ctx, 0, new Intent(
						ACTION_USB_PERMISSION), 0);
				// 该代码执行后，系统弹出一个对话框，询问用户是否授予程序操作USB设备的权限
				usbManager.requestPermission(device, pi); 
				return null;
			}
		}
		return strDevInfo;
	}
	
	/**
	 * 功	能：	根据VID和PID，获取连接设备个数
	 * 参	数：	vid 	- 	VendorId，十进制
	 * 				pid	-	ProductId，十进制
	 * 返	回：  >=0	-	设备个数，<0	-	失败
	 * */
	public int getDevNum(int vid,int pid){
		if(m_ctx == null){
			return MXErrCode.ERR_NO_CONTEXT;
		}
		int iDevNum = 0; 
		UsbManager usbManager = (UsbManager)m_ctx.getSystemService(Context.USB_SERVICE);
		HashMap<String, UsbDevice> map = usbManager.getDeviceList();
		for (UsbDevice device : map.values()) {	
			if (usbManager.hasPermission(device)) {
				if ((vid == device.getVendorId()) && (pid==device.getProductId())) {
					iDevNum++;
				}
			}
			else{
				//没有权限询问用户是否授予权限
				PendingIntent pi = PendingIntent.getBroadcast(m_ctx, 0, new Intent(
						ACTION_USB_PERMISSION), 0);
				// 该代码执行后，系统弹出一个对话框，询问用户是否授予程序操作USB设备的权限
				usbManager.requestPermission(device, pi); 
				return MXErrCode.ERR_NO_PERMISION;
			}
		}
		return iDevNum;
	}
	
	public int openDev(int vid,int pid)
	{
		int iCount = 0;
		int nRet = openDev_In(vid,pid);
		while(nRet == MXErrCode.ERR_NO_PERMISION)
		{
			iCount++;
			MySleep(200);
			nRet = openDev_In(vid,pid);
			if(iCount>5)
				break;
		}
		return nRet;
	}
	/**
	 * 功	能：	根据VID和PID，打开设备 
	 * 参	数：	vid - 	VendorId，十进制
	 * 			pid	-	ProductId，十进制
	 * 返	回：  0	-	成功，其他	-	失败
	 * */
	public int openDev_In(int vid, int pid) {
		//MXLog.SendMsg("openDev");
		if (m_ctx == null) {
//			MXLog.SendMsg("ERRCODE_NO_CONTEXT");
			return MXErrCode.ERR_NO_CONTEXT;
		}

//		MXLog.SendMsg(String.format("vid: 0x%x \t pid: 0x%x", vid, pid));
		UsbManager usbManager = (UsbManager) m_ctx.getSystemService(Context.USB_SERVICE);
		HashMap<String, UsbDevice> map = usbManager.getDeviceList();
		for (UsbDevice device : map.values()) {
			if ((vid == device.getVendorId()) && (pid == device.getProductId())) {
				if (usbManager.hasPermission(device)) {
					//SendMsg("++++++++++++++++++++++++++++++++++++++++++++");
					//MXLog.SendMsg("dName: " + device.getDeviceName());
					//MXLog.SendMsg("vid: " + device.getVendorId() + "\t pid: "+ device.getProductId());
					m_usbDevice = device;
					m_usbInterface = m_usbDevice.getInterface(0);
					//USBEndpoint为读写数据所需的节点
					m_inEndpoint = m_usbInterface.getEndpoint(0);  //读数据节点
					m_outEndpoint = m_usbInterface.getEndpoint(1);  //写数据节点;
					m_connection = usbManager.openDevice(m_usbDevice);
					m_connection.claimInterface(m_usbInterface, true);
					m_iSendPackageSize = m_outEndpoint.getMaxPacketSize();
					m_iRecvPackageSize = m_inEndpoint.getMaxPacketSize();
					//MXLog.SendMsg("-------------------------------------------");
					//MXLog.SendMsg("SendPackageSize: " + m_iSendPackageSize+",RecvPackageSize: " + m_iRecvPackageSize);
					return 0;
				} else {
					//没有权限询问用户是否授予权限
					PendingIntent pi = PendingIntent.getBroadcast(m_ctx, 0, new Intent(
							ACTION_USB_PERMISSION), 0);
					// 该代码执行后，系统弹出一个对话框，询问用户是否授予程序操作USB设备的权限
					usbManager.requestPermission(device, pi);
					return MXErrCode.ERR_NO_PERMISION;
				}
			}
		}
		return MXErrCode.ERR_NO_DEV;
	}
	
	public int sendPacketSize()
	{
		return m_iSendPackageSize;
	}
	
	public int recvPacketSize()
	{
		return m_iRecvPackageSize;
	}
	
	/**
	 * 功	能：	清空缓存区
	 * 参	数：
	 * 返	回： 
	 * */
	public int clearBuffer(){
		int iRV = 0;
		int iEndpointSize   = recvPacketSize();
		byte[] bRecvBufTmp = new byte[iEndpointSize];
		while(true)
		{
			iRV= m_connection.bulkTransfer(m_inEndpoint, bRecvBufTmp, iEndpointSize, 5);
			if(iRV < 0)
			{
				//MXLog.SendMsg("clearBuffer iRV="+iRV);
				return MXErrCode.ERR_OK;
			}
			//MXLog.SendMsg("clearBuffer iRV="+iRV);
		}
	}
	
	/**
	 * 	public int bulkTransfer (UsbEndpoint endpoint, byte[] buffer, int length, int timeout)
		功	能：	Performs a bulk transaction on the given endpoint. 
				  	The direction of the transfer is determined by the direction of the endpoint
		参	数：	endpoint	the endpoint for this transaction
					buffer	buffer for data to send or receive,
					length	the length of the data to send or receive
					timeout	in milliseconds
		返	回：	length of data transferred (or zero) for success, or negative value for failure
	 * */
	/**
	 * 功	能：	发送数据
	 * 参	数：	bSendBuf 	- 	待发送数据缓存
	 * 				iSendLen   -  待发送数据长度
	 * 				iTimeOut	-	超时时间，单位：毫秒
	 * 返	回：  >=0	-	成功（实际发送数据长度），<0	-	失败
	 * */
	public int sendData(byte[] bSendBuf,int iSendLen,int iTimeOut){		
		int iRV = -1;
		if (iSendLen > bSendBuf.length) {
			return MXErrCode.ERR_MEMORY_OVER;
		}
		int iPackageSize   = sendPacketSize();
		if (iSendLen > iPackageSize) {
			return MXErrCode.ERR_MEMORY_OVER;
		}
		byte[] bSendBufTmp = new byte[iPackageSize];
		System.arraycopy(bSendBuf, 0, bSendBufTmp, 0,iSendLen);
		//MXLog.SendMsg("发送数据包："+zzStringTrans.hex2str(bSendBufTmp));
//		Log.e(TAG, "发送数据包："+zzStringTrans.hex2str(bSendBufTmp));
		iRV = m_connection.bulkTransfer(m_outEndpoint, bSendBufTmp, iPackageSize, iTimeOut);
		//SendMsg("实际发送数据长度："+iRV);
		return iRV;
	}
	final String TAG="USBBase";
	/**
	 * 功	能：	接收数据
	 * 参	数：	bRecvBuf 	- 	待接收数据缓存
	 * 			iRecvLen    -   待接收数据长度
	 * 			iTimeOut	-	超时时间，单位：毫秒
	 * 返	回：  >=0	-	成功（实际接收数据长度），<0	-	失败
	 * */
	public int recvData(byte[] bRecvBuf,int iRecvLen,int iTimeOut){
		int iRV = -1;
		if (iRecvLen > bRecvBuf.length) {
			return MXErrCode.ERR_MEMORY_OVER;
		}
		int iPackageSize   = recvPacketSize();
		byte[] bRecvBufTmp = new byte[iPackageSize];
		for (int i=0; i<iRecvLen; i+=iPackageSize)
		{
			int nDataLen = iRecvLen-i;
			if (nDataLen > iPackageSize)
			{
				nDataLen = iPackageSize;
			}
//			Log.e(TAG, "================开始接收=======================");
			iRV= m_connection.bulkTransfer(m_inEndpoint, bRecvBufTmp, nDataLen, iTimeOut);
//			Log.e(TAG, "================接收结束=======================");
//			Log.e(TAG, "接收数据包："+ zzStringTrans.hex2str(bRecvBufTmp));
//			Log.e(TAG, "iRV===" + iRV);
			if(iRV < 0)
			{
				return iRV;
			}
			System.arraycopy(bRecvBufTmp, 0, bRecvBuf, i,iRV);
		}
		//SendMsg("实际接收数据长度："+iRV);
		//SendMsg("接收数据包："+zzStringTrans.hex2str(bRecvBuf));
		return iRV;
	}
	
	/**
	 * 功	能：	关闭设备
	 * 参	数：	
	 * 返	回：  0	-	成功，其他	-	失败
	 * */
	public int closeDev(){
		if(m_connection!=null){
			m_connection.releaseInterface(m_usbInterface);
			m_connection.close();
			m_connection = null;
		}	
		return MXErrCode.ERR_OK;
	}
	
	// 在跟USB设备进行通信之前，你的应用程序必须要获取用户的许可。
	// 创建一个广播接收器。这个接收器用于监听你调用requestPermission()方法时，系统所发出的Intent对象
	private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
	private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (ACTION_USB_PERMISSION.equals(action)) {
				synchronized (this) {
					UsbDevice device = (UsbDevice) intent
							.getParcelableExtra(UsbManager.EXTRA_DEVICE);
					if (intent.getBooleanExtra(
							UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
						if (device != null) {
							// call method to set up device communication
						}
					} else {
						Log.d("MIAXIS", "permission denied for device "+ device);
					}
				}
			}
			//当您在完成和设备的“交流”之后，又或者该设备被移除了，
			//通过调用releaseInterface()和close()的方法来关闭UseInterface和UsbDeviceConnection
			if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
				UsbDevice device = (UsbDevice) intent
						.getParcelableExtra(UsbManager.EXTRA_DEVICE);
				if (device != null) {
					// call your method that cleans up and closes communication with the device
					m_connection.releaseInterface(m_usbInterface);
					m_connection.close();
				}
			}
		}
	};

	/**
	 * 功	能：注册接收USB通信权限的广播，并放在onCreate中，用于获取跟设备通信的权限
	 * 参	数：
	 * 返	回：
	 * 备	注：在跟USB设备进行通信之前，你的应用程序必须要获取用户的许可。
	 *     如果你的应用程序使用Intent过滤器来发现接入的USB设备，
	 *     而且用户允许你的应用程序处理该Intent，那么它会自动的接收权限，
	 *     否则，在你的应用程序接入该设备之前，必须明确的申请权限。
	 *     明确的申请权限在某些情况下是必须的，
	 *     如你的应用程序列举已经接入的USB设备并想要跟其中的一个设备通信的时候。
	 *     在试图跟一个设备通信之前，你必须要检查是否有访问设备的权限。
	 *     否则，如果用户拒绝了你访问该设备的请求，你会收到一个运行时错误。
	 * */
	private void regUsbMonitor()
	{
		IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
		m_ctx.registerReceiver(mUsbReceiver, filter);
	}
	
	public void unRegUsbMonitor()
	{
		m_ctx.unregisterReceiver(mUsbReceiver);
	}
	
	public static void MySleep(int iTimeout){
		long duration = -1;
		Calendar time1,time2;
		time1 = Calendar.getInstance();
		while(duration<=iTimeout){ 
			time2 = Calendar.getInstance();
			duration = time2.getTimeInMillis() - time1.getTimeInMillis();
		}
	}
}
