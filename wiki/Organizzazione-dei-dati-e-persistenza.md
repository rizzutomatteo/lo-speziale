# Organizzazione dei dati e persistenza

Il progetto distingue nettamente due tipi di dati:

1. i **contenuti statici** del gioco, uguali per ogni partita (sintomi, malanni,
   ingredienti, rimedi, ricette, mondo);
2. lo **stato della partita**, che cambia mentre si gioca e va salvato.

Entrambi sono gestiti con **file JSON** tramite la libreria **Gson**, come visto
nel corso.

## Contenuti statici

I contenuti vivono come risorse JSON sotto
`resources/it/unicam/cs/mpgc/rpg126114/data/`, un file per tipo:

```
data/symptoms.json     data/ingredients.json   data/recipes.json
data/ailments.json     data/remedies.json      data/world.json
```

`JsonContentLoader` li legge con uno _stream_ di caratteri
(`InputStreamReader` su `BufferedReader`), li converte in piccoli _record_ di
trasferimento e infine li **mappa** sugli oggetti di dominio. La mappatura in un
passo dedicato tiene il dominio del tutto ignaro del JSON e permette al loader di
**risolvere i riferimenti** (una ricetta verso il suo rimedio, un male verso i suoi
sintomi) e di **rifiutare in anticipo** i contenuti incoerenti, sollevando
`ContentException`.

Un rimedio dichiara i propri effetti in modo dichiarativo:

```json
{
  "id": "elisir_argento",
  "name": "Elisir d'Argento",
  "effects": [
    { "type": "humoral_shift", "humor": "SANGUE", "amount": -3 },
    { "type": "humoral_shift", "humor": "BILE_NERA", "amount": -3 },
    { "type": "toxicity", "amount": 2 }
  ]
}
```

Gli effetti sono **polimorfi**: il campo `type` fa da discriminante e
`RemedyEffectAdapter`, registrato sul `GsonBuilder` con `registerTypeAdapter`,
sceglie l'implementazione giusta. È esattamente lo schema _adapter_ per la
serializzazione visto a lezione, ed è ciò che consente di descrivere gli effetti
nei dati senza che il loader conosca in anticipo le classi concrete.

I contenuti caricati sono offerti, in sola lettura, dietro l'interfaccia
`ContentRepository`: il resto del gioco dipende da questa astrazione, non dai file.

## Stato della partita

Il salvataggio è affidato all'interfaccia `GameRepository`; l'implementazione
`FileGameRepository` scrive un file JSON nella cartella personale dell'utente
(`~/.lo-speziale/partita.json`).

Punto chiave: il salvataggio **non** serializza gli oggetti interi, ma solo
**identificatori e quantità** (le abilità, i fiorini, gli id degli ingredienti e
dei rimedi con i loro conteggi, le ricette conosciute, il giorno e il villaggio
corrente). In fase di caricamento lo speziale viene **ricostruito** e il mondo è
ripreso dal `ContentRepository`.

```json
{
  "apothecaryName": "Orsola",
  "day": 4,
  "currentVillageId": "borgocenere",
  "reputationPoints": 26,
  "coins": 41,
  "knownRecipes": [
    "r_salice",
    "r_camomilla",
    "r_menta",
    "r_genziana",
    "r_carbone"
  ],
  "ingredients": { "salice": 2, "miele": 3 },
  "remedies": { "decotto_genziana": 1 },
  "savedAt": "2026-06-13"
}
```

Questa scelta tiene il file **piccolo** e permette ai contenuti statici di
evolvere senza invalidare i salvataggi. Il campo `savedAt` è una `LocalDate`,
gestita da `LocalDateAdapter` (di nuovo lo schema _adapter_ di Gson).

Tutte le operazioni di lettura/scrittura passano per `BufferedReader`/
`BufferedWriter` e gli eventuali errori sono incapsulati in `PersistenceException`.
