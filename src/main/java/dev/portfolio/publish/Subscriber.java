package dev.portfolio.publish;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "subscribers")
public class Subscriber {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 254)
	private String email;

	protected Subscriber() {
	}

	public Subscriber(String email) {
		this.email = email;
	}

	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}
}
