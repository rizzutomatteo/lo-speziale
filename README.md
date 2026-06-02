# Lo Speziale

**Lo Speziale** è un gioco di ruolo di alchimia e medicina ambientato in un regno
flagellato da una pestilenza, la _Moria Cinerea_. Si vestono i panni di uno
speziale errante che viaggia di villaggio in villaggio, diagnostica i mali secondo
la teoria dei **quattro umori** (sangue, flemma, bile gialla, bile nera), prepara
rimedi a partire dagli ingredienti e cura i pazienti in **incontri a turni**,
accrescendo via via le proprie abilità e la propria fama.

> Il gioco è il **contesto**: l'obiettivo del progetto è mostrare un sistema
> software ben progettato — responsabilità delle classi, principi **SOLID**, Clean
> Code, estendibilità — realizzato con gli strumenti e le metodologie viste nel
> corso di **Metodologie di Programmazione** (UNICAM, A.A. 2025/2026).

![Il banco dello speziale](wiki/images/treatment.png)

## Requisiti

- **JDK 25** (il `toolchain` di Gradle può scaricarlo automaticamente)
- Nient'altro: il **Gradle Wrapper** è incluso nel repository

## Compilazione ed esecuzione

Due soli comandi bastano per compilare ed eseguire il progetto su qualsiasi
computer:

```bash
# 1 - Compilazione (compila ed esegue i test)
./gradlew build

# 2 - Esecuzione
./gradlew run
```

## Funzionalità principali

- Creazione dello speziale e progressione di **abilità** (Diagnosi, Erboristeria,
  Distillazione) e di **fama**
- **Diagnosi** e **cura a turni** fondate sul riequilibrio dei quattro umori
- **Preparazione di rimedi** dagli ingredienti, secondo ricette
- **Mercato**, **viaggio** tra i villaggi e scorrere dei giorni
- **Salvataggio e caricamento** della partita
- Tutti i contenuti (sintomi, malanni, ingredienti, rimedi, ricette, mondo) sono
  **definiti in file di dati JSON**: aggiungerne di nuovi non richiede di
  modificare il codice

## Struttura del progetto

```
src/main/java/it/unicam/cs/mpgc/rpg126114/
├── model/        dominio: umori, malanni, alchimia, personaggi, mondo, cura, inventario, progressione
├── content/      caricamento dei contenuti statici dalle risorse JSON (Gson)
├── game/         orchestrazione della partita (stato, servizi, regia)
├── persistence/  salvataggio e caricamento su file (Gson)
├── gui/          interfaccia grafica JavaFX (controller e navigazione, schema MVC)
└── util/         utilità trasversali (validazione, sorgente di casualità)

src/main/resources/it/unicam/cs/mpgc/rpg126114/
├── data/         contenuti del gioco (JSON)
└── view/         interfacce FXML e foglio di stile CSS

src/test/java/    test JUnit di modello, motore, contenuti, partita e persistenza
```

## Documentazione

La documentazione completa — funzionalità, responsabilità di classi e interfacce,
organizzazione dei dati e persistenza, meccanismi di estensione e scelte
progettuali — si trova nella **[Wiki](../../wiki)** del repository; i sorgenti
delle pagine sono anche nella cartella [`wiki/`](wiki).

## Pacchetto

Tutte le classi appartengono al package `it.unicam.cs.mpgc.rpg126114`, come
richiesto dalla specifica di progetto.

## Dichiarazione sull'uso di strumenti di AI

<!-- Sezione da compilare a cura dell'autore prima della consegna, come richiesto dalla specifica. -->

_(da completare)_
