# Vintage Rent
## !!! ATENTIE !!!
Pentru conexiunea cu baza de date, va trebui sa intrati in folderul **docker_oracle_21c** si sa rulati

```bash
docker-compose up
```

Aceste instructiuni creaza user-ul, baza de date fiind creata de aplicatie.

## Etapa 1
Pentru prima etapa poate fi folosita suita de unit testing definita in test.java.org.suites.AllTests

De asemenea, poate fi folosita si interfata grafica de unde se va alege Datasource->InMemory, deoarece reprezinta obiectele stocate in memorie, fara sa fie nevoie deo conexiune la baza de date.
### Definirea sistemului
#### Să se creeze o lista pe baza temei alese cu cel puțin 10 acțiuni/interogări care se pot face în cadrul sistemului și o lista cu cel puțin 8 tipuri de obiecte.

1. Actiuni 
   - CRUD pe fiecare tabela (Address, Administrator, AdministratorSubdomain, Camera, CameraType, Client, Employee, Format, Mount, Objective, Rent, Salary, User)
   - Filtrare (==, <, >, >=, <= si !=) pe fiecare tabela mentionata anterior
   - Raport clienti
   - Raport formate
   - Raport monturi
   - Email pentru rapoarte
   - Schimbare datasource
2. Lista
   - org.database.memory.InMemory contine List\<ModelList> modelLists. Fiecare ModelList contine un template separat
   - org.database.DatabaseConnection contine ArrayList\<DatabaseConnection> instances, fiecare obiect mostenind DatabaseConnection
   - org.actions.MainService contine o metoda denumita getForMount care contine o lista cu 2 tipuri de obiecte (CameraModel.InnerCameraType si ObjectiveModel.InnerObjectiveType, ambele mostenesc Model.AbstractInnerModel) si minim 8 elemente pentru MountID = 1. Acest lucru este testat in MainServiceTest.getForMountTest()

### Implementare
#### clase simple cu atribute private / protected și metode de acces
Clasele sunt: 
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
    - AddressAdd
    - AdministratorAdd
    - AdministratorSubdomainAdd
    - ClientAdd
    - MountAdd
    - ObjectiveAdd
    - SalaryAdd
    - SubdomainAdd
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
  - AddressModel extends Model implements LinkModelToDatabase
    - InnerAddressModel extends AbstractInnerModel
  - AdministratorModel extends Model implements LinkModelToDatabase
    - InnerAdministratorModel extends AbstractInnerModel
  - AdministratorSubdomainModel extends Model implements LinkModelToDatabase
    - InnerAdministratorSubdomainModel extends AbstractInnerModel
  - SubdomainModel extends Model implements LinkModelToDatabase
    - InnerSubdomainModel extends AbstractInnerModel
  - ClientTypeModel extends Model implements LinkModelToDatabase
    - InnerClientTypeModel extends AbstractInnerModel
  - MountTypeModel extends Model implements LinkModelToDatabase
    - InnerMountTypeModel extends AbstractInnerModel
  - ObjectiveTypeModel extends Model implements LinkModelToDatabase
    - InnerObjectiveTypeModel extends AbstractInnerModel
  - SalaryTypeModel extends Model implements LinkModelToDatabase
    - InnerSalaryTypeModel extends AbstractInnerModel
  - LinkModelToDatabase (interface)
  - ModelInit
  - Pair\<T,U>
  - ModelList\<T>
- vintage
  - Main
#### cel puțin 2 colecții diferite capabile să gestioneze obiectele definite anterior (eg: List, Set, Map, etc.) dintre care cel puțin una sa fie sortata –se vor folosi array-uri uni-/bidimensionale în cazul în care nu se parcurg colectiile pana la data checkpoint-ului.
Colectiile sunt:
- ModelList \<T extends Model.AbstractInnerModel> contine o lista de obiecte de tipul T si o poate sorta
- org.database.DatabaseConnection contine ArrayList\<DatabaseConnection> instances o lista cu instantele de conexiune la baza de date
- org.database.memory.InMemory contine List\<ModelList> modelLists o lista cu modelele de date
- org.actions.MainService contine metoda formatSales() care are ca liste:
  - Map\<String, String> result
  - TreeMap\<Double, Double> rentPriceMap
- org.action.MainService contine metoda ClientRents() care are ca liste:
  - List\<Model.AbstractInnerModel> list
  - Map\<String, String> result
- org.action.MainService contine metoda getForMount() care are ca lista:
  - List\<Model.AbstractInnerModel> result
#### utilizare moștenire pentru crearea de clase adiționale și utilizarea lor încadrul colecțiilor;
Am utilizat mostenire si apoi am incadrat in colectii in:
- ModelList\<T extends Model.AbstractInnerModel>
- org.action.MainService metoda getForMount() are ca lista List\<Model.AbstractInnerModel> result
- org.database.DatabaseConnection contine ArrayList\<DatabaseConnection> instances o lista cu instantele de conexiune la baza de date mostenite din DatabaseConnection
#### cel puțin o clasă serviciu care sa expună operațiile sistemului
Clasa de serviciu este MainService. Prin ea se face CRUD, filtrare si se genereaza rapoarte.

#### o clasa Main din care sunt făcute apeluri către servicii
Am utilizat unit testing avand ca suita: test.java.org.suites.AllTests . Pentru acest lucru a fost nevoie de o stocare in memorie cu ajutorul: org.databases.memory.InMemory

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
- Filtrare inchiriere
___
- Vizualizare camere
- Modificare camera
- Stergere camera
- Creare camera
- Filtrare camera
___
- Vizualizare tipuri camere
- Modificare tip camera
- Stergere tip camera
- Creare tip camera
- Filtrare tip camera
___
- Vizualizare formaturi camere
- Modificare format camera
- Stergere format camera
- Creare format camera
- Filtrare format camera
___
- Vizualizare angajati
- Modificare angajat
- Stergere angajat
- Creare angajat
- Filtrare angajat
___
- Vizualizare salarii
- Modificare salariu
- Stergere salariu
- Creare salariu
- Filtrare salariu
___
- Vizualizare utilizatori
- Modificare utilizator
- Stergere utilizator
- Creare utilizator
- Filtrare utilizator
___
- Vizualizare adrese
- Modificare adresa
- Stergere adresa
- Creare adresa
- Filtrare adresa
___
- Vizualizare administratori
- Modificare administrator
- Stergere administrator
- Creare administrator
- Filtrare administrator
___
- Vizualizare administratori subdomenii
- Stergere administrator subdomeniu
- Creare administrator subdomeniu
- Filtrare administrator subdomeniu
___
- Vizualizare clienti
- Modificare client
- Stergere client
- Creare client
- Filtrare client
___
- Vizualizare tipuri client
- Modificare tip client
- Stergere tip client
- Creare tip client
- Filtrare tip client
___
- Vizualizare monturi
- Modificare montura
- Stergere montura
- Creare montura
- Filtrare montura
___
- Vizualizare obiective
- Modificare obiectiv
- Stergere obiectiv
- Creare obiectiv
- Filtrare obiectiv
___
- Vizualizare salarii
- Modificare salariu
- Stergere salariu
- Creare salariu
- Filtrare salariu
___
- Vizualizare subdomenii
- Modificare subdomeniu
- Stergere subdomeniu
- Creare subdomeniu
- Filtrare subdomeniu
___
- Raport clienti
- Raport vanzari per format
- Raport monturi
___
- Email pentru rapoarte
___
- Schimbare datasource: Oracle/CSV
___
- Logging
- Afisare loguri
___
