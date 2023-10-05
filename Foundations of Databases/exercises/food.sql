/*
The Problem:

You will find an archive containing 2 record files with the following contents:

The file 'Florida-bay-meta.csv' has 3 columns separated by a comma:
    - The first column is an integer identifying an organism or species.
    - The second column is the name of this organism or species.
    - The third column is the group to which this organism or species belongs. From now on, we will use the term "species" to refer to the values in the second column.
The file 'Florida-bay.txt' has 2 columns separated by a space, both containing identifiers of species.
The lines of this file describe a directed graph indicating the transfer of carbon between species.
In general, there is a transfer of carbon when an organism of one species consumes an organism of another species.
Each file has a header line.
*/

-- 1. Create the schema for a table named NAMES to receive the data from the Florida-bay-meta.csv file.
CREATE TABLE NAMES
(
    id            integer PRIMARY KEY,
    name          varchar(255),
    species_group varchar(255)
);

-- 2. Import the data from the Florida-bay-meta.csv file into the NAMES table.
COPY NAMES(id, name, species_group) FROM 'Florida-bay-meta.csv' DELIMITER ',' CSV HEADER;

-- 3. Create the schema for a table named TRANSFER to receive the data from the Florida-bay.txt file.
CREATE TABLE TRANSFER
(
    source_species_id integer,
    target_species_id integer
);

-- 4. Import the data from the Florida-bay.txt file into the TRANSFER table.
COPY TRANSFER(source_species_id, target_species_id) FROM 'Florida-bay.txt' DELIMITER ' ';

-- 5. What is the number of species that are sources of direct carbon transfer?
SELECT COUNT(DISTINCT source_species_id)
FROM TRANSFER;

-- 6. For each group, what is the number of distinct species that are sources of direct carbon transfer?
SELECT species_group, COUNT(DISTINCT source_species_id)
FROM NAMES
         LEFT JOIN TRANSFER ON NAMES.id = TRANSFER.source_species_id
GROUP BY species_group;

-- 7. Are all the species in the TRANSFER table present in NAMES and vice versa?
SELECT DISTINCT 'Species in TRANSFER not in NAMES' AS discrepancy
FROM TRANSFER
WHERE source_species_id NOT IN (SELECT id FROM NAMES)
UNION
SELECT DISTINCT 'Species in NAMES not in TRANSFER' AS discrepancy
FROM NAMES
WHERE id NOT IN (SELECT DISTINCT source_species_id FROM TRANSFER);

-- 8. Which species are not the target of any direct carbon transfer?
SELECT DISTINCT name
FROM NAMES
WHERE id NOT IN (SELECT DISTINCT target_species_id FROM TRANSFER);

-- 9. How many species are the source of direct carbon transfer to themselves?
SELECT COUNT(DISTINCT T1.source_species_id)
FROM TRANSFER AS T1
         INNER JOIN TRANSFER AS T2 ON T1.source_species_id = T2.target_species_id;

-- 10. How many groups are not the source of any direct carbon transfer?
SELECT COUNT(DISTINCT species_group)
FROM NAMES
WHERE id NOT IN (SELECT DISTINCT source_species_id FROM TRANSFER);

-- 11. Are there species that are the target of direct carbon transfer from all other species?
SELECT DISTINCT N1.name
FROM NAMES AS N1
WHERE NOT EXISTS (SELECT DISTINCT T2.source_species_id
                  FROM TRANSFER AS T2
                  WHERE T2.source_species_id NOT IN (SELECT DISTINCT T1.source_species_id
                                                     FROM TRANSFER AS T1
                                                     WHERE T1.target_species_id = N1.id));

-- 12. Are there groups that are the target of direct carbon transfer from all other species (i.e., the union of species that target them directly)?
SELECT DISTINCT species_group
FROM NAMES AS N1
WHERE NOT EXISTS (SELECT DISTINCT T2.source_species_id
                  FROM TRANSFER AS T2
                  WHERE T2.source_species_id NOT IN (SELECT DISTINCT T1.source_species_id
                                                     FROM TRANSFER AS T1
                                                     WHERE T1.target_species_id IN
                                                           (SELECT id FROM NAMES WHERE species_group = N1.species_group)));

-- 13. Which species can indirectly transfer carbon to themselves?
WITH RECURSIVE IndirectTransfer AS (SELECT source_species_id, target_species_id
                                    FROM TRANSFER
                                    UNION ALL
                                    SELECT IT.source_species_id, T.target_species_id
                                    FROM IndirectTransfer IT
                                             INNER JOIN TRANSFER T ON IT.target_species_id = T.source_species_id)
SELECT DISTINCT N1.name
FROM NAMES AS N1
WHERE N1.id IN (SELECT DISTINCT source_species_id FROM IndirectTransfer WHERE source_species_id = target_species_id);

