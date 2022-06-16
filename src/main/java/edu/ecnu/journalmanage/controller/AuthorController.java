package edu.ecnu.journalmanage.controller;

import cn.hutool.core.io.FileUtil;
import com.github.pagehelper.PageInfo;
import edu.ecnu.journalmanage.model.*;
import edu.ecnu.journalmanage.service.ArticleService;
import edu.ecnu.journalmanage.service.AuthorService;
import edu.ecnu.journalmanage.service.FileService;
import edu.ecnu.journalmanage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
public class AuthorController {
    @Autowired
    public AuthorService authorService;

    @Autowired
    public FileService fileService;

    @Autowired
    public UserService userService;

    @Autowired
    public ArticleService articleService;

    @RequestMapping("/authorIndex")
    public String authorIndex(){
        return "author/author_index";
    }

    @GetMapping("/submitPaper")
    public String submit(){
        return "author/submit_paper";
    }

    @RequestMapping(value = "/submitPapers", method = RequestMethod.POST)
    @ResponseBody
    public String submitPaper(String title, String authors, String abstracts, String keyword, @RequestParam("fileName") MultipartFile file,
                              HttpSession session) throws IOException {
        //获取上传文件的文件名
        String fileName = session.getAttribute("uid") + file.getOriginalFilename();
        //获取文件绝对存储路径 System.getProperty("user.dir")是获取当前项目的地址
        // String savePath = System.getProperty("user.dir")+"/src/main/resources/static/files/" + fileName;
        String savePath = "/root/SmartPaper/src/main/resources/static/files/" + fileName;
        //相对路径
        String relativeSavePath = "/src/main/resources/static/files/" + fileName;
        try{
            //实现文件存储(绝对路径)
            FileUtil.writeBytes(file.getBytes(), savePath);
            Article article = new Article();
            article.setAuthorId((Integer) session.getAttribute("uid"));
            article.setTitle(title);
            article.setAuthors(authors);
            article.setAbstractText(abstracts);
            article.setKeywords(keyword);
            article.setFilePath(relativeSavePath);
            authorService.submitArticle(article);
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
        return "success";
    }

    @RequestMapping("/submitTXPapers")
    @ResponseBody
    public String submitTXPaper(int articleID, @RequestParam("fileName") MultipartFile file, HttpSession session){
        //获取上传文件的文件名
        String fileName = session.getAttribute("uid") + file.getOriginalFilename();
        //获取文件绝对存储路径 System.getProperty("user.dir")是获取当前项目的地址
        // String savePath = System.getProperty("user.dir")+"/src/main/resources/static/files/" + fileName;
        String savePath = "/root/SmartPaper/src/main/resources/static/files/" + fileName;
        //相对路径
        String relativeSavePath = "/src/main/resources/static/files/" + fileName;
        System.out.println(relativeSavePath);
        try{
            //实现文件存储(绝对路径)
            FileUtil.writeBytes(file.getBytes(), savePath);
            Article article = articleService.getArticleById(articleID);
            article.setFilePath(relativeSavePath);
            authorService.submitRevisionArticle(article);
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
        return "success";
    }

    @RequestMapping("/searchPaper")
    public String searchPaper(Model model, @RequestParam(defaultValue = "1") int iPpageNum, @RequestParam(defaultValue = "1") int AccpageNum,
                              @RequestParam(defaultValue = "1") int RejpageNum,
                              @RequestParam(defaultValue = "5") int pageSize, HttpSession session){
        int authorID = (Integer) session.getAttribute("uid");
        PageInfo<Article> inProgressArticles = authorService.getInProgressArticlesPaged(authorID, iPpageNum, pageSize);
        PageInfo<Article> acceptedArticles = authorService.getAcceptedArticlesPaged(authorID, AccpageNum, pageSize);
        PageInfo<Article> rejectedArticles = authorService.getRejectedArticlesPaged(authorID, RejpageNum, pageSize);
        model.addAttribute("inProgressArticles", inProgressArticles);
        model.addAttribute("acceptedArticles", acceptedArticles);
        model.addAttribute("rejectedArticles", rejectedArticles);

        return "author/search_paper";
    }

    @GetMapping("/changeInfo")
    public String change(){
        return "author/change_info";
    }

    @RequestMapping(value = "/changeInfos", method = RequestMethod.POST)
    @ResponseBody
    public String changeInfos(String username, String password, String password1, String email, HttpSession session){
        int UID = (Integer) session.getAttribute("uid");
        if(!password.equals(password1)){
            return "pwd_not_equal";
        }
        User user = new User();
        user.setName(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setId(UID);
        String msg = userService.completeUserInfo(user);
        if(msg == null){
            return "success";
        }
        return "error";
    }

    @RequestMapping("/articleDetail")
    public String ArticleDetail(int articleID, Model model, HttpSession session){
        //根据articleID获取到详细信息，存入model域中，返回前端页面进行展示
        //根据Map中的ReviewType，将对应的评审意见存入对应的变量中
        Article article = articleService.getArticleById(articleID);
        Map<ReviewType, List<Review>> reviewOfArticle = authorService.getReviewOfArticle(articleID);
        List<Review> CS_reviews = reviewOfArticle.get(ReviewType.preliminaryReview);
        List<Review> CSCS_reviews = reviewOfArticle.get(ReviewType.preliminaryRebuttalReview);
        List<Review> WS_reviews = reviewOfArticle.get(ReviewType.externalReview);
        List<Review> WSCS_reviews = reviewOfArticle.get(ReviewType.externalRebuttalReview);
        List<Review> ZS_reviews = reviewOfArticle.get(ReviewType.finalReview);
        List<Review> ZSCS_reviews = reviewOfArticle.get(ReviewType.finalRebuttalReview);

        model.addAttribute("article_Detail", article);
        model.addAttribute("CS_reviews", CS_reviews);
        model.addAttribute("CSCS_reviews", CSCS_reviews);
        model.addAttribute("WS_reviews", WS_reviews);
        model.addAttribute("WSCS_reviews", WSCS_reviews);
        model.addAttribute("ZS_reviews", ZS_reviews);
        model.addAttribute("ZSCS_reviews", ZSCS_reviews);

        int UID = (Integer) session.getAttribute("uid");
        User user = userService.getUserById(UID);
        Role role = user.getRole();

        if(article.getStatus() == ArticleStatus.editorReview){
            return "article/cs";
        }else if(article.getStatus() == ArticleStatus.editorRejection){
            return "article/cs_tg";
        }else if(article.getStatus() == ArticleStatus.editorReturned){
            if(role == Role.author){
                return "article/cs_tx";
            }else{
                return "article/editor_cs_tx";
            }
        }else if(article.getStatus() == ArticleStatus.editorRevision){
            return "article/cs_cs";
        }else if(article.getStatus() == ArticleStatus.expertReview){
            return "article/ws";
        }else if(article.getStatus() == ArticleStatus.expertRejection){
            return "article/ws_tg";
        }else if(article.getStatus() == ArticleStatus.expertReturned){
            if(role == Role.author){
                return "article/ws_tx";
            }else{
                return "article/expert_ws_tx";
            }
        }else if(article.getStatus() == ArticleStatus.expertRevision){
            return "article/ws_cs";
        }else if(article.getStatus() == ArticleStatus.chiefEditorReview){
            return "article/zs";
        }else if(article.getStatus() == ArticleStatus.chiefEditorRejection){
            return "article/zs_tg";
        }else if(article.getStatus() == ArticleStatus.chiefEditorReturned){
            if(role == Role.author){
                return "article/zs_tx";
            }else{
                return "article/chief_zs_tx";
            }
        }else if(article.getStatus() == ArticleStatus.chiefEditorRevision){
            return "article/zs_cs";
        }
        return "article/accept";
    }
}
