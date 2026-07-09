package com.example.bookworm.data;

import com.example.bookworm.R;
import com.example.bookworm.model.Book;
import com.example.bookworm.model.Store;
import java.util.Arrays;
import java.util.List;

public class Catalogue {

    public static final List<Book> BOOKS = Arrays.asList(
        // Non-Fiction
        new Book("meditations", "Meditations", "Marcus Aurelius",
            "Non-Fiction", "Stoic Philosophy",
            "#2E5248", "#E7C48B", "c. 180", 254, 91000,
            "The private notes of a Roman emperor to himself — a timeless manual on discipline, perspective and living with virtue amid chaos.",
            R.drawable.cover_art_meditations),
        new Book("republic", "The Republic", "Plato",
            "Non-Fiction", "Classical Thought",
            "#6E2B2B", "#E7C48B", "c. 375 BC", 416, 110000,
            "Plato's dialogue on justice, the ideal city and the nature of the good — the cornerstone of Western political philosophy.",
            R.drawable.cover_art_republic),
        new Book("walden", "Walden", "Henry David Thoreau",
            "Non-Fiction", "Reflection",
            "#3B5230", "#E7C48B", "1854", 352, 89000,
            "Two years, two months and two days at Walden Pond — a meditation on simplicity, self-reliance and the natural world.",
            R.drawable.cover_art_walden),
        new Book("seneca", "Letters from a Stoic", "Seneca",
            "Non-Fiction", "Stoic Philosophy",
            "#27384F", "#E7C48B", "c. 65", 254, 92000,
            "Warm, practical letters on friendship, mortality and tranquillity — Stoic wisdom that still reads like advice from a wise friend.",
            R.drawable.cover_art_seneca),
        // Fiction
        new Book("pride", "Pride and Prejudice", "Jane Austen",
            "Fiction", "Classic Romance",
            "#7C4A63", "#EBD0AE", "1813", 432, 99000,
            "Elizabeth Bennet and Mr Darcy spar their way toward love in Austen's sparkling comedy of manners, class and first impressions.",
            R.drawable.cover_art_pride),
        new Book("gatsby", "The Great Gatsby", "F. Scott Fitzgerald",
            "Fiction", "Jazz Age",
            "#1F2B47", "#E7C48B", "1925", 180, 89000,
            "A bootlegger's pursuit of a green light across the bay — Fitzgerald's shimmering elegy for the American dream.",
            R.drawable.cover_art_gatsby),
        new Book("janeeyre", "Jane Eyre", "Charlotte Brontë",
            "Fiction", "Gothic",
            "#5A2E33", "#E7C48B", "1847", 532, 115000,
            "An orphan governess, a brooding master and a secret in the attic — Brontë's fierce story of conscience and independence.",
            R.drawable.cover_art_janeeyre),
        new Book("moby", "Moby-Dick", "Herman Melville",
            "Fiction", "Adventure",
            "#374A54", "#E7C48B", "1851", 720, 129000,
            "Call me Ishmael. Captain Ahab's obsessive hunt for the white whale becomes a vast meditation on fate, nature and madness.",
            R.drawable.cover_art_moby)
    );

    public static final List<String> FEATURED_IDS = Arrays.asList("pride", "meditations", "gatsby", "republic");

    // Order, data & image mapping match the updated Figma Stores Page (456:509, Store 1..5).
    // Each store uses its matching store_N photo (straight 1-to-1).
    public static final List<Store> STORES = Arrays.asList(
        new Store("ivory-tower", "Ivory Tower Books",    R.drawable.store_1,
            "Bandung",   "Jl. Braga No. 18, Bandung",  "+62 812 3344 5566", "09.00 – 21.00"),
        new Store("alexandria",  "Alexandria Branch",    R.drawable.store_2,
            "Jakarta",   "Jl. Kemang No. 7, Jakarta",  "+62 812 3212 7676", "08.00 – 20.00"),
        new Store("golden-leaf", "The Golden Leaf",      R.drawable.store_3,
            "Yogyakarta","Jl. Malioboro Jogja",        "+62 812 6767 3366", "09.30 – 21.30"),
        new Store("serpents",    "The Serpent's Archive", R.drawable.store_4,
            "Surabaya",  "Jl. Tunjungan, Surabaya",    "+62 856 4455 6677", "09.00 – 21.00"),
        new Store("platos",      "Plato's Atheneum",     R.drawable.store_5,
            "Semarang",  "Jl. Pemuda, Semarang",       "+62 813 5566 7788", "09.00 – 21.00")
    );

    // Home carousel shows 5 slides in Figma order (Carousel component 753:638,
    // Image=1..5): Ivory Tower Books, Alexandria Branch, The Golden Leaf,
    // The Serpent's Archive, Plato's Atheneum. Separate from the 4-item STORES list.
    public static final List<Integer> CAROUSEL_IMAGES = Arrays.asList(
        R.drawable.carousel_slide_1,
        R.drawable.carousel_slide_2,
        R.drawable.carousel_slide_3,
        R.drawable.carousel_slide_4,
        R.drawable.carousel_slide_5
    );

    public static Book findById(String id) {
        for (Book b : BOOKS) {
            if (b.getId().equals(id)) return b;
        }
        return null;
    }

    public static List<Book> getFeatured() {
        java.util.List<Book> result = new java.util.ArrayList<>();
        for (String id : FEATURED_IDS) {
            Book b = findById(id);
            if (b != null) result.add(b);
        }
        return result;
    }
}
