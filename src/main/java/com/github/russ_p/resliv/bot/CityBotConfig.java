package com.github.russ_p.resliv.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.russ_p.resliv.bot.repository.CityRepository;
import com.pengrad.telegrambot.TelegramBot;

@Configuration
@ConditionalOnProperty(name = "app.telegram.bot.enable")
public class CityBotConfig {

	@Value("${app.telegram.bot.token:''}")
	private String token;

	@Autowired
	private CityRepository cityRepository;

	@Autowired
	private ApplicationContext applicationContext;

	@Bean
	public TelegramBot createTelegramBot() {
		return new CityBot(token, cityRepository, applicationContext);
	}

}
