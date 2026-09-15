package com.ningshang.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Component("richText")
public class RichText {
    private static final Safelist ALLOWED = Safelist.relaxed()
            .addTags("h1", "h2", "h3", "h4", "h5", "h6", "span", "div", "hr")
            .addAttributes(":all", "style", "class")
            .addAttributes("img", "width", "height")
            .addProtocols("img", "src", "data")
            .preserveRelativeLinks(true);
    private static final Set<String> CSS = new HashSet<>(Arrays.asList(
            "color", "background-color", "font-size", "font-family", "font-weight", "font-style",
            "text-align", "text-decoration", "text-indent", "line-height", "width", "height", "max-width"));

    public static boolean isContentKey(String key) {
        return key != null && (key.matches(".*(_body|_description|_intro)$") || key.startsWith("culture_")
                || key.equals("speech_quote") || key.equals("footer_brand_desc"));
    }

    public static String clean(String value) {
        if (value == null || value.isEmpty()) return value;
        String html = value;
        if (!java.util.regex.Pattern.compile("</?[a-z][^>]*>", java.util.regex.Pattern.CASE_INSENSITIVE).matcher(value).find()) {
            html = Arrays.stream(value.split("\\r?\\n\\s*\\r?\\n", -1))
                    .map(p -> "<p>" + Entities.escape(p).replaceAll("\\r?\\n", "<br>") + "</p>")
                    .collect(Collectors.joining());
        }
        Document doc = Jsoup.parseBodyFragment(Jsoup.clean(html, "http://localhost/", ALLOWED));
        doc.outputSettings().prettyPrint(false);
        doc.select("[style]").forEach(element -> {
            String style = Arrays.stream(element.attr("style").split(";"))
                    .filter(rule -> {
                        String[] pair = rule.split(":", 2);
                        return pair.length == 2 && CSS.contains(pair[0].trim().toLowerCase(Locale.ROOT))
                                && !pair[1].toLowerCase(Locale.ROOT).matches(".*(url|expression|javascript|@|\\\\).*");
                    }).collect(Collectors.joining(";"));
            if (style.isEmpty()) element.removeAttr("style"); else element.attr("style", style);
        });
        return doc.body().html();
    }
}
