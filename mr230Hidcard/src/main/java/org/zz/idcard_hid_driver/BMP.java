package org.zz.idcard_hid_driver;

import android.graphics.*;
import java.io.*;

public class BMP
{
    public static int JUnsigned(final int x) {
        if (x >= 0) {
            return x;
        }
        return x + 256;
    }
    
    public static Bitmap Iso2Bimap(final byte[] imgIsoBuf) {
        if (imgIsoBuf[0] != 70 || imgIsoBuf[1] != 73 || imgIsoBuf[2] != 82) {
            return null;
        }
        final int iImgX = JUnsigned(imgIsoBuf[41] << 8) + JUnsigned(imgIsoBuf[42]);
        final int iImgY = JUnsigned(imgIsoBuf[43] << 8) + JUnsigned(imgIsoBuf[44]);
        if (iImgX < 0 || iImgX > 1000) {
            return null;
        }
        if (iImgY < 0 || iImgY > 1000) {
            return null;
        }
        final byte[] imgBuf = new byte[iImgX * iImgY];
        System.arraycopy(imgIsoBuf, 46, imgBuf, 0, iImgX * iImgY);
        return Raw2Bimap(imgBuf, iImgX, iImgY);
    }
    
    public static Bitmap Raw2Bimap(final byte[] imgBuf, final int iImgX, final int iImgY) {
        final byte[] bmpBuf = new byte[iImgX * iImgY + 1078];
        Raw2Bmp(bmpBuf, imgBuf, iImgX, iImgY);
        return BitmapFactory.decodeByteArray(bmpBuf, 0, bmpBuf.length);
    }
    
    public static int Raw2Bmp(final byte[] pBmp, final byte[] pRaw, final int X, final int Y) {
        final byte[] head = new byte[1078];
        final byte[] temp = { 66, 77, 0, 0, 0, 0, 0, 0, 0, 0, 54, 4, 0, 0, 40, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        System.arraycopy(temp, 0, head, 0, temp.length);
        int num = X;
        head[18] = (byte)(num & 0xFF);
        num >>= 8;
        head[19] = (byte)(num & 0xFF);
        num >>= 8;
        head[20] = (byte)(num & 0xFF);
        num >>= 8;
        head[21] = (byte)(num & 0xFF);
        num = Y;
        head[22] = (byte)(num & 0xFF);
        num >>= 8;
        head[23] = (byte)(num & 0xFF);
        num >>= 8;
        head[24] = (byte)(num & 0xFF);
        num >>= 8;
        head[25] = (byte)(num & 0xFF);
        int j = 0;
        for (int i = 54; i < 1078; i += 4) {
            final byte[] array = head;
            final int n = i;
            final byte[] array2 = head;
            final int n2 = i + 1;
            final byte[] array3 = head;
            final int n3 = i + 2;
            final byte b = (byte)j;
            array3[n3] = b;
            array[n] = (array2[n2] = b);
            head[i + 3] = 0;
            ++j;
        }
        System.arraycopy(head, 0, pBmp, 0, 1078);
        for (int i = 0; i < Y; ++i) {
            System.arraycopy(pRaw, i * X, pBmp, 1078 + (Y - 1 - i) * X, X);
        }
        return 0;
    }
    
    public static int SaveData(final String filepath, final byte[] buffer, final int size) {
        final File f = new File(filepath);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
        try {
            fos.write(buffer, 0, size);
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
        try {
            fos.close();
        }
        catch (IOException e2) {
            e2.printStackTrace();
            return -2;
        }
        return 0;
    }
    
    public static int SaveBMP(final String strFileName, final byte[] ucImgBuf, final int iWidth, final int iHeight) {
        final byte[] bmpimagebuff = new byte[iWidth * iHeight + 1078];
        Raw2Bmp(bmpimagebuff, ucImgBuf, iWidth, iHeight);
        return SaveData(strFileName, bmpimagebuff, bmpimagebuff.length);
    }

    public int SaveIsoImg(final String strFileName, final byte[] bIsoFingerImage) {
        if (bIsoFingerImage[0] != 70 || bIsoFingerImage[1] != 73 || bIsoFingerImage[2] != 82) {
            return -1;
        }
        final int iWidth = JUnsigned(bIsoFingerImage[41] << 8) + JUnsigned(bIsoFingerImage[42]);
        final int iHeight = JUnsigned(bIsoFingerImage[43] << 8) + JUnsigned(bIsoFingerImage[44]);
        if (iWidth < 0 || iWidth > 1000) {
            return -2;
        }
        if (iHeight < 0 || iHeight > 1000) {
            return -3;
        }
        final byte[] imageBuf = new byte[iWidth * iHeight];
        System.arraycopy(bIsoFingerImage, 46, imageBuf, 0, iWidth * iHeight);
        return this.SaveBMP(strFileName, imageBuf, iWidth, iHeight);
    }
}
