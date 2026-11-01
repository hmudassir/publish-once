package dev.portfolio.publish;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class PublishOnceTests {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private PublishService publisher;

	@Autowired
	private ArticleRepository articles;

	@Autowired
	private SubscriberRepository subscribers;

	@Autowired
	private NoticeRepository notices;

	@Test
	void onlyTheFirstPublicationInsertsNotices() throws Exception {
		mvc.perform(post("/api/articles/1/publish"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.firstPublication").value(true))
				.andExpect(jsonPath("$.notices").value(2));

		mvc.perform(post("/api/articles/1/publish"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.firstPublication").value(false))
				.andExpect(jsonPath("$.notices").value(2));

		assertThat(notices.countByArticleId(1L)).isEqualTo(2);
	}

	@Test
	void aFailedNoticeInsertRollsTheClaimBack() {
		Article article = articles.save(new Article("Retry me"));
		Subscriber failing = subscribers.save(new Subscriber("ada@fail.test"));

		assertThatThrownBy(() -> publisher.publish(article.getId()))
				.isInstanceOf(IllegalStateException.class);

		Article reloaded = articles.findById(article.getId()).orElseThrow();
		assertThat(reloaded.getFirstPublishedAt()).isNull();
		assertThat(reloaded.getStatus()).isEqualTo(Article.Status.DRAFT);
		assertThat(notices.countByArticleId(article.getId())).isZero();

		subscribers.delete(failing);
		PublishService.PublishResult result = publisher.publish(article.getId());
		assertThat(result.firstPublication()).isTrue();
		assertThat(result.notices()).isEqualTo(2);
	}
}
