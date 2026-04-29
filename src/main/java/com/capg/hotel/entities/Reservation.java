package com.capg.hotel.entities;

import java.time.LocalDate;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Integer reservationId;

    @Column(name = "guest_name")
    @NotBlank(message = "Guest name cannot be empty")
    @Size(min = 2, max = 50, message = "Guest name must be between 2 and 50 characters")
    private String guestName;

    @Column(name = "guest_email")
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String guestEmail;

    @Column(name = "guest_phone")
    @NotBlank(message = "Phone number cannot be empty")
    @Pattern(regexp = "^[6-9][0-9]{9}$", message = "Invalid Indian phone number")
    private String guestPhone;

    @Column(name = "check_in_date")
    @NotNull(message = "Check-in date cannot be null")
    @FutureOrPresent(message = "Check-in date cannot be in the past")
    private LocalDate checkInDate;

    @Column(name = "check_out_date")
    @NotNull(message = "Check-out date cannot be null")
    @Future(message = "Check-out date must be in the future")
    private LocalDate checkOutDate;

    @ManyToOne
    @JoinColumn(name = "room_id")
    @NotNull(message = "Room must be assigned")
    private Room room;

	public Integer getReservationId() {
		return reservationId;
	}

	public void setReservationId(Integer reservationId) {
		this.reservationId = reservationId;
	}

	public String getGuestName() {
		return guestName;
	}

	public void setGuestName(String guestName) {
		this.guestName = guestName;
	}

	public String getGuestEmail() {
		return guestEmail;
	}

	public void setGuestEmail(String guestEmail) {
		this.guestEmail = guestEmail;
	}

	public String getGuestPhone() {
		return guestPhone;
	}

	public void setGuestPhone(String guestPhone) {
		this.guestPhone = guestPhone;
	}

	public LocalDate getCheckInDate() {
		return checkInDate;
	}

	public void setCheckInDate(LocalDate checkInDate) {
		this.checkInDate = checkInDate;
	}

	public LocalDate getCheckOutDate() {
		return checkOutDate;
	}

	public void setCheckOutDate(LocalDate checkOutDate) {
		this.checkOutDate = checkOutDate;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}


	public Reservation(Integer reservationId, String guestName, String guestEmail, String guestPhone,
			LocalDate checkInDate, LocalDate checkOutDate, Room room) {
		super();
		this.reservationId = reservationId;
		this.guestName = guestName;
		this.guestEmail = guestEmail;
		this.guestPhone = guestPhone;
		this.checkInDate = checkInDate;
		this.checkOutDate = checkOutDate;
		this.room = room;
	}

	public Reservation() {
	}
	
}