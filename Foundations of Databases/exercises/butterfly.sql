/*
The Problem

You will find an archive containing 2 record files with the following contents:
- The file 'SS-Butterfly labels.tsv' has 2 columns separated by a tabulation:
    - the first one is an integer identifying a butterfly
    - the second one is an integer identifying the butterfly species.
- The file 'SS-Butterfly weights.tsv' has 3 columns separated by a tabulation:
    - the first two contain butterfly identifiers
    - the third one is a floating-point number representing a visual similarity between the two butterflies (calculated from images).
Each file has a header line.
*/

-- Create the schema for the LABELS table
CREATE TABLE LABELS
(
    butterfly_id INT,
    species_id   INT
);

-- Import data from the SS-Butterfly labels.tsv file into the LABELS table
COPY LABELS FROM 'SS-Butterfly labels.tsv' DELIMITER E '\t' CSV HEADER;

-- Create the schema for the WEIGHTS table
CREATE TABLE WEIGHTS
(
    butterfly_id1 INT,
    butterfly_id2 INT,
    similarity    FLOAT
);

-- Import data from the SS-Butterfly weights.tsv file into the WEIGHTS table
COPY WEIGHTS FROM 'SS-Butterfly weights.tsv' DELIMITER E '\t' CSV HEADER;

-- Find the range of similarity values between butterflies
SELECT MIN(similarity) AS min_similarity, MAX(similarity) AS max_similarity
FROM WEIGHTS;

-- Check if there are butterflies belonging to two different species
SELECT DISTINCT butterfly_id
FROM LABELS
GROUP BY butterfly_id
HAVING COUNT(DISTINCT species_id) > 1;

-- Verify that the WEIGHTS table does not contain duplicate pairs with different similarities
SELECT DISTINCT w1.butterfly_id1, w1.butterfly_id2
FROM WEIGHTS w1
         JOIN WEIGHTS w2
              ON (w1.butterfly_id1 = w2.butterfly_id1 AND w1.butterfly_id2 = w2.butterfly_id2 AND
                  w1.similarity != w2.similarity)
                  OR (w1.butterfly_id1 = w2.butterfly_id2 AND w1.butterfly_id2 = w2.butterfly_id1 AND
                      w1.similarity != w2.similarity);

-- Count the number of butterflies with no similarity values
SELECT COUNT(DISTINCT butterfly_id1) + COUNT(DISTINCT butterfly_id2) -
       COUNT(DISTINCT WEIGHTS.butterfly_id1) AS num_butterflies_no_similarity
FROM WEIGHTS;

-- Calculate the number of similarities per species of butterflies
SELECT species_id, COUNT(*) AS num_similarities
FROM LABELS
         JOIN WEIGHTS
              ON LABELS.butterfly_id = WEIGHTS.butterfly_id1
GROUP BY species_id;

-- Calculate the average similarity between species of butterflies
SELECT AVG(similarity) AS avg_similarity_between_species
FROM WEIGHTS
         JOIN LABELS l1
              ON WEIGHTS.butterfly_id1 = l1.butterfly_id
         JOIN LABELS l2
              ON WEIGHTS.butterfly_id2 = l2.butterfly_id
WHERE l1.species_id <> l2.species_id;

-- Create the maxSim view
CREATE
OR REPLACE VIEW maxSim AS
SELECT *
FROM WEIGHTS
WHERE similarity > 0.5;

-- Create the minSim view
CREATE
OR REPLACE VIEW minSim AS
SELECT *
FROM WEIGHTS
WHERE similarity BETWEEN 0.25 AND 0.33;

-- Find species present in maxSim but not in minSim
SELECT DISTINCT l1.species_id
FROM LABELS l1
         LEFT JOIN maxSim
                   ON l1.butterfly_id = maxSim.butterfly_id1
WHERE maxSim.butterfly_id1 IS NOT NULL
  AND l1.species_id NOT IN (SELECT DISTINCT l2.species_id
                            FROM LABELS l2
                                     LEFT JOIN minSim
                                               ON l2.butterfly_id = minSim.butterfly_id1
                            WHERE minSim.butterfly_id1 IS NOT NULL);

-- Find butterflies in maxSim that are similar to all butterflies in maxSim
SELECT DISTINCT w1.butterfly_id1
FROM maxSim w1
         LEFT JOIN (SELECT DISTINCT butterfly_id1
                    FROM maxSim) w2
                   ON w1.butterfly_id1 = w2.butterfly_id1;

-- Calculate transMax, the transitive closure of maxSim
-- Create a temporary table to store the results
CREATE
TEMP TABLE tempTransitivePairs AS
WITH RECURSIVE SimilarPairs AS (
    -- Select initial pairs with indirect similarity > 0.5
    SELECT
        t1.butterfly_id1 AS start_id,
        t2.butterfly_id2 AS end_id,
        t1.indirect_similarity * t2.indirect_similarity AS similarity
    FROM
        transMax t1
    JOIN
        transMax t2 ON t1.butterfly_id2 = t2.butterfly_id1
    WHERE
        t1.butterfly_id1 <> t2.butterfly_id2
        AND t1.indirect_similarity * t2.indirect_similarity > 0.5
    UNION ALL
    -- Recursively add more pairs with indirect similarity > 0.5
    SELECT
        sp.start_id,
        t.butterfly_id2 AS end_id,
        sp.similarity * t.indirect_similarity AS similarity
    FROM
        SimilarPairs sp
    JOIN
        transMax t ON sp.end_id = t.butterfly_id1
    WHERE
        sp.start_id <> t.butterfly_id2
        AND sp.similarity * t.indirect_similarity > 0.5
)
-- Calculate the average similarity for each pair of butterflies
SELECT start_id,
       end_id,
       AVG(similarity) AS average_similarity
FROM SimilarPairs
GROUP BY start_id, end_id;

-- Select pairs with an average similarity > 0.5
SELECT start_id AS butterfly_id1,
       end_id   AS butterfly_id2,
       average_similarity
FROM tempTransitivePairs
WHERE average_similarity > 0.5;

-- Drop the temporary table
DROP TABLE IF EXISTS tempTransitivePairs;