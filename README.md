# 2022T1

## Heimdall: statistiche file di log http in modo semplificato tramite web app.
### Web app che permette di visualizzare tramite statistiche e grafici il contenuto di file di log generati da Apache web server.
# GRUPPO

- Alex Caraffi (superflem), Project Owner
- Fabio Zanichelli (Zukkerino) , Scrum Master
- Francesco Castorini (Il-castor)
- Antonio Benevento Vitale Nigro (BennyBanana)
- Luca Dall'Olio (Belix)
- Francesco Malferrari  

# Come utilizzare il repository
- /doc/presentation ← Presentazioni (ppt. o simile); NON caricate i video, mettete dei link.
- /code ← Codice del progetto (inizialmente vuoto); opzionalmente può essere anche in radice.
- /database ← Dump del/dei database (se previsti)
- /test ← Codice di test di unità (TDD, TL), descrizione test di integrazione e accettazione se non
integrati nell’IDE

# LINK
- [NodeJS](https://nodejs.dev/)id=sxcmexmn8jgm3xfw3s7atdaqwy)
# Usare l'applicazione

## Frontend

### Per eseguire questo servizio è necessario avere installato NodeJS

### Script eseguibili

Nella cartella /code/frontend si possono eseguire i seguenti script:

#### `npm install`
Installa tutti i moduli necessari per eseguire i file

#### `npm start`
Esegue l'applicazione React. Apri [http://localhost:3000](http://localhost:3000) nel browser.
Se non funziona, lanciarlo da amministratore

## Backend

### Per eseguire questo servizio è necessario avere una versione di JDK 15 o superiore

Aprire la root del progetto da terminale e lanciare il seguente comando:

#### `java -jar ./code/code.jar`
