package crawler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

public class Parser {
	private String resource;

	public Parser(String resource) {
		this.resource = resource;

	}

	public String parseDoc() throws IOException, SAXException,
			TikaException {
		File file = new File(this.resource);
		FileInputStream stream = new FileInputStream(file);

		AutoDetectParser parser = new AutoDetectParser();
		BodyContentHandler handler = new BodyContentHandler(-1);
		Metadata metadata = new Metadata();

		try {
			parser.parse(stream, handler, metadata);
			return handler.toString();
		} finally {
			stream.close();
		}
	}
}
