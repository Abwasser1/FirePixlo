package me.abwasser.FirePixlo.webservices;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class TP_Map extends Thread {

	HttpWebServer hws;
	public int port;

	public TP_Map() {
		this.port = 69420;
	}

	@Override
	public void run() {
		hws = new HttpWebServer(port);
		init();
		return;
	}

	public void init() {
		try {
			hws.StartServer();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		return;

	}

	public void a(String context, HttpHandler hh) {
		hws.CreateContext(context, hh);
	}

	public void a(String context, HttpHandlerC hh) {
		a(context, hh.getHandler());
	}

	public void a(String context, File file) {
		String[] extention = file.getName().replace(".", "#DOT#").split("#DOT#");
		String ext = extention[extention.length - 1];
		a(context, new HttpHandlerC(file, ext));
	}

	@SuppressWarnings("deprecation")
	public void StopWebserver() {
		try {
			hws.StopServer();
			stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}

	public static void sendDataUnfiltered(HttpExchange t, String response) {
		byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
		sendDataUnfiltered(t, bytes);

	}

	public static void sendDataUnfiltered(HttpExchange t, byte[] bytes) {
		System.out.println("Hompage.sendData> " + bytes.length);
		OutputStream os = t.getResponseBody();
		t.getResponseHeaders().add("Strict-Transport-Security", "max-age=63072000; includeSubDomains;"/* preload" */);
		try {
			t.sendResponseHeaders(200, bytes.length);
			os.write(bytes, 0, bytes.length);
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	static class RedirectHandler implements HttpHandler {
		public String url;

		public RedirectHandler(String url) {
			this.url = url;
		}

		@Override
		public void handle(HttpExchange t) throws IOException {
			long start = System.currentTimeMillis();
			Headers h = t.getResponseHeaders();
			h.add("Content-Type", "text/html");

			String response1 = "<!DOCTYPE html>\r\n" + "<html lang=\"de\">\r\n" + "<head>\r\n"
					+ "    <meta charset=\"UTF-8\">\r\n"
					+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n"
					+ "    <meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">\r\n"
					+ "    <title>Document</title>\r\n" + "</head>\r\n"
					+ "<body style=\"background-color: rgb(56, 56, 56);\">\r\n" + "    <script>\r\n"
					+ "\r\nif (window.location.protocol != \"https:\") {\r\n"
					+ "		          window.location.protocol = \"https:\";\r\n" + "		        }\r\n"
					+ "        window.location.replace(\"%Replacement-Goes-Here%\");\r\n" + "\r\n" + "    </script>\r\n"
					+ "</body>\r\n" + "</html>";

			String response = response1.replace("%Replacement-Goes-Here%", url);
			sendDataUnfiltered(t, response);
			System.out.println("[Perfmon] RedirectHandler> " + (System.currentTimeMillis() - start) + "ms");
		}
	}
}