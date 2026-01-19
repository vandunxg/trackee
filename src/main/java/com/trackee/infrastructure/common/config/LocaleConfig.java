/* Copyright (c) 2026 Trackee */
package com.trackee.infrastructure.common.config;

import com.trackee.shared.kernel.util.StrUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author vandunxg
 */
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LocaleConfig extends AcceptHeaderLocaleResolver {

    static Logger log = LoggerFactory.getLogger(LocaleConfig.class);
    static Locale DEFAULT_LOCALE = Locale.of("vi", "VN");
    static String KC_LOCALE_KEY = "kc-language";
    static Map SUPPORT_LOCALES;

    public LocaleConfig() {
        this.setSupportedLocales(new ArrayList<>(SUPPORT_LOCALES.values()));
    }

    @Bean(name = {"messageResourceTp"})
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageResource = new ReloadableResourceBundleMessageSource();
        messageResource.setBasenames("classpath:i18n/messages");
        messageResource.setDefaultEncoding("UTF-8");
        messageResource.setCacheSeconds(60);
        messageResource.setDefaultLocale(this.getDefaultLocale());
        return messageResource;
    }

    @Override
    public Locale getDefaultLocale() {
        return DEFAULT_LOCALE;
    }

    @Override
    public Locale resolveLocale(@NonNull HttpServletRequest request) {
        String acceptLanguage = request.getHeader("Accept-Language");
        String tpLanguage = request.getHeader("kc-language");
        String language;
        if (StrUtils.isNotBlank(tpLanguage)) {
            language = tpLanguage;
        } else {
            language = acceptLanguage;
        }

        if (!StringUtils.hasText(language)) {
            return this.getDefaultLocale();
        } else {
            try {
                List<Locale.LanguageRange> languageRanges = Locale.LanguageRange.parse(language);
                Locale foundLocale = Locale.lookup(languageRanges, this.getSupportedLocales());
                return foundLocale == null ? this.getDefaultLocale() : foundLocale;
            } catch (Exception e) {
                log.error("Error when get language in header: {}", e.getMessage());
                return DEFAULT_LOCALE;
            }
        }
    }

    static {
        SUPPORT_LOCALES = Stream.of(Locale.ENGLISH, DEFAULT_LOCALE)
                .collect(Collectors.toMap(Locale::getLanguage, (locale) -> locale));
    }
}
