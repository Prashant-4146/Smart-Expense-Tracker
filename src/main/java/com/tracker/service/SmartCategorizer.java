package com.tracker.service;

import java.util.HashMap;
import java.util.Map;

public class SmartCategorizer {
    private static final Map<String, String[]> KEYWORDS = new HashMap<>();

    static {
        // Personal
        KEYWORDS.put("Personal|Clothes", new String[]{"shirt", "pant", "jeans", "zara", "h&m", "myntra", "clothes"});
        KEYWORDS.put("Personal|Food / Restaurants", new String[]{"pizza", "burger", "kfc", "mcdonalds", "swiggy", "zomato", "restaurant", "food", "lunch", "dinner", "swiggy"});
        KEYWORDS.put("Personal|Travel", new String[]{"uber", "ola", "train", "flight", "bus", "metro", "ticket", "travel"});
        KEYWORDS.put("Personal|Entertainment", new String[]{"movie", "netflix", "prime", "spotify", "concert", "game", "entertainment"});
        
        // Official
        KEYWORDS.put("Official|Books", new String[]{"book", "amazon", "kindle", "textbook", "novel"});
        KEYWORDS.put("Official|Printing", new String[]{"print", "xerox", "photocopy"});
        KEYWORDS.put("Official|Projects", new String[]{"project", "hardware", "software", "component", "domain", "hosting"});
        KEYWORDS.put("Official|Fees", new String[]{"fee", "tuition", "exam", "college", "school"});
        KEYWORDS.put("Official|Supplies", new String[]{"pen", "paper", "notebook", "stationery", "supply"});
    }

    public static String[] suggestCategory(String description) {
        if (description == null || description.trim().isEmpty()) {
            return new String[]{"Others", "Miscellaneous"};
        }
        
        String descLower = description.toLowerCase();
        
        for (Map.Entry<String, String[]> entry : KEYWORDS.entrySet()) {
            for (String keyword : entry.getValue()) {
                if (descLower.contains(keyword)) {
                    return entry.getKey().split("\\|");
                }
            }
        }
        
        return new String[]{"Others", "Miscellaneous"};
    }
}
