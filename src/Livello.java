public enum Livello {
    CRITICO(1, 15),
    ALTO(2, 30),
    MEDIO(3, 60),
    BASSO(4, 120);

    private final int ordine;
    private final int minutiLavorazione;

    Livello(int ordine, int minutiLavorazione) {
        this.ordine = ordine;
        this.minutiLavorazione = minutiLavorazione;
    }

    public int getOrdine() { return ordine; }
    public int getMinutiLavorazione() { return minutiLavorazione; }

    public static Livello parseTollerante(String valore) {
        if (valore == null) return null;
        return Livello.valueOf(valore.trim().toUpperCase());
    }
}