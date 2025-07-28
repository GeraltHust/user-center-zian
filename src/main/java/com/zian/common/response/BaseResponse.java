package com.zian.common.response;

import com.zian.constant.BaseResponseCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse<T> implements Serializable {
    private static final long serialVersionUID = 1928378923L;

    private BaseResponseCode code;
    private T data;
    private String msg;


    public BaseResponse(BaseResponseCode code, T data) {
        this.code = code;
        this.data = data;
    }

}
