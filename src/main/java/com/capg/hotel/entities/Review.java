package com.capg.hotel.entities;

import java.time.LocalDate;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "Review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Integer reviewId;

    @Column(name = "rating")
    @NotNull(message = "Rating cannot be null")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot exceed 5")
    private Integer rating;

    @Column(name = "comment")
    @NotBlank(message = "Comment cannot be empty")
    @Size(min = 5, max = 300, message = "Comment must be between 5 and 300 characters")
    private String comment;

    @Column(name = "review_date")
    @NotNull(message = "Review date cannot be null")
    @PastOrPresent(message = "Review date cannot be in the future")
    private LocalDate reviewDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reservation_id")
    @NotNull(message = "Reservation must be associated with review")
    @JsonIgnoreProperties({"reviews", "hibernateLazyInitializer", "handler"})
    private Reservation reservation;

    public Integer getReviewId() {
        return reviewId;
    }
    public void setReviewId(Integer reviewId) {
        this.reviewId = reviewId;
    }
    public Integer getRating() {
        return rating;
    }
    public void setRating(Integer rating) {
        this.rating = rating;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public LocalDate getReviewDate() {
        return reviewDate;
    }
    public void setReviewDate(LocalDate reviewDate) {
        this.reviewDate = reviewDate;
    }
    public Reservation getReservation() {
        return reservation;
    }
    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }
    public Review(Integer reviewId, Integer rating, String comment, LocalDate reviewDate, Reservation reservation) {
        super();
        this.reviewId = reviewId;
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = reviewDate;
        this.reservation = reservation;
    }
    public Review() {
    }
}