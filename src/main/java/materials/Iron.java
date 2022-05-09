package materials;

/**
 * A vas az egyik nyersanyag amit ki lehet bányászni a játékban.
 */
public class Iron extends Material {
    /**
     * Növeli a paraméterként kapott számlálóban a típusához tartozó értéket.
     * @param counter: a számoláshoz használt segédosztály
     */
    @Override
    public void Count(MaterialCounter counter) {
        counter.Count(Iron.class);
    }
}
