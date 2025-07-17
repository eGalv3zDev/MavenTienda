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

create table Usuarios(
	idUsuario int auto_increment,
    usuario varchar(50) not null,
    apellido varchar(50) not null,
    email varchar(50) not null,
    contraseña varchar(50) not null,
    nit varchar(50) not null,
    direccion varchar(50) not null,
    rol varchar(20) not null default'cliente',
    
    constraint pk_usuarios primary key (idUsuario)
);

create table Carrito(
	idCarrito int auto_increment,
    idProducto int not null,
    idUsuario int not null,
    cantidad int not null,
    precioUnitario decimal(10,2) null,
    total decimal(10,2) generated always as (cantidad * precioUnitario) stored,
    
    constraint pk_carrito primary key(idCarrito),
    constraint fk_carrito_producto foreign key (idProducto)
        references Productos(idProducto) on delete cascade,
	constraint fk_carrito_usuarios foreign key (idUsuario)
		references Usuarios(idUsuario) on delete cascade
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

-- Usuarios
delimiter //
create procedure sp_listarUsuarios()
	begin
		select
			U.idUsuario,
            U.apellido,
            U.usuario,
            U.email,
            U.contraseña,
            U.nit,
            U.direccion,
            U.rol
		from Usuarios U;
    end//
delimiter ;
call sp_listarUsuarios();

delimiter //
create procedure sp_agregarUsuario(
    in u_usuario varchar(50),
    in u_apellido varchar(50),
    in u_email varchar(50),
    in u_contraseña varchar(50),
    in u_nit varchar(50),
    in u_direccion varchar(50),
    in u_rol varchar(20))
		begin
			insert into Usuarios(usuario, apellido, email, contraseña, nit, direccion, rol)
				values(u_usuario, u_apellido, u_email, u_contraseña, u_nit, u_direccion, u_rol);
        end//
delimiter ;
call sp_agregarUsuario("Breyner", "Benitez", "bbe@gmail.com", "breyner2007", "497871-4", "Cayala", "cliente");

delimiter //
create procedure sp_verificarEmail(
    in _email varchar(100),
    in _contraseña varchar(100)
)
	begin
		select * from Usuarios 
		where email = _email and contraseña = _contraseña;
	end//
delimiter ;


delimiter //
create procedure sp_listarCarritos(in p_idUsuario int)
	begin
		select
			C.idCarrito,
            C.idProducto,
            C.cantidad,
            C.precioUnitario,
            C.total
		from Carrito C
        where C.idUsuario = p_idUsuario;
    end//
delimiter ;

delimiter //
create procedure sp_agregarCarrito(
	in c_idproducto int,
	in c_cantidad int,
    in c_idUsuario int
)
	begin
		insert into Carrito(idProducto, cantidad, idUsuario)
		values (c_idproducto, c_cantidad, c_idUsuario);
	end//
delimiter ;

delimiter //
create trigger before_insert_carrito
before insert on Carrito
for each row
begin
    declare precio decimal(10,2);

    select p.precio into precio
    from Productos p
    where p.idProducto = new.idProducto;

    set new.precioUnitario = precio;
end//
delimiter ;

delimiter //
create procedure sp_eliminarCarrito(in c_idCarrito int)
	begin
		delete from Carrito where c_idCarrito = idCarrito;
	end//
delimiter ;

delimiter //
create procedure sp_totalGeneralCarrito(in p_idUsuario int)
begin
    select sum(total) as totalGeneral from Carrito
    where idUsuario = p_idUsuario;
end //
delimiter ;

