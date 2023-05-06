package com.example.marketgospring.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "storereview")
public class StoreReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer storeReviewId;
    @ManyToOne
    @JoinColumn(name = "storeId")
    private Store storeId;
    @ManyToOne
    @JoinColumn(name = "store_review_member")
    private Member memberId;
    @Column
    private String memberName;
    @Column
    private Float ratings;
    @Column
    private String reviewContent;
    @Column
    private LocalDateTime reviewDate;
    @OneToOne
    @JoinColumn(name = "store_review_file")
    private S3File storeReviewFile;
}
