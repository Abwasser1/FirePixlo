package me.abwasser.FirePixlo.webservices;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class Homepage extends Thread {

	HttpWebServer hws;
	public int port;

	public Homepage(int port) {
		this.port = port;
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

	public void b(String context, HttpHandlerC hh) {
		hws.CreateContext(context, hh.getUnfilteredHandler());
	}

	public void b(String context, HttpHandler hh) {
		hws.CreateContext(context, hh);
	}

	public void b(String context, File file) {
		String[] extention = file.getName().replace(".", "#DOT#").split("#DOT#");
		String ext = extention[extention.length - 1];
		b(context, new HttpHandlerC(file, ext));
	}

	public void a(String context, HttpHandlerC hh) {
		a(context, hh.getHandler());
	}

	public void a(String context, File file) {
		String[] extention = file.getName().replace(".", "#DOT#").split("#DOT#");
		String ext = extention[extention.length - 1];
		a(context, new HttpHandlerC(file, ext));
	}

	public void c(String context, HttpHandler hh) {
		hws.CreateContext(context, hh);
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
				t.getResponseHeaders().add("Strict-Transport-Security", "max-age=63072000; includeSubDomains;"/* preload"*/);
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
}