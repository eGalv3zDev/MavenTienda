# MavenTienda

--- BASE DE DATOS ---
drop database if exists ventaProductos;
create database ventaProductos;
use ventaProductos;

create table Productos(
	idProducto int auto_increment,
    producto varchar(100) not null,
    descripcion varchar(250) not null,
    stock int not null not null,
    precio decimal(10,2) not null,
    
    constraint pk_productos primary key(idProducto) 
);


-- CRUD --
delimiter //
create procedure sp_listarProductos()
	begin
		select p.idPRoducto,
				p.producto,
                p.descripcion,
                p.stock,
                p.precio
		from Productos p;
    end//
delimiter ;
call sp_listarProductos();

delimiter //
create procedure sp_AgregarProducto(
	in p_producto varchar(100),
    in p_descripcion varchar(250),
    in p_stock int,
    in p_precio decimal(10,2))
	begin
		insert into Productos(producto, descripcion, stock, precio)
			values (p_producto, p_descripcion, p_stock, p_precio);
    end//
delimiter ;
call sp_AgregarProducto("Tomate", "verdura color rojo", 20, 1.50);

delimiter //
create procedure sp_EditarProducto(
	in p_id int,
	in p_producto varchar(100),
    in p_descripcion varchar(250),
    in p_stock int,
    in p_precio decimal(10,2))
    begin
		update Productos
        set producto = p_producto,
			descripcion = p_descripcion,
            stock = p_stock,
            precio = p_precio
		where idProducto = p_id;
    end//
delimiter ;

delimiter //
create procedure sp_BuscarProducto(in p_id int)
	begin
		select p.idPRoducto,
				p.producto,
                p.descripcion,
                p.stock,
                p.precio
		from Productos p
        where idProducto = p_id;
    end//
delimiter ;
call sp_BuscarProducto(1);

delimiter //
create procedure sp_EliminarProducto(in p_id int)
	begin
		delete from Productos where idProducto = p_id;
    end//
delimiter ;
call sp_EliminarProducto(2);