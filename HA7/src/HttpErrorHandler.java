import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

class HttpErrorHandler {

    static void HTTP_400(OutputStream stream, String msg) throws IOException {
        send(stream, 400, "Bad Request", msg);
    }

    static void HTTP_404(OutputStream stream, String msg) throws IOException {
        send(stream, 404, "Not Found", msg);
    }

    static void HTTP_405(OutputStream stream, String msg) throws IOException {
        send(stream, 405, "Method Not Allowed", msg);
    }

    static void HTTP_415(OutputStream stream, String msg) throws IOException {
        send(stream, 415, "Unsupported Media Type", msg);
    }

    static void HTTP_505(OutputStream stream, String msg) throws IOException {
        send(stream, 505, "HTTP Version Not Supported", msg);
    }

    private static void send(OutputStream stream, int code, String reason, String msg) throws IOException {
        try(DataOutputStream out = new DataOutputStream(stream)) {
            out.writeBytes(String.format("HTTP/1.1 %d %s\r\n", code, reason));
            out.writeBytes("Content-Type: text/html\r\n");
            String html = String.format("<html><head></head><body>%s: %s</body></html>", reason, msg);
            out.writeBytes("Content-Length: " + html.length() + "\r\n");
            out.writeBytes("\r\n");
            out.writeBytes(html);
            out.flush();
        }
    }

}
