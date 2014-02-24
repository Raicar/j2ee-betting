-- Indexes for primary keys have been explicitly created.

-- ---------- Table for validation queries from the connection pool. ----------

DROP TABLE PingTable;
CREATE TABLE PingTable (foo CHAR(1));

-- ------------------------------ Drops ----------------------------------

DROP INDEX UserProfileIndexByUsrId ON UserProfile;
DROP INDEX UserProfileIndexByLoginName ON UserProfile;

DROP INDEX VirtualAccountIndexByVirtualAccId ON VirtualAccount;
DROP INDEX VirtualAccountIndexByUsrId ON VirtualAccount;

DROP INDEX BetIndexByBetId ON Bet;

DROP INDEX BetOptionIndexByBetOptionId ON BetOption;

DROP INDEX BetTypeIndexByBetTypeId ON BetType;

DROP INDEX EventIndexByEventId ON Event;

DROP INDEX CategoryIndexByCategoryId ON Category;

DROP TABLE Bet;
DROP TABLE BetOption;
DROP TABLE BetType;
DROP TABLE Event;
DROP TABLE Category;
DROP TABLE VirtualAccount;
DROP TABLE UserProfile;



-- ------------------------------ UserProfile ----------------------------------

CREATE TABLE UserProfile (
    usrId BIGINT NOT NULL AUTO_INCREMENT,
    loginName VARCHAR(30) COLLATE latin1_bin NOT NULL,
    enPassword VARCHAR(13) NOT NULL, 
    firstName VARCHAR(30) NOT NULL,
    lastName VARCHAR(40) NOT NULL, email VARCHAR(60) NOT NULL,
    version BIGINT,
    CONSTRAINT UserProfile_PK PRIMARY KEY (usrId),
    CONSTRAINT LoginNameUniqueKey UNIQUE (loginName)) 
    ENGINE = InnoDB;

CREATE INDEX UserProfileIndexByUsrId ON UserProfile (usrId);
CREATE INDEX UserProfileIndexByLoginName ON UserProfile (loginName);

-- ------------------------------ VirtualAccount ----------------------------------

CREATE TABLE VirtualAccount(
	virtAccId BIGINT NOT NULL AUTO_INCREMENT,
	balance REAL NOT NULL,
	usrId BIGINT NOT NULL,
   version BIGINT,
	CONSTRAINT VirtualAccount_PK PRIMARY KEY (virtAccId),
	CONSTRAINT VirtualAccountUniqueUsrId UNIQUE (usrId), 
	CONSTRAINT VirtualAccount_FK_UserProfile FOREIGN KEY (usrId) REFERENCES UserProfile(usrId))
	ENGINE = InnoDB;

CREATE INDEX VirtualAccountIndexByVirtualAccId ON VirtualAccount (virtAccId);
CREATE INDEX VirtualAccountIndexByUsrId ON VirtualAccount (usrId);



-- ------------------------------ Bet ----------------------------------

CREATE TABLE Bet(
	betId BIGINT NOT NULL AUTO_INCREMENT,
	money REAL NOT NULL,
	betDate DATE NOT NULL, 
	virtAccId BIGINT NOT NULL,
	betOptionId BIGINT NOT NULL,
	CONSTRAINT Bet_PK PRIMARY KEY (betId),
	CONSTRAINT Bet_FK_VirtualAccount FOREIGN KEY (virtAccId) REFERENCES VirtualAccount(virtAccId),
	CONSTRAINT Bet_CH_money CHECK(money>0))
	ENGINE = InnoDB;

CREATE INDEX BetIndexByBetId ON Bet (betId);


-- ------------------------------ BetOption ----------------------------------

CREATE TABLE BetOption(
	betOptionId BIGINT NOT NULL AUTO_INCREMENT,	
	betAnswer VARCHAR(60) NOT NULL,
	rateProfit REAL NOT NULL,
	winner BIT DEFAULT NULL, 
	betTypeId BIGINT NOT NULL,
   	version BIGINT,
	CONSTRAINT BetOption_PK PRIMARY KEY (betOptionId))
	ENGINE = InnoDB;

CREATE INDEX BetOptionIndexByBetOptionId ON BetOption (betOptionId);

ALTER TABLE Bet ADD CONSTRAINT Bet_FK_BetOption FOREIGN KEY (betOptionId) REFERENCES BetOption(betOptionId);


-- ------------------------------ BetType ----------------------------------

CREATE TABLE BetType(
	betTypeId BIGINT NOT NULL AUTO_INCREMENT,	
	question VARCHAR(60) NOT NULL,
 	multChoice BIT NOT NULL, 
 	eventId BIGINT NOT NULL,
 	CONSTRAINT BetType_PK PRIMARY KEY (betTypeId))
 	ENGINE = InnoDB;

CREATE INDEX BetTypeIndexByBetTypeId ON BetType(betTypeId);

ALTER TABLE BetOption ADD CONSTRAINT Bet_FK_BetType FOREIGN KEY (betTypeId) REFERENCES BetType(betTypeId);

-- ------------------------------ Event ----------------------------------

CREATE TABLE Event(
	eventId BIGINT NOT NULL AUTO_INCREMENT,
	name VARCHAR(40) NOT NULL,
	eventDate DATE NOT NULL, 
	categoryId BIGINT NOT NULL,
	version BIGINT,
	CONSTRAINT Event_PK PRIMARY KEY (eventId))
	ENGINE = InnoDB;

CREATE INDEX EventIndexByEventId ON Event(eventId);

ALTER TABLE BetType ADD CONSTRAINT BetType_FK_Event FOREIGN KEY (eventId) REFERENCES Event(eventId);

-- ------------------------------ Category ----------------------------------

CREATE TABLE Category(
	categoryId BIGINT NOT NULL AUTO_INCREMENT,
	name VARCHAR(40) NOT NULL,
	CONSTRAINT Category_PK PRIMARY KEY (categoryId),
	CONSTRAINT Category_name_unique UNIQUE (name))
	ENGINE = InnoDB; 

CREATE INDEX CategoryIndexByCategoryId ON Category(categoryId);

ALTER TABLE Event ADD CONSTRAINT Event_FK_Category FOREIGN KEY (categoryId) REFERENCES Category(categoryId);


-- --------------------------------------------------------------------------



