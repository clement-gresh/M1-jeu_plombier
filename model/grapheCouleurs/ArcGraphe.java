package projetIG.model.grapheCouleurs;

public class ArcGraphe {
    protected SommetGraphe sommet1;
    protected SommetGraphe sommet2;

    public ArcGraphe(SommetGraphe sommet1, SommetGraphe sommet2) {
        this.sommet1 = sommet1;
        this.sommet2 = sommet2;
    }

    public SommetGraphe getSommet1() {
        return sommet1;
    }

    public SommetGraphe getSommet2() {
        return sommet2;
    }
}
