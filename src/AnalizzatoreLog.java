import java.util.*;

public class AnalizzatoreLog {

    //Storico completo: ArrayList garantisce inserimenti veloci in coda e accesso posizionale
    private List<RichiestaHttp> storicoCompleto = new ArrayList<>();

    //Sliding Window (ultime 10): LinkedList usata come coda
    private LinkedList<RichiestaHttp> finestraScorrevole = new LinkedList<>();

    //IP Sospetti (errori 4xx e 5xx): HashSet evita duplicati e fa controlli in O(1)
    private Set<String> ipSospetti = new HashSet<>();

    //Tempi di risposta univoci ordinati: TreeSet mantiene l'ordinamento naturale in O(log N)
    private NavigableSet<Long> tempiRispostaUnivoci = new TreeSet<>();

    /**
     * Processa ogni nuova richiesta aggiornando tutte e 4 le strutture dati.
     */
    public void aggiungiRichiesta(RichiestaHttp richiesta) {
        //Accoda allo storico completo
        storicoCompleto.add(richiesta);

        //Gestione della finestra scorrevole (max 10 elementi)
        finestraScorrevole.addLast(richiesta);
        if (finestraScorrevole.size() > 10) {
            finestraScorrevole.removeFirst(); //Rimozione O(1) in testa
        }

        //Tracciamento IP sospetti (errori 4xx e 5xx)
        if (richiesta.getStatusCode() >= 400) {
            ipSospetti.add(richiesta.getIp());
        }

        //Tempi di risposta unici ordinati
        tempiRispostaUnivoci.add(richiesta.getTempoRispostaMs());
    }

    /**
     * Restituisce le ultime N richieste che hanno restituito uno status >= 500.
     * Itera all'indietro sullo storico senza allocare nuove sottoliste.
     */
    public List<RichiestaHttp> getUltimeNErroriServer(int n) {
        List<RichiestaHttp> risultato = new ArrayList<>();

        //Scorriamo l'ArrayList partendo dalla fine per trovare i più recenti
        for (int i = storicoCompleto.size() - 1; i >= 0 && risultato.size() < n; i--) {
            RichiestaHttp r = storicoCompleto.get(i);
            if (r.getStatusCode() >= 500) {
                risultato.add(r);
            }
        }
        return risultato;
    }

    /**
     * Calcola un percentile approssimato sfruttando i metodi di navigazione di TreeSet.
     */
    public Long calcolaPercentile90() {
        if (tempiRispostaUnivoci.isEmpty()) return 0L;

        //Calcoliamo la posizione dell'elemento al 90° percentile
        int totaleElementi = tempiRispostaUnivoci.size();
        int indicePercentile = (int) Math.ceil(totaleElementi * 0.90) - 1;

        Iterator<Long> it = tempiRispostaUnivoci.iterator();
        Long tempoPercentile = 0L;
        for (int i = 0; i <= indicePercentile && it.hasNext(); i++) {
            tempoPercentile = it.next();
        }

        return tempoPercentile;
    }

    /**
     * Genera un report incrociando i dati presenti nelle 4 strutture.
     */
    public void stampaReport() {
        System.out.println("==================================================");
        System.out.println("            REPORT ANALISI TELEMETRIA             ");
        System.out.println("==================================================");

        System.out.println("1. Richieste totali elaborate: " + storicoCompleto.size());
        System.out.println("2. Dimensioni finestra scorrevole corrente: " + finestraScorrevole.size());

        //Conteggio IP sospetti presenti nella finestra scorrevole
        long ipSospettiInFinestra = finestraScorrevole.stream()
                .map(RichiestaHttp::getIp)
                .filter(ipSospetti::contains) // Ricerca O(1) grazie ad HashSet
                .distinct()
                .count();

        System.out.println("3. IP Sospetti identificati in totale (4xx/5xx): " + ipSospetti.size());
        System.out.println("4. IP Sospetti attualmente presenti negli ultimi 10 eventi: " + ipSospettiInFinestra);

        Long p90 = calcolaPercentile90();
        System.out.println("5. Tempo di risposta al 90° percentile (su tempi univoci): " + p90 + " ms");

        //Esempio d'uso dei metodi specifici di TreeSet
        System.out.println("\n--- Dettaglio Tempi di Risposta (NavigableSet / TreeSet) ---");
        System.out.println("Tempi univoci superiori o uguali al P90 (tailSet): " + tempiRispostaUnivoci.tailSet(p90));
        System.out.println("Tempo univoco immediatamente inferiore o uguale a 150ms (floor): " + tempiRispostaUnivoci.floor(150L));
        System.out.println("Tempo univoco immediatamente superiore a 150ms (higher): " + tempiRispostaUnivoci.higher(150L));
    }

    public List<RichiestaHttp> getFinestraScorrevole() {
        return finestraScorrevole;
    }
}