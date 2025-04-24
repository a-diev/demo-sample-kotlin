CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE catalogs (
    catalog_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100),
    description TEXT,
    status BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE products (
    product_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    catalog_id UUID REFERENCES catalogs(catalog_id),
    name VARCHAR(100),
    description TEXT,
    price DECIMAL(10, 2),
    stock INT,
    sku VARCHAR(50),
    image_url VARCHAR(255),
    status BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (catalog_id) REFERENCES catalogs(catalog_id)
);
