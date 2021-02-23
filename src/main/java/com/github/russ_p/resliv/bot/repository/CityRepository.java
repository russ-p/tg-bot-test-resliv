package com.github.russ_p.resliv.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.russ_p.resliv.bot.entity.City;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

}
