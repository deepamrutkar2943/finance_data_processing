package com.example.fdp.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.fdp.model.TransactionType;

public class RecordResponse {

	private Long id;
	private BigDecimal amount;
	private TransactionType type;
	private String category;
	private LocalDate date;
	private String notes;
	private String createdByName;
	private LocalDateTime createdAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public TransactionType getType() {
		return type;
	}

	public void setType(TransactionType type) {
		this.type = type;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getCreatedByName() {
		return createdByName;
	}

	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public RecordResponse(Long id, BigDecimal amount, TransactionType type, String category, LocalDate date,
			String notes, String createdByName, LocalDateTime createdAt) {
		super();
		this.id = id;
		this.amount = amount;
		this.type = type;
		this.category = category;
		this.date = date;
		this.notes = notes;
		this.createdByName = createdByName;
		this.createdAt = createdAt;
	}

	public RecordResponse() {
		super();
	}

}
