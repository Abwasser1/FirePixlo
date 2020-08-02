package me.abwasser.FirePixlo.webservices;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class HttpHandlerC {

	String mimeType;
	File file;

	public HttpHandlerC(String mimeType, File file) {
		this.mimeType = mimeType;
		this.file = file;
	}

	public HttpHandlerC(File file, String ext) {
		this.mimeType = getMime(ext);
		this.file = file;
	}

	public HttpHandlerC(String file, String ext) {
		this.mimeType = getMime(ext);
		this.file = new File(file);
	}

	public String getMime(String ext) {
		String mime = "text/plain";
		switch (ext) {
		case "apk":
			mime = "application/vnd.android.package-archive";
			break;
		case "html":
			mime = "text/html";
			break;
		case "css":
			mime = "text/css";
			break;
		case "ico":
			mime = "image/x-icon";
			break;
		case "js":
			mime = "text/javascript";
			break;
		case "jpg":
			mime = "image/jpeg ";
			break;
		case "png":
			mime = "image/png";
			break;
		case "gif":
			mime = "image/gif";
			break;
		case "json":
			mime = "application/json";
			break;
		case "docx":
			mime = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
			break;
		case "pptx":
			mime = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
			break;
		case "xlsx":
			mime = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
			break;
		case "pdf":
			mime = "application/pdf";
			break;
		case "txt":
			mime = "text/plain";
			break;
		case "webmanifest":
			mime = "application/manifest+json";
			break;
		case "woff":
			mime = "font/woff";
			break;
		case "woff2":
			mime = "font/woff2";
			break;
		case "xml":
			mime = "text/xml";
			break;
		case "svg":
			mime = "image/svg+xml";
			break;
		case "zip":
			mime = "application/zip";
			break;
		case "wav":
			mime = "audio/x-wav";
			break;
		case "mp3":
			mime = "audio/mpeg3";
			break;
		case "mp4":
			mime = "video/mp4";
			break;
		case "jar":
			mime = "application/java-archive";
			break;
		default:
			mime = "text/plain";
			break;
		}
		return mime;
	}

	public Handler getHandler() {
		return new Handler();

	}

	public class Handler implements HttpHandler {

		@Override
		@SuppressWarnings("resource")
		public void handle(HttpExchange t) throws IOException {

			Headers h = t.getResponseHeaders();
			h.add("Content-Type", mimeType);

			byte[] bytearray = new byte[(int) file.length()];
			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(fis);
			bis.read(bytearray, 0, bytearray.length);
			Homepage.sendDataUnfiltered(t, bytearray);

		}

	}

	public VertretungsHandler getVertretungsHandler() {
		return new VertretungsHandler();

	}

	public class VertretungsHandler implements HttpHandler {

		@Override
		@SuppressWarnings("resource")
		public void handle(HttpExchange t) throws IOException {

			Headers h = t.getResponseHeaders();
			h.add("Content-Type", mimeType);
			h.add("X-Frame-Options", "sameorigin");
			// System.out.println("Responding to"+t.getRemoteAddress().toString());

			byte[] bytearray = new byte[(int) file.length()];
			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(fis);
			bis.read(bytearray, 0, bytearray.length);
			Homepage.sendDataUnfiltered(t, bytearray);

		}

	}

	public HttpHandler getUnfilteredHandler() {
		return new UnfilteredHandler();
	}

	public class UnfilteredHandler implements HttpHandler {

		@Override
		@SuppressWarnings("resource")
		public void handle(HttpExchange t) throws IOException {

			Headers h = t.getResponseHeaders();
			h.add("Content-Type", mimeType);
			h.add("X-Frame-Options", "sameorigin");
			// System.out.println("Responding to"+t.getRemoteAddress().toString());

			byte[] bytearray = new byte[(int) file.length()];
			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(fis);
			bis.read(bytearray, 0, bytearray.length);
			Homepage.sendDataUnfiltered(t, bytearray);

		}

	}

}
/*
 * static class VertretungHHandler implements HttpHandler {
 * 
 * @Override public void handle(HttpExchange t) throws IOException { Headers h =
 * t.getResponseHeaders(); h.add("Content-Type", "application/pdf"); File file =
 * new File("V/h.pdf"); byte[] bytearray = new byte[(int)file.length()];
 * FileInputStream fis = new FileInputStream(file); BufferedInputStream bis =
 * new BufferedInputStream(fis); bis.read(bytearray, 0, bytearray.length);
 * t.sendResponseHeaders(200, file.length()); OutputStream os =
 * t.getResponseBody(); os.write(bytearray, 0, bytearray.length); os.close();
 * 
 * } }
 */