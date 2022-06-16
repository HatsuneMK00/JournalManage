package edu.ecnu.journalmanage.controller;

import com.github.pagehelper.PageInfo;
import edu.ecnu.journalmanage.model.Role;
import edu.ecnu.journalmanage.model.User;
import edu.ecnu.journalmanage.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AdminController {
    @Autowired
    public AdminService adminService;

    @RequestMapping("/adminIndex")
    public String adminIndex(){
        return "admin/admin_index";
    }

    @RequestMapping("/adminRegister")
    public String adminRegister(@RequestParam(defaultValue = "1") int aIUpageNum, @RequestParam(defaultValue = "5") int pageSize,
                                Model model){
        PageInfo<User> allInvalidUsers = adminService.getAllInvalidUsersPaged(aIUpageNum, pageSize);
        model.addAttribute("allInvalidUsers", allInvalidUsers);
        return "admin/admin_register";
    }

    @RequestMapping("/adminDelete")
    public String adminDelete(@RequestParam(defaultValue = "1") int EditorpageNum,
                              @RequestParam(defaultValue = "1") int ExpertpageNum,
                              @RequestParam(defaultValue = "5") int pageSize,
                              Model model){
        PageInfo<User> allEditors = adminService.getAllEditorsPaged(EditorpageNum, pageSize);
        PageInfo<User> allExperts = adminService.getAllExpertsPaged(ExpertpageNum, pageSize);
        model.addAttribute("allEditors", allEditors);
        model.addAttribute("allExperts", allExperts);
        return "admin/admin_delete";
    }

    @RequestMapping("/adminChangeInfo")
    public String adminChangeInfo(){
        return "ChangeInfo";
    }

    @RequestMapping("/validateUser")
    public String AdminValidateUser(int userID, String role){
        if(role.equals(Role.editor.toString())){
            adminService.validateUser(userID, Role.editor);
        }else if(role.equals(Role.expert.toString())){
            adminService.validateUser(userID, Role.expert);
        }else{
            adminService.validateUser(userID, Role.chiefEditor);
        }
        return "redirect:/adminRegister";
    }

    @RequestMapping("/deleteUser")
    public String DeleteUser(int userID){
        adminService.deleteUser(userID);
        return "redirect:/adminDelete";
    }
}
