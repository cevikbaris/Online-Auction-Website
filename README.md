# Online-Auction-Website

--This insert for create a admin user

INSERT INTO `auction`.`user`
(`email`,`image`,`is_approved`,`name`,`password`,`username`)VALUES
('bariscevik378@gmail.com', NULL, 1, 'Barış Çevik', '$2a$12$8.NZKKVL6abVaAog4zDAKOx9U/8xsQq1gSSGL4pqnMB0ByY0/cws6', 'admin');

-- The project has 2 type role. You must insert these roles to database .

INSERT INTO `auction`.`role`
(`name`)VALUES('ROLE_USER');
INSERT INTO `auction`.`role`
(`name`)VALUES('ROLE_ADMIN');

-- My admin user must have admin role

INSERT INTO `auction`.`users_roles`
(`user_id`,
`role_id`)
VALUES(1,2);


-- some categories you can add manually.It is not possible to add categories from the website yet.

INSERT INTO `auction`.`category`
(`category_name`)VALUES('Car');

INSERT INTO `auction`.`category`
(`category_name`)VALUES('House');

INSERT INTO `auction`.`category`
(`category_name`)VALUES('Antique');

INSERT INTO `auction`.`category`
(`category_name`)VALUES('Technology');


INSERT INTO `auction`.`category`
(`category_name`)VALUES('Others');
