package cn.bugstack.springframework.test.bean;

/**
 *
 *
 *
 * 作者：DerekYRC https://github.com/DerekYRC/mini-spring
 * @description 模拟含有入参构造函数的用户 Bean 对象
 * @date 2022/03/07
 *
 *
 */
public class UserService {

    private String name;

    public void queryUserInfo() {
        System.out.println("查询用户信息：" + name);
    }
    //新增入参信息得构造函数
    public UserService(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("");
        sb.append("").append(name);
        return sb.toString();
    }

}
