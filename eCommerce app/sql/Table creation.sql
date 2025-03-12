create type rol as enum('USER', 'ADMIN');
create type ProductStatus as enum('PENDING','SHIPPED','DELIVERED');

create table Users(
	user_id serial primary key,
	first_name varchar(50),
	last_name varchar(50),
	email varchar(50) unique,
	"password" varchar(20),
	"role" rol 
);

create table "order"(
	order_id  serial primary key,
	user_id int,
	total_price decimal,
	status ProductStatus,
	foreign key (user_id) references Users(user_id)
);

create table product(
	product_id serial primary key,
	"name" varchar(50),
	description text,
	price decimal,
	stock int
);

create table orderItem(
	order_item_id serial primary key,
	order_id int,
	product_id int,
	quantity int,
	price decimal,
	foreign key (order_id) references "order"(order_id),
	foreign key (product_id) references product(product_id)
);

create table cartItem(
	cart_item_id serial primary key,
	user_id int,
	product_id int,
	quantity int,
	foreign key (user_id) references users(user_id),
	foreign key (product_id) references product(product_id)
);