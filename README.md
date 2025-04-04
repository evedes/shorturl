# shorturl
A URL Shortener App

## HOW TO RUN THE FRONTEND?

`npx shadow-cljs watch app`

## DATABASE

```bash
CREATE TABLE `redirects` (
	`slug` varchar(10) NOT NULL,
	`url` varchar(1000) NOT NULL,
	PRIMARY KEY (`slug`)
) ENGINE InnoDB,
  CHARSET utf8mb4,
  COLLATE utf8mb4_0900_ai_ci;
```

## REPL

- Backend REPL: `clj -M:nrepl`

- Frontend REPL: `clj -M:cljs`

