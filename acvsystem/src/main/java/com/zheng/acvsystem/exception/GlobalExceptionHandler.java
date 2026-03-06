package com.zheng.acvsystem.exception;

import com.aliyun.oss.common.utils.StringUtils;
import com.zheng.acvsystem.entity.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
/*当Controller层抛出异常时，会被该类捕获并处理
 *   上面的UserController类中的register方法 通过Validiton注解参数校验
 *   如果校验失败则会放回异常，如果不对异常进行捕获 则postman端读取的到 json则是错误500 不符合接口规范
 *   所以需要该类捕获该异常并返回一个包含错误信息的Result对象
 * */
@RestControllerAdvice//标记该类为全局异常处理类
public class GlobalExceptionHandler {
    //@ExceptionHandler（） 用于指定该方法处理的异常类型  Exception.class表示处理所有异常
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e){
        //将异常信息打印到控制台
        e.printStackTrace();
        //e.getMessage() : 获取异常的详细信息
        //StringUtils.isNullOrEmpty(e.getMessage()) : 判断字符串是否为空或null
        return Result.error(StringUtils.isNullOrEmpty(e.getMessage()) ? "操作失败" : e.getMessage());
    }
}
