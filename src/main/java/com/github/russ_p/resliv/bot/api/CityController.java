package com.github.russ_p.resliv.bot.api;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.russ_p.resliv.bot.entity.City;
import com.github.russ_p.resliv.bot.entity.CityInfo;
import com.github.russ_p.resliv.bot.repository.CityRepository;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

@Api
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/cities")
public class CityController {

	private final CityRepository cityRepository;

	@GetMapping
	public List<City> getAll() {
		// TODO: pagination
		return cityRepository.findAll();
	}

	@Transactional(readOnly = true)
	@GetMapping("{id}/info")
	public String getInfo(@PathVariable("id") Long id) {
		return cityRepository.findById(id)
				.map(city -> {
					return Optional.ofNullable(city.getInfo())
							.map(CityInfo::getText)
							.orElse("");
				})
				.orElseThrow(NotFoundException::new);
	}

	@Transactional
	@PutMapping("{id}/info")
	public void setInfo(@PathVariable("id") Long id, @RequestBody String text) {
		cityRepository.findById(id)
				.map(city -> {
					if (city.getInfo() == null) {
						city.setInfo(new CityInfo());
						city.getInfo().setCity(city);
					}
					city.getInfo().setText(text);
					return cityRepository.save(city);
				})
				.orElseThrow(NotFoundException::new);
	}

	@PostMapping
	public City create(@RequestBody @Valid City newCity) {
		newCity.setId(null);
		return cityRepository.save(newCity);
	}

	@PutMapping("{id}")
	public City update(@PathVariable("id") Long id, @RequestBody @Valid City updatedCity) {
		return cityRepository.findById(id)
				.map(city -> {
					city.setTitle(updatedCity.getTitle());
					return cityRepository.save(city);
				})
				.orElseThrow(NotFoundException::new);
	}

	@DeleteMapping("{id}")
	public void delete(@PathVariable("id") Long id) {
		cityRepository.findById(id)
				.map(city -> {
					cityRepository.delete(city);
					return true;
				})
				.orElseThrow(NotFoundException::new);
	}

}
