CREATE TABLE metrics_group1 (
	id SERIAL8 NOT NULL PRIMARY KEY,
    name varchar(100) NOT NULL,
	value text NOT NULL,
	segmentType varchar(20),
	segmentKey varchar(200),
	segmentKeyValue varchar(200),
	groupKey varchar(200),
	groupKeyValue varchar(200),
	labels jsonb,
	"timestamp" timestamp(3) NOT NULL
);


CREATE INDEX idx_metrics_group1_groupKey ON metrics_group1 USING hash (groupKeyValue);

CREATE TABLE tb_metrics_group1 (
	id SERIAL8 NOT NULL PRIMARY KEY,
    name varchar(100) NOT NULL,
	value text NOT NULL,
	segmentType varchar(20),
	segmentKey varchar(200),
	segmentKeyValue varchar(200),
	groupKey varchar(200),
	groupKeyValue varchar(200),
	labels jsonb,
	"timestamp" timestamp(3) NOT NULL
);

CREATE INDEX idx_tb_metrics_group1 ON tb_metrics_group1 USING zombodb ((tb_metrics_group1.*)) WITH (url='http://192.168.160.136:9201/');

insert into metrics_group1(name, value, labels, "timestamp") values('para_a', '1', now());

