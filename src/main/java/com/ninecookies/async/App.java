package com.ninecookies.async;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * @author alex.panchenko
 * @since 2017-03-01
 */
public class App {
    public static class AsyncServlet extends HttpServlet {

        private final ScheduledExecutorService service = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

        @Override
        protected void doGet(HttpServletRequest request, final HttpServletResponse resp) throws ServletException, IOException {
            final AsyncContext ctxt = request.startAsync();
            service.schedule(() -> ctxt.start(() -> {
                try {
                    final ServletResponse response = ctxt.getResponse();
                    response.getWriter().write("{\"status\":\"OK\"}\n");
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    ctxt.complete();
                }
            }), 5, TimeUnit.SECONDS);
        }
    }

    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);

        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/");
        ServletHolder asyncHolder = context.addServlet(AsyncServlet.class, "/async");
        asyncHolder.setAsyncSupported(true);
        server.setHandler(context);

        server.start();
        server.join();
    }
}
