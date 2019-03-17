CREATE TABLE Users (
  ID int not null,
  PRIMARY KEY (ID),
  Username varchar(20),
  Password varchar(60),
  Name varchar(60),
  UserType varchar(10),
  StudentNumber varchar(8)
);

INSERT INTO Users (ID, Username, Password, Name, UserType) VALUES
(1, 'admin', 'admin', 'Admin', 'Admin', null),
(2, 'k4-bourne', 'password', 'Kieran Bourne', 'Student', '15007163'),
(3, 'joe-bloggs', 'password', 'Joe Bloggs', 'Lecturer', null);

CREATE TABLE SESSION (
  ID int not null,
  PRIMARY KEY (ID),
  Module varchar(20),
  Room varchar(60),
  Time varchar(30),
  Reference varchar(16),
  Owner_ID int
);

INSERT INTO Session (ID, Module, Room, Time, Owner_ID) VALUES
(1, 'Mobile Applications', '2Q28', '14:00 - 16:00', 'kU9vRVm9T1lFMbi3', 3);