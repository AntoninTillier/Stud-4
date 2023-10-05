/*
The problem

You will find an archive containing 2 record files with the following contents:
- The file categories.tsv has 2 columns separated by a tabulation:
    - the first one is the name of a Wikipedia article
    - the second one is the category of that article. An article can have multiple categories, and some articles may have no category.
- The file links.tsv has 2 columns separated by a tabulation, both containing names of Wikipedia articles.
This file describes hyperlinks between articles. The first column indicates the source of the link, and the second column indicates the target of the link.
*/

-- Create the schema for the CATEGORIES table
CREATE TABLE CATEGORIES
(
    article_name VARCHAR(255),
    category     VARCHAR(255)
);

-- Import data from categories.tsv into the CATEGORIES table
COPY CATEGORIES FROM '/path/to/categories.tsv' DELIMITER E '\t' CSV HEADER;

-- Create the schema for the LINKS table
CREATE TABLE LINKS
(
    source_article VARCHAR(255),
    target_article VARCHAR(255)
);

-- Import data from links.tsv into the LINKS table
COPY LINKS FROM '/path/to/links.tsv' DELIMITER E '\t' CSV HEADER;

-- 5: Number of articles in each category
SELECT category, COUNT(DISTINCT article_name) AS article_count
FROM CATEGORIES
GROUP BY category
ORDER BY article_count DESC;

-- 6: Number of distinct articles
SELECT COUNT(DISTINCT article_name) AS distinct_articles
FROM CATEGORIES;

-- 7: Number of articles with no category
SELECT COUNT(*) AS articles_with_no_category
FROM CATEGORIES
WHERE category IS NULL;

-- 8: Number of articles with no links to other articles
SELECT COUNT(*) AS articles_with_no_links
FROM CATEGORIES AS c
         LEFT JOIN LINKS AS l ON c.article_name = l.source_article
WHERE l.source_article IS NULL;

-- 9: Number of articles with direct links to each other
SELECT COUNT(*) AS articles_with_direct_links
FROM LINKS AS l1
WHERE EXISTS (SELECT 1
              FROM LINKS AS l2
              WHERE l1.source_article = l2.target_article
                AND l1.target_article = l2.source_article);

-- 10: Articles with the highest number of links to other articles
SELECT source_article, COUNT(*) AS link_count
FROM LINKS
GROUP BY source_article
ORDER BY link_count DESC LIMIT 5;

-- 11: Articles that are targets of the most links
SELECT target_article, COUNT(*) AS link_count
FROM LINKS
GROUP BY target_article
ORDER BY link_count DESC LIMIT 5;

-- 12: Articles with direct links to themselves
SELECT DISTINCT source_article
FROM LINKS
WHERE source_article = target_article;

-- 13: Articles with only direct links to themselves
SELECT DISTINCT source_article
FROM LINKS AS l1
WHERE NOT EXISTS (SELECT 1
                  FROM LINKS AS l2
                  WHERE l1.source_article = l2.target_article
                    AND l1.target_article != l2.source_article);

-- 14: Create views "Countries" and "Maths"
CREATE VIEW Countries AS
SELECT *
FROM LINKS
WHERE source_article IN (SELECT article_name
                         FROM CATEGORIES
                         WHERE category = 'subject.Countries');

CREATE VIEW Maths AS
SELECT *
FROM LINKS
WHERE target_article IN (SELECT article_name
                         FROM CATEGORIES
                         WHERE category = 'subject.Mathematics');

-- 15: Articles in "Countries" with indirect links to themselves but no direct links
SELECT DISTINCT l.source_article
FROM Countries AS l
         LEFT JOIN (SELECT DISTINCT source_article, target_article
                    FROM Countries) AS dl ON l.source_article = dl.target_article
WHERE dl.target_article IS NULL
  AND l.source_article != l.target_article;

-- 16: Article in "Maths" that is the target of all other articles in "Maths"
SELECT DISTINCT target_article
FROM Maths
WHERE target_article NOT IN (SELECT DISTINCT source_article
                             FROM Maths);

-- 17: Different articles from "Countries" reachable directly or indirectly from "France"
WITH RECURSIVE ArticleHierarchy AS (SELECT target_article
                                    FROM Countries
                                    WHERE source_article = 'France'
                                    UNION
                                    SELECT l.target_article
                                    FROM Countries AS l
                                             INNER JOIN ArticleHierarchy AS ah ON l.source_article = ah.target_article)
SELECT COUNT(DISTINCT ah.target_article) AS reachable_articles
FROM ArticleHierarchy AS ah;

-- 18: Different categories of articles in "Countries" not reachable directly or indirectly from "France"
WITH RECURSIVE CategoryHierarchy AS (SELECT category
                                     FROM CATEGORIES
                                     WHERE article_name = 'France'
                                     UNION
                                     SELECT c.category
                                     FROM CATEGORIES AS c
                                              INNER JOIN CategoryHierarchy AS ch ON c.article_name = ch.category)
SELECT DISTINCT c.category
FROM CATEGORIES AS c
WHERE c.category NOT IN (SELECT category
                         FROM CategoryHierarchy);

-- 19: Descendants of the article "Algebra" in the "Maths" view
WITH RECURSIVE Descendants AS (SELECT DISTINCT target_article
                               FROM Maths
                               WHERE source_article = 'Algebra'
                               UNION
                               SELECT l.target_article
                               FROM Maths AS l
                                        INNER JOIN Descendants AS d ON l.source_article = d.target_article)
SELECT DISTINCT target_article
FROM Descendants;

-- 20: Average number of different descendants of an article in "Maths" using "Maths" or "Countries" view
WITH RECURSIVE Descendants AS (SELECT DISTINCT target_article
                               FROM Maths
                               WHERE source_article = 'Algebra'
                               UNION
                               SELECT l.target_article
                               FROM Maths AS l
                                        INNER JOIN Descendants AS d ON l.source_article = d.target_article)
SELECT AVG(COUNT(DISTINCT d.target_article)) AS average_descendants
FROM Descendants AS d;
