CREATE TABLE recipe (
	rid integer PRIMARY KEY,
	name VARCHAR( 255 ),
	write_up VARCHAR( 5000 )
);


CREATE TABLE ingredient (
	inid integer PRIMARY KEY,
	name VARCHAR( 255 ),
	category VARCHAR( 255 )
);


CREATE TABLE contains (
	rid integer NOT NULL,
	inid integer NOT NULL,
	qty float CHECK( qty >= 0 ),
	metric VARCHAR( 255 ),
	FOREIGN KEY( rid ) REFERENCES recipe( rid ),
	FOREIGN KEY( inid ) REFERENCES ingredient( inid ),
	PRIMARY KEY( rid, inid )
);
