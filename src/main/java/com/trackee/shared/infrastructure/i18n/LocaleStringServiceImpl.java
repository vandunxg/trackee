/* Copyright (c) 2026 Trackee */
package com.trackee.shared.infrastructure.i18n;

import com.trackee.shared.kernel.util.StrUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author vandunxg
 */
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LocaleStringServiceImpl implements LocaleStringService {

    static Logger log = LoggerFactory.getLogger(LocaleStringServiceImpl.class);
    static Locale DEFAULT_LOCALE = Locale.of("vi", "VN");
    static String KC_LOCALE_KEY = "kc-language";
    MessageSource messageSource;

    @Autowired
    public LocaleStringServiceImpl(@Qualifier("messageResourceTp") MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public Locale getCurrentLocale() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (!(requestAttributes instanceof ServletRequestAttributes)) {
            return DEFAULT_LOCALE;
        } else {
            HttpServletRequest request =
                    ((ServletRequestAttributes) requestAttributes).getRequest();
            return this.resolveLocale(request);
        }
    }

    public Locale resolveLocale(HttpServletRequest request) {
        String acceptLanguage = request.getHeader("Accept-Language");
        String tpLanguage = request.getHeader("kc-language");
        Map<String, String[]> map = request.getParameterMap();
        if (map.containsKey("kc-language")) {
            tpLanguage = ((String[]) map.get("kc-language"))[0];
        }

        if (StrUtils.isNotBlank(tpLanguage)) {
            if (!StringUtils.hasText(tpLanguage)) {
                return DEFAULT_LOCALE;
            } else {
                List<Locale> allAvailableLocales = Arrays.asList(Locale.getAvailableLocales());

                try {
                    List<Locale.LanguageRange> languageRanges =
                            Locale.LanguageRange.parse(tpLanguage);
                    Locale foundLocale = Locale.lookup(languageRanges, allAvailableLocales);
                    return foundLocale == null ? DEFAULT_LOCALE : foundLocale;
                } catch (Exception e) {
                    log.error("Error when get language in header: {}", e.getMessage());
                    return DEFAULT_LOCALE;
                }
            }
        } else {
            return DEFAULT_LOCALE;
        }
    }

    @Override
    public String getMessage(String messageCode, String defaultMessage, Object... params) {
        Locale currentLocale = this.getCurrentLocale();

        try {
            return this.messageSource.getMessage(messageCode, params, currentLocale);
        } catch (Exception var8) {
            log.warn(
                    "Could not find message {} for locale {}. Using default message",
                    messageCode,
                    currentLocale);

            try {
                return MessageFormat.format(defaultMessage, params);
            } catch (Exception e1) {
                log.error(e1.getMessage(), e1);
                return defaultMessage;
            }
        }
    }
}
