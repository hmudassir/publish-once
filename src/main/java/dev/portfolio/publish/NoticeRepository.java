package dev.portfolio.publish;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

	long countByArticleId(Long articleId);
}
