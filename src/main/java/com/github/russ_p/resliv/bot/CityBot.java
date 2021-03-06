package com.github.russ_p.resliv.bot;

import java.util.Locale;

import javax.annotation.PreDestroy;

import org.springframework.context.MessageSource;

import com.github.russ_p.resliv.bot.repository.CityRepository;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CityBot extends TelegramBot {

	private static final String START = "/start";

	private final CityRepository cityRepository;

	private final String helloMessage;
	private final String notFoundMessage;

	public CityBot(String botToken, CityRepository cityRepository, MessageSource messageSource) {
		super(botToken);
		this.cityRepository = cityRepository;
		this.helloMessage = messageSource.getMessage("bot.hello", null, Locale.getDefault());
		this.notFoundMessage = messageSource.getMessage("bot.not.found", null, Locale.getDefault());

		this.setUpdatesListener(updates -> {
			for (Update update : updates) {
				try {
					processUpdate(update);
				} catch (Exception e) {
					log.error("Bot Error:", e);
				}
			}
			return UpdatesListener.CONFIRMED_UPDATES_ALL;
		});
	}

	private void processUpdate(Update update) {
		if (update.message() == null) {
			return;
		}
		String text = update.message().text();
		if (text != null) {
			if (START.equalsIgnoreCase(text)) {
				execute(createSendMessage(update.message().chat().id(), helloMessage));
				return;
			}
			String answer = cityRepository.findCityInfoByCityTitleIgnoreCase(text).orElse(notFoundMessage);
			execute(createSendMessage(update.message().chat().id(), answer));
		}
	}

	@PreDestroy
	public void onDestroy() throws Exception {
		removeGetUpdatesListener();
	}

	private SendMessage createSendMessage(Long chatId, String text, KeyboardButton... btns) {
		return new SendMessage(chatId, text)
				.disableWebPagePreview(true)
				.disableNotification(true)
				.replyMarkup(new ReplyKeyboardMarkup(btns));
	}
}
