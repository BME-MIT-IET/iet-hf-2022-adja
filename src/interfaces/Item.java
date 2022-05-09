package interfaces;

/**
 * A játéknak azon entitásai, amik olyan craftolható dolgok amiket lehet tárolni is.
 */
public interface Item {
    /**
     * Igazzal tér vissza, ha a paraméterként kapott Item ugyanolyan típusú, mint ő, egyébként hamis.
     * @param item: összehasonlítandó item
     * @return true: megegyezik a paraméter típusa a sajátjával, false: nem egyezik meg
     */
    boolean CompatibleWith(Item item);
}
