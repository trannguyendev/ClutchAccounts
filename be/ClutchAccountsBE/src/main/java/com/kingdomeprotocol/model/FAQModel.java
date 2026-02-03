package com.kingdomeprotocol.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "FAQ")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FAQModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	
	String title;
	
	String content;
	
	LocalDateTime created_at;
	
	LocalDateTime updated_at;
	
	int like_amount;
	
	int dislike_amount;
}
