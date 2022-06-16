package edu.ecnu.journalmanage.controller;

import edu.ecnu.journalmanage.model.Role;
import edu.ecnu.journalmanage.model.User;
import edu.ecnu.journalmanage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {
    @Autowired
    public UserService userService;

    @RequestMapping("/login")
    public String UserLogin(){
        return "UserLogin";
    }

    @RequestMapping(value = "/UserLogin", method = RequestMethod.POST)
    @ResponseBody
    public String usersLogin(String username, String password, String role, HttpSession session){
        User user = userService.login(username, password);
        System.out.println(user);
        if(user == null){
            return "error";
        }
        session.setAttribute("uid", user.getId());
        String role_str = user.getRole().toString();
        if(role.equals("4") && role_str.equals("admin")){
            return "admin";
        }else if(role.equals("3") && role_str.equals("chiefEditor")){
            return "chief";
        }else if(role.equals("2") && role_str.equals("expert")){
            return "expert";
        }else if(role.equals("1") && role_str.equals("editor")){
            return "editor";
        }else if(role.equals("0") && role_str.equals("author")){
            return "author";
        }
        //登录失败
        return "error";
    }

    @RequestMapping(value = "/UserRegister", method = RequestMethod.POST)
    @ResponseBody
    public String UserRegister(String username, String email, String password, String password1, String role){
        if(!password.equals(password1)){
            return "pwd_not_equal";
        }else if(role.equals("")){
            return "role_null";
        }
        User user = new User();
        user.setName(username);
        user.setEmail(email);
        user.setPassword(password);
        if(role.equals("0")){
            user.setRole(Role.author);
        }else if(role.equals("1")){
            user.setRole(Role.editor);
        }else if(role.equals("2")){
            user.setRole(Role.expert);
        }else if(role.equals("3")){
            user.setRole(Role.chiefEditor);
        }
        String register = userService.register(user);
        if(register == null) {
            return "success";
        }
        return "failed";
    }
}
