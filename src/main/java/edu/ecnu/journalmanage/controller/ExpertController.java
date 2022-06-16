package edu.ecnu.journalmanage.controller;

import com.github.pagehelper.PageInfo;
import edu.ecnu.journalmanage.model.*;
import edu.ecnu.journalmanage.service.ArticleService;
import edu.ecnu.journalmanage.service.AuthorService;
import edu.ecnu.journalmanage.service.ExpertService;
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
public class ExpertController {
    @Autowired
    public ExpertService expertService;

    @Autowired
    public ArticleService articleService;

    @Autowired
    public AuthorService authorService;

    @RequestMapping("/expertIndex")
    public String expertIndex(){
        return "expert/expert_index";
    }

    @RequestMapping("/expertOnline")
    public String expertOnline(Model model, HttpSession session, @RequestParam(defaultValue = "1") int ExTopageNum,
                               @RequestParam(defaultValue = "1") int ExEdpageNum,
                               @RequestParam(defaultValue = "5") int pageSize){
        int expertID = (Integer) session.getAttribute("uid");
        PageInfo<Article> ExtoArticles = expertService.getToReviewArticlesPaged(expertID, ExTopageNum, pageSize);
        PageInfo<Article> ExedArticles = expertService.getReviewedArticlesPaged(expertID, ExEdpageNum, pageSize);
        model.addAttribute("ExtoArticles", ExtoArticles);
        model.addAttribute("ExedArticles", ExedArticles);
        return "expert/expert_online";
    }

    @RequestMapping("/expertChangeInfo")
    public String expertChangeInfo(){
        return "ChangeInfo";
    }

    @RequestMapping("/expertReview")
    public String expertReview(int articleID, Model model){
        Article eX_article = articleService.getArticleById(articleID);
        Map<ReviewType, List<Review>> reviewOfArticle = authorService.getReviewOfArticle(articleID);
        List<Review> CS_reviews = reviewOfArticle.get(ReviewType.preliminaryReview);
        List<Review> CSCS_reviews = reviewOfArticle.get(ReviewType.preliminaryRebuttalReview);
        List<Review> WS_reviews = reviewOfArticle.get(ReviewType.externalReview);
        List<Review> WSCS_reviews = reviewOfArticle.get(ReviewType.externalRebuttalReview);
        model.addAttribute("eX_article", eX_article);
        model.addAttribute("CS_reviews", CS_reviews);
        model.addAttribute("CSCS_reviews", CSCS_reviews);
        model.addAttribute("WS_reviews", WS_reviews);
        model.addAttribute("WSCS_reviews", WSCS_reviews);
        if(eX_article.getStatus() == ArticleStatus.expertReview){
            return "expert/ws_detail";
        }else{
            return "expert/wscs_detail";
        }
    }

    @RequestMapping(value = "/expertAccept", method = RequestMethod.POST)
    @ResponseBody
    public String expertAccept(int articleID, String WS_content, HttpSession session){
        Article article = articleService.getArticleById(articleID);
        int reviewerID = (Integer) session.getAttribute("uid");
        Review review = new Review();
        review.setReviewerId(reviewerID);
        review.setArticleId(articleID);
        review.setContent(WS_content);
        if(article.getStatus() == ArticleStatus.expertReview){
            review.setType(ReviewType.externalReview);
        }else{
            review.setType(ReviewType.externalRebuttalReview);
        }
        expertService.giveReviewToArticle(review, ReviewResult.pass);
        return "success";
    }

    @RequestMapping(value = "/expertRejection", method = RequestMethod.POST)
    @ResponseBody
    public String expertRejection(int articleID, String WS_content, HttpSession session){
        Article article = articleService.getArticleById(articleID);
        int reviewerID = (Integer) session.getAttribute("uid");
        Review review = new Review();
        review.setReviewerId(reviewerID);
        review.setArticleId(articleID);
        review.setContent(WS_content);
        if(article.getStatus() == ArticleStatus.expertReview){
            review.setType(ReviewType.externalReview);
        }else{
            review.setType(ReviewType.externalRebuttalReview);
        }
        expertService.giveReviewToArticle(review, ReviewResult.reject);
        return "success";
    }

    @RequestMapping(value = "/expertRevision", method = RequestMethod.POST)
    @ResponseBody
    public String expertRevision(int articleID, String WS_content, HttpSession session){
        Article article = articleService.getArticleById(articleID);
        int reviewerID = (Integer) session.getAttribute("uid");
        Review review = new Review();
        review.setReviewerId(reviewerID);
        review.setArticleId(articleID);
        review.setContent(WS_content);
        if(article.getStatus() == ArticleStatus.expertReview){
            review.setType(ReviewType.externalReview);
        }else{
            review.setType(ReviewType.externalRebuttalReview);
        }
        expertService.giveReviewToArticle(review, ReviewResult.revise);
        return "success";
    }
}
