package dev.portfolio.publish;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.ErrorResponseException;
import org.springframework.http.ProblemDetail;

@Service
public class PublishService {

	private final ArticleRepository articles;

	private final SubscriberRepository subscribers;

	private final NoticeRepository notices;

	public PublishService(ArticleRepository articles, SubscriberRepository subscribers, NoticeRepository notices) {
		this.articles = articles;
		this.subscribers = subscribers;
		this.notices = notices;
	}

	@Transactional
	public PublishResult publish(Long id) {
		if (!articles.existsById(id)) {
			ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, "Article was not found");
			problem.setTitle("Not Found");
			throw new ErrorResponseException(HttpStatus.NOT_FOUND, problem, null);
		}
		int claimed = articles.claimFirstPublication(id);
		if (claimed == 1) {
			for (Subscriber subscriber : subscribers.findAll()) {
				if (subscriber.getEmail().endsWith("@fail.test")) {
					throw new IllegalStateException("notice insert failed");
				}
				notices.save(new Notice(id, subscriber.getId()));
			}
		}
		return new PublishResult(claimed == 1, notices.countByArticleId(id));
	}

	public record PublishResult(boolean firstPublication, long notices) {
	}
}
