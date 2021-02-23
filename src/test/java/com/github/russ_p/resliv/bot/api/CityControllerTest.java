package com.github.russ_p.resliv.bot.api;

import static com.github.russ_p.resliv.bot.TestUtils.json;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.github.russ_p.resliv.bot.entity.City;
import com.github.russ_p.resliv.bot.entity.CityInfo;
import com.github.russ_p.resliv.bot.repository.CityRepository;

@WebMvcTest(value = CityController.class)
public class CityControllerTest {

	@MockBean
	private CityRepository cityRepository;

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testGetAll() throws Exception {
		City city = new City();

		when(cityRepository.findAll())
				.thenReturn(Arrays.asList(city));

		mockMvc.perform(get("/api/v1/cities"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)));
	}

	@Test
	public void testCreate_BadReqest() throws Exception {
		City city = new City();

		mockMvc.perform(post("/api/v1/cities")
				.content(json(city))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testCreate_Ok() throws Exception {
		City city = new City();
		city.setTitle("Minsk");

		mockMvc.perform(post("/api/v1/cities")
				.content(json(city))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	public void testUpdate_OK() throws Exception {
		City city = new City();
		city.setId(1L);
		city.setTitle("Minsk");

		when(cityRepository.findById(1L))
				.thenReturn(Optional.of(city));

		City updatedCity = new City();
		updatedCity.setTitle("New Minsk");

		when(cityRepository.save(Mockito.any(City.class)))
				.thenAnswer(invMock -> invMock.getArgument(0));

		mockMvc.perform(put("/api/v1/cities/1")
				.content(json(updatedCity))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	public void testUpdate_NotFound() throws Exception {
		City updatedCity = new City();
		updatedCity.setTitle("New Minsk");

		mockMvc.perform(put("/api/v1/cities/100500")
				.content(json(updatedCity))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testUpdate_BadReq() throws Exception {
		City city = new City();
		city.setId(1L);
		city.setTitle("Minsk");

		when(cityRepository.findById(1L))
				.thenReturn(Optional.of(city));

		City updatedCity = new City();
		updatedCity.setTitle("");

		// вернем аргумент вместо null
		when(cityRepository.save(Mockito.any(City.class)))
				.thenAnswer(invMock -> invMock.getArgument(0));

		mockMvc.perform(put("/api/v1/cities/1")
				.content(json(updatedCity))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testDelete_OK() throws Exception {
		City city = new City();

		when(cityRepository.findById(1L))
				.thenReturn(Optional.of(city));

		mockMvc.perform(delete("/api/v1/cities/1"))
				.andExpect(status().isOk());

		verify(cityRepository).delete(city);
	}

	@Test
	public void testDelete_NotFound() throws Exception {
		mockMvc.perform(delete("/api/v1/cities/100500"))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testGetInfo_thatReturnsText() throws Exception {
		City city = new City();
		city.setId(1L);
		city.setTitle("Minsk");
		city.setInfo(new CityInfo());
		city.getInfo().setText("text");

		when(cityRepository.findById(1L))
				.thenReturn(Optional.of(city));

		mockMvc.perform(get("/api/v1/cities/1/info"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", equalTo("text")));
	}

	@Test
	public void testGetInfo_thatReturnsEmptyStringByDefault() throws Exception {
		City city = new City();
		city.setId(1L);
		city.setTitle("Minsk");

		when(cityRepository.findById(1L))
				.thenReturn(Optional.of(city));

		mockMvc.perform(get("/api/v1/cities/1/info"))
				.andExpect(status().isOk())
				.andExpect(content().string(""));
	}

	@Test
	public void testSetInfo() throws Exception {
		City city = new City();
		city.setId(1L);
		city.setTitle("Minsk");

		when(cityRepository.findById(1L))
				.thenReturn(Optional.of(city));
		// вернем аргумент вместо null
		when(cityRepository.save(Mockito.any(City.class)))
				.thenAnswer(invMock -> invMock.getArgument(0));

		mockMvc.perform(put("/api/v1/cities/1/info")
				.content("text"))
				.andExpect(status().isOk());

		assertThat(city.getInfo()).isNotNull();
		assertThat(city.getInfo().getText()).isEqualTo("text");
	}

}
