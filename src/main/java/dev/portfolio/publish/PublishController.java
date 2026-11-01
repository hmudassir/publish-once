package dev.portfolio.publish;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PublishController {

	private final PublishService publisher;

	public PublishController(PublishService publisher) {
		this.publisher = publisher;
	}

	@PostMapping("/api/articles/{id}/publish")
	public PublishService.PublishResult publish(@PathVariable Long id) {
		return publisher.publish(id);
	}
}
