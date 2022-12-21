/* Clientes */

INSERT INTO customers (id, name, surname, email, create_at, image) VALUES(1, 'Roxy', 'Pérez', 'rperez@bolsadeideas.com', '2021-12-12', '');
INSERT INTO customers (id, name, surname, email, create_at, image) VALUES(2, 'Robert', 'Hernández', 'rhernandez@bolsadeideas.com', '2021-12-12', '');
INSERT INTO customers (id, name, surname, email, create_at, image) VALUES(3, 'Andres', 'Guzman', 'profesor@bolsadeideas.com', '2021-12-12', '');
INSERT INTO customers (id, name, surname, email, create_at, image) VALUES(4, 'Arianza', 'Lidia', 'alidia@bolsadeideas.com', '2021-12-12', '');
INSERT INTO customers (id, name, surname, email, create_at, image) VALUES(5, 'Nayra', 'Sujell', 'nsujell@bolsadeideas.com', '2021-08-06', '');
INSERT INTO customers (id, name, surname, email, create_at, image) VALUES(6, 'Eric', 'Jimenez', 'eric@bolsadeideas.com', '2021-10-21', '');
INSERT INTO customers (id, name, surname, email, create_at, image) VALUES(7, 'Alvaro', 'Torres', 'alvaro@bolsadeideas.com', '1980-02-17', '');
INSERT INTO customers (id, name, surname, email, create_at, image) VALUES(8, 'Paco', 'León', 'paco@bolsadeideas.com', '1975-01-12', '');
INSERT INTO customers (id, name, surname, email, create_at, image) VALUES(9, 'Ernesto', 'Herminio', 'ern@bolsadeideas.com', '1986-03-22', '');
INSERT INTO customers (id, name, surname, email, create_at, image) VALUES(10, 'Lola', 'Lolamento', 'lola@bolsadeideas.com', '1987-07-25', '');
INSERT INTO customers (id, name, surname, email, create_at, image) VALUES(11, 'Patricia', 'Vergara', 'patri@bolsadeideas.com', '1981-06-04', '');
INSERT INTO customers (id, name, surname, email, create_at, image) VALUES(12, 'Rubén', 'Cuesta', 'ruben@bolsadeideas.com', '1993-04-09', '');
INSERT INTO customers (id, name, surname, email, create_at, image) VALUES(13, 'Jorge Juan', 'Fernández', 'jj@bolsadeideas.com', '1994-01-07', '');
INSERT INTO customers (id, name, surname, email, create_at, image) VALUES(14, 'Jose Luis', 'Peralta', 'peralta@bolsadeideas.com', '1978-11-05', '');
INSERT INTO customers (id, name, surname, email, create_at, image) VALUES(15, 'Teresa', 'Gonzalez', 'tere@bolsadeideas.com', '1995-08-06', '');
INSERT INTO customers (id, name, surname, email, create_at, image) VALUES(16, 'Monica', 'Gutierrez', 'mon@bolsadeideas.com', '1993-08-27', '');

/* Productos */

INSERT INTO products (id, name, price, create_at) VALUES(1, 'Apple TV 4K', 179.00, NOW());
INSERT INTO products (id, name, price, create_at) VALUES(2, 'Apple Watch 7', 339.55, NOW());
INSERT INTO products (id, name, price, create_at) VALUES(3, 'HomePod mini', 99.99, NOW());
INSERT INTO products (id, name, price, create_at) VALUES(4, 'iMac 24', 1499.00, NOW());
INSERT INTO products (id, name, price, create_at) VALUES(5, 'iMac 27', 1799.00, NOW());
INSERT INTO products (id, name, price, create_at) VALUES(6, 'iPad Pro', 1699.00, NOW());
INSERT INTO products (id, name, price, create_at) VALUES(7, 'Mac mini', 899.79, NOW());
INSERT INTO products (id, name, price, create_at) VALUES(8, 'Mac Pro 2019', 5999.00, NOW());
INSERT INTO products (id, name, price, create_at) VALUES(9, 'MacBook Air', 999.89, NOW());
INSERT INTO products (id, name, price, create_at) VALUES(10, 'MacBook Pro', 1299.00, NOW());
INSERT INTO products (id, name, price, create_at) VALUES(11, 'iPhone 13 Pro Max', 1099.50, NOW());
INSERT INTO products (id, name, price, create_at) VALUES(12, 'iPhone 13 SE', 489.00, NOW());
INSERT INTO products (id, name, price, create_at) VALUES(13, 'AirTag', 30.70, NOW());
INSERT INTO products (id, name, price, create_at) VALUES(14, 'Airpods Pro', 249.00, NOW());

/* Facturas y lineas de facturas */

INSERT INTO invoices (id, description, note, customer_id, create_at) VALUES(1, 'Factura portátiles', 'Con 10% descuento', 7, NOW());
INSERT INTO invoices_items (quantity, product_id, invoice_id) VALUES(2, 9, 1);
INSERT INTO invoices_items (quantity, product_id, invoice_id) VALUES(1, 10, 1);

INSERT INTO invoices (id, description, note, customer_id, create_at) VALUES(2, 'Factura iMac Monica', 'Con personalización', 16, NOW());
INSERT INTO invoices_items (quantity, product_id, invoice_id) VALUES(1, 5, 2);

INSERT INTO invoices (id, description, note, customer_id, create_at) VALUES(3, 'Factura Robert', 'Varios componentes', 2, NOW());
INSERT INTO invoices_items (quantity, product_id, invoice_id) VALUES(3, 13, 3);
INSERT INTO invoices_items (quantity, product_id, invoice_id) VALUES(1, 14, 3);
INSERT INTO invoices_items (quantity, product_id, invoice_id) VALUES(1, 11, 3);

/* Usuarios */
INSERT INTO `users` (id, username, password, enabled) VALUES (1, 'roxy','$2a$10$qoeNsamWa53oFw7l.7fucuLynNR3WhNYPy9RxAW0txEIFMT3mni0q',1);
INSERT INTO `users` (id, username, password, enabled) VALUES (2, 'admin','$2a$10$zlfmMFXhR1FOSUq2hxqXDeSzsEvu/MX7K9HgsPWP/U1Avo1gXfjVy',1);

/* Roles */
INSERT INTO `authorities` (id, user_id, authority) VALUES (1, 1,'ROLE_USER');
INSERT INTO `authorities` (id, user_id, authority) VALUES (2, 2,'ROLE_ADMIN');
INSERT INTO `authorities` (id, user_id, authority) VALUES (3, 2,'ROLE_USER');
