# Vintage Rent
**Atentie!** Va trebui sa clonati submodulul de docker din proiect si sa-l configurati asa cum apare in README.md-ul lui.

Apoi rulati comenzile:
```sql
CREATE USER C##TUX IDENTIFIED BY 'fmilove';
```
si
```sql
GRANT ALL PRIVILEGES TO C##TUX
```

Aceste instructiuni creaza user-ul, baza de date fiind creata de aplicatie.

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
- Raport clienti
___
- Schimbare datasource: Oracle/CSV
___
- Afisare informatii about
___

## Obiecte
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
  - logs
    - LogGUI
- logger
  - CsvLogger
- models
  - Model
  - CameraModel extends Model
    - InnerCameraModel
  - CameraTypeModel extends Model implements LinkModelToDatabase
    - InnerCameraTypeModel
  - ClientModel extends Model implements LinkModelToDatabase
    - InnerClient
  - EmployeeModel extends Model implements LinkModelToDatabase
    - InnerEmployeeModel
  - FormatModel extends Model implements LinkModelToDatabase
    - InnerFormatModel
  - MountModel extends Model implements LinkModelToDatabase
    - InnerMountModel
  - ObjectiveModel extends Model implements LinkModelToDatabase
    - InnerObjectiveModel
  - RentModel extends Model implements LinkModelToDatabase
    - InnerRentModel
  - LinkModelToDatabase (interface)
  - ModelInit
  - Pair<T,U>
  - ModelList<T>
- vintage
  - Main
  
## Cerinte
