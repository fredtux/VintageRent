-- DROP TABLE Inchiriere;
-- DROP TABLE Administrator_Subdomenii;
-- DROP TABLE Subdomenii;
-- DROP TABLE Administratori;
-- DROP TABLE Angajati;
-- DROP TABLE Salariu;
-- DROP TABLE Adrese;
-- DROP TABLE Clienti;
-- DROP TABLE Utilizatori;
-- DROP TABLE TipClient;
-- DROP TABLE Camere;
-- DROP TABLE Format;
-- DROP TABLE TipCamera;
-- DROP TABLE Obiective;
-- DROP TABLE Montura;
-- DROP SEQUENCE SQ_IDCAMERE;
-- -- DROP SEQUENCE SQ_IDINCHIRIERE;
-- DROP SEQUENCE SQ_IDUtilizator;
-- DROP SEQUENCE SQ_IDOBIECTIV;


CREATE SEQUENCE SQ_IDObiectiv START WITH 1 INCREMENT BY 1 NOCYCLE NOCACHE;
CREATE SEQUENCE SQ_IDCamere START WITH 1 INCREMENT BY 1 NOCYCLE NOCACHE;
CREATE SEQUENCE SQ_IDUtilizator START WITH 1 INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 NOCYCLE CACHE 10;
-- CREATE SEQUENCE SQ_IDInchiriere START WITH 1 INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 NOCYCLE CACHE 50;


CREATE TABLE Format
(
    IDFormat   INT GENERATED BY DEFAULT AS IDENTITY (START WITH 1 INCREMENT BY 1) PRIMARY KEY,
    Denumire   VARCHAR(255) NOT NULL,
    LatimeFilm INT          NOT NULL,

    CONSTRAINT CK_LatimeValida_LatimeFilm CHECK ( LatimeFilm > 0 )
);

INSERT INTO Format(Denumire, LatimeFilm)
VALUES ('Ingust', 35);
INSERT INTO Format(Denumire, LatimeFilm)
VALUES ('Mediu', 120);
INSERT INTO Format(Denumire, LatimeFilm)
VALUES ('Mare', 150);
INSERT INTO Format(Denumire, LatimeFilm)
VALUES ('Dagherotip-half', 176);
INSERT INTO Format(Denumire, LatimeFilm)
VALUES ('Dagherotip-quarter', 135);

COMMIT;

CREATE TABLE TipCamera
(
    IDTip    INT GENERATED BY DEFAULT AS IDENTITY (START WITH 1 INCREMENT BY 1) PRIMARY KEY,
    Denumire VARCHAR(255) NOT NULL
);

INSERT INTO TipCamera(Denumire)
VALUES ('TLR');
INSERT INTO TipCamera(Denumire)
VALUES ('SLR');
INSERT INTO TipCamera(Denumire)
VALUES ('Rangefinder');
INSERT INTO TipCamera(Denumire)
VALUES ('Half-frame');
INSERT INTO TipCamera(Denumire)
VALUES ('Pinhole');

COMMIT;

CREATE TABLE Montura
(
    IDMontura INT GENERATED BY DEFAULT AS IDENTITY (START WITH 1 INCREMENT BY 1) PRIMARY KEY,
    Denumire  VARCHAR(255) NOT NULL
);

INSERT INTO Montura(Denumire)
VALUES ('M39');
INSERT INTO Montura(Denumire)
VALUES ('M42');
INSERT INTO Montura(Denumire)
VALUES ('Canon FD');
INSERT INTO Montura(Denumire)
VALUES ('Nikon F');
INSERT INTO Montura(Denumire)
VALUES ('Leica R');

COMMIT;

