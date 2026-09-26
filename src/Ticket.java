public class Ticket implements Comparable<Ticket> {
    private String id;
    private String descrizione;
    private Livello livello;
    private long timestampArrivo;

    public Ticket(String id, String descrizione, Livello livello, long timestampArrivo) {
        this.id = id;
        this.descrizione = descrizione;
        this.livello = livello;
        this.timestampArrivo = timestampArrivo;
    }

    public String getId() { return id; }
    public String getDescrizione() { return descrizione; }
    public Livello getLivello() { return livello; }
    public long getTimestampArrivo() { return timestampArrivo; }

    @Override
    public int compareTo(Ticket altro) {
        //Ordina per livello di priorità
        int compLivello = Integer.compare(this.livello.getOrdine(), altro.livello.getOrdine());
        if (compLivello != 0) {
            return compLivello;
        }
        //A parità di livello, vince il timestamp più vecchio (FIFO)
        return Long.compare(this.timestampArrivo, altro.timestampArrivo);
    }

    @Override
    public String toString() {
        return String.format("[%s] %-7s | TS: %d | %s", id, livello, timestampArrivo, descrizione);
    }
}