# Publish once

Subscribers are notified on the **first successful publication** only. The lock is a conditional update:

```sql
update articles
set status = 'PUBLISHED', first_published_at = current_timestamp
where id = :id and first_published_at is null
```

The claim and the notice inserts share one transaction. The layout is in [docs/architecture.md](docs/architecture.md).

- One row updated: this transaction owns the first publication and inserts one notice per subscriber.
- Zero rows updated: someone already published it. The call inserts nothing.
- If inserting a notice fails, the transaction rolls back. `first_published_at` stays null, so a later attempt can notify.

A second publish of the same article leaves the notice count unchanged.

## Run

Java 21.

```sh
./mvnw spring-boot:run
```

```sh
curl -sS -X POST http://127.0.0.1:8080/api/articles/1/publish
curl -sS -X POST http://127.0.0.1:8080/api/articles/1/publish
```

The first response has `"firstPublication": true`. The second has `"firstPublication": false` and the same notice count.

## Tests

```sh
./mvnw test
```

## What this is

A portfolio proof of concept. It does not send the notices anywhere.
