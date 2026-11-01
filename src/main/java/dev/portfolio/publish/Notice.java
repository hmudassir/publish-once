package dev.portfolio.publish;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "notices", uniqueConstraints = @UniqueConstraint(columnNames = { "article_id", "subscriber_id" }))
public class Notice {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "article_id", nullable = false)
	private Long articleId;

	@Column(name = "subscriber_id", nullable = false)
	private Long subscriberId;

	protected Notice() {
	}

	public Notice(Long articleId, Long subscriberId) {
		this.articleId = articleId;
		this.subscriberId = subscriberId;
	}

	public Long getArticleId() {
		return articleId;
	}

	public Long getSubscriberId() {
		return subscriberId;
	}
}
