package org.zz.api;


import java.io.Serializable;

/**
 * @author tank
 * @version $
 * @des 请求返回类
 * @updateAuthor $
 * @updateDes
 */
public class MXResult<T> implements Serializable {

    private final int code;
    private final String msg;
    private final T data;

    private MXResult() {
        this(MXErrorCode.ERR_OK, null);
    }

    public MXResult(int code, String msg) {
        this(code, msg, null);
    }

    public MXResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> MXResult<T> Create() {
        return new MXResult<T>();
    }

    public static <T> MXResult<T> Create(int code, String msg, T data) {
        return new MXResult<T>(code, msg, data);
    }

    public static <T> MXResult<T> Create(MXResult<?> zzResponse, Class<T> clazz) {
        if (zzResponse == null) {
            return Create();
        }
        return Create(zzResponse.getCode(), zzResponse.getMsg(), null);
    }

    public static <T> MXResult<T> CreateFail(int code, String msg) {
        return Create(code, msg, null);
    }

    public static <T> MXResult<T> CreateFail(MXResult<?> zzResponse) {
        if (zzResponse == null) {
            return Create();
        }
        return CreateFail(zzResponse.getCode(), zzResponse.getMsg());
    }

    public static <T> MXResult<T> CreateSuccess(T data) {
        return Create(MXErrorCode.ERR_OK, null, data);
    }

    public static <T> MXResult<T> CreateSuccess() {
        return Create(MXErrorCode.ERR_OK, null, null);
    }

    public static boolean isSuccess(MXResult<?> zzResponse, int successCode) {
        if (zzResponse == null) {
            return false;
        }
        return zzResponse.getCode() == successCode;
    }

    public static boolean isSuccess(MXResult<?> zzResponse) {
        return isSuccess(zzResponse, MXErrorCode.ERR_OK);
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Response{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

}
