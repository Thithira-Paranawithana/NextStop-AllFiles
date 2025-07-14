package com.example.booking.booking_service;

import com.example.booking.booking_service.model.Booking;
import com.example.booking.booking_service.service.BookingService;
import com.example.booking.booking_service.dto.BookingRequest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureDataMongo
class BookingServiceApplicationTests {

	@Autowired
	BookingService svc;

//	@Test
//	void createReturnsSavedBooking() {
//		Booking b = svc.create(new BookingRequest(
//				"u1", "t1", 1,
//				BigDecimal.valueOf(1000), "LKR"));
//		assertThat(b.getId()).isNotNull();
//	}

}
