package org.seckill.http;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author Ryan-hou
 * @description:
 * @className: SeckillResult
 * @date January 16,2017
 */
// 统一封装json结果,返回给前台;
// 范型类型T是service与web层数据传递的DTO类型
@ApiModel(value = "SeckillResult", description = "与前端数据交互对象")
public class SeckillResult<T> {

    @ApiModelProperty(value = "接口调用是否成功,true表示成功", required = true)
    private boolean success;

    @ApiModelProperty(value = "封装调用成功后返回的数据")
    private T data;

    @ApiModelProperty(value = "接口调用失败后的失败信息")
    private String error;

    public SeckillResult(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    public SeckillResult(boolean success, String error) {
        this.success = success;
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
