-- V1.1.1__Modify_and_add_column_to_palette_table

ALTER TABLE palette
    CHANGE COLUMN color hex_code VARCHAR(10),
    ADD COLUMN name VARCHAR(50);

UPDATE palette
SET hex_code = CASE id
                  WHEN 1 THEN '#DA6022'
                  WHEN 2 THEN '#DE8989'
                  WHEN 3 THEN '#E1B000'
                  WHEN 4 THEN '#5C8596'
                  WHEN 5 THEN '#DADADA'
                  WHEN 6 THEN '#EB5353'
                  WHEN 7 THEN '#FFA192'
                  WHEN 8 THEN '#EC9B3B'
                  WHEN 9 THEN '#FFE70F'
                  WHEN 10 THEN '#B3DF67'
                  WHEN 11 THEN '#78A756'
                  WHEN 12 THEN '#24794F'
                  WHEN 13 THEN '#5AE0BC'
                  WHEN 14 THEN '#45C1D4'
    END,
    name = CASE id
               WHEN 1 THEN 'Namo Orange'
               WHEN 2 THEN 'Namo Pink'
               WHEN 3 THEN 'Namo Yellow'
               WHEN 4 THEN 'Namo Blue'
               WHEN 5 THEN 'Light Gray'
               WHEN 6 THEN 'Red'
               WHEN 7 THEN 'Pink'
               WHEN 8 THEN 'Orange'
               WHEN 9 THEN 'Yellow'
               WHEN 10 THEN 'Lime'
               WHEN 11 THEN 'Light Green'
               WHEN 12 THEN 'Green'
               WHEN 13 THEN 'Cyan'
               WHEN 14 THEN 'Light Blue'
        END,
    belong = '1'
WHERE id BETWEEN 1 AND 14;

INSERT INTO palette (id, belong, hex_code, name)
VALUES
    (15, '1', '#355080', 'Blue'),
    (16, '1', '#8571BF', 'Lavendar'),
    (17, '1', '#833286', 'Purple'),
    (18, '1', '#FF70DE', 'Magenta'),
    (19, '1', '#9C9C9C', 'Dark Gray'),
    (20, '1', '#1D1D1D', 'Black');
