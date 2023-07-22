CREATE TABLE IF NOT EXISTS story_category
(
    story_category_id serial PRIMARY KEY,
    story_id          INT,
    category_id       INT,
    CONSTRAINT fk_story
    FOREIGN KEY (story_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_category
    FOREIGN KEY (category_id) REFERENCES categories (category_id) ON DELETE CASCADE

);