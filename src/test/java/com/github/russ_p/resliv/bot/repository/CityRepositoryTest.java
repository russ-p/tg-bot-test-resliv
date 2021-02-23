package com.github.russ_p.resliv.bot.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.github.russ_p.resliv.bot.entity.City;
import com.github.russ_p.resliv.bot.entity.CityInfo;

@DataJpaTest
public class CityRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private CityRepository repository;

	@Test
		public void testFindCityInfoByCityTitleIgnoreCase() throws Exception {
			City city = new City();
			city.setTitle("Minsk");
			city.setInfo(new CityInfo());
			city.getInfo().setText("text");
	
			entityManager.persist(city);
	
			Optional<String> minskInfo = repository.findCityInfoByCityTitleIgnoreCase("minsk");
			Optional<String> notMinskInfo = repository.findCityInfoByCityTitleIgnoreCase("NotMinsk");
	
			assertThat(minskInfo).isNotEmpty();
			assertThat(notMinskInfo).isEmpty();
		}

}
