package net.xdclass.base_project.mapper;



import java.util.List;

import net.xdclass.base_project.domain.User;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 功能描述：访问数据库的接口
 *
 * <p> 创建时间：May 6, 2018 3:51:49 PM </p> 
 *
 *@作者 小D课堂  小D
 */
public interface UserMapper {
	
	
	//推荐使用#{}取值，不要用${},因为存在注入的风险
	 @Insert("INSERT INTO user(name,phone,create_time,age) VALUES(#{name}, #{phone}, #{createTime},#{age})")
	 @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")   //keyProperty java对象的属性；keyColumn表示数据库的字段
	 int insert(User user);
	 
	 
	 
	

	 /**
	  * 功能描述：查找全部
	  * @return
	  */
    @Select("SELECT * FROM user")
    @Results({
        @Result(column = "create_time",property = "createTime"),
        @Result(column = "create_time",property = "createTime")
        //javaType = java.util.Date.class        
    })
    List<User> getAll();
  
    

    /**
     * 功能描述：根据id找对象
     * @param id
     * @return
     */
    @Select("SELECT * FROM user WHERE id = #{id}")
    @Results({
    	 @Result(column = "create_time",property = "createTime")
    })
    User findById(Long id);

   

    /**
     * 功能描述：更新对象
     * @param user
     */
    @Update("UPDATE user SET name=#{name} WHERE id =#{id}")
    void update(User user);

    /**
     * 功能描述：根据id删除用户
     * @param userId
     */
    @Delete("DELETE FROM user WHERE id =#{userId}")
    void delete(Long userId);

}