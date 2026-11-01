package dev.portfolio.publish;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArticleRepository extends JpaRepository<Article, Long> {

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query(value = """
			update articles
			set status = 'PUBLISHED', first_published_at = current_timestamp
			where id = :id and first_published_at is null
			""", nativeQuery = true)
	int claimFirstPublication(@Param("id") Long id);
}
