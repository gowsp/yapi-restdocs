package io.github.gowsp;

import io.github.gowsp.generator.Generator;
import io.github.gowsp.module.source.parser.ParserEnum;
import org.junit.jupiter.api.Test;

class SourceGenerateTest {

    @Test
    public void generate() {
        String source = "C:\\Work\\project\\analytics\\fmanalytics-master\\fmanalytics-dashboard\\src\\main\\java";
        Generator generator = Generator.newBuilder()
                .setProjectId(34)
                .addSourcePath(source)
                .addSourcePath("C:\\Work\\project\\analytics\\fmanalytics-master\\fmanalytics-common\\src\\main\\java")
                .setParser(ParserEnum.SPRING)
                .setUrl("http://docs.fsboss.tk")
                .setToken("016b102b33e095dba37b62d7c81a09ae68dddfc4fc0215edde443671d9ada10d")
                .build();
//        generator.generate("/versions", "POST");
//        generator.generate("/upgrades/rules", "PUT");
//        generator.generate("/upgrades/rules/{id}", "PUT");
//        generator.generate("/upgrades/rules/{id}", "PUT");
        generator.generate("/upgrades/bpartner-rules", "PUT");
    }
}