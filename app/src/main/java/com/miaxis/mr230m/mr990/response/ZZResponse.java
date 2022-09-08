package com.miaxis.mr230m.mr990.response;


import java.io.Serializable;

/**
 * @author tank
 * @version $
 * @des 请求返回类
 * @updateAuthor $
 * @updateDes
 */
public class ZZResponse<T> implements Serializable {

    private final int code;
    private final String msg;
    private final T data;

    private ZZResponse() {
        this(ZZResponseCode.CODE_DEFAULT, null);
    }

    public ZZResponse(int code, String msg) {
        this(code, msg, null);
    }

    public ZZResponse(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> ZZResponse<T> Create() {
        return new ZZResponse<T>();
    }

    public static <T> ZZResponse<T> Create(int code, String msg, T data) {
        return new ZZResponse<T>(code, msg, data);
    }

    public static <T> ZZResponse<T> Create(ZZResponse<?> zzResponse, Class<T> clazz) {
        if (zzResponse == null) {
            return Create();
        }
        return Create(zzResponse.getCode(), zzResponse.getMsg(), null);
    }

    public static <T> ZZResponse<T> CreateFail(int code, String msg) {
        return Create(code, msg, null);
    }

    public static <T> ZZResponse<T> CreateFail(ZZResponse<?> zzResponse) {
        if (zzResponse == null) {
            return Create();
        }
        return CreateFail(zzResponse.getCode(), zzResponse.getMsg());
    }

    public static <T> ZZResponse<T> CreateSuccess(T data) {
        return Create(ZZResponseCode.CODE_SUCCESS, ZZResponseCode.MSG_SUCCESS, data);
    }

    public static <T> ZZResponse<T> CreateSuccess() {
        return Create(ZZResponseCode.CODE_SUCCESS, ZZResponseCode.MSG_SUCCESS, null);
    }

    public static boolean isSuccess(ZZResponse<?> zzResponse, int successCode) {
        if (zzResponse == null) {
            return false;
        }
        return zzResponse.getCode() == successCode;
    }

    public static boolean isSuccess(ZZResponse<?> zzResponse) {
        return isSuccess(zzResponse, ZZResponseCode.CODE_SUCCESS);
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