CREATE TABLE Obiective
(
    IDObiectiv      INT PRIMARY KEY,
    Denumire        VARCHAR(255) NOT NULL,
    DistantaFocala  NUMBER(3, 0) NOT NULL,
    DiafragmaMinima NUMBER(3, 1) NOT NULL,
    DiafragmaMaxima NUMBER(3, 1) NOT NULL,
    Diametru        NUMBER(2, 0) NOT NULL,
    Pret            NUMBER(6, 0) NOT NULL,
    PretInchiriere  NUMBER(6, 0) NOT NULL,
    IDMontura       INT,

    CONSTRAINT FK_Obiective_Montura FOREIGN KEY (IDMontura) REFERENCES Montura (IDMontura),
    CONSTRAINT CK_PretValid_Pret CHECK (Pret > 0),
    CONSTRAINT CK_PretValid_PretInchiriere CHECK (PretInchiriere > 0),
    CONSTRAINT CK_DiametruValid_Diametru CHECK (Diametru > 0),
    CONSTRAINT CK_DiafragmaValida_DiafragmaMaxima CHECK (DiafragmaMaxima > 0),
    CONSTRAINT CK_DiafragmaValida_DiafragmaMinima CHECK (DiafragmaMinima > 0),
    CONSTRAINT CK_DistantaValida_DistantaFocala CHECK (DistantaFocala > 0)
);

INSERT INTO Obiective(IDObiectiv, Denumire, DistantaFocala, DiafragmaMinima, DiafragmaMaxima, Diametru, Pret,
                      PretInchiriere, IDMontura)
VALUES (SQ_IDObiectiv.nextval, 'Helios 55-2', 58, 16, 1.8, 45, 200, 20, 2);
INSERT INTO Obiective(IDObiectiv, Denumire, DistantaFocala, DiafragmaMinima, DiafragmaMaxima, Diametru, Pret,
                      PretInchiriere, IDMontura)
VALUES (SQ_IDObiectiv.nextval, 'Jupyter', 50, 16, 2, 45, 190, 20, 1);
INSERT INTO Obiective(IDObiectiv, Denumire, DistantaFocala, DiafragmaMinima, DiafragmaMaxima, Diametru, Pret,
                      PretInchiriere, IDMontura)
VALUES (SQ_IDObiectiv.nextval, 'Super-Takumar', 50, 16, 1.4, 45, 220, 30, 1);
INSERT INTO Obiective(IDObiectiv, Denumire, DistantaFocala, DiafragmaMinima, DiafragmaMaxima, Diametru, Pret,
                      PretInchiriere, IDMontura)
VALUES (SQ_IDObiectiv.nextval, 'W-Nikkor C', 35, 16, 1.8, 45, 300, 30, 4);
INSERT INTO Obiective(IDObiectiv, Denumire, DistantaFocala, DiafragmaMinima, DiafragmaMaxima, Diametru, Pret,
                      PretInchiriere, IDMontura)
VALUES (SQ_IDObiectiv.nextval, 'Helios 40-2', 85, 16, 1.5, 45, 180, 40, 2);
INSERT INTO Obiective(IDObiectiv, Denumire, DistantaFocala, DiafragmaMinima, DiafragmaMaxima, Diametru, Pret,
                      PretInchiriere, IDMontura)
VALUES (SQ_IDObiectiv.nextval, 'Leitz Summar', 50, 16, 2, 45, 5000, 70, 5);
INSERT INTO Obiective(IDObiectiv, Denumire, DistantaFocala, DiafragmaMinima, DiafragmaMaxima, Diametru, Pret,
                      PretInchiriere, IDMontura)
VALUES (SQ_IDObiectiv.nextval, 'Jena Biotar', 58, 16, 2, 40, 5500, 50, 5);
INSERT INTO Obiective(IDObiectiv, Denumire, DistantaFocala, DiafragmaMinima, DiafragmaMaxima, Diametru, Pret,
                      PretInchiriere, IDMontura)
VALUES (SQ_IDObiectiv.nextval, '100mm Canon', 100, 16, 3.5, 42, 700, 30, 3);
INSERT INTO Obiective(IDObiectiv, Denumire, DistantaFocala, DiafragmaMinima, DiafragmaMaxima, Diametru, Pret,
                      PretInchiriere, IDMontura)
