package android.serialport.api;

import java.util.Vector;

public class zzStringTrans
{
    public static Vector<Byte> str2vectorbcd(final String str) {
        final byte[] a = str.getBytes();
        final Vector<Byte> v = new Vector<Byte>();
        v.clear();
        for (int i = 0; i < str.length() / 2; ++i) {
            final byte b = (byte)(a[2 * i] - 48 << 4 | a[2 * i + 1] - 48);
            v.add(b);
        }
        return v;
    }
    
    public static byte[] str2bcd(final String str) {
        final byte[] a = str.getBytes();
        final Vector<Byte> v = new Vector<Byte>();
        v.clear();
        for (int i = 0; i < str.length() / 2; ++i) {
            final byte b = (byte)(a[2 * i] - 48 << 4 | a[2 * i + 1] - 48);
            v.add(b);
        }
        final byte[] tmpbt = new byte[v.size()];
        for (int j = 0; j < v.size(); ++j) {
            tmpbt[j] = v.get(j);
        }
        return tmpbt;
    }
    
    public static Vector<Byte> datetime2vectorbcd(String time) {
        String[] a = time.split(" ");
        time = String.valueOf(a[0]) + a[1];
        a = time.split("[:-]");
        final StringBuilder b = new StringBuilder();
        String[] array;
        for (int length = (array = a).length, i = 0; i < length; ++i) {
            final String s = array[i];
            b.append(s);
        }
        time = b.toString().substring(2);
        return str2vectorbcd(time);
    }
    
    public static byte[] datetime2bcd(String time) {
        String[] a = time.split(" ");
        time = String.valueOf(a[0]) + a[1];
        a = time.split("[:-]");
        final StringBuilder b = new StringBuilder();
        String[] array;
        for (int length = (array = a).length, i = 0; i < length; ++i) {
            final String s = array[i];
            b.append(s);
        }
        time = b.toString().substring(2);
        return str2bcd(time);
    }
    
    public static byte[] time2bcd(final String time) {
        return str2bcd(time);
    }
    
    public static String bcd2time(final byte[] time) {
        final int hour = (time[0] >> 4) * 10 + (time[0] & 0xF);
        final int min = (time[1] >> 4) * 10 + (time[1] & 0xF);
        final int sec = (time[2] >> 4) * 10 + (time[2] & 0xF);
        return String.format("%02d:%02d:%02d", hour, min, sec);
    }
    
    public static String bcd2date(final byte[] date) {
        final int year = 2000 + 10 * (date[0] >> 4) + (date[0] & 0xF);
        final int mon = 10 * (date[1] >> 4) + (date[1] & 0xF);
        final int day = 10 * (date[2] >> 4) + (date[2] & 0xF);
        return String.format("%02d-%02d-%02d", year, mon, day);
    }
    
    public static String bcd2datetime(final byte[] datetime) {
        final int year = 2000 + 10 * (datetime[0] >> 4) + (datetime[0] & 0xF);
        final int mon = 10 * (datetime[1] >> 4) + (datetime[1] & 0xF);
        final int day = 10 * (datetime[2] >> 4) + (datetime[2] & 0xF);
        final int hour = (datetime[3] >> 4) * 10 + (datetime[3] & 0xF);
        final int min = (datetime[4] >> 4) * 10 + (datetime[4] & 0xF);
        final int sec = (datetime[5] >> 4) * 10 + (datetime[5] & 0xF);
        return String.format("%02d-%02d-%02d %02d:%02d:%02d", year, mon, day, hour, min, sec);
    }
    
    public static String bcd2str(final byte[] bcd) {
        final StringBuilder sb = new StringBuilder();
        for (final byte b : bcd) {
            sb.append(String.format("%d%d", b >> 4, b & 0xF));
        }
        return sb.toString();
    }
    
    public static String hex2str(final byte[] hex) {
        final StringBuilder sb = new StringBuilder();
        for (final byte b : hex) {
            sb.append(String.format("%02x ", b));
        }
        return sb.toString();
    }
    
    public static byte[] str2hex(final String str) {
        final byte[] a = str.getBytes();
        final Vector<Byte> v = new Vector<Byte>();
        v.clear();
        for (int i = 0; i < str.length() / 2; ++i) {
            byte b1;
            if (48 <= a[2 * i] && a[2 * i] <= 57) {
                b1 = (byte)(a[2 * i] - 48);
            }
            else if (65 <= a[2 * i] && a[2 * i] <= 70) {
                b1 = (byte)(a[2 * i] - 65 + 10);
            }
            else {
                b1 = (byte)(a[2 * i] - 97 + 10);
            }
            byte b2;
            if (48 <= a[2 * i + 1] && a[2 * i + 1] <= 57) {
                b2 = (byte)(a[2 * i + 1] - 48);
            }
            else if (65 <= a[2 * i + 1] && a[2 * i + 1] <= 70) {
                b2 = (byte)(a[2 * i + 1] - 65 + 10);
            }
            else {
                b2 = (byte)(a[2 * i + 1] - 97 + 10);
            }
            final byte b3 = (byte)(b1 << 4 | b2);
            v.add(b3);
        }
        final byte[] tmpbt = new byte[v.size()];
        for (int j = 0; j < v.size(); ++j) {
            tmpbt[j] = v.get(j);
        }
        return tmpbt;
    }

    public static String byteToStr(byte[] buffer) {
        try {
            int length = 0;
            for (int i = 0; i < buffer.length; ++i) {
                if (buffer[i] == 0) {
                    length = i;
                    break;
                }
            }
            return new String(buffer, 0, length, "UTF-8");
        } catch (Exception e) {
            return "";
        }
    }
}
