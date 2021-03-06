package com.app.grip.src.review.models;

import com.app.grip.config.BaseEntity;
import com.app.grip.src.product.models.Product;
import com.app.grip.src.user.models.User;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.List;

@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@Table(name = "review")
public class Review extends BaseEntity {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_no")
    private User user;

    @Column(name = "star", nullable = false)
    private Integer star;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @OneToMany(mappedBy = "review", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<ReviewPicture> reviewPictureList;

    public void setReviewPictureList(List<ReviewPicture> reviewPictureList) {
        this.reviewPictureList = reviewPictureList;

        for (ReviewPicture picture : reviewPictureList) {
            picture.setReview(this);
        }
    }

    public Review(Integer star, String content) {
        this.star = star;
        this.content = content;
    }
}