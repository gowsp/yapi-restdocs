package io.github.gowsp.generator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Preconditions;
import com.thoughtworks.qdox.model.JavaMethod;
import io.github.gowsp.module.config.YApiConfig;
import io.github.gowsp.module.source.finder.CatalogFinder;
import io.github.gowsp.module.source.finder.SourceMethodFinder;
import io.github.gowsp.module.source.parser.Parser;
import io.github.gowsp.module.yapi.YApiDocument;
import io.github.gowsp.module.yapi.YApiRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Set;

@Singleton
public final class Generator {

    private static final Logger log = LoggerFactory.getLogger(Generator.class);
    private final ObjectMapper mapper;
    private final Set<Parser> parsers;
    private final CatalogFinder catalogFinder;
    private final SourceMethodFinder methodFinder;
    private final YApiConfig config;

    public static GeneratorBuilder newBuilder(YApiConfig config) {
        return new GeneratorBuilder(config);
    }

    public static GeneratorBuilder newBuilder() {
        return new GeneratorBuilder(new YApiConfig());
    }

    @Inject
    Generator(ObjectMapper mapper, SourceMethodFinder methodFinder, Set<Parser> parsers, CatalogFinder catalogFinder,
              YApiConfig config) {
        this.mapper = mapper;
        this.config = config;
        this.parsers = parsers;
        this.methodFinder = methodFinder;
        this.catalogFinder = catalogFinder;
    }

    public boolean generate(String path, String method) {
        YApiRequest request = new YApiRequest(path, method);
        return generate(request);
    }

    public boolean generate(YApiRequest request) {
        Preconditions.checkNotNull(config);
        YApiDocument document = new YApiDocument().setRequest(request);
        document.setProjectId(config.getProjectId());

        JavaMethod javaMethod = methodFinder.findMethod(request);
        document.setTitle(javaMethod.getComment());

        Number catalogId = catalogFinder.findCatalogId(javaMethod);
        document.setCatId(catalogId);
        parsers.forEach(parser -> parser.assemble(document, javaMethod));
        try {
            return send(config, document);
        } catch (IOException e) {
            log.error("send request error", e);
            return false;
        }
    }

    public boolean send(YApiConfig config, YApiDocument document) throws IOException {
        document.setProjectId(config.getProjectId());
        URL url = new URL(config.getSavePath());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        mapper.writeValue(conn.getOutputStream(), document);

        ObjectNode result = mapper.readValue(conn.getInputStream(), ObjectNode.class);
        JsonNode msg = result.get("errmsg");
        log.info("result: {}", msg.asText());
        return result.get("errcode").asInt() == 0;
    }

}
