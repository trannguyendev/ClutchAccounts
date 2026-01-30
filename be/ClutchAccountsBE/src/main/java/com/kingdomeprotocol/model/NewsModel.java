package com.kingdomeprotocol.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "News")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty("news_id")
	int news_id;
	
	@JsonProperty("title")
	String title;
	
	@JsonProperty("content")
	String content;
	
	@JsonProperty("embed_link")
	String embed_link;
	
	@JsonProperty("created_at")
	LocalDateTime created_at;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "author_id")
	UserModel author_id;
}
