CREATE TABLE demo_group
(
    name     VARCHAR(64) NOT NULL,
    group_id int PRIMARY KEY
);


CREATE TABLE demo_user
(
    name     VARCHAR(64) NOT NULL,
    user_id  int PRIMARY KEY,
    group_id int         NOT NULL,
    FOREIGN KEY (group_id)
        REFERENCES demo_group (group_id)
);

INSERT INTO demo_group (name, group_id)
VALUES ('users', 1),
       ('admins', 0);

INSERT INTO demo_user (name, user_id, group_id)
VALUES ('gunnar', 1, 0),
       ('root', 2, 1);
