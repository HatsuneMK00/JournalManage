package edu.ecnu.journalmanage.controller;

import com.github.pagehelper.PageInfo;
import edu.ecnu.journalmanage.model.*;
import edu.ecnu.journalmanage.service.ArticleService;
import edu.ecnu.journalmanage.service.AuthorService;
import edu.ecnu.journalmanage.service.ChiefEditorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
public class ChiefEditorController {
    @Autowired
    public ChiefEditorService chiefEditorService;

    @Autowired
    public ArticleService articleService;

    @Autowired
    public AuthorService authorService;

    @RequestMapping("/chiefIndex")
    public String expertIndex(){
        return "chief_editor/chief_index";
    }

    @RequestMapping("/chiefOnline")
    public String chiefOnline(Model model, HttpSession session, @RequestParam(defaultValue = "1") int ChTopageNum,
                              @RequestParam(defaultValue = "1") int ChEdpageNum,
                              @RequestParam(defaultValue = "5") int pageSize){
        int chiefID = (Integer) session.getAttribute("uid");
        PageInfo<Article> ChtoArticles = chiefEditorService.getToReviewArticlesPaged(chiefID, ChTopageNum, pageSize);
        PageInfo<Article> ChedArticles = chiefEditorService.getReviewedArticlesPaged(chiefID, ChEdpageNum, pageSize);
        model.addAttribute("ChtoArticles", ChtoArticles);
        model.addAttribute("ChedArticles", ChedArticles);
        return "chief_editor/chief_online";
    }

    @RequestMapping("/chiefChangeInfo")
    public String chiefChangeInfo(){
        return "ChangeInfo";
    }

    @RequestMapping("/chiefReview")
    public String chiefReview(int articleID, Model model){
        Article cH_article = articleService.getArticleById(articleID);
        Map<ReviewType, List<Review>> reviewOfArticle = authorService.getReviewOfArticle(articleID);
        List<Review> CS_reviews = reviewOfArticle.get(ReviewType.preliminaryReview);
        List<Review> CSCS_reviews = reviewOfArticle.get(ReviewType.preliminaryRebuttalReview);
        List<Review> WS_reviews = reviewOfArticle.get(ReviewType.externalReview);
        List<Review> WSCS_reviews = reviewOfArticle.get(ReviewType.externalRebuttalReview);
        List<Review> ZS_reviews = reviewOfArticle.get(ReviewType.finalReview);
        List<Review> ZSCS_reviews = reviewOfArticle.get(ReviewType.finalRebuttalReview);
        model.addAttribute("cH_article", cH_article);
        model.addAttribute("CS_reviews", CS_reviews);
        model.addAttribute("CSCS_reviews", CSCS_reviews);
        model.addAttribute("WS_reviews", WS_reviews);
        model.addAttribute("WSCS_reviews", WSCS_reviews);
        model.addAttribute("ZS_reviews", ZS_reviews);
        model.addAttribute("ZSCS_reviews", ZSCS_reviews);
        if(cH_article.getStatus() == ArticleStatus.chiefEditorReview){
            return "chief_editor/zs_detail";
        }else{
            return "chief_editor/zscs_detail";
        }
    }

    @RequestMapping(value = "/chiefAccept", method = RequestMethod.POST)
    @ResponseBody
    public String chiefAccept(int articleID, String ZS_content, HttpSession session){
        Article article = articleService.getArticleById(articleID);
        int reviewerID = (Integer) session.getAttribute("uid");
        Review review = new Review();
        review.setReviewerId(reviewerID);
        review.setArticleId(articleID);
        review.setContent(ZS_content);
        if(article.getStatus() == ArticleStatus.chiefEditorReview){
            review.setType(ReviewType.finalReview);
        }else{
            review.setType(ReviewType.finalRebuttalReview);
        }
        chiefEditorService.giveReviewToArticle(review, ReviewResult.pass);
        return "success";
    }

    @RequestMapping(value = "/chiefRejection", method = RequestMethod.POST)
    @ResponseBody
    public String chiefRejection(int articleID, String ZS_content, HttpSession session){
        Article article = articleService.getArticleById(articleID);
        int reviewerID = (Integer) session.getAttribute("uid");
        Review review = new Review();
        review.setReviewerId(reviewerID);
        review.setArticleId(articleID);
        review.setContent(ZS_content);
        if(article.getStatus() == ArticleStatus.chiefEditorReview){
            review.setType(ReviewType.finalReview);
        }else{
            review.setType(ReviewType.finalRebuttalReview);
        }
        chiefEditorService.giveReviewToArticle(review, ReviewResult.reject);
        return "success";
    }

    @RequestMapping(value = "/chiefRevision", method = RequestMethod.POST)
    @ResponseBody
    public String chiefRevision(int articleID, String ZS_content, HttpSession session){
        Article article = articleService.getArticleById(articleID);
        int reviewerID = (Integer) session.getAttribute("uid");
        Review review = new Review();
        review.setReviewerId(reviewerID);
        review.setArticleId(articleID);
        review.setContent(ZS_content);
        if(article.getStatus() == ArticleStatus.chiefEditorReview){
            review.setType(ReviewType.finalReview);
        }else{
            review.setType(ReviewType.finalRebuttalReview);
        }
        chiefEditorService.giveReviewToArticle(review, ReviewResult.revise);
        return "success";
    }
}
