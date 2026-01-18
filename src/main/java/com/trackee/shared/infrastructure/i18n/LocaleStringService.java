/* Copyright (c) 2026 Trackee */
package com.trackee.shared.infrastructure.i18n;

import java.util.Locale;

/**
 * @author vandunxg
 */
public interface LocaleStringService {

    Locale getCurrentLocale();

    String getMessage(String messageCode, String defaultMessage, Object... params);
}
