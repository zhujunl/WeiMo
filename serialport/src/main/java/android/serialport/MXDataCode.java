package android.serialport;

import java.math.BigInteger;

public class MXDataCode
{
    public static byte[] intToByteArray(final int value) {
        final byte[] b = new byte[4];
        for (int i = 0; i < 4; ++i) {
            final int offset = i * 8;
            b[i] = (byte)(value >> offset & 0xFF);
        }
        return b;
    }
    
    public static int byteArrayToInt(final byte[] b, final int offset) {
        int value = 0;
        for (int i = 0; i < 4; ++i) {
            final int shift = i * 8;
            value += (b[i + offset] & 0xFF) << shift;
        }
        return value;
    }
    
    public static BigInteger byteArrayToBigInteger(final byte[] data) {
        final byte[] temp = new byte[4];
        for (int i = 0; i < 4; ++i) {
            temp[i] = data[4 - i - 1];
        }
        final BigInteger a = new BigInteger("4294967296");
        final BigInteger b = new BigInteger(temp);
        BigInteger value;
        if (BigInteger.ZERO.compareTo(b) > 0) {
            value = a.add(b);
        }
        else {
            value = b;
        }
        return value;
    }
    
    public static byte[] shortToByteArray(final short value) {
        final byte[] b = new byte[2];
        for (int i = 0; i < 2; ++i) {
            final int offset =8- i * 8;
            b[i] = (byte)(value >> offset & 0xFF);
        }
        return b;
    }
    
    public static short byteArrayToShort(final byte[] b, final int offset) {
        short value = 0;
        for (int i = 0; i < 2; ++i) {
            final int shift = i * 8;
            value += (short)((b[i + offset] & 0xFF) << shift);
        }
        return value;
    }
    
    public static int JUnsigned(final int x) {
        if (x >= 0) {
            return x;
        }
        return x + 256;
    }
    
    public static void EncData(final byte[] lpRawData, final int nRawLen, final byte[] lpEncData) {
        int i;
        int aaa;
        for (i = 0, i = 0; i < nRawLen; ++i) {
            aaa = JUnsigned(lpRawData[i]);
            lpEncData[2 * i] = (byte)((aaa >> 4) + 48);
            lpEncData[2 * i + 1] = (byte)((aaa & 0xF) + 48);
        }
    }
    
    public static void DecData(final byte[] lpEncData, final int nRawLen, final byte[] lpRawData) {
        int i;
        for (i = 0, i = 0; i < nRawLen; ++i) {
            lpRawData[i] = (byte)((lpEncData[2 * i] - 48 << 4) + (lpEncData[2 * i + 1] - 48));
        }
    }
    
