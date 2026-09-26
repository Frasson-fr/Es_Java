import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Main {
// Eserizio 1
    /*
    public static void main(String[] args) {

        // Simulazione dell'arrivo di 8 pacchetti di telemetria grezzi
        List<String> pacchettiGrezzi = Arrays.asList(
                "temp=23.5;umid=61;ts=1732000000;batt_low=false",  // 1. Pacchetto valido completo
                "temp=xx.xx;umid=50;ts=1732000001;batt_low=true",  // 2. Temp corrotta
                "temp=18.2;umid=150;ts=1732000002;batt_low=false", // Umidità fuori range (>100) da scartare
                "temp=29.1;ts=1732000003;batt_low=false",          // Manca il campo umid
                "temp=21.0;umid=45;ts=1732000004",                //  Manca batt_low
                "formato_non_valido_senza_chiave_valore",         //  Pacchetto corrotto
                "temp=31.4;umid=80;ts=1732000005;batt_low=false",  // Pacchetto valido completo
                "temp=-5.5;umid=90;ts=1732000006;batt_low=true"    // Pacchetto valido completo
        );

        List<LetturaSensore> lettureValide = new ArrayList<>();
        int erroriParsing = 0;

        System.out.println("=============================");
        System.out.println("1. INIZIO PARSING TELEMETRIA");
        System.out.println("=============================");

        for (int i = 0; i < pacchettiGrezzi.size(); i++) {
            String raw = pacchettiGrezzi.get(i);
            System.out.println("Parsing pacchetto [" + (i + 1) + "]: \"" + raw + "\"");

            Optional<LetturaSensore> optLettura = LetturaSensore.parsePacchetto(raw);
            if (optLettura.isPresent()) {
                lettureValide.add(optLettura.get());
                System.out.println("  -> Risultato: " + optLettura.get());
            } else {
                erroriParsing++;
                System.out.println("  -> Risultato: Pacchetto scartato.");
            }
            System.out.println();
        }

        // Calcolo media delle temperature valide
        double sommaTemp = 0.0;
        int contatoreTempValide = 0;

        for (LetturaSensore l : lettureValide) {
            if (l.getTemperatura() != null) {
                sommaTemp += l.getTemperatura();
                contatoreTempValide++;
            }
        }

        double mediaTemp = contatoreTempValide > 0 ? (sommaTemp / contatoreTempValide) : 0.0;

        System.out.println("=============================");
        System.out.println(" 2. REPORT RIASSUNTIVO  ");
        System.out.println("=============================");
        System.out.println("Numero di letture valide registrate: " + lettureValide.size());
        System.out.println("Numero di pacchetti scartati/errori: " + erroriParsing);
        System.out.printf("Media delle temperature valide:       %.2f °C\n", mediaTemp);

        System.out.println("\n=========================================");
        System.out.println("3. DIMOSTRAZIONE TRABOCCHETTO AUTOBOXING");
        System.out.println("===========================================");
        TestAutoboxing.eseguiDimostrazione();

        System.out.println("\n==================================================");
        System.out.println("  4. BONUS: ORDINAMENTO TEMPERATURE DECRESCENTI   ");
        System.out.println("==================================================");
        ordinaEStampaBonus(lettureValide);
    }

    private static void ordinaEStampaBonus(List<LetturaSensore> letture) {
        letture.sort((l1, l2) -> {
            // Gestione dei null
            if (l1.getTemperatura() == null && l2.getTemperatura() == null) return 0;
            if (l1.getTemperatura() == null) return 1;
            if (l2.getTemperatura() == null) return -1;

            // Invertiamo l2 e l1 per ottenere un ordine decrescente
            return Double.compare(l2.getTemperatura(), l1.getTemperatura());
        });

        for (LetturaSensore l : letture) {
            System.out.println(l);
        }
    }*/

    //Eserizio 2
    /*
    public static void main(String[] args) {
        AnalizzatoreLog analizzatore = new AnalizzatoreLog();
        Random rand = new Random();

        String[] ips = {
                "192.168.1.10", "10.0.0.5", "172.16.0.1",
                "192.168.1.10", "192.168.1.99", "10.0.0.5"
        };

        String[] paths = {"/home", "/login", "/api/data", "/checkout", "/admin", "/images/logo.png"};
        int[] statusCodes = {200, 200, 200, 301, 404, 403, 500, 503};

        long baseTime = System.currentTimeMillis() - 60000;

        System.out.println("--- SIMULAZIONE ARRIVO 30 RICHIESTE HTTP ---");
        for (int i = 0; i < 30; i++) {
            String ip = ips[rand.nextInt(ips.length)];
            String path = paths[rand.nextInt(paths.length)];
            int status = statusCodes[rand.nextInt(statusCodes.length)];
            long tempoRisposta = 20 + rand.nextInt(480); // Da 20ms a 500ms
            long timestamp = baseTime + (i * 2000); // 2 secondi tra una richiesta e l'altra

            RichiestaHttp richiesta = new RichiestaHttp(ip, path, status, tempoRisposta, timestamp);
            analizzatore.aggiungiRichiesta(richiesta);
        }

        System.out.println("\n");
        analizzatore.stampaReport();

        System.out.println("\n--- ULTIME 3 RICHIESTE CON ERRORE SERVER (Status >= 500) ---");
        for (RichiestaHttp err : analizzatore.getUltimeNErroriServer(3)) {
            System.out.println(err);
        }
    } */

    //Eserizio 3

    public static void main(String[] args) {
        //Inserisci qui il nome o il percorso del tuo file CSV
        String percorsoFile = "ticket.csv";

        PriorityQueue<Ticket> codaLavorazione = new PriorityQueue<>();
        List<String> logErrori = new ArrayList<>();
        Map<Livello, Integer> conteggioLivelli = new EnumMap<>(Livello.class);

        for (Livello l : Livello.values()) {
            conteggioLivelli.put(l, 0);
        }

        System.out.println("--- 1. LETTURA DEL FILE CSV ---");

        Path path = Paths.get(percorsoFile);

        //Usiamo BufferedReader e try-with-resources per una lettura sicura ed efficiente
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String riga;
            int numeroRiga = 0;

            while ((riga = reader.readLine()) != null) {
                numeroRiga++;

                //Salta l'intestazione ("id,descrizione...") o le righe vuote
                if (numeroRiga == 1 && riga.toLowerCase().startsWith("id,")) continue;
                if (riga.trim().isEmpty()) continue;

                String[] campi = riga.split(",");

                if (campi.length < 4) {
                    logErrori.add("Riga " + numeroRiga + " scartata (campi insufficienti): \"" + riga + "\"");
                    continue;
                }

                try {
                    String id = campi[0].trim();
                    String descrizione = campi[1].trim();
                    Livello livello = Livello.parseTollerante(campi[2]);
                    long timestamp = Long.parseLong(campi[3].trim());

                    if (livello == null) {
                        throw new IllegalArgumentException("Livello non valido: " + campi[2]);
                    }

                    Ticket ticket = new Ticket(id, descrizione, livello, timestamp);
                    codaLavorazione.add(ticket);

                    //Allarme bonus: avviso in console se ci sono più di 2 critici in attesa
                    long criticiInAttesa = codaLavorazione.stream()
                            .filter(t -> t.getLivello() == Livello.CRITICO)
                            .count();

                    if (criticiInAttesa > 2) {
                        System.out.println("⚠️ ALLARME: Rilevati " + criticiInAttesa + " ticket CRITICI in coda!");
                    }

                } catch (Exception e) {
                    logErrori.add("Riga " + numeroRiga + " scartata (errore formato): \"" + riga + "\" (" + e.getMessage() + ")");
                }
            }

        } catch (NoSuchFileException | FileNotFoundException e) {
            System.err.println("ERRORE: Impossibile trovare il file '" + percorsoFile + "'. Verifica che il percorso sia corretto.");
            return;
        } catch (IOException e) {
            System.err.println("ERRORE I/O durante la lettura del file: " + e.getMessage());
            return;
        }

        System.out.println("\n--- 2. ELABORAZIONE E STAMPA DEL REPORT ---");
        System.out.println("=================================================================");
        System.out.printf("%-6s | %-8s | %-12s | %-35s\n", "ID", "LIVELLO", "COMPLETAMENTO", "DESCRIZIONE");
        System.out.println("-----------------------------------------------------------------");

        int tempoCumulativoMinuti = 0;
        int consecutiviAltiCritici = 0;
        int totaleElaborati = 0;

        //Estrazione ordinata in base alla priorità
        while (!codaLavorazione.isEmpty()) {
            Ticket t = codaLavorazione.poll();

            //Gestione pausa obbligatoria di 10 minuti dopo 5 ticket critii o alti
            if (t.getLivello() == Livello.CRITICO || t.getLivello() == Livello.ALTO) {
                consecutiviAltiCritici++;
                if (consecutiviAltiCritici > 5) {
                    tempoCumulativoMinuti += 10;
                    System.out.println("--- [PAUSA TECNICA OBBLIGATORIA (+10 min)] ---");
                    consecutiviAltiCritici = 1;
                }
            } else {
                consecutiviAltiCritici = 0;
            }

            tempoCumulativoMinuti += t.getLivello().getMinutiLavorazione();
            totaleElaborati++;
            conteggioLivelli.put(t.getLivello(), conteggioLivelli.get(t.getLivello()) + 1);

            String orarioFormattato = String.format("%dh %02dmin", tempoCumulativoMinuti / 60, tempoCumulativoMinuti % 60);

            System.out.printf("%-6s | %-8s | %-12s | %-35s\n",
                    t.getId(), t.getLivello(), orarioFormattato, t.getDescrizione());
        }

        // RIEPILOGO FINALE
        System.out.println("\n=================================================================");
        System.out.println("                       RIEPILOGO FINALE                          ");
        System.out.println("=================================================================");
        System.out.println("Totale ticket elaborati: " + totaleElaborati);
        for (Livello l : Livello.values()) {
            System.out.println(" - Ticket " + String.format("%-7s", l) + ": " + conteggioLivelli.get(l));
        }
        System.out.println("Tempo totale stimato: " + (tempoCumulativoMinuti / 60) + "h " + (tempoCumulativoMinuti % 60) + "m");

        if (!logErrori.isEmpty()) {
            System.out.println("\n--- ANOMALIE SEGNALATE / RIGHE SCARTATE ---");
            for (String err : logErrori) {
                System.out.println("Errore " + err);
            }
        }
    }
}