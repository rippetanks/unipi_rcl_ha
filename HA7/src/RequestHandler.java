import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class RequestHandler implements Runnable {

    private final UUID uuid;
    private final Socket socket;

    public RequestHandler(Socket socket) {
        this.uuid = UUID.randomUUID();
        this.socket = socket;
    }

    public UUID getUuid() {
        return uuid;
    }

    @Override
    public void run() {
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            List<String> headers = new ArrayList<>();
            String line;
            while((line = reader.readLine()) != null && !line.isEmpty()) {
                headers.add(line);
            }
            if(headers.isEmpty()) {
                HttpErrorHandler.HTTP_400(socket.getOutputStream(), "not an HTTP request");
                throw new HttpException();
            }
            String path = getPath(headers);
            List<String> mimes = getAcceptMimes(headers);
            System.out.printf("request %s path: %s\n", uuid, path);
            System.out.printf("request %s mimes: %s\n", uuid, mimes);
            sendFile(path, mimes);
        } catch(IOException | HttpException e) {
            System.err.printf("request %s exception: %s\n", uuid, e.getMessage());
        }
    }

    /**
     * Try to get the file path from the request headers.
     * It fails if the request is not a valid HTTP request or the HTTP method is not GET.
     *
     * @param headers   request headers
     * @return          the path of the requested file
     * @throws IOException
     * @throws HttpException
     */
    private String getPath(List<String> headers) throws IOException, HttpException {
        String[] tokens = headers.get(0).split(" ");
        if(tokens.length != 3) {
            HttpErrorHandler.HTTP_400(socket.getOutputStream(), "not an http request");
            throw new HttpException();
        }
        if(!tokens[2].equals("HTTP/1.1")) {
            HttpErrorHandler.HTTP_505(socket.getOutputStream(), tokens[2]);
            throw new HttpException();
        }
        if(!tokens[0].equals("GET")) {
            HttpErrorHandler.HTTP_405(socket.getOutputStream(), "required GET");
            throw new HttpException();
        }
        return tokens[1];
    }

    /**
     * Get the list of mime types supported by the client.
     *
     * @param headers   request headers
     * @return          a list of mime types
     * @throws IOException
     * @throws HttpException
     */
    private List<String> getAcceptMimes(List<String> headers) throws IOException, HttpException {
        Optional<String> opt = headers.stream().filter(h -> h.startsWith("Accept: ")).findAny();
        if(opt.isPresent()) {
            String mimes = opt.get();
            String[] arr = mimes.split(": ");
            if(arr.length != 2) {
                HttpErrorHandler.HTTP_400(socket.getOutputStream(), "wrong accept header");
                throw new HttpException();
            }
            return Arrays.stream(arr[1].split(","))
                    .map(m -> m.split(";")[0])
                    .collect(Collectors.toList());
        } else {
            HttpErrorHandler.HTTP_400(socket.getOutputStream(), "accept header missing");
            throw new HttpException();
        }
    }

    /**
     * Attempts to send the requested file.
     * Fails if the requested file does not exist or the file type is not supported by the client.
     *
     * @param path      file path
     * @param mimes     a list of mime types
     * @throws IOException
     * @throws HttpException
     */
    private void sendFile(String path, List<String> mimes) throws IOException, HttpException {
        Path file = Paths.get("./content/" + path);
        if(Files.notExists(file)) {
            HttpErrorHandler.HTTP_404(socket.getOutputStream(), path);
            throw new HttpException();
        }
        String mime = checkMimeType(file, mimes);
        try(DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {
            byte[] bytes = Files.readAllBytes(file);
            out.writeBytes("HTTP/1.1 200 OK\r\n");
            out.writeBytes("Content-Length: "+bytes.length+"\r\n");
            out.writeBytes("Content-Type: "+mime+"\r\n");
            out.writeBytes("\r\n");
            out.write(bytes);
            out.flush();
        }
    }

    /**
     * Check if the file respects the mime types accepted by the client.
     *
     * @param file      file
     * @param mimes     a list of mime types
     * @return          the mime type for the requested file
     * @throws IOException
     * @throws HttpException
     */
    private String checkMimeType(Path file, List<String> mimes) throws IOException, HttpException {
        boolean acceptAll = mimes.stream().anyMatch(m -> m.equals("*/*"));
        String path = file.toFile().getName();
        if(path.endsWith(".html")
                && mimes.stream().anyMatch(m -> acceptAll || m.equals("text/html"))) {
            return "text/html";
        }
        if((path.endsWith(".jpg") || path.endsWith(".jpeg"))
                && mimes.stream().anyMatch(m -> acceptAll || m.equals("image/jpeg") || m.equals("image/*"))) {
            return "image/jpeg";
        }
        if(path.endsWith(".png")
                && mimes.stream().anyMatch(m -> acceptAll || m.equals("image/png"))) {
            return "image/png";
        }
        HttpErrorHandler.HTTP_415(socket.getOutputStream(), path.toString());
        throw new HttpException();
    }

}