VALUES (SQ_IDObiectiv.nextval, '50mm Canon', 50, 16, 1.4, 42, 600, 20, 3);
INSERT INTO Obiective(IDObiectiv, Denumire, DistantaFocala, DiafragmaMinima, DiafragmaMaxima, Diametru, Pret,
                      PretInchiriere, IDMontura)
VALUES (SQ_IDObiectiv.nextval, 'Leitz Summicron-M', 58, 16, 1.8, 45, 7000, 100, 5);

COMMIT;

CREATE TABLE Camere
(
    IDCamera       INT PRIMARY KEY,
    Marca          VARCHAR(255) NOT NULL,
    ModelCamera    VARCHAR(255) NOT NULL,
    IDFormat       INT,
    IDTip          INT,
    IDMontura      INT,
    AnFabricatie   INT          NOT NULL,
    Pret           NUMBER(8, 2) NOT NULL,
    PretInchiriere NUMBER(6, 2) NOT NULL,

    CONSTRAINT FK_Camere_Format FOREIGN KEY (IDFormat) REFERENCES Format (IDFormat),
    CONSTRAINT FK_Camere_TipCamera FOREIGN KEY (IDTip) REFERENCES TipCamera (IDTip),
    CONSTRAINT FK_Camere_Montura FOREIGN KEY (IDMontura) REFERENCES Montura (IDMontura),
    CONSTRAINT CK_PretValid_PretCamera CHECK (Pret > 0),
    CONSTRAINT CK_PretValid_PretInchiriereCamera CHECK (Pret > 0)
);

INSERT INTO Camere(IDCamera, Marca, ModelCamera, IDFormat, IDTip, IDMontura, AnFabricatie, Pret, PretInchiriere)
VALUES (SQ_IDCamere.nextval, 'Canon', 'A-1', 1, 2, 3, 1985, 1000, 20);
INSERT INTO Camere(IDCamera, Marca, ModelCamera, IDFormat, IDTip, IDMontura, AnFabricatie, Pret, PretInchiriere)
VALUES (SQ_IDCamere.nextval, 'Canon', 'AE-1', 1, 2, 3, 1980, 1200, 30);
INSERT INTO Camere(IDCamera, Marca, ModelCamera, IDFormat, IDTip, IDMontura, AnFabricatie, Pret, PretInchiriere)
VALUES (SQ_IDCamere.nextval, 'Canon', 'AL-1', 1, 2, 3, 1982, 1100, 30);
INSERT INTO Camere(IDCamera, Marca, ModelCamera, IDFormat, IDTip, IDMontura, AnFabricatie, Pret, PretInchiriere)
VALUES (SQ_IDCamere.nextval, 'Nikon', 'EM', 1, 2, 4, 1979, 800, 25);
INSERT INTO Camere(IDCamera, Marca, ModelCamera, IDFormat, IDTip, IDMontura, AnFabricatie, Pret, PretInchiriere)
VALUES (SQ_IDCamere.nextval, 'Nikon', 'FE', 1, 2, 4, 1978, 800, 25);
INSERT INTO Camere(IDCamera, Marca, ModelCamera, IDFormat, IDTip, IDMontura, AnFabricatie, Pret, PretInchiriere)
VALUES (SQ_IDCamere.nextval, 'Zorki', '4', 1, 3, 1, 1967, 800, 50);
INSERT INTO Camere(IDCamera, Marca, ModelCamera, IDFormat, IDTip, IDMontura, AnFabricatie, Pret, PretInchiriere)
VALUES (SQ_IDCamere.nextval, 'Leica', 'Leicaflex SL', 1, 2, 5, 1968, 3000, 50);
INSERT INTO Camere(IDCamera, Marca, ModelCamera, IDFormat, IDTip, IDMontura, AnFabricatie, Pret, PretInchiriere)
VALUES (SQ_IDCamere.nextval, 'Leica', 'M2', 1, 3, 5, 1960, 5000, 50);
INSERT INTO Camere(IDCamera, Marca, ModelCamera, IDFormat, IDTip, IDMontura, AnFabricatie, Pret, PretInchiriere)
VALUES (SQ_IDCamere.nextval, 'Leica', 'II', 1, 3, 5, 1940, 3000, 40);
INSERT INTO Camere(IDCamera, Marca, ModelCamera, IDFormat, IDTip, IDMontura, AnFabricatie, Pret, PretInchiriere)
VALUES (SQ_IDCamere.nextval, 'Leica', 'M4', 1, 3, 5, 1970, 4000, 40);

COMMIT;

CREATE TABLE TipClient
(
    IDTip    INT GENERATED BY DEFAULT AS IDENTITY (START WITH 1 INCREMENT BY 1) PRIMARY KEY,
    Denumire VARCHAR(255) NOT NULL,
    Discount NUMBER(4, 2)
);

INSERT INTO TipClient(Denumire, Discount)
VALUES ('Normal', 0);
INSERT INTO TipClient(Denumire, Discount)
VALUES ('Bronze', 1);
INSERT INTO TipClient(Denumire, Discount)
VALUES ('Silver', 2.5);
INSERT INTO TipClient(Denumire, Discount)
VALUES ('Gold', 5.25);
INSERT INTO TipClient(Denumire, Discount)
VALUES ('VIP', 20);

COMMIT;


CREATE TABLE Utilizatori
(
    IDUtilizator   INT PRIMARY KEY,
    NumeUtilizator VARCHAR(255) NOT NULL,
    Parola         CHAR(32),
    Nume           VARCHAR(255) NOT NULL,
    Prenume        VARCHAR(255) NOT NULL,
    CNP            CHAR(13),
    EMAIL          VARCHAR(255) NOT NULL,

    CONSTRAINT UQ_NumeUtilizator UNIQUE (NumeUtilizator),
    CONSTRAINT CK_ProperlyFormedHash_Parola CHECK (LENGTH(Parola) = 32),
    CONSTRAINT CK_CNPExists_CNP CHECK (LENGTH(CNP) = 13)
);
INSERT INTO Utilizatori(IDUtilizator, NumeUtilizator, Parola, Nume, Prenume, CNP, EMAIL)
VALUES (SQ_IDUtilizator.nextval, 'tuxmaster', '3c4ac1a78d4a9974c20d7ac6773f94d9', 'Dinu', 'Florin', NULL,
        'tuxmaster@magazinbd.ro');
INSERT INTO Utilizatori(IDUtilizator, NumeUtilizator, Parola, Nume, Prenume, CNP, EMAIL)
VALUES (SQ_IDUtilizator.nextval, 'richard.avedon', '3c4ac1a78d4a9974c20d7ac6773f94d9', 'Richard', 'Avedon',
        '1590506100523', 'richardavedon@magazinbd.ro');
INSERT INTO Utilizatori(IDUtilizator, NumeUtilizator, Parola, Nume, Prenume, CNP, EMAIL)
VALUES (SQ_IDUtilizator.nextval, 'dorothea.lange', '3c4ac1a78d4a9974c20d7ac6773f94d9', 'Lange', 'Dorothea',
        '2400506100523', 'dorothea.lange@magazinbd.ro');
INSERT INTO Utilizatori(IDUtilizator, NumeUtilizator, Parola, Nume, Prenume, CNP, EMAIL)
VALUES (SQ_IDUtilizator.nextval, 'henri.cartier.bresson', '3c4ac1a78d4a9974c20d7ac6773f94d9', 'Cartier-Bresson',
        'Henri', '1080506100523', 'henri.cartier.bresson@magazinbd.ro');
INSERT INTO Utilizatori(IDUtilizator, NumeUtilizator, Parola, Nume, Prenume, CNP, EMAIL)
VALUES (SQ_IDUtilizator.nextval, 'ion.ionescu', '3c4ac1a78d4a9974c20d7ac6773f94d9', 'Ionescu', 'Ion', '1990506100523',
        'ion.ionescu@gmail.com');
INSERT INTO Utilizatori(IDUtilizator, NumeUtilizator, Parola, Nume, Prenume, CNP, EMAIL)
VALUES (SQ_IDUtilizator.nextval, 'vlad.voiculescu', '3c4ac1a78d4a9974c20d7ac6773f94d9', 'Voiculescu', 'Vlad',
        '5050506100523', 'voiculescu.vlad@gmail.com');
INSERT INTO Utilizatori(IDUtilizator, NumeUtilizator, Parola, Nume, Prenume, CNP, EMAIL)
VALUES (SQ_IDUtilizator.nextval, 'gheorghe.gheorghe', '3c4ac1a78d4a9974c20d7ac6773f94d9', 'Gheorghe', 'Gheorghe',
        '5110506100523', 'gheorghe.gheorghe@gmail.com');
INSERT INTO Utilizatori(IDUtilizator, NumeUtilizator, Parola, Nume, Prenume, CNP, EMAIL)
VALUES (SQ_IDUtilizator.nextval, 'alina.alinescu', '3c4ac1a78d4a9974c20d7ac6773f94d9', 'Alinescu', 'Alina',
        '6010506100523', 'alina.alinescu@gmail.com');
INSERT INTO Utilizatori(IDUtilizator, NumeUtilizator, Parola, Nume, Prenume, CNP, EMAIL)
VALUES (SQ_IDUtilizator.nextval, 'viorica.ionescu', '3c4ac1a78d4a9974c20d7ac6773f94d9', 'Ionescu', 'Viorica',
        '2990506100523', 'viorica.ionescu@gmail.com');
INSERT INTO Utilizatori(IDUtilizator, NumeUtilizator, Parola, Nume, Prenume, CNP, EMAIL)
VALUES (SQ_IDUtilizator.nextval, 'sandu.sandescu', '3c4ac1a78d4a9974c20d7ac6773f94d9', 'Sandescu', 'Sandu',
        '1800506100523', 'sandu.sandescu@magazinbd.ro');
INSERT INTO Utilizatori(IDUtilizator, NumeUtilizator, Parola, Nume, Prenume, CNP, EMAIL)
VALUES (SQ_IDUtilizator.nextval, 'corina.corinescu', '3c4ac1a78d4a9974c20d7ac6773f94d9', 'Corinescu', 'Corina',
        '2800506100523', 'corina.corinescu@magazinbd.ro');

COMMIT;


CREATE TABLE Administratori
(
    IDUtilizator INT PRIMARY KEY,
    EsteActiv    CHAR(1) DEFAULT '1' NOT NULL,

    CONSTRAINT FK_Administratori_Utilizatori FOREIGN KEY (IDUtilizator) REFERENCES Utilizatori (IDUtilizator),
    CONSTRAINT CK_BOOLHACK_EsteActiv CHECK (EsteActiv IN ('1', '0'))
);

INSERT INTO Administratori(IDUtilizator, EsteActiv)
VALUES (1, '1');
INSERT INTO Administratori(IDUtilizator, EsteActiv)
VALUES (2, '1');
INSERT INTO Administratori(IDUtilizator, EsteActiv)
VALUES (3, '0');
INSERT INTO Administratori(IDUtilizator, EsteActiv)
VALUES (4, '0');
INSERT INTO Administratori(IDUtilizator, EsteActiv)
VALUES (10, '0');

COMMIT;


CREATE TABLE Subdomenii
(
    IDSubdomeniu INT GENERATED BY DEFAULT AS IDENTITY (START WITH 1 INCREMENT BY 1) PRIMARY KEY,
    Denumire     VARCHAR(255),

    CONSTRAINT CK_DenumireValida_Denumire CHECK (LENGTH(Denumire) > 0)
);

INSERT INTO Subdomenii(Denumire)
VALUES ('www');
INSERT INTO Subdomenii(Denumire)
VALUES ('store');
INSERT INTO Subdomenii(Denumire)
VALUES ('blog');
INSERT INTO Subdomenii(Denumire)
VALUES ('elearning');
INSERT INTO Subdomenii(Denumire)
VALUES ('events');

COMMIT;


CREATE TABLE Administrator_Subdomenii
(
    IDAdministrator INT,
    IDSubdomeniu    INT,

    CONSTRAINT PK_Administrator_Subdomenii PRIMARY KEY (IDAdministrator, IDSubdomeniu),
    CONSTRAINT FK_Administrator_Subdomenii_Administrator FOREIGN KEY (IDAdministrator) REFERENCES Administratori (IDUtilizator),
    CONSTRAINT FK_Administrator_Subdomenii_Subdomenii FOREIGN KEY (IDSubdomeniu) REFERENCES Subdomenii (IDSubdomeniu)
);

INSERT INTO Administrator_Subdomenii(IDAdministrator, IDSubdomeniu)
VALUES (1, 1);
INSERT INTO Administrator_Subdomenii(IDAdministrator, IDSubdomeniu)
VALUES (1, 2);
INSERT INTO Administrator_Subdomenii(IDAdministrator, IDSubdomeniu)
VALUES (1, 3);
INSERT INTO Administrator_Subdomenii(IDAdministrator, IDSubdomeniu)
VALUES (1, 4);
INSERT INTO Administrator_Subdomenii(IDAdministrator, IDSubdomeniu)
VALUES (1, 5);
INSERT INTO Administrator_Subdomenii(IDAdministrator, IDSubdomeniu)
VALUES (2, 1);
INSERT INTO Administrator_Subdomenii(IDAdministrator, IDSubdomeniu)
VALUES (2, 2);
INSERT INTO Administrator_Subdomenii(IDAdministrator, IDSubdomeniu)
VALUES (2, 3);
INSERT INTO Administrator_Subdomenii(IDAdministrator, IDSubdomeniu)
VALUES (3, 1);
INSERT INTO Administrator_Subdomenii(IDAdministrator, IDSubdomeniu)
VALUES (3, 5);

COMMIT;


CREATE TABLE Salariu
(
    IDSalariu INT GENERATED BY DEFAULT AS IDENTITY (START WITH 1 INCREMENT BY 1) PRIMARY KEY,
    Salariu      NUMBER(6, 0) NOT NULL,
    Bonus        NUMBER(4, 2) NOT NULL,

    CONSTRAINT CK_SalariuValid_Salariu CHECK (Salariu > 0),
    CONSTRAINT CK_BonusValid_Bonus CHECK (Bonus >= 0)
);

INSERT INTO Salariu(Salariu, Bonus)
VALUES (10000, 10.2);
INSERT INTO Salariu(Salariu, Bonus)
VALUES (9000, 9.7);
INSERT INTO Salariu(Salariu, Bonus)
VALUES (6000, 7.5);
INSERT INTO Salariu(Salariu, Bonus)
VALUES (5500, 8.43);
INSERT INTO Salariu(Salariu, Bonus)
VALUES (5700, 8.47);

COMMIT;


CREATE TABLE Angajati
(
    IDUtilizator INT PRIMARY KEY,
    DataNasterii DATE NOT NULL,
    DataAngajarii DATE NOT NULL,
    IDManager    INT,
    IDSalariu   INT,

    CONSTRAINT FK_Angajati_Utilizatori FOREIGN KEY (IDUtilizator) REFERENCES Utilizatori (IDUtilizator),
    CONSTRAINT FK_Angajati_Angajati FOREIGN KEY (IDManager) REFERENCES Angajati (IDUtilizator),
    CONSTRAINT FK_Angajati_Salarii FOREIGN KEY (IDSalariu) REFERENCES Salariu(IDSalariu),
    CONSTRAINT CK_DataAngajariiValida_DataAngajarii_DataNasterii CHECK(DataNasterii < DataAngajarii)
);

