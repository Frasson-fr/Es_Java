public class RichiestaHttp {
    private String ip;
    private String path;
    private int statusCode;
    private long tempoRispostaMs;
    private long timestamp;

    public RichiestaHttp(String ip, String path, int statusCode, long tempoRispostaMs, long timestamp) {
        this.ip = ip;
        this.path = path;
        this.statusCode = statusCode;
        this.tempoRispostaMs = tempoRispostaMs;
        this.timestamp = timestamp;
    }

    public String getIp() { return ip; }
    public String getPath() { return path; }
    public int getStatusCode() { return statusCode; }
    public long getTempoRispostaMs() { return tempoRispostaMs; }
    public long getTimestamp() { return timestamp; }

    public String toString() {
        return String.format("[%tT] IP: %-15s | Status: %d | Time: %4dms | Path: %s",
                timestamp, ip, statusCode, tempoRispostaMs, path);
    }
}