DROP TABLE IF EXISTS envio;

CREATE TABLE envio (
   id uuid default random_uuid() primary key,
   orden_id uuid,
   producto_id int,
   cliente_id int,
   cantidad int,
   estado VARCHAR(50),
   fecha_envio TIMESTAMP
);
