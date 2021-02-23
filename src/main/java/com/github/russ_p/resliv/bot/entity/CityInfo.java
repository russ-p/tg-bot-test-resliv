package com.github.russ_p.resliv.bot.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Ничего не сказано, может ли у города быть несколько записей со справочной
 * инфой (например, на разных языках, либо чтобы просто выдавать разные
 * случайные ответы), но использование отд. сущности упростит последующие
 * изменения
 *
 */
@Data
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "city_info")
public class CityInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty
	@Column(nullable = false, length = 10000)
	private String text;
}
