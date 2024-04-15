DROP TABLE IF EXISTS cliente;
DROP TABLE IF EXISTS cliente_pago;

CREATE TABLE cliente (
    id int AUTO_INCREMENT primary key,
    nombre VARCHAR(50) NOT NULL,
    saldo int
)

CREATE TABLE cliente_pago {
    pago_id uuid default random_uuid() primary key,
    orden_id uuid,
    cliente_id int,
    estado VARCHAR(50),
    importe int
}

insert into cliente (nombre, saldo) values ('Juan', 100);