-- SQLite version of tokobuku database
-- Import ini ke DB Browser for SQLite via: File > Import > Database from SQL file

PRAGMA foreign_keys = ON;

-- --------------------------------------------------------
-- Tabel admin
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS admin (
    id_admin  TEXT NOT NULL PRIMARY KEY,
    username  TEXT NOT NULL UNIQUE,
    password  TEXT NOT NULL
);

INSERT INTO admin (id_admin, username, password) VALUES
('ADM001', 'Chantiqia', 'admin123');

-- --------------------------------------------------------
-- Tabel buku
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS buku (
    isbn      TEXT NOT NULL PRIMARY KEY,
    judul     TEXT NOT NULL,
    harga     REAL NOT NULL,
    kategori  TEXT,
    stok      INTEGER DEFAULT 0
);

INSERT INTO buku (isbn, judul, harga, kategori, stok) VALUES
('9780132306331', 'Calculus', 565000.00, 'Mathematika', 7),
('9780199291151', 'The Selfish Gene', 210000.00, 'Sains', 9),
('9780316055437', 'The Goldfinch', 230000.00, 'Fiksi', 6),
('9780374275631', 'Thinking, Fast and Slow', 240000.00, 'Non-Fiksi', 8),
('9780375508325', 'Cosmos', 150000.00, 'Sains', 10),
('9780385539258', 'A Little Life', 190000.00, 'Fiksi', 14),
('9780767905923', 'Tuesdays with Morrie: An Old Man, a Young Man, and Life''s Greatest Lesson', 210000.00, 'Non-Fiksi', 4),
('9781423160915', 'The Sword of Summer', 120000.00, 'Fiksi', 7),
('9781451673319', 'All the Light We Cannot See', 250000.00, 'Fiksi', 10),
('9781476746586', 'Fahrenheit 451', 165000.00, 'Fiksi', 8);

-- --------------------------------------------------------
-- Tabel kasir
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS kasir (
    id_kasir  TEXT NOT NULL PRIMARY KEY,
    username  TEXT NOT NULL UNIQUE,
    password  TEXT NOT NULL,
    is_active INTEGER DEFAULT 1
);

INSERT INTO kasir (id_kasir, username, password, is_active) VALUES
('KSR001', 'Jeremy', 'kasir123', 0);

-- --------------------------------------------------------
-- Tabel transaksi
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS transaksi (
    id_transaksi  TEXT NOT NULL PRIMARY KEY,
    tanggal       TEXT DEFAULT (datetime('now', 'localtime')),
    total_harga   REAL NOT NULL,
    metode_bayar  TEXT NOT NULL,
    nominal_bayar REAL NOT NULL,
    id_kasir      TEXT,
    FOREIGN KEY (id_kasir) REFERENCES kasir(id_kasir)
);

INSERT INTO transaksi (id_transaksi, tanggal, total_harga, metode_bayar, nominal_bayar, id_kasir) VALUES
('TRK20260514183202', '2026-05-14 18:32:02', 190000.00, 'TUNAI', 200000.00, 'KSR001'),
('TRK20260514190405', '2026-05-14 19:04:05', 775000.00, 'TUNAI', 780000.00, 'KSR001');

-- --------------------------------------------------------
-- Tabel item_belanja
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS item_belanja (
    id_item       TEXT NOT NULL PRIMARY KEY,
    id_transaksi  TEXT,
    isbn          TEXT,
    qty           INTEGER NOT NULL,
    subtotal      REAL NOT NULL,
    FOREIGN KEY (id_transaksi) REFERENCES transaksi(id_transaksi),
    FOREIGN KEY (isbn) REFERENCES buku(isbn)
);

INSERT INTO item_belanja (id_item, id_transaksi, isbn, qty, subtotal) VALUES
('ITM177875832291824',   'TRK20260514183202', '9780385539258', 1, 190000.00),
('ITM1778760245318681',  'TRK20260514190405', '9780132306331', 1, 565000.00),
('ITM1778760245320161',  'TRK20260514190405', '9780767905923', 1, 210000.00);