# Architecture

The first publication is a conditional update. The transaction that changes the row from "never published" to "published" is the one that inserts notices. A later publish finds the timestamp already set and inserts nothing.

## Boundaries

```text
POST /api/articles/{id}/publish
        |
        v
PublishService                         one transaction
        |
        +--> update articles
        |      set status = PUBLISHED,
        |          first_published_at = current_timestamp
        |      where id = :id
        |        and first_published_at is null
        |
        +--> one row updated
        |      insert one notice per subscriber
        |
        +--> zero rows updated
               insert nothing
```

The update is the lock. Two callers can both try. One updates the row. The other updates zero rows and does not notify.

## Request path

1. A missing article is `404` before the claim.
2. The claim runs. The database applies the `where first_published_at is null` check as part of the update.
3. When the claim updates one row, the service inserts one notice for each subscriber in the same transaction.
4. When the claim updates nothing, the article was already published. The response says `firstPublication: false` and the notice count stays as it was.

## Failure

If inserting a notice throws, the transaction rolls back. `first_published_at` stays null and no notice remains. A later publish can claim the row and notify. A failed attempt must not look like a finished first publication, or the notices would never be sent.

A second successful publish does not add notices. The unique key `(article_id, subscriber_id)` is a backstop against a duplicate row for the same pair.

## What this sample leaves out

Subscribers are everyone in the table. A larger system would choose the recipient list inside the same transaction, after the claim, from the rows that are allowed to hear about that article. The claim would stay the same statement.
