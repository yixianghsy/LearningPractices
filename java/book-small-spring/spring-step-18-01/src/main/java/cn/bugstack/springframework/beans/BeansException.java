package cn.bugstack.springframework.beans;

/**
 *
 *
 *
 * 作者：DerekYRC https://github.com/DerekYRC/mini-spring
 * @description 定义 Bean 异常
 * @date 2022/3/7
 *
 *
 */
public class BeansException extends RuntimeException{

    public BeansException(String msg) {
        super(msg);
    }

    public BeansException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