    public static String hex2str(final byte[] hex) {
        final StringBuilder sb = new StringBuilder();
        for (final byte b : hex) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
    
    public static void HexToAsc(final byte[] lpHexData, final int nHexLength, final byte[] lpAscData) {
        final String strTmp = hex2str(lpHexData);
        final byte[] szTmp2 = strTmp.getBytes();
        for (int i = 0; i < szTmp2.length; ++i) {
            lpAscData[i] = szTmp2[i];
        }
    }
    
    public static int JavaBase64Encode(final byte[] pInput, final int inputLen, final byte[] pOutput, final int outputbufsize) {
        int currentin = 0;
        int currentin2 = 0;
        int currentin3 = 0;
        final String codebuffer = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        byte[] encodingTable = new byte[65];
        encodingTable = codebuffer.getBytes();
        final int outlen = (inputLen + 2) / 3 * 4;
        final int modulus = inputLen % 3;
        final int datalen = inputLen - modulus;
        final int encodedatalen = datalen * 4 / 3;
        if (outputbufsize < outlen) {
            return 0;
        }
        switch (modulus) {
            case 1: {
                final int i = inputLen - 1;
                final int j = outlen - 4;
                currentin = pInput[i];
                if (currentin < 0) {
                    currentin += 256;
                }
                final long ltmp = (long)currentin << 16;
                pOutput[j] = encodingTable[(int)(ltmp >> 18 & 0x3FL)];
                pOutput[j + 1] = encodingTable[(int)(ltmp >> 12 & 0x3FL)];
                pOutput[j + 3] = (pOutput[j + 2] = 61);
                break;
            }
            case 2: {
                final int i = inputLen - 2;
                final int j = outlen - 4;
                currentin = pInput[i];
                currentin2 = pInput[i + 1];
                if (currentin < 0) {
                    currentin += 256;
                }
                if (currentin2 < 0) {
                    currentin2 += 256;
                }
                final long ltmp = (long)pInput[i] << 16 | (long)currentin2 << 8;
                pOutput[j] = encodingTable[(int)(ltmp >> 18 & 0x3FL)];
                pOutput[j + 1] = encodingTable[(int)(ltmp >> 12 & 0x3FL)];
                pOutput[j + 2] = encodingTable[(int)(ltmp >> 6 & 0x3FL)];
                pOutput[j + 3] = 61;
                break;
            }
        }
        int i;
        int j;
        for (i = datalen - 3,  j = encodedatalen - 4; i >= 0; i -= 3, j -= 4) {
            currentin = pInput[i];
            currentin2 = pInput[i + 1];
            currentin3 = pInput[i + 2];
            if (currentin < 0) {
                currentin += 256;
            }
            if (currentin2 < 0) {
                currentin2 += 256;
            }
            if (currentin3 < 0) {
                currentin3 += 256;
            }
            final long ltmp = (long)currentin << 16 | (long)currentin2 << 8 | (long)currentin3;
            pOutput[j] = encodingTable[(int)(ltmp >> 18 & 0x3FL)];
            pOutput[j + 1] = encodingTable[(int)(ltmp >> 12 & 0x3FL)];
            pOutput[j + 2] = encodingTable[(int)(ltmp >> 6 & 0x3FL)];
            pOutput[j + 3] = encodingTable[(int)(ltmp & 0x3FL)];
        }
        return outlen;
    }
    
    public static int JavaBase64Decode(final byte[] pInput, final int inputLen, final byte[] pOutput) {
        final char np = '\u00ff';
        final char[] decodingTable = new char[256];
        for (int i = 0; i < 256; ++i) {
            decodingTable[i] = np;
        }
        for (int i = 65; i <= 90; ++i) {
            decodingTable[i] = (char)(i - 65);
        }
        for (int i = 97; i <= 122; ++i) {
            decodingTable[i] = (char)(i - 97 + 26);
        }
        for (int i = 48; i <= 57; ++i) {
            decodingTable[i] = (char)(i - 48 + 52);
        }
        decodingTable[43] = '>';
        decodingTable[47] = '?';
        if (inputLen % 4 != 0) {
            return 0;
        }
        int padnum;
        if (pInput[inputLen - 2] == 61) {
            padnum = 2;
        }
        else if (pInput[inputLen - 1] == 61) {
            padnum = 1;
        }
        else {
            padnum = 0;
        }
        final int outlen = inputLen / 4 * 3 - padnum;
        for (int datalen = (inputLen - padnum) / 4 * 3, i = 0, j = 0; i < datalen; i += 3, j += 4) {
            long ltmp = 0L;
            for (int m = j; m < j + 4; ++m) {
                final char ctmp = decodingTable[pInput[m]];
                if (ctmp == np) {
                    return 0;
                }
                ltmp = (ltmp << 6 | (long)ctmp);
            }
            pOutput[i] = (byte)(ltmp >> 16 & 0xFFL);
            pOutput[i + 1] = (byte)(ltmp >> 8 & 0xFFL);
            pOutput[i + 2] = (byte)(ltmp & 0xFFL);
        }
        switch (padnum) {
            case 1: {
                long ltmp = 0L;
                for (int m = inputLen - 4; m < inputLen - 1; ++m) {
                    final char ctmp = decodingTable[pInput[m]];
                    if (ctmp == np) {
                        return 0;
                    }
                    ltmp = (ltmp << 6 | (long)ctmp);
                }
                ltmp <<= 6;
                pOutput[outlen - 2] = (byte)(ltmp >> 16 & 0xFFL);
                pOutput[outlen - 1] = (byte)(ltmp >> 8 & 0xFFL);
                break;
            }
            case 2: {
                long ltmp = 0L;
                for (int m = inputLen - 4; m < inputLen - 2; ++m) {
                    final char ctmp = decodingTable[pInput[m]];
                    if (ctmp == np) {
                        return 0;
                    }
                    ltmp = (ltmp << 6 | (long)ctmp);
                }
                ltmp <<= 12;
                pOutput[outlen - 1] = (byte)(ltmp >> 16 & 0xFFL);
                break;
            }
        }
        return outlen;
    }
}
