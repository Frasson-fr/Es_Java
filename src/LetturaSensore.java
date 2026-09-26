import java.util.Optional;

public class LetturaSensore {

    private Double temperatura;
    private Integer umiditaPercentuale;
    private Long timestampUnix;
    private Boolean batteriaScarica;

    public LetturaSensore(Double temperatura, Integer umiditaPercentuale, Long timestampUnix, Boolean batteriaScarica) {
        this.temperatura = temperatura;
        this.umiditaPercentuale = umiditaPercentuale;
        this.timestampUnix = timestampUnix;
        this.batteriaScarica = batteriaScarica;
    }

    /**
     * Esegue il parsing di una stringa grezza di telemetria.
     * Formato atteso: "temp=23.5;umid=61;ts=1732000000;batt_low=false"
     */
    public static Optional<LetturaSensore> parsePacchetto(String raw) {
        if (raw == null || raw.trim().isEmpty()) {
            return Optional.empty();
        }

        Double temp = null;
        Integer umid = null;
        Long ts = null;
        Boolean battLow = null;

        String[] coppie = raw.split(";");
        for (String coppia : coppie) {
            String[] kv = coppia.split("=");
            if (kv.length != 2) {
                continue;
            }

            String chiave = kv[0].trim();
            String valore = kv[1].trim();

             /* In questo contesto utilizzo Double.valueOf / Long.valueOf / Boolean.valueOf
                per ottenere direttamente gli oggetti wrapper evitando conversioni o autoboxing inutili.
             */
            try {
                switch (chiave) {
                    case "temp":
                        temp = Double.valueOf(valore);
                        break;
                    case "umid":
                        int valUmid = Integer.parseInt(valore);
                        // Controllo del range consentito (0 - 100)
                        if (valUmid < 0 || valUmid > 100) {
                            throw new LetturaInvalidaException("Umidità fuori range [0-100]: " + valUmid);
                        }
                        umid = valUmid; // Autoboxing a Integer
                        break;
                    case "ts":
                        ts = Long.valueOf(valore);
                        break;
                    case "batt_low":
                        battLow = Boolean.valueOf(valore);
                        break;
                }
            } catch (NumberFormatException e) {
                // Logghiamo l'errore senza interrompere il parsing degli altri campi del pacchetto
                System.err.println("  [LOG ERROR] Errore di formato nel campo '" + chiave + "' con valore '" + valore + "'");
            } catch (LetturaInvalidaException e) {
                // Se un campo è fuori range logghiamo l'errore e scartiamo l'intero pacchetto
                System.err.println("  [LOG ERROR] " + e.getMessage());
                return Optional.empty();
            }
        }
        return Optional.of(new LetturaSensore(temp, umid, ts, battLow));
    }

    public Double getTemperatura() {
        return temperatura;
    }

    public Integer getUmiditaPercentuale() {
        return umiditaPercentuale;
    }

    public Long getTimestampUnix() {
        return timestampUnix;
    }

    public Boolean getBatteriaScarica() {
        return batteriaScarica;
    }

    public String toString() {
        return "LetturaSensore{" +
                "temp=" + temperatura +
                ", umid=" + umiditaPercentuale +
                ", ts=" + timestampUnix +
                ", battLow=" + batteriaScarica +
                '}';
    }
}