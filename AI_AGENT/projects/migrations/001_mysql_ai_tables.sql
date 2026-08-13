-- AI Agent auxiliary tables for the existing `wdd` MySQL database.
-- This migration only creates missing `ai_*` tables. It never drops or alters
-- WebBE business tables and is safe to run repeatedly.

CREATE TABLE IF NOT EXISTS `ai_student` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL,
  `student_id` varchar(64) NOT NULL,
  `grade` varchar(32) DEFAULT NULL,
  `class_name` varchar(64) DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_ai_student_student_id` (`student_id`),
  KEY `ix_ai_student_student_id` (`student_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `ai_learning_profile` (
  `id` int NOT NULL AUTO_INCREMENT,
  `student_id` int NOT NULL,
  `weak_points` text NOT NULL,
  `mastered_points` text NOT NULL,
  `overall_score` float DEFAULT NULL,
  `last_update` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_ai_profile_student_id` (`student_id`),
  CONSTRAINT `fk_ai_profile_student` FOREIGN KEY (`student_id`)
    REFERENCES `ai_student` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `ai_student_knowledge_progress` (
  `id` int NOT NULL AUTO_INCREMENT,
  `student_id` int NOT NULL,
  `knowledge_point` varchar(191) NOT NULL,
  `mastery_level` float NOT NULL,
  `last_practice_date` datetime DEFAULT NULL,
  `practice_count` int NOT NULL DEFAULT 0,
  `correct_count` int NOT NULL DEFAULT 0,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_ai_progress_student_point` (`student_id`, `knowledge_point`),
  KEY `ix_ai_progress_student_id` (`student_id`),
  CONSTRAINT `fk_ai_progress_student` FOREIGN KEY (`student_id`)
    REFERENCES `ai_student` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `ai_question` (
  `id` int NOT NULL AUTO_INCREMENT,
  `question_text` text NOT NULL,
  `question_type` varchar(32) NOT NULL DEFAULT 'multiple_choice',
  `difficulty` float NOT NULL,
  `subject` varchar(128) NOT NULL DEFAULT 'general',
  `knowledge_points` text DEFAULT NULL,
  `options` text DEFAULT NULL,
  `correct_answer` text NOT NULL,
  `answer_analysis` text DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `ix_ai_question_subject` (`subject`),
  KEY `ix_ai_question_difficulty` (`difficulty`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `ai_exam_record` (
  `id` int NOT NULL AUTO_INCREMENT,
  `student_id` int NOT NULL,
  `exam_name` varchar(255) NOT NULL,
  `exam_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `total_score` float NOT NULL,
  `max_score` float NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `ix_ai_exam_student_id` (`student_id`),
  KEY `ix_ai_exam_date` (`exam_date`),
  CONSTRAINT `fk_ai_exam_student` FOREIGN KEY (`student_id`)
    REFERENCES `ai_student` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `ai_answer_record` (
  `id` int NOT NULL AUTO_INCREMENT,
  `exam_record_id` int NOT NULL,
  `question_id` int NOT NULL,
  `student_answer` text NOT NULL,
  `is_correct` tinyint(1) NOT NULL,
  `score` float NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `ix_ai_answer_exam_id` (`exam_record_id`),
  KEY `ix_ai_answer_question_id` (`question_id`),
  CONSTRAINT `fk_ai_answer_exam` FOREIGN KEY (`exam_record_id`)
    REFERENCES `ai_exam_record` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_ai_answer_question` FOREIGN KEY (`question_id`)
    REFERENCES `ai_question` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
