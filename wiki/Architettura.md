# Architettura

Il progetto è organizzato in **livelli** con una direzione delle dipendenze
precisa: i livelli alti dipendono da quelli bassi, mai il contrario. Ogni livello
è un package sotto `it.unicam.cs.mpgc.rpg126114`.

```
gui  ─────────────┐
                  ▼
game (regia) ──► persistence ──► content ──► model ◄── util
```

- **`model`** — il **dominio**: umori, malanni, alchimia, personaggi, mondo, cura,
  inventario, progressione. Non dipende da nessun altro livello del progetto e non
  sa nulla di JSON, di file o di interfaccia grafica.
- **`content`** — carica i **contenuti statici** (sintomi, malanni, ingredienti,
  rimedi, ricette, mondo) dalle risorse JSON e li offre dietro l'interfaccia
  `ContentRepository`.
- **`persistence`** — **salva e carica** lo stato della partita su file, dietro
  l'interfaccia `GameRepository`.
- **`game`** — la **regia**: coordina viaggio, visite, preparazione e mercato sopra
  il dominio e i due livelli di dati, esponendo una facciata `Game` all'interfaccia.
- **`gui`** — l'**interfaccia** JavaFX, secondo lo schema **MVC**: ogni schermata è
  un file FXML (la _vista_) con un _controller_, e `GameUi` fa da navigatore.
- **`util`** — utilità trasversali: validazione degli argomenti e una sorgente di
  casualità astratta.

## Perché questa divisione

La regola «le dipendenze puntano verso il dominio» è ciò che rende il progetto
**estendibile** e **collaudabile**:

- il dominio si può testare senza file, senza rete e senza schermo (i test di
  `model`, infatti, non avviano nulla di grafico);
- la provenienza dei dati (oggi JSON) e il modo di disegnare le schermate (oggi
  JavaFX) sono **dettagli sostituibili**, perché il resto del codice dipende dalle
  interfacce `ContentRepository`, `GameRepository`, `RandomSource` e non dalle loro
  implementazioni concrete.

## L'interfaccia grafica come MVC

- **Modello**: gli oggetti di `model` e `game`.
- **Vista**: i file FXML in `resources/.../view` con un foglio di stile CSS comune.
- **Controller**: una classe per schermata in `gui`, che legge il modello tramite
  la facciata `Game` e reagisce agli eventi.

La navigazione è centralizzata in `GameUi`, che ospita l'unica finestra e sa
costruire ogni schermata. Lo schermo della cura osserva il motore di gioco
(`TreatmentSession`) tramite l'interfaccia `TreatmentObserver`: dopo ogni mossa il
motore notifica e il controller si ridisegna, senza che il dominio sappia nulla
dell'interfaccia.

## Strumenti e concetti del corso impiegati

| Argomento del corso                     | Dove compare nel progetto                                                                |
| --------------------------------------- | ---------------------------------------------------------------------------------------- |
| Classi astratte e metodi astratti       | `AbstractEntity`, `AbstractCharacter` (con `getRole()` astratto)                         |
| Interfacce e polimorfismo               | `RemedyEffect`, `TreatmentAction`, `ContentRepository`, `GameRepository`, `RandomSource` |
| Ereditarietà ed `equals`/`identità`     | gerarchia delle entità con uguaglianza per id in `AbstractEntity`                        |
| Incapsulamento e modificatori d'accesso | campi privati e metodi dall'intento chiaro in tutto il dominio                           |
| Generici                                | `Registry<T extends Identifiable>`                                                       |
| Collezioni (List, Set, Map)             | inventario, ricettario, registri dei contenuti, mappa del mondo                          |
| Stream                                  | interrogazioni su rimedi, ricette, villaggi                                              |
| Persistenza con Gson e _type adapter_   | `JsonContentLoader`, `FileGameRepository`, `RemedyEffectAdapter`, `LocalDateAdapter`     |
| Interfaccia grafica (JavaFX, FXML, CSS) | l'intero package `gui` e le risorse `view`                                               |
| Testing (JUnit)                         | i test in `src/test`                                                                     |
| Gradle, Git, GitHub, CI, Wiki           | build a due comandi, cronologia dei commit, GitHub Actions, questa Wiki                  |

Si veda **[Principi SOLID e Clean Code](Principi-SOLID-e-Clean-Code)** per il
dettaglio delle scelte progettuali.
