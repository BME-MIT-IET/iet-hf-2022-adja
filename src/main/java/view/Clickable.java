package view;

/**
 * Kattintható entitások
 */
public interface Clickable {

    /**
     * Jelzés, hogy rákattintottak
     * @param pos: kattintás pozíciója
     * @param cameraPos: kamera pozíciója
     */
    void Clicked(Position pos, Position cameraPos);

    /**
     * Jelzés, hogy nincs rákattintva
     */
    void UnClicked();

    /**
     * Ellenőrzi, hogy rákattintottak-e
     * @param clickPos: kattintás pozíciója
     * @param cameraPos: kamera pozíciója
     * @return bool: rákattintottak-e
     */
    boolean ClickedCheck(Position clickPos, Position cameraPos);
}
