//package com.app.grip.src.product;
//
//import com.app.grip.config.BaseEntity;
//import com.app.grip.src.review.ReviewInfo;
//import lombok.*;
//import lombok.experimental.Accessors;
//
//import javax.persistence.*;
//import java.util.List;
//
//@Accessors(chain = true)
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor(access = AccessLevel.PUBLIC)
//@EqualsAndHashCode(callSuper = false)
//@Data
//@Entity
//@Table(name = "productCategory")
//public class ProductCategoryInfo extends BaseEntity {
//    @Id
//    @Column(name = "id", nullable = false, updatable = false)
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(name = "name", nullable = false)
//    private String name;
//
//    @OneToMany(mappedBy = "productCategory", orphanRemoval = true, cascade = CascadeType.ALL)
//    private List<ProductInfo> productInfoList;
//}