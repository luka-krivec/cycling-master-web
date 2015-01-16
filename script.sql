CREATE TABLE Users
(
    id_user INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email varchar(256) NOT NULL,
    password varchar(512) NOT NULL,
    username varchar(256),
	auth_type varchar(256),
    date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Routes
(
    id_route INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	id_user INT NOT NULL,
    name varchar(256),
    distance float,
	average_speed float,
    start_time DATETIME,
	end_time DATETIME,
    date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	FOREIGN KEY (id_user) 
        REFERENCES Users(id_user)
        ON DELETE CASCADE
);

CREATE TABLE Points
(
    id_point INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	id_route INT NOT NULL,
    lat float(10,6) NOT NULL,
    lng float(10,6) NOT NULL,
	altitude float,
    time_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	FOREIGN KEY (id_route) 
        REFERENCES Routes(id_route)
        ON DELETE CASCADE
);


    