INSERT INTO Angajati(IDUtilizator, DataNasterii, IDManager, DataAngajarii,IDSalariu)
VALUES (2, date '1959-01-07', NULL, date '2015-01-08', 1);
INSERT INTO Angajati(IDUtilizator, DataNasterii, IDManager, DataAngajarii,IDSalariu)
VALUES (3, date '1990-02-08', 2, date '2016-01-08', 2);
INSERT INTO Angajati(IDUtilizator, DataNasterii, IDManager, DataAngajarii,IDSalariu)
VALUES (4, date '1999-01-07', 2, date '2017-01-08', 3);
INSERT INTO Angajati(IDUtilizator, DataNasterii, IDManager, DataAngajarii,IDSalariu)
VALUES (10, date '2002-07-11', 3, date '2018-01-08', 3);
INSERT INTO Angajati(IDUtilizator, DataNasterii, IDManager, DataAngajarii,IDSalariu)
VALUES (11, date '2001-10-02', 3, date '2020-01-08', 4);

COMMIT;


CREATE TABLE Clienti
(
    IDUtilizator INT PRIMARY KEY,
    DataNasterii DATE NOT NULL,
    IDTip        INT,

    CONSTRAINT FK_Clienti_Utilizatori FOREIGN KEY (IDUtilizator) REFERENCES Utilizatori (IDUtilizator),
    CONSTRAINT FK_Clienti_TipClient FOREIGN KEY (IDTip) REFERENCES TipClient (IDTip)
);

INSERT INTO Clienti(IDUtilizator, DataNasterii, IDTip)
VALUES (5, date '1990-01-08', 1);
INSERT INTO Clienti(IDUtilizator, DataNasterii, IDTip)
VALUES (6, date '1995-04-09', 2);
INSERT INTO Clienti(IDUtilizator, DataNasterii, IDTip)
VALUES (7, date '1999-06-10', 2);
INSERT INTO Clienti(IDUtilizator, DataNasterii, IDTip)
VALUES (8, date '2000-08-11', 3);
INSERT INTO Clienti(IDUtilizator, DataNasterii, IDTip)
VALUES (9, date '2001-09-12', 4);

COMMIT;


CREATE TABLE Adrese(
    IDAdresa INT GENERATED BY DEFAULT AS IDENTITY (START WITH 1 INCREMENT BY 1) PRIMARY KEY,
    Strada VARCHAR(255),
    Oras VARCHAR(255),
    Judet VARCHAR(255),
    CodPostal VARCHAR(255),
    IDClient INT,

    CONSTRAINT FK_Adrese_Clienti FOREIGN KEY (IDClient) REFERENCES Clienti(IDUtilizator)
);

INSERT INTO Adrese (Strada, Oras, Judet, CodPostal, IDClient)
VALUES ('Str. Plopului', 'Buzau', 'Buzau', '120200', 5);
INSERT INTO Adrese (Strada, Oras, Judet, CodPostal, IDClient)
VALUES ('Str. Macesului', 'Bucuresti', 'Bucuresti', '120201', 6);
INSERT INTO Adrese (Strada, Oras, Judet, CodPostal, IDClient)
VALUES ('Str. Rosiei', 'Buzau', 'Buzau', '120200', 7);
INSERT INTO Adrese (Strada, Oras, Judet, CodPostal, IDClient)
VALUES ('Str. Prunului', 'Bucuresti', 'Bucuresti', '120202', 8);
INSERT INTO Adrese (Strada, Oras, Judet, CodPostal, IDClient)
VALUES ('Str. Ardeiului', 'Bucuresti', 'Bucuresti', '120203', 9);
INSERT INTO Adrese (Strada, Oras, Judet, CodPostal, IDClient)
VALUES ('Str. Trandafirului', 'Buzau', 'Buzau', '120204', 5);

COMMIT;


