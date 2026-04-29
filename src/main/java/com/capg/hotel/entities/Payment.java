package com.capg.hotel.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Payment")
public class Payment {

    @Id
    @Column(name = "payment_id")
    @NotNull(message = "Payment ID cannot be null")
    private Integer paymentId;

    @Column(name = "amount")
    @NotNull(message = "Amount cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0")
    private BigDecimal amount;

    @Column(name = "payment_date")
    @NotNull(message = "Payment date cannot be null")
    @PastOrPresent(message = "Payment date cannot be in the future")
    private LocalDate paymentDate;

    @Column(name = "payment_status")
    @NotNull(message = "Payment status cannot be null")
    @Size(min = 3, max = 20, message = "Payment status must be between 3 and 20 characters")
    @Pattern(regexp = "SUCCESS|FAILED|PENDING", message = "Payment status must be SUCCESS, FAILED, or PENDING")
    private String paymentStatus;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    @NotNull(message = "Reservation cannot be null")
    private Reservation reservation;

	public Integer getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(Integer paymentId) {
		this.paymentId = paymentId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public LocalDate getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(LocalDate paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public Reservation getReservation() {
		return reservation;
	}

	public void setReservation(Reservation reservation) {
		this.reservation = reservation;
	}

	public Payment(Integer paymentId, BigDecimal amount, LocalDate paymentDate, String paymentStatus,
			Reservation reservation) {
		super();
		this.paymentId = paymentId;
		this.amount = amount;
		this.paymentDate = paymentDate;
		this.paymentStatus = paymentStatus;
		this.reservation = reservation;
	}

	public Payment() {

	}
}