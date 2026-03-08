package com.gruposcout21.birthdayreminder.repository;

import org.springframework.stereotype.Repository;

import com.gruposcout21.birthdayreminder.entity.Person;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
    @Query(value = "SELECT * FROM person " +
                   "WHERE MONTH(birth_date) = :month " +
                   "AND DAY(birth_date) = :day",
           nativeQuery = true)
    List<Person> findByBirthday(@Param("month") int month, @Param("day") int day);

}
