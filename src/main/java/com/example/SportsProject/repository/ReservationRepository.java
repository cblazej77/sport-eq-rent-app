package com.example.SportsProject.repository;

import com.example.SportsProject.entity.Reservation;
import com.example.SportsProject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByUser(User user);
}
