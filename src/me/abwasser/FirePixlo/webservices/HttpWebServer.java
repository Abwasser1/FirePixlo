package me.abwasser.FirePixlo.webservices;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class HttpWebServer {

	static HttpServer server;
	int port;
	boolean run = false;
	public ArrayList<String> contexts = new ArrayList<>();

	public HttpWebServer(int Port) {
		port = Port;
		try {
			server = HttpServer.create(new InetSocketAddress(Port), 0);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("ERROR HTMLWEBSERVER CONSTRUKTOR");
		}
	}

	public void CreateContext(String Context, HttpHandler httpHandler) {
		contexts.add(Context);
		server.createContext(Context, httpHandler);
	}

	public void StartServer() throws Exception {
		server.setExecutor(null);
		server.start();
		run = true;
		/*
		 * String s1 = ""; for (String s : contexts) s1 += "\"" + s + "\",\r\n";
		 * System.out.println(s1);
		 */
		return;
	}

	public boolean isRunning() {
		return run;
	}

	public void StopServer() throws Exception {
		try {
			server.stop(1);
			run = false;
		} catch (Exception e) {
			System.out.println("[[[HYPO-ERROR]]] SYSTEM.EXIT (No SUICIDE!)");
		}
	}

}

/*
 * static class MyHandler implements HttpHandler {
 * 
 * @Override public void handle(HttpExchange t) throws IOException { String
 * response =
 * "<html> <head> <title>Unix-Webtest</title> </head><body><h1><span>Unix-WebTest</span></h1><p>@Coolness <br />Abwasser</p><p>@Lappen Lutz</p><iframe width=\"560\" height=\"315\" src=\"https://www.youtube-nocookie.com/embed/4kJpEJPI2Bs?rel=0&amp;showinfo=1\" frameborder=\"0\" allow=\"autoplay; encrypted-media\" allowfullscreen></iframe><p>FONTS: </p><p><b>BOLD</b></p><p><big>BIG</big></p><p><i>ITALIC</i></p><p><small>SMALL</small></p><p><strong>STRONG</strong></p><p><sub>SUB TEXT</sub></p><p><sup>SUP TEXT</sup></p><p><ins>INSERTED TEXT</ins></p><p><del>Deleted TEXT</del></p><h1>Title1</h1><h2>Title2</h2><h3>Title3</h3><h4>Title4</h4><h5>Title5</h5><h6>Title6</h6><hr /><p>Nice Line :D</p><!--Kommentar --><h1><span>SPAN SPAN</span></h1><div class=\"section\"><h1><span>Section</span></h1> </div> </body> </html>"
 * ; t.sendResponseHeaders(200, response.length()); OutputStream os =
 * t.getResponseBody(); os.write(response.getBytes()); os.close(); } }
 * 
 * 
 * 
 * static class VertretungHHandler implements HttpHandler {
 * 
 * @Override public void handle(HttpExchange t) throws IOException { Headers h =
 * t.getResponseHeaders(); h.add("Content-Type", "application/pdf");
 * 
 * File file = new File("V/h.pdf"); byte[] bytearray = new byte[(int)
 * file.length()]; FileInputStream fis = new FileInputStream(file);
 * BufferedInputStream bis = new BufferedInputStream(fis); bis.read(bytearray,
 * 0, bytearray.length);
 * 
 * t.sendResponseHeaders(200, file.length()); OutputStream os =
 * t.getResponseBody(); os.write(bytearray, 0, bytearray.length); os.close();
 * 
 * } }
 */