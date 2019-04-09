CREATE TABLE Users (
  ID int not null,
  PRIMARY KEY (ID),
  Username varchar(20),
  Password varchar(60),
  Name varchar(60),
  UserType varchar(10),
  StudentNumber varchar(8)
);

INSERT INTO Users (ID, Username, Password, Name, UserType, StudentNumber) VALUES
(1, 'admin', 'admin', 'Admin', 'Admin', null),
(2, 'k4-bourne', 'password', 'Kieran Bourne', 'Student', '15007163'),
(3, 'joe-bloggs', 'password', 'Joe Bloggs', 'Lecturer', null),
(4, 'b3-gates', 'password', 'Bill Gates', 'Student', '15007162'),
(5, 's2-jobs', 'password', 'Steve Jobs', 'Student', '15007161'),
(6, 'a2-smith', 'password', 'Andy Smith', 'Student', '15007160');

CREATE TABLE Session (
  ID int not null,
  PRIMARY KEY (ID),
  Module varchar(20),
  Room varchar(60),
  Time varchar(30),
  Reference varchar(16),
  Owner_ID int
);

INSERT INTO Session (ID, Module, Room, Time, Reference, Owner_ID) VALUES
(1, 'Mobile Applications', '2Q28', '14:00 - 16:00', 'kU9vRVm9T1lFMbi3', 3),
(2, 'Biocomputation', '2Q12', '12:00 - 14:00', 'hbvXiAAN6nk6JTRm', 3);

CREATE TABLE RegisteredStudents (
  ID int not null,
  PRIMARY KEY (ID),
  User_ID int,
  Session_ID int
);

INSERT INTO RegisteredStudents (ID, User_ID, Session_ID) VALUES
(1, 2, 1),
(2, 4, 1),
(3, 5, 1),
(4, 6, 1),
(5, 2, 2);

CREATE TABLE Attendance_Record (
  ID int not null,
  PRIMARY KEY (ID),
  Reference varchar(16),
  Module varchar(20),
  Date date,
  Session_ID int
);

INSERT INTO Attendance_Record (ID, Reference, Module, Date, Session_ID) VALUES
(1, 'edWyuFjrouGD6dBA', 'Mobile Applications', '2018-11-27', 1),
(2, 'EWpkFiuSnAMRXLIv', 'Mobile Applications', '2018-11-29', 1),
(3, 'EWpkFiuSnAMRXLIv', 'Biocomputation', '2018-11-30', 2);

CREATE TABLE Attendee_Record (
  ID int not null,
  PRIMARY KEY (ID),
  StudentNumber varchar(8),
  AttendanceRecord_ID int
);

INSERT INTO Attendee_Record (ID, StudentNumber, AttendanceRecord_ID) VALUES
(1, '15007163', 1),
(2, '15007162', 1),
(3, '15007161', 1),
(4, '15007160', 1),
(5, '15007163', 2),
(6, '15007162', 2),
(7, '15007163', 3);

CREATE TABLE User_Attendance (
  ID int not null,
  PRIMARY KEY (ID),
  User_ID int,
  Session_ID int,
  Sessions_Attended int,
  Attendance_Percentage double
);

INSERT INTO User_Attendance (ID, User_ID, Session_ID, Sessions_Attended, Attendance_Percentage) VALUES
(1, 2, 1, 2, 100.0),
(2, 4, 1, 2, 100.0),
(3, 5, 1, 1, 50.0),
(4, 6, 1, 1, 50.0),
(5, 2, 2, 1, 100.0);