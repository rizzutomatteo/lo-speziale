# Estendibilità

Il progetto è pensato perché lo si possa ampliare con il minimo intervento. Di
seguito i casi più comuni, dal più semplice al più strutturale.

## Aggiungere contenuti — nessuna modifica al codice

Nuovi sintomi, malanni, ingredienti, rimedi, ricette o villaggi si aggiungono
**scrivendo nei file JSON** sotto `data/`. Poiché i contenuti sono caricati e
risolti per id, basta rispettare i riferimenti esistenti: il gioco li raccoglie
da solo all'avvio.

Esempio: per introdurre un nuovo male è sufficiente aggiungere una voce a
`ailments.json` con il suo squilibrio e i suoi sintomi, ed elencarlo tra i
`localAilments` di qualche villaggio.

## Aggiungere un nuovo tipo di effetto dei rimedi

Gli effetti sono dietro l'interfaccia `RemedyEffect`. Un nuovo effetto richiede:

1. una nuova classe che implementa `RemedyEffect`;
2. un nuovo `case` in `RemedyEffectAdapter` per leggerlo/scriverlo dal JSON.

Nessun'altra parte del gioco va toccata: rimedi e motore lavorano sull'interfaccia
(principio _Open/Closed_).

## Aggiungere una nuova mossa di cura

Le mosse sono dietro l'interfaccia `TreatmentAction` (schema Command). Una nuova
mossa è una nuova classe che la implementa; basta poi aggiungere il pulsante
corrispondente nel `TreatmentController`.

## Sostituire la provenienza dei contenuti

Il gioco dipende da `ContentRepository`, non dai file. Per leggere i contenuti da
un'altra fonte — un database, un servizio remoto — si fornisce una nuova
implementazione di `ContentLoader`/`ContentRepository`, senza cambiare il dominio.

## Sostituire il modo di salvare

Allo stesso modo, il salvataggio dipende da `GameRepository`. Un salvataggio su
database o su _cloud_ è una nuova implementazione di questa interfaccia.

## Aggiungere una schermata

Una nuova schermata è un file FXML, un controller e un metodo di navigazione in
`GameUi`. Le schermate esistenti restano invariate.

## Verso più dispositivi

La specifica chiede che il progetto sia predisposto all'uso su **più dispositivi**
(desktop, mobile, web). Qui questo è possibile perché i livelli `model`, `game`,
`content` e `persistence` **non dipendono da JavaFX**: una futura interfaccia
mobile o web potrebbe riusarli integralmente, fornendo soltanto una nuova vista al
posto del package `gui`. La sorgente di casualità è essa stessa astratta
(`RandomSource`), così anche la logica resta riproducibile e indipendente
dall'ambiente.
