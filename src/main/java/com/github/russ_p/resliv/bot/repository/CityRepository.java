package com.github.russ_p.resliv.bot.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.github.russ_p.resliv.bot.entity.City;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

	@Query("SELECT info.text FROM City c"
			+ " INNER JOIN c.info info"
			+ " WHERE LOWER(c.title) = LOWER(:city) ")
	Optional<String> findCityInfoByCityTitleIgnoreCase(String city);

}
