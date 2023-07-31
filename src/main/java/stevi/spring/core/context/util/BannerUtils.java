package stevi.spring.core.context.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class BannerUtils {

    private static final String BANNER = """
            ********************************************
            *     Custom Spring Framework (v1.0.0)     *
            ********************************************
            """;

    public static void printBanner() {
        System.out.println(BANNER);
    }
}
