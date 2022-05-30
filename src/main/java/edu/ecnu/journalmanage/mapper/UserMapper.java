package edu.ecnu.journalmanage.mapper;

import edu.ecnu.journalmanage.model.Role;
import edu.ecnu.journalmanage.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    // get user by userid
    @Select("select * from user where id=#{userId}")
    User getUserById(int userId);

    @Select("select * from user where name=#{name}")
    User findByUsername(String name);

    // get all users of certain role
    @Select("select * from user where role=#{role}")
    List<User> getUsersByRole(Role role);

    @Select("select * from user where invalid=0")
    List<User> getAllInvalidUsers();

    // get chief editor count
    @Select("select count(*) from user where role=1")
    int getChiefEditorCount();

    // insert user(id, name, password, role, valid, researchArea, title) id is auto generated primary key
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into user(name, password, role, valid, research_area, title, email) values(#{name}, #{password}, #{role}, #{valid}, #{researchArea}, #{title}, #{email})")
    int addUser(User user);

    // update user(name, password, role, valid, researchArea, title)
    @Update("update user set name=#{name}, password=#{password}, valid=#{valid}, research_area=#{researchArea}, title=#{title}, email=#{email} where id=#{id}")
    int updateUser(User user);

    @Update("update user set role=#{role} where id=#{id}")
    int changeUserRole(User user);

    // delete user by id
    @Delete("delete from user where id=#{id}")
    int deleteUser(int id);
}
