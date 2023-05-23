# Vintage Rent
## Etapa 1
Pentru prima etapa poate fi folosita suita de unit testing definita in test.java.org.suites.AllTests

De asemenea, poate fi folosita si interfata grafica de unde se va alege Datasource->InMemory, deoarece reprezinta obiectele stocate in memorie, fara sa fie nevoie deo conexiune la baza de date.
### Definirea sistemului
#### Să se creeze o lista pe baza temei alese cu cel puțin 10 acțiuni/interogări care se pot face în cadrul sistemului și o lista cu cel puțin 8 tipuri de obiecte.

1. Actiuni 
   - CRUD pe fiecare tabela (Address, Administrator, AdministratorSubdomain, Camera, CameraType, Client, Employee, Format, Mount, Objective, Rent, Salary, User)
   - Filtrare (==, <, >, >=, <= si !=) pe fiecare tabela mentionata anterior
   - Trunchiere pe fiecare tabela mentionata anterior
   - Raport clienti
   - Raport formate
   - Raport monturi
   - Email pentru rapoarte
   - Schimbare datasource
2. Lista
   - org.database.memory.InMemory contine Map\<String, ModelList\<?>> modelLists. Fiecare ModelList contine un template separat
   - org.actions.MainService contine o metoda denumita getForMount care contine o lista cu 2 tipuri de obiecte (CameraModel.InnerCameraType si ObjectiveModel.InnerObjectiveType, ambele mostenesc Model.AbstractInnerModel) si minim 8 elemente pentru MountID = 1. Acest lucru este testat in MainServiceTest.getForMountTest()
   - org.database.DatabaseConnection contine ArrayList\<DatabaseConnection> instances, fiecare obiect mostenind DatabaseConnection (fiind toate singleton, numarul de obiecte memorate este 3)

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

## Etapa 2
## !!! ATENTIE !!!
Pentru conexiunea cu baza de date, va trebui sa intrati in folderul **docker_oracle_21c** si sa rulati

```bash
docker-compose up
```

#### Extindeti proiectul din prima etpa prin realizarea persistentei utilizand o baza de date relationala si JDBC
Am folosit solutia de SGBD Oracle DB implementata folosind un container de docker personalizat si urcat pe docker hub.

Conexiunea la baza de date se face in **org.database.oracle.OracleConnection** . Este o clasa de tip singleton ce mosteneste **org.database.DatabaseConnection** . In DatabaseConnection se tine un ArrayList cu toate conexiunile la "bazele de date", adica Oracle, CSV si InMemory deoarece toate acestea au metode similare de manipulare a datelor.

Am implementat CRUD pentru urmatoarele tabele:
- Address
- Administrator
- AdministratorSubdomain
- Camera
- CameraType
- Client
- ClientType
- Employee
- Format
- Mount
- Objective
- Rent
- Salary
- Subdomain
- User

Toate metodele de CRUD pot fi accesate din interfata grafica.

#### Realizarea unui serviciu de audit
Am realizat un serviciu de audit in **org.logger.CsvLogger** . Acesta este un singleton care are o metoda **log(String message)** care scrie in fisierul **Log.csv** mesajul primit ca parametru si data la care a fost scris.

Logurile pot fi citite cu ajutorul metodei **readLogToday()** pentru logul curent sau **readLog()** pentru intreg logul.

Loggerul foloseste o conexiune de tip **org.database.csv.CsvConnection** pentru a scrie in fisierul CSV.

Logul poate fi citit in interfata grafica.

#### Implementarea code reviewului
- Am tradus tot in engleza, inclusiv denumirile campurilor si ale tabelelor din baza de date
- Am sters codul comentat
- Am rezolvat exception handling (nu garantez ca peste tot, deoarece e mai greu de cautat in cod)