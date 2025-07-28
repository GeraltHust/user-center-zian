package com.zian.common.exception;

import com.zian.constant.BaseResponseCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessException extends RuntimeException {
    private BaseResponseCode code;
    private String msg;
}
