--DROP Table User;
CREATE TABLE Users (
  Username varchar(20),
  Password varchar(60),
  Name varchar(60),
  UserType varchar(10),
  id int not null,
  PRIMARY KEY (id)
);

INSERT INTO Users (Username, Password, Name, UserType, id) VALUES
('k4-bourne', 'password', 'Kieran Bourne', 'Student', 1);