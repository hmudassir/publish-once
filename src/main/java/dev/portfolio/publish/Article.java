package dev.portfolio.publish;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "articles")
public class Article {

	public enum Status {
		DRAFT,
		PUBLISHED
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 200)
	private String title;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private Status status;

	@Column(name = "first_published_at")
	private Instant firstPublishedAt;

	protected Article() {
	}

	public Article(String title) {
		this.title = title;
		this.status = Status.DRAFT;
	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public Status getStatus() {
		return status;
	}

	public Instant getFirstPublishedAt() {
		return firstPublishedAt;
	}
}
