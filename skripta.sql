CREATE TABLE Users
(
    id_user INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email varchar(256) NOT NULL,
    password varchar(512) NOT NULL,
    username varchar(256),
	auth_type varchar(256),
    date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


    