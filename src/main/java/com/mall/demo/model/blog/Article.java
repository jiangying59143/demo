package com.mall.demo.model.blog;

import com.mall.demo.model.base.BaseTO;
import com.mall.demo.model.privilege.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
public class Article extends BaseTO {

    public static final int Article_TOP = 1;

    public static final int Article_Common = 0;

    /**
     *
     */
    private static final long serialVersionUID = -4470366380115322213L;

    @NotBlank
    @Column(name = "title", length = 40)
    private String title;

    @NotBlank
    @Column(name = "summary", length = 100)
    private String summary;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_user_id")
    private User author;

    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "body_id")
    private ArticleBody body;

    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "article_tag",
            joinColumns = {@JoinColumn(name = "article_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")})
    private List<Tag> tags;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "article", orphanRemoval = true)
    private List<Comment> comments;

    @Column(name = "comment_counts")
    private int commentCounts;

    @Column(name = "view_counts")
    private int viewCounts;

    /**
     * 置顶
     */
    private int weight = Article_Common;


    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public String getSummary() {
        return summary;
    }


    public void setSummary(String summary) {
        this.summary = summary;
    }

    public ArticleBody getBody() {
        return body;
    }


    public void setBody(ArticleBody body) {
        this.body = body;
    }


    public Category getCategory() {
        return category;
    }


    public void setCategory(Category category) {
        this.category = category;
    }


    public List<Tag> getTags() {
        return tags;
    }


    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }


    public List<Comment> getComments() {
        return comments;
    }


    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }


    public int getCommentCounts() {
        return commentCounts;
    }


    public void setCommentCounts(int commentCounts) {
        this.commentCounts = commentCounts;
    }


    public int getViewCounts() {
        return viewCounts;
    }


    public void setViewCounts(int viewCounts) {
        this.viewCounts = viewCounts;
    }


    public int getWeight() {
        return weight;
    }


    public void setWeight(int weight) {
        this.weight = weight;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