-- 14. Which species can indirectly transfer carbon to themselves but not directly?
WITH RECURSIVE IndirectTransfer AS (SELECT source_species_id, target_species_id
                                    FROM TRANSFER
                                    UNION ALL
                                    SELECT IT.source_species_id, T.target_species_id
                                    FROM IndirectTransfer IT
                                             INNER JOIN TRANSFER T ON IT.target_species_id = T.source_species_id)
SELECT DISTINCT N1.name
FROM NAMES AS N1
WHERE N1.id IN (SELECT DISTINCT source_species_id FROM IndirectTransfer WHERE source_species_id = target_species_id)
  AND N1.id NOT IN (SELECT DISTINCT source_species_id FROM TRANSFER);

-- 15. Which species are at the top of the indirect carbon transfer chain?
WITH RECURSIVE IndirectTransfer AS (SELECT source_species_id, target_species_id
                                    FROM TRANSFER
                                    UNION ALL
                                    SELECT IT.source_species_id, T.target_species_id
                                    FROM IndirectTransfer IT
                                             INNER JOIN TRANSFER T ON IT.target_species_id = T.source_species_id)
SELECT DISTINCT N1.name
FROM NAMES AS N1
WHERE N1.id NOT IN (SELECT DISTINCT target_species_id FROM IndirectTransfer);

-- 16. Which species are at the bottom of the indirect carbon transfer chain?
WITH RECURSIVE IndirectTransfer AS (SELECT source_species_id, target_species_id
                                    FROM TRANSFER
                                    UNION ALL
                                    SELECT IT.source_species_id, T.target_species_id
                                    FROM IndirectTransfer IT
                                             INNER JOIN TRANSFER T ON IT.target_species_id = T.source_species_id)
SELECT DISTINCT N1.name
FROM NAMES AS N1
WHERE N1.id NOT IN (SELECT DISTINCT source_species_id FROM IndirectTransfer);

-- 17. How many species are neither at the top nor at the bottom of the indirect carbon transfer chain?
WITH RECURSIVE IndirectTransfer AS (SELECT source_species_id, target_species_id
                                    FROM TRANSFER
                                    UNION ALL
                                    SELECT IT.source_species_id, T.target_species_id
                                    FROM IndirectTransfer IT
                                             INNER JOIN TRANSFER T ON IT.target_species_id = T.source_species_id)
SELECT COUNT(DISTINCT N1.id)
FROM NAMES AS N1
WHERE N1.id NOT IN (SELECT DISTINCT source_species_id FROM IndirectTransfer)
  AND N1.id NOT IN (SELECT DISTINCT target_species_id FROM IndirectTransfer);

-- 18. Calculate, by group, the number of indirect carbon transfer targets.
WITH RECURSIVE IndirectTransfer AS (SELECT source_species_id, target_species_id
                                    FROM TRANSFER
                                    UNION ALL
                                    SELECT IT.source_species_id, T.target_species_id
                                    FROM IndirectTransfer IT
                                             INNER JOIN TRANSFER T ON IT.target_species_id = T.source_species_id)
SELECT species_group, COUNT(DISTINCT T.target_species_id) AS num_targets
FROM NAMES AS N
         LEFT JOIN IndirectTransfer AS T ON N.id = T.source_species_id
GROUP BY species_group
ORDER BY species_group;

-- 19. What are the pairs of species that have no common targets for indirect carbon transfers?
WITH IndirectTransfer AS (SELECT DISTINCT IT1.source_species_id AS species1, IT2.source_species_id AS species2
                          FROM TRANSFER AS IT1
                                   CROSS JOIN TRANSFER AS IT2
                          WHERE IT1.source_species_id < IT2.source_species_id)
SELECT DISTINCT N1.name AS species1, N2.name AS species2
FROM IndirectTransfer AS I
         JOIN NAMES AS N1 ON I.species1 = N1.id
         JOIN NAMES AS N2 ON I.species2 = N2.id
         LEFT JOIN TRANSFER AS T1 ON I.species1 = T1.source_species_id AND I.species2 = T1.target_species_id
         LEFT JOIN TRANSFER AS T2 ON I.species2 = T2.source_species_id AND I.species1 = T2.target_species_id
WHERE T1.source_species_id IS NULL
  AND T2.source_species_id IS NULL;

-- 20. Are there species that are the indirect target of carbon transfer from all other species that are direct sources of transfer?
WITH AllSources AS (SELECT DISTINCT T1.target_species_id, T2.source_species_id
                    FROM TRANSFER AS T1
                             CROSS JOIN TRANSFER AS T2
                    WHERE T1.target_species_id < T2.source_species_id)
SELECT DISTINCT N1.name
FROM AllSources AS A
         JOIN NAMES AS N1 ON A.target_species_id = N1.id
         LEFT JOIN TRANSFER AS T
                   ON A.target_species_id = T.target_species_id AND A.source_species_id = T.source_species_id
WHERE T.source_species_id IS NULL;
