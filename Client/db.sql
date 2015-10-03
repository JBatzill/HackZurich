CREATE TABLE "users" (
    "email" varchar(255) NOT NULL PRIMARY KEY;
    "firstname" varchar(40) NOT NULL,
    "lastname" varchar(40) NOT NULL
);

CREATE TABLE "shares" (
    "id" integer NOT NULL PRIMARY KEY AUTOINCREMENT,
    "remote_id" integer,
    "group_remote_id" integer NOT NULL REFERENCES "groups" ("remote_id") ON DELETE CASCADE,
    "path" varchar(255) NOT NULL,
    "is_shared" integer NOT NULL
);

CREATE TABLE "groups" (
    "id" integer NOT NULL PRIMARY KEY AUTOINCREMENT,
    "remote_id" integer,
    "start_time" integer NOT NULL,
    "end_time" integer NOT NULL,
    "state" integer NOT NULL,
);

CREATE TABLE "group_memberships" (
    "id" integer NOT NULL PRIMARY KEY AUTOINCREMENT,
    "user_email" varchar(32) NOT NULL REFERENCES "users" ("email") ON DELETE CASCADE,
    "group_remote_id" integer NOT NULL REFERENCES "groups" ("remote_id") ON DELETE CASCADE
);