package edu.ecnu.journalmanage.controller;

import com.github.pagehelper.PageInfo;
import edu.ecnu.journalmanage.model.*;
import edu.ecnu.journalmanage.service.ArticleService;
import edu.ecnu.journalmanage.service.AuthorService;
import edu.ecnu.journalmanage.service.EditorService;
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
public class EditorController {
    @Autowired
    public EditorService editorService;

    @Autowired
    public ArticleService articleService;

    @Autowired
    public AuthorService authorService;

    @RequestMapping("/editorIndex")
    public String editorIndex(){
        return "editor/editor_index";
    }

    @RequestMapping("/editorOnline")
    public String editorOnline(HttpSession session, @RequestParam(defaultValue = "1") int UnbindpageNum,
                               @RequestParam(defaultValue = "1") int ToreviewpageNum,
                               @RequestParam(defaultValue = "1") int ReviewedpageNum,
                               @RequestParam(defaultValue = "5") int pageSize, Model model){
        int editorID = (Integer) session.getAttribute("uid");
        PageInfo<Article> allUnbindArticles = editorService.getAllUnbindArticlesPaged(UnbindpageNum, pageSize);
        PageInfo<Article> toReviewArticles = editorService.getToReviewArticlesPaged(editorID, ToreviewpageNum, pageSize);
        PageInfo<Article> reviewedArticles = editorService.getReviewedArticlesPaged(editorID, ReviewedpageNum, pageSize);
        model.addAttribute("allUnbindArticles", allUnbindArticles);
        model.addAttribute("toReviewArticles", toReviewArticles);
        model.addAttribute("reviewedArticles", reviewedArticles);
        return "editor/editor_online";
    }

    @RequestMapping("/editorChangeInfo")
    public String editorChangeInfo(){
        return "ChangeInfo";
    }

    @RequestMapping("/editorReview")
    public String editorReview(int articleID, Model model, @RequestParam(defaultValue = "1") int expageNum, @RequestParam(defaultValue = "5") int pageSize,
                               HttpSession session){
        Article eR_article = articleService.getArticleById(articleID);
        Map<ReviewType, List<Review>> reviewOfArticle = authorService.getReviewOfArticle(articleID);
        List<Review> CS_reviews = reviewOfArticle.get(ReviewType.preliminaryReview);
        List<Review> CSCS_reviews = reviewOfArticle.get(ReviewType.preliminaryRebuttalReview);
        PageInfo<User> allExpert = editorService.getAllExpertPaged(expageNum, pageSize);

        model.addAttribute("eR_article", eR_article);
        model.addAttribute("CS_reviews", CS_reviews);
        model.addAttribute("CSCS_reviews", CSCS_reviews);
        model.addAttribute("allExpert", allExpert);
        if(eR_article.getStatus() == ArticleStatus.editorReview){
            int editorID = (Integer) session.getAttribute("uid");
            editorService.bindEditor(articleID, editorID);
            return "editor/cs_detail";
        }else{
            return "editor/cscs_detail";
        }
    }

    @RequestMapping(value = "/editorAccept", method = RequestMethod.POST)
    @ResponseBody
    public String editorAccept(int articleID, String CS_content, int expertID, HttpSession session){
        Article article = articleService.getArticleById(articleID);
        int reviewerID = (Integer) session.getAttribute("uid");
        Review review = new Review();
        review.setReviewerId(reviewerID);
        review.setArticleId(articleID);
        review.setContent(CS_content);
        editorService.assignExpert(articleID, expertID);
        if(article.getStatus() == ArticleStatus.editorReview){
            review.setType(ReviewType.preliminaryReview);
        }else{
            review.setType(ReviewType.preliminaryRebuttalReview);
        }
        editorService.giveReviewToArticle(review, ReviewResult.pass);
        return "success";
    }

    @RequestMapping(value = "/editorRejection", method = RequestMethod.POST)
    @ResponseBody
    public String editorRejection(int articleID, String CS_content, HttpSession session){
        Article article = articleService.getArticleById(articleID);
        int reviewerID = (Integer) session.getAttribute("uid");
        Review review = new Review();
        review.setReviewerId(reviewerID);
        review.setArticleId(articleID);
        review.setContent(CS_content);
        if(article.getStatus() == ArticleStatus.editorReview){
            review.setType(ReviewType.preliminaryReview);
        }else{
            review.setType(ReviewType.preliminaryRebuttalReview);
        }
        editorService.giveReviewToArticle(review, ReviewResult.reject);
        return "success";
    }

    @RequestMapping(value = "/editorRevision", method = RequestMethod.POST)
    @ResponseBody
    public String editorRevision(int articleID, String CS_content, HttpSession session){
        Article article = articleService.getArticleById(articleID);
        int reviewerID = (Integer) session.getAttribute("uid");
        Review review = new Review();
        review.setReviewerId(reviewerID);
        review.setArticleId(articleID);
        review.setContent(CS_content);
        if(article.getStatus() == ArticleStatus.editorReview){
            review.setType(ReviewType.preliminaryReview);
        }else{
            review.setType(ReviewType.preliminaryRebuttalReview);
        }
        editorService.giveReviewToArticle(review, ReviewResult.revise);
        return "success";
    }
}
