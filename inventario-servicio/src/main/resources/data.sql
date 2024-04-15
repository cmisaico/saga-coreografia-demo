DROP TABLE IF EXISTS producto;
DROP TABLE IF EXISTS inventario_orden;

CREATE TABLE producto (
   id int AUTO_INCREMENT primary key,
   descripcion VARCHAR(50),
   cantidad_disponible int
);

CREATE TABLE inventario_orden (
   inventario_id uuid default random_uuid() primary key,
   orden_id uuid,
   producto_id int,
   estado VARCHAR(50),
   cantidad int,
   foreign key (producto_id) references producto(id)
);

insert into producto(descripcion, cantidad_disponible)
    values
        ('book', 10),
        ('pen', 10),
        ('rug', 10);