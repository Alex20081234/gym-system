CREATE DATABASE IF NOT EXISTS `gymsystemdatabase`;
CREATE TABLE IF NOT EXISTS `blacklist` (
                             `id` varchar(255) NOT NULL,
                             `ExpireDate` date NOT NULL,
                             PRIMARY KEY (`id`),
                             UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `user` (
                        `IsActive` bit(1) NOT NULL,
                        `id` int NOT NULL AUTO_INCREMENT,
                        `FirstName` varchar(255) NOT NULL,
                        `LastName` varchar(255) NOT NULL,
                        `Password` varchar(255) NOT NULL,
                        `Username` varchar(255) NOT NULL,
                        PRIMARY KEY (`id`),
                        UNIQUE KEY `UKseh7nteifndaopocsq9f1w8ia` (`Username`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `trainee` (
                           `DateOfBirth` date DEFAULT NULL,
                           `id` int NOT NULL,
                           `Address` varchar(255) DEFAULT NULL,
                           PRIMARY KEY (`id`),
                           CONSTRAINT `FK1b1rs0qdwriiqxlqflj5njnho` FOREIGN KEY (`id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `trainingtype` (
                                `id` int NOT NULL AUTO_INCREMENT,
                                `TrainingTypeName` varchar(255) NOT NULL,
                                PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `trainer` (
                           `Specialization` int NOT NULL,
                           `id` int NOT NULL,
                           PRIMARY KEY (`id`),
                           KEY `FKg1xt0c1nfosc7dpv4tehwwvie` (`Specialization`),
                           CONSTRAINT `FKg1xt0c1nfosc7dpv4tehwwvie` FOREIGN KEY (`Specialization`) REFERENCES `trainingtype` (`id`),
                           CONSTRAINT `FKm4ck87wwdx2p9ttytj6drmhng` FOREIGN KEY (`id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `trainee_trainer` (
                                   `trainee_id` int NOT NULL,
                                   `trainer_id` int NOT NULL,
                                   PRIMARY KEY (`trainee_id`,`trainer_id`),
                                   KEY `FKe6n92jfm3hp9nuxk2u3ipshei` (`trainer_id`),
                                   CONSTRAINT `FK30hetj4tutcpj1q7qhgk96785` FOREIGN KEY (`trainee_id`) REFERENCES `trainee` (`id`),
                                   CONSTRAINT `FKe6n92jfm3hp9nuxk2u3ipshei` FOREIGN KEY (`trainer_id`) REFERENCES `trainer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `training` (
                            `TraineeId` int NOT NULL,
                            `TrainerId` int NOT NULL,
                            `TrainingDate` date NOT NULL,
                            `TrainingDuration` int NOT NULL,
                            `TrainingTypeId` int NOT NULL,
                            `id` int NOT NULL AUTO_INCREMENT,
                            `TrainingName` varchar(255) NOT NULL,
                            PRIMARY KEY (`id`),
                            KEY `FKlimd0kk0dah62b0206cadrib4` (`TraineeId`),
                            KEY `FKs9pox8k10cws4p6jdba9vsdtq` (`TrainerId`),
                            KEY `FK9cah2yg5yv04itvdxppl2bjn5` (`TrainingTypeId`),
                            CONSTRAINT `FK9cah2yg5yv04itvdxppl2bjn5` FOREIGN KEY (`TrainingTypeId`) REFERENCES `trainingtype` (`id`),
                            CONSTRAINT `FKlimd0kk0dah62b0206cadrib4` FOREIGN KEY (`TraineeId`) REFERENCES `trainee` (`id`),
                            CONSTRAINT `FKs9pox8k10cws4p6jdba9vsdtq` FOREIGN KEY (`TrainerId`) REFERENCES `trainer` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `usertries` (
                             `id` varchar(255) NOT NULL,
                             `Attempts` int NOT NULL,
                             `BlockTime` bigint DEFAULT NULL,
                             `ExpireDate` date DEFAULT NULL,
                             PRIMARY KEY (`id`),
                             UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
