package org.zz.idcard_hid_driver;

public class zzJavaBase64
{
    static int JavaBase64Encode(final byte[] pInput, final int inputLen, final byte[] pOutput, final int outputbufsize) {
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
        for (i = datalen - 3,j = encodedatalen - 4; i >= 0; i -= 3, j -= 4) {
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
    
    static int JavaBase64Decode(final byte[] pInput, final int inputLen, final byte[] pOutput) {
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
