DROP TABLE IF EXISTS orden_pago;
DROP TABLE IF EXISTS orden_inventario;
DROP TABLE IF EXISTS orden_compra;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE orden_compra (
    orden_id uuid default random_uuid() primary key,
    cliente_id int,
    producto_id int,
    cantidad int,
    precio_unidad int,
    monto int,
    estado VARCHAR(50),
    fecha_entrega TIMESTAMP,
    version int
);

CREATE TABLE orden_pago (
    id int AUTO_INCREMENT primary key,
    orden_id uuid unique,
    pago_id uuid,
    exito boolean,
    mensaje VARCHAR(50),
    estado VARCHAR(50),
    foreign key (orden_id) references orden_compra(orden_id)
);

CREATE TABLE orden_inventario (
    id int AUTO_INCREMENT primary key,
    orden_id uuid unique,
    inventario_id uuid,
    exito boolean,
    estado VARCHAR(50),
    mensaje VARCHAR(50),
    foreign key (orden_id) references orden_compra(orden_id)
);


 --# PostgresSQL
 DROP TABLE IF EXISTS orden_pago;
 DROP TABLE IF EXISTS orden_inventario;
 DROP TABLE IF EXISTS orden_compra;

 CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

 CREATE TABLE orden_compra (
     orden_id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
     cliente_id int,
     producto_id int,
     cantidad int,
     precio_unidad int,
     monto int,
     estado VARCHAR(50),
     fecha_entrega TIMESTAMP,
     version int
 );

 CREATE TABLE orden_pago (
     id SERIAL PRIMARY KEY,
     orden_id uuid UNIQUE,
     pago_id uuid,
     exito boolean,
     mensaje VARCHAR(50),
     estado VARCHAR(50),
     FOREIGN KEY (orden_id) REFERENCES orden_compra(orden_id)
 );


 CREATE TABLE orden_inventario (
     id SERIAL PRIMARY KEY,
     orden_id uuid UNIQUE,
     inventario_id uuid,
     exito boolean,
     estado VARCHAR(50),
     mensaje VARCHAR(50),
     FOREIGN KEY (orden_id) REFERENCES orden_compra(orden_id)
 );
