CREATE TABLE pago (
    id_pago BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_pedido BIGINT NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    monto DECIMAL(10,2) NOT NULL,
    fecha DATETIME NOT NULL,
    estado VARCHAR(50) NOT NULL
);

CREATE TABLE factura (
    id_factura BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_pago BIGINT NOT NULL,
    folio VARCHAR(100) NOT NULL,
    fecha_emision DATETIME NOT NULL,
    monto_total DECIMAL(10,2) NOT NULL,
    estado VARCHAR(50) NOT NULL,
    FOREIGN KEY (id_pago) REFERENCES pago(id_pago)
);