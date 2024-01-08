package com.bedrockcloud.bedrockcloud.api.event;

import java.lang.reflect.Method;

/**
 * @author LucGamesYT
 * @version 1.0
 */
public record RegisteredListener(Method method, Listener listener) {

}
