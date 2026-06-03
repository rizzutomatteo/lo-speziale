# Responsabilità delle classi e delle interfacce

Per ogni tipo è indicata la **responsabilità** che gli compete. I tipi sono
raggruppati per livello.

## util

| Tipo                           | Responsabilità                                                                               |
| ------------------------------ | -------------------------------------------------------------------------------------------- |
| `Preconditions`                | Raccogliere i controlli sugli argomenti (non nullo, non vuoto, intervallo) in un solo posto. |
| `RandomSource` _(interfaccia)_ | Astrarre la casualità, così che il dominio resti deterministico nei test.                    |
| `DefaultRandomSource`          | Fornire la casualità reale basata su `java.util.Random` (con seme opzionale).                |

## model (fondamenta)

| Tipo                               | Responsabilità                                                      |
| ---------------------------------- | ------------------------------------------------------------------- |
| `Identifiable` _(interfaccia)_     | Dichiarare che un oggetto possiede un id stabile.                   |
| `AbstractEntity`                   | Dare a un'entità id e uguaglianza per id, una volta sola per tutte. |
| `Registry<T extends Identifiable>` | Offrire una raccolta immutabile di entità interrogabili per id.     |

## model.humor

| Tipo             | Responsabilità                                                                                                        |
| ---------------- | --------------------------------------------------------------------------------------------------------------------- |
| `Humor` _(enum)_ | Rappresentare i quattro umori e le loro qualità.                                                                      |
| `HumoralBalance` | Custodire in modo **immutabile** i livelli dei quattro umori e calcolare squilibrio, tolleranza e umore più alterato. |

## model.ailment

| Tipo                | Responsabilità                                                                                                    |
| ------------------- | ----------------------------------------------------------------------------------------------------------------- |
| `Symptom`           | Descrivere un sintomo e l'umore che lo provoca.                                                                   |
| `Severity` _(enum)_ | Tradurre un punteggio di gravità in una parola (Lieve…Critica).                                                   |
| `Ailment`           | Definire un male: squilibrio caratteristico, sintomi e virulenza.                                                 |
| `Affliction`        | Custodire lo **stato clinico mutevole** di un malato durante la cura e farne evolvere umori, tossicità e sintomi. |

## model.alchemy e model.alchemy.effect

| Tipo                           | Responsabilità                                                      |
| ------------------------------ | ------------------------------------------------------------------- |
| `RemedyEffect` _(interfaccia)_ | Rappresentare _una_ cosa che un rimedio fa al corpo (strategia).    |
| `HumoralShiftEffect`           | Spostare un umore.                                                  |
| `ToxicityEffect`               | Aggiungere tossicità.                                               |
| `PurifyEffect`                 | Rimuovere tossicità.                                                |
| `Rarity` _(enum)_              | Indicare la rarità di un ingrediente e il moltiplicatore di prezzo. |
| `Ingredient`                   | Descrivere un ingrediente (rarità, prezzo, affinità).               |
| `Remedy`                       | Un rimedio: un fascio di effetti che si applica a un'affezione.     |
| `Recipe`                       | La formula che trasforma ingredienti in un rimedio.                 |

## model.character, model.progression, model.inventory, model.economy

| Tipo                      | Responsabilità                                                                                   |
| ------------------------- | ------------------------------------------------------------------------------------------------ |
| `AbstractCharacter`       | Base delle persone del gioco (id, nome, ruolo astratto).                                         |
| `Patient`                 | Un malato che porta con sé l'affezione da curare.                                                |
| `Apothecary`              | Il personaggio giocante: abilità, fama, borsa, inventario e ricettario, con i propri invarianti. |
| `SkillType` _(enum)_      | I tre mestieri dello speziale.                                                                   |
| `Skill`                   | Un mestiere con il suo livello ed esperienza.                                                    |
| `ReputationTier` _(enum)_ | I ranghi di fama.                                                                                |
| `Reputation`              | I punti fama e il rango che ne deriva.                                                           |
| `Stockpile`               | Contare oggetti per id (riusato per inventario e mercato).                                       |
| `Inventory`               | Tenere distinti gli ingredienti e i rimedi dello speziale.                                       |
| `RecipeBook`              | L'insieme delle ricette conosciute.                                                              |
| `Coins`                   | Un importo di fiorini **immutabile** e mai negativo.                                             |

## model.world

| Tipo        | Responsabilità                                         |
| ----------- | ------------------------------------------------------ |
| `Region`    | Un'area che raggruppa villaggi affini.                 |
| `Village`   | Un luogo con mercato e mali caratteristici.            |
| `WorldMap`  | I villaggi, le regioni e i giorni di cammino tra loro. |
| `GameClock` | Tenere il conto dei giorni.                            |

## model.treatment

| Tipo                                                                                      | Responsabilità                                                                                  |
| ----------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------- |
| `TreatmentOutcome` _(enum)_                                                               | Come finisce una cura, o che è ancora in corso.                                                 |
| `TreatmentAction` _(interfaccia)_                                                         | Una mossa della cura (schema Command).                                                          |
| `TreatmentContext` _(interfaccia)_                                                        | La vista ristretta che le mosse possono manipolare.                                             |
| `ExamineAction`, `ApplyRemedyAction`, `ComfortAction`, `BloodlettingAction`, `WaitAction` | Le singole mosse.                                                                               |
| `TreatmentObserver` _(interfaccia)_                                                       | Essere avvisati a ogni turno (schema Observer).                                                 |
| `TreatmentSession`                                                                        | Il **motore a turni**: regola il tempo, applica le mosse, fa evolvere il male e decide l'esito. |

## content

| Tipo                                | Responsabilità                                                                           |
| ----------------------------------- | ---------------------------------------------------------------------------------------- |
| `ContentRepository` _(interfaccia)_ | Offrire in sola lettura tutti i contenuti statici.                                       |
| `GameContent`                       | Implementare `ContentRepository` con un `Registry` per tipo.                             |
| `ContentLoader` _(interfaccia)_     | Astrarre da dove provengono i contenuti.                                                 |
| `JsonContentLoader`                 | Leggere i contenuti dai file JSON, risolverne i riferimenti e segnalare le incongruenze. |
| `RemedyEffectAdapter`               | Insegnare a Gson a leggere/scrivere gli effetti polimorfi.                               |
| `ContentException`                  | Segnalare contenuti mancanti o incoerenti.                                               |

## game

| Tipo               | Responsabilità                                                                     |
| ------------------ | ---------------------------------------------------------------------------------- |
| `GameState`        | Lo stato mutevole della partita (speziale, calendario, luogo, conteggi).           |
| `PatientGenerator` | Generare i malati di un villaggio a partire dai suoi mali.                         |
| `Brewer`           | Preparare un rimedio da una ricetta, con esito riferito.                           |
| `Market`           | Vendere ingredienti a prezzo legato alla rarità.                                   |
| `Game`             | La **facciata**: coordina viaggio, cura, preparazione e mercato per l'interfaccia. |
| `TreatmentReport`  | L'esito di una cura conclusa, pronto da mostrare.                                  |
| `GameFactory`      | Costruire la situazione iniziale di una nuova partita.                             |

## persistence

| Tipo                             | Responsabilità                                     |
| -------------------------------- | -------------------------------------------------- |
| `GameRepository` _(interfaccia)_ | Salvare e caricare una partita.                    |
| `FileGameRepository`             | Implementare il salvataggio su file JSON con Gson. |
| `LocalDateAdapter`               | Insegnare a Gson a leggere/scrivere una data.      |
| `PersistenceException`           | Segnalare salvataggi non scrivibili o illeggibili. |

## gui

| Tipo                                                                                                                                                                     | Responsabilità                                                                         |
| ------------------------------------------------------------------------------------------------------------------------------------------------------------------------ | -------------------------------------------------------------------------------------- |
| `GameUi`                                                                                                                                                                 | Ospitare la finestra e fare da **navigatore** tra le schermate.                        |
| `UiComponents`                                                                                                                                                           | Costruire i nodi grafici condivisi (etichette, tag, badge).                            |
| `MainMenuController`, `NewGameController`, `VillageController`, `TreatmentController`, `WorkshopController`, `MarketController`, `TravelController`, `VictoryController` | Un _controller_ per schermata: legge il modello tramite `Game` e reagisce agli eventi. |
| `Main`, `LoSpezialeApp`                                                                                                                                                  | Avvio dell'applicazione e composizione dei collaboratori.                              |
