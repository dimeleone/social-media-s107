package com.inatel.s107.socialmedia.jpa;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inatel.s107.socialmedia.model.User;

public interface UserRepository extends JpaRepository<User, UUID> {
}
