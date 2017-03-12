CREATE TABLE account (
	account_id INT NOT NULL
,	email VARCHAR
,	password VARCHAR
,	CONSTRAINT pk_account PRIMARY KEY (account_id)
,	CONSTRAINT uq_account_email UNIQUE (email)
);

CREATE SEQUENCE account_seq OWNED BY account.account_id;
ALTER TABLE account ALTER account_id SET DEFAULT nextval('account_seq');


CREATE TABLE child (
	child_id INT NOT NULL
,	account_id INT NOT NULL
,	first_name VARCHAR
,	last_name VARCHAR
,	date_of_birth DATE
,	image BYTEA
,	CONSTRAINT pk_child PRIMARY KEY (child_id)
,	CONSTRAINT fk_child_account FOREIGN KEY (account_id) REFERENCES account (account_id) ON DELETE CASCADE
);

CREATE SEQUENCE child_seq OWNED BY child.child_id;
ALTER TABLE child ALTER child_id SET DEFAULT nextval('child_seq');

CREATE TABLE achievement (
	achievement_id INT NOT NULL
,	child_id INT NOT NULL
,	achievement_date DATE
,	sleep BOOLEAN
,	nutrition BOOLEAN
,	movement BOOLEAN
);

CREATE SEQUENCE achievement_seq OWNED BY achievement.achievement_id;
ALTER TABLE achievement ALTER achievement_id SET DEFAULT nextval('achievement_seq');
