package com.app.grip.src.review.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetReviewRes {
    private final Long reviewId;
    private final String storeName;
    private final String userName;
    private final Integer star;
    private final String content;
    private final List<PictureRes> picture;
    private final String createDate;
    private final String updateDate;
    private final String status;
}