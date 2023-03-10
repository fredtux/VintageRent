# Vintage Rent
## !!! ATENTIE !!!
Pentru conexiunea cu baza de date, va trebui sa intrati in folderul **docker_oracle_21c** si sa rulati

```bash
docker-compose up
```

Apoi va conectati pe Linux cu:
- host: 172.21.128.2
- port: 1521
- SID: XE
- user: SYSTEM
- parola: fmilove

Sau pe Windows cu:
- host: 0.0.0.0
- port: 1521
- SID: XE
- user: SYSTEM
- parola: fmilove

Dupa aceea rulati comenzile urmatoare pentru a crea userul folosit de program:
```sql
CREATE USER C##TUX IDENTIFIED BY "fmilove";
```
si
```sql
GRANT ALL PRIVILEGES TO C##TUX;
```

Aceste instructiuni creaza user-ul, baza de date fiind creata de aplicatie.

## Documentatie
Documentatia se gaseste in format PDF in fisierul [Documentatie.pdf](https://github.com/fredtux/VintageRent/blob/main/Documentatie.pdf)

## Cerinte
### Etapa I
1. Definirea sistemului
- [x] Să se creeze o lista pe baza temei alese cu cel puțin 10 acțiuni/interogări care se pot face în cadrul sistemului și o lista cu cel puțin 8 tipuri de obiecte.
2. Implementare
- [x] Sa se implementeze în limbajul Java o aplicație pe baza celor definite la primul punct.
- Aplicatia va contine:
  - [x] clase simple cu atribute private / protected și metode de acces
  - [x] cel puțin 2 colecții diferite capabile să gestioneze obiectele definite anterior (eg: List, Set, Map, etc.) dintre care cel puțin una sa fie sortata –se vor folosi array-uri uni-/bidimensionale în cazul în care nu se parcurg colectiile pana la data checkpoint-ului.
  - [x] utilizare moștenire pentru crearea de clase adiționale și utilizarea lor încadrul colecțiilor;
  - [x] cel puțin o clasă serviciu care sa expună operațiile sistemului
  - [x] o clasa Main din care sunt făcute apeluri către servicii
    - Implementare proprie: O clasa Main care incarca resursele si lanseaza GUI-ul. O clasa MainForm care contine ecranul principal + clase pentru ecranele de adaugare

### Etapa II
1. Extindeți proiectul din prima etapa prin realizarea persistentei utilizând o baza de date relationala si JDBC.
- [x] Să se realizeze servicii care sa expună operații de tip create, read, update si delete pentru cel puțin 4 dintre clasele definite.
- [x] Se vor realiza servicii singleton generice pentru scrierea și citirea din baza de date.
2. Realizarea unui serviciu de audit
- [x] Se va realiza un serviciu care sa scrie într-un fișier de tip CSV de fiecare data când este executată una dintre acțiunile descrise în prima etapa.
- [x] Structura fișierului: nume_actiune, timestamp.

### Observatii
- [x] Toata lumea trebuie sa foloseasca Git & Maven. Proiectele nu se trimit ca arhiva.
- [x] Proiectul trebuie sa aiba documentatie. Fiecare scrie documentatia cum vrea, in doc/pdf sau in cod. Trebuie sa stii sa explici ce ai facut in cod, nu e de ajuns doar sa-l scrii.
- [x] Versiunea Java o sa fie MINIM 17.
- [x] O sa folosim Docker & Docker-Compose pentru baza de date.
- [x] O sa folosim Github Workflows pentru pipelines. Se pot accepta alternative pentru cei care vor sa foloseasca altceva.
- [ ] Cei mai activi o sa aiba o surpriza la final de semestru.

## Actiuni / Interogari:
___
- Vizualizare inchirieri
- Modificare inchiriere
- Stergere inchiriere
- Creare inchiriere
___
- Vizualizare camere
- Modificare camera
- Stergere camera
- Creare camera
___
- Vizualizare tipuri camere
- Modificare tip camera
- Stergere tip camera
- Creare tip camera
___
- Vizualizare formaturi camere
- Modificare format camera
- Stergere format camera
- Creare format camera
___
- Vizualizare angajati
- Modificare angajat
- Stergere angajat
- Creare angajat
___
- Vizualizare salarii
- Modificare salariu
- Stergere salariu
- Creare salariu
___
- Vizualizare utilizatori
- Modificare utilizator
- Stergere utilizator
- Creare utilizator
___
- Raport clienti
- Raport vanzari per format
___
- Schimbare datasource: Oracle/CSV
___
- Logging
- Afisare loguri
___

## Clase
- actions
  - MainService
- database
  - DatabaseConnection (factory)
  - csv
    - CSVConnection
  - oracle
    - OracleConnection
- gui
  - custom
    - ComboItem
  - main
    - MainGUI
  - reports
    - ClientReport
  - splash
    - Splash
  - tables
    - CameraAdd
    - CameraTypeAdd
    - FormatAdd
    - RentAdd
    - EmployeeAdd
    - UserAdd
  - logs
    - LogGUI
- logger
  - CsvLogger
- models
  - Model
    - AbstractInnerModel 
  - CameraModel extends Model
    - InnerCameraModel extends AbstractInnerModel
  - CameraTypeModel extends Model implements LinkModelToDatabase
    - InnerCameraTypeModel extends AbstractInnerModel
  - ClientModel extends Model implements LinkModelToDatabase
    - InnerClient extends AbstractInnerModel
  - EmployeeModel extends Model implements LinkModelToDatabase
    - InnerEmployeeModel extends AbstractInnerModel
  - FormatModel extends Model implements LinkModelToDatabase
    - InnerFormatModel extends AbstractInnerModel
  - MountModel extends Model implements LinkModelToDatabase
    - InnerMountModel extends AbstractInnerModel
  - ObjectiveModel extends Model implements LinkModelToDatabase
    - InnerObjectiveModel extends AbstractInnerModel
  - RentModel extends Model implements LinkModelToDatabase
    - InnerRentModel extends AbstractInnerModel
  - SalaryModel extends Model implements LinkModelToDatabase
    - InnerSalaryModel extends AbstractInnerModel
  - UserModel extends Model implements LinkModelToDatabase
    - InnerUserModel extends AbstractInnerModel
  - LinkModelToDatabase (interface)
  - ModelInit
  - Pair<T,U>
  - ModelList<T>
- vintage
  - Main