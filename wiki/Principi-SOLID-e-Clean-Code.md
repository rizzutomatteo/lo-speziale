# Principi SOLID e Clean Code

Le scelte progettuali sono guidate dai principi **SOLID** e dalle pratiche di
**Clean Code** viste nel corso. Di seguito, per ciascun principio, come è stato
applicato.

## S — Single Responsibility

Ogni classe ha **una** ragione per cambiare. Qualche esempio:

- `HumoralBalance` custodisce i livelli degli umori; `Affliction` ne governa
  l'evoluzione clinica; `TreatmentSession` regola i turni e l'esito. Sono tre
  responsabilità distinte, in tre classi distinte.
- La generazione dei malati (`PatientGenerator`), la preparazione dei rimedi
  (`Brewer`) e il commercio (`Market`) sono servizi separati, non metodi gonfi di
  un'unica classe.

## O — Open/Closed

Il sistema è **aperto all'estensione, chiuso alla modifica**:

- nuovi **effetti** dei rimedi (`RemedyEffect`) e nuove **mosse** di cura
  (`TreatmentAction`) si aggiungono come nuove classi, senza toccare il motore;
- nuovi **contenuti** si aggiungono nei file JSON, senza toccare il codice.

## L — Liskov Substitution

Ogni sottotipo è utilizzabile al posto del suo tipo base senza sorprese: il motore
applica un qualsiasi `TreatmentAction`, un rimedio applica un qualsiasi
`RemedyEffect`, l'interfaccia mostra un qualsiasi `AbstractCharacter` tramite il
suo `getRole()`.

## I — Interface Segregation

Le interfacce sono **piccole e mirate**. Il caso più chiaro è `TreatmentContext`:
espone alle mosse solo ciò che serve (leggere paziente e speziale, registrare nel
diario, concedere esperienza o pazienza), **non** il governo dei turni, che resta
di `TreatmentSession`. Le mosse possono così agire senza poter alterare il corso
della cura.

## D — Dependency Inversion

I livelli alti dipendono da **astrazioni**, non da implementazioni concrete:
`ContentRepository`, `GameRepository`, `RandomSource`, `ContentLoader`. Le classi
concrete sono scelte e collegate in un solo punto (`LoSpezialeApp`, `GameFactory`),
mentre il resto del codice resta indipendente dai dettagli — ed è per questo che il
dominio è testabile in isolamento e che fonti di dati e interfaccia sono
sostituibili.

## Clean Code

- **Nomi che rivelano l'intento**: `mostImbalanced()`, `isWithinTolerance()`,
  `concludeTreatment()`, `worsen()`/`recover()`.
- **Immutabilità** dove ha senso: `HumoralBalance` e `Coins` sono valori
  immutabili, condivisibili senza rischi.
- **Niente _primitive obsession_**: il denaro è un tipo (`Coins`) con i propri
  invarianti, non un `int` sparso ovunque.
- **Validazione agli ingressi** e _fail-fast_: i costruttori difendono i propri
  invarianti con `Preconditions`; i contenuti incoerenti sono respinti con
  `ContentException`.
- **Metodi brevi e a un solo livello di astrazione**, con _guard clause_ anziché
  annidamenti profondi.
- **Incapsulamento**: campi privati, raccolte restituite come viste non
  modificabili, nessuno stato condiviso esposto.

## Una nota sull'efficienza

Le ricerche sui contenuti passano per `Registry`/`Map`, quindi sono costanti nel
tempo; gli umori usano `EnumMap`; i valori immutabili evitano copie difensive
inutili. Le scelte privilegiano comunque la **chiarezza**: il gioco non ha
esigenze di prestazioni tali da giustificare complicazioni.