CREATE TABLE Inchiriere
(
--     IDInchiriere   INT PRIMARY KEY,
    DataInchiriere DATE                NOT NULL,
    DurataInZile   NUMBER(2, 0)        NOT NULL,
    IDCamera       INT,
    IDClient       INT,
    IDAngajat      INT,
    IDObiectiv     INT,
    EsteReturnat   CHAR(1) DEFAULT '0' NOT NULL,
    Penalizare     NUMBER(6, 2),

    CONSTRAINT PK_Inchiriere PRIMARY KEY (IDCamera, IDClient, IDAngajat, IDObiectiv, DataInchiriere),
    CONSTRAINT FK_Inchiriere_Camere FOREIGN KEY (IDCamera) REFERENCES Camere (IDCamera),
    CONSTRAINT FK_Inchiriere_Clienti FOREIGN KEY (IDClient) REFERENCES Clienti (IDUtilizator),
    CONSTRAINT FK_Inchiriere_Angajati FOREIGN KEY (IDAngajat) REFERENCES Angajati (IDUtilizator),
    CONSTRAINT FK_Inchiriere_Obiectiv FOREIGN KEY (IDObiectiv) REFERENCES Obiective (IDObiectiv),
    CONSTRAINT CK_BOOLHACK_EsteReturnat CHECK (EsteReturnat IN ('1', '0'))
);

INSERT INTO Inchiriere(DataInchiriere, DurataInZile, IDCamera, IDClient, IDAngajat, EsteReturnat,
                       Penalizare, IDObiectiv)
VALUES (date '2022-01-01', 5, 1, 5, 2, '1', 0, 1);
INSERT INTO Inchiriere(DataInchiriere, DurataInZile, IDCamera, IDClient, IDAngajat, EsteReturnat,
                       Penalizare, IDObiectiv)
VALUES (date '2022-01-05', 2, 1, 5, 2, '1', 10, 2);
INSERT INTO Inchiriere( DataInchiriere, DurataInZile, IDCamera, IDClient, IDAngajat, EsteReturnat,
                       Penalizare, IDObiectiv)
VALUES (date '2022-02-02', 3, 2, 6, 3, '1', 11, 4);
INSERT INTO Inchiriere( DataInchiriere, DurataInZile, IDCamera, IDClient, IDAngajat, EsteReturnat,
                       Penalizare, IDObiectiv)
VALUES (date '2022-03-02', 10, 3, 7, 4, '1', 20, 5);
INSERT INTO Inchiriere( DataInchiriere, DurataInZile, IDCamera, IDClient, IDAngajat, EsteReturnat,
                       Penalizare, IDObiectiv)
VALUES (date '2022-04-03', 11, 4, 8, 3, '1', 0, 6);
INSERT INTO Inchiriere( DataInchiriere, DurataInZile, IDCamera, IDClient, IDAngajat, EsteReturnat,
                       Penalizare, IDObiectiv)
VALUES (date '2022-05-04', 3, 5, 9, 4, '1', 0, 7);
INSERT INTO Inchiriere( DataInchiriere, DurataInZile, IDCamera, IDClient, IDAngajat, EsteReturnat,
                       Penalizare, IDObiectiv)
VALUES (date '2022-05-05', 5, 6, 5, 2, '1', 30, 8);
INSERT INTO Inchiriere( DataInchiriere, DurataInZile, IDCamera, IDClient, IDAngajat, EsteReturnat,
                       Penalizare, IDObiectiv)
VALUES (date '2022-05-10', 4, 7, 5, 2, '1', 0, 9);
INSERT INTO Inchiriere( DataInchiriere, DurataInZile, IDCamera, IDClient, IDAngajat, EsteReturnat,
                       Penalizare, IDObiectiv)
VALUES (date '2022-05-11', 4, 1, 6, 2, '1', 0, 10);
INSERT INTO Inchiriere( DataInchiriere, DurataInZile, IDCamera, IDClient, IDAngajat, EsteReturnat,
                       Penalizare, IDObiectiv)
VALUES (date '2022-05-12', 5, 3, 6, 10, '1', 0, 1);
INSERT INTO Inchiriere( DataInchiriere, DurataInZile, IDCamera, IDClient, IDAngajat, EsteReturnat,
                       Penalizare, IDObiectiv)
VALUES (date '2022-05-13', 6, 1, 7, 11, '1', 50, 2);
INSERT INTO Inchiriere( DataInchiriere, DurataInZile, IDCamera, IDClient, IDAngajat, EsteReturnat,
                       Penalizare, IDObiectiv)
VALUES (date '2022-05-14', 9, 2, 9, 4, '0', 0, 4);

COMMIT;

