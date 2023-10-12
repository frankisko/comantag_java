package com.frankisko.comantag.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class PluralsConfig {
    public String getPluralFor(String singular) {
        Map<String, String> plurals = new HashMap<>();
        plurals.put("artist", "artists");
        plurals.put("character", "characters");
        plurals.put("group", "groups");
        plurals.put("language", "languages");
        plurals.put("serie", "series");
        plurals.put("tag", "tags");
        plurals.put("type", "types");

        return plurals.get(singular);
    }
}
