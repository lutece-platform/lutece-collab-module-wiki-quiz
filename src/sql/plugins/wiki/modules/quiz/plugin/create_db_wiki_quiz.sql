-- liquibase formatted sql
-- changeset module-wiki-quiz:create_db_wiki_quiz.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = 'wiki_item'

-- Quiz tables for module wiki-quiz

CREATE TABLE IF NOT EXISTS wiki_quiz (
    id_quiz INT AUTO_INCREMENT PRIMARY KEY,
    id_book INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    time_limit_minutes INT DEFAULT NULL,
    max_attempts INT DEFAULT NULL,
    passing_score INT DEFAULT 70,
    random_questions BOOLEAN DEFAULT FALSE,
    is_published BOOLEAN DEFAULT FALSE,
    display_order INT DEFAULT 0,
    INDEX idx_quiz_book (id_book),
    INDEX idx_quiz_published (is_published),
    CONSTRAINT fk_quiz_book FOREIGN KEY (id_book) REFERENCES wiki_item(id_item) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS wiki_quiz_question (
    id_question INT AUTO_INCREMENT PRIMARY KEY,
    id_quiz INT NOT NULL,
    question_type ENUM('MCQ', 'TRUE_FALSE', 'MATCHING', 'ORDERING') NOT NULL,
    question_text TEXT NOT NULL,
    explanation TEXT,
    points INT DEFAULT 1,
    display_order INT DEFAULT 0,
    INDEX idx_question_quiz (id_quiz),
    INDEX idx_question_order (id_quiz, display_order),
    CONSTRAINT fk_question_quiz FOREIGN KEY (id_quiz) REFERENCES wiki_quiz(id_quiz) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS wiki_quiz_answer (
    id_answer INT AUTO_INCREMENT PRIMARY KEY,
    id_question INT NOT NULL,
    answer_text VARCHAR(1000) NOT NULL,
    is_correct BOOLEAN DEFAULT FALSE,
    match_target VARCHAR(1000) DEFAULT NULL,
    correct_order INT DEFAULT NULL,
    display_order INT DEFAULT 0,
    INDEX idx_answer_question (id_question),
    INDEX idx_answer_order (id_question, display_order),
    CONSTRAINT fk_answer_question FOREIGN KEY (id_question) REFERENCES wiki_quiz_question(id_question) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS wiki_quiz_answer_page (
    id_answer INT NOT NULL,
    id_page INT NOT NULL,
    PRIMARY KEY (id_answer, id_page),
    CONSTRAINT fk_answer_page_answer FOREIGN KEY (id_answer)
        REFERENCES wiki_quiz_answer(id_answer) ON DELETE CASCADE,
    CONSTRAINT fk_answer_page_item FOREIGN KEY (id_page)
        REFERENCES wiki_item(id_item) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS wiki_quiz_question_page (
    id_question INT NOT NULL,
    id_page INT NOT NULL,
    PRIMARY KEY (id_question, id_page),
    CONSTRAINT fk_question_page_question FOREIGN KEY (id_question)
        REFERENCES wiki_quiz_question(id_question) ON DELETE CASCADE,
    CONSTRAINT fk_question_page_item FOREIGN KEY (id_page)
        REFERENCES wiki_item(id_item) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS wiki_quiz_attempt (
    id_attempt INT AUTO_INCREMENT PRIMARY KEY,
    id_quiz INT NOT NULL,
    user_guid VARCHAR(255) NOT NULL,
    score INT DEFAULT NULL,
    passed BOOLEAN DEFAULT NULL,
    started_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP NULL,
    INDEX idx_attempt_quiz (id_quiz),
    INDEX idx_attempt_user (user_guid),
    INDEX idx_attempt_quiz_user (id_quiz, user_guid),
    CONSTRAINT fk_attempt_quiz FOREIGN KEY (id_quiz) REFERENCES wiki_quiz(id_quiz) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS wiki_quiz_response (
    id_response INT AUTO_INCREMENT PRIMARY KEY,
    id_attempt INT NOT NULL,
    id_question INT NOT NULL,
    user_answer TEXT,
    is_correct BOOLEAN DEFAULT NULL,
    points_earned INT DEFAULT 0,
    UNIQUE KEY uk_response_attempt_question (id_attempt, id_question),
    INDEX idx_response_attempt (id_attempt),
    CONSTRAINT fk_response_attempt FOREIGN KEY (id_attempt) REFERENCES wiki_quiz_attempt(id_attempt) ON DELETE CASCADE,
    CONSTRAINT fk_response_question FOREIGN KEY (id_question) REFERENCES wiki_quiz_question(id_question) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
