package ru.mail.polis.Egorov;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.jetbrains.annotations.NotNull;
import ru.mail.polis.KVService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class MyService implements KVService {
    private static final String PREFIX = "id=";
    private static final String STATUS_RESPONSE = "ONLINE";
    @NotNull
    private final HttpServer server;

    @NotNull
    private static String extractId(@NotNull final String query) {
        if(!query.startsWith(PREFIX)) {
            throw new IllegalArgumentException("Error");
        }
        return query.substring(PREFIX.length());
    }

    public MyService(int port, @NotNull final MyDAO dao) throws IOException{
        this.server = HttpServer.create(new InetSocketAddress(port),0);
        this.server.setExecutor(Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors()
        ));

        this.server.createContext("/v0/status", http -> {
            http.sendResponseHeaders(200, STATUS_RESPONSE.length());
            http.getResponseBody().write(STATUS_RESPONSE.getBytes());
            http.close();
        });

        this.server.createContext("/v0/entity", (HttpExchange http) -> {
            final String id;

            try {
                id = extractId(http.getRequestURI().getQuery());
            } catch (IllegalArgumentException e) {
                http.sendResponseHeaders(405, 0);
                http.close();
                return;
            }

            if (id.length() == 0) {
                http.sendResponseHeaders(400, 0);
                http.close();
                return;
            }

            switch (http.getRequestMethod()) {
                case "GET":

                    if (dao.isDataExist(id)) {
                        http.sendResponseHeaders(200, 0);
                        http.getResponseBody().write(dao.get(id));
                    } else {
                        http.sendResponseHeaders(404, 0);
                    }

                    break;

                case "DELETE":
                    dao.delete(id);
                    http.sendResponseHeaders(202, 0);
                    break;

                case "PUT":
                    try (ByteArrayOutputStream out = new ByteArrayOutputStream();
                         InputStream in = http.getRequestBody()) {
                        byte[] buffer = new byte[1024];
                        while (true) {
                            int readBytes = in.read(buffer);
                            if (readBytes < 0) {
                                break;
                            }
                            out.write(buffer, 0, readBytes);
                        }

                        dao.upsert(id, out.toByteArray());
                        http.sendResponseHeaders(201, 0);
                    } catch (IOException e) {
                        http.sendResponseHeaders(500, 0);
                    }

                    break;

                default:
                    http.sendResponseHeaders(405, 0);
            }
            http.close();
        });

    }

    @Override
    public void start(){
        this.server.start();
    }
    @Override
    public void stop() {
        this.server.stop(0);
    }
}