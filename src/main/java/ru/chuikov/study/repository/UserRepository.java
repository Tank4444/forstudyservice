package ru.chuikov.study.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.chuikov.study.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    @Query("select u from User u where LOWER(u.username) = lower(?1) ")
    Optional<User> findUserByUsername(String username);

    @Query("select u from User u where LOWER(u.email) = lower(?1) ")
    Optional<User> findUserByEmail(String email);

    @Query("select u from User u where u.id = ?1 ")
    Optional<User> findUserById(Long id);
}
