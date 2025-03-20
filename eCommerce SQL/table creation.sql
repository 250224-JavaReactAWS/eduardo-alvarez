create table users (
	user_id serial primary key,
	first_name varchar(50) not null,
	last_name varchar(50) not null,
	email varchar(50) not null unique,
	"password" varchar(50) not null,
	"role" varchar(15) default 'USER'
);

create table products(
	product_id serial primary key,
	"name" varchar(50) not null unique,
	description text,
	price decimal not null check(price>=0),
	stock int not null check(stock>=0)
);

create table cartItems(
	cart_item_id serial primary key,
	user_id int not null,
	product_id int not null,
	quantity int check(quantity>0),
	foreign key (user_id) references users(user_id),
	foreign key (product_id) references products(product_id)
);

create table orders(
	order_id serial primary key,
	user_id int not null,
	total_price decimal not null check(total_price>=0),
	status varchar(15) default 'PENDING',
	foreign key (user_id) references users(user_id)
);

create table orderItems(
	order_item_id serial primary key,
	order_id int not null,
	product_id int not null,
	quantity int not null check(quantity>0),
	price decimal not null check(price>=0),
	foreign key (order_id) references orders(order_id),
	foreign key (product_id) references products(product_id)
);