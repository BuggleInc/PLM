package plm.universe;

public class SVGOperation {

    private String svgOperation;

    public SVGOperation(String svgOperation) {
        this.svgOperation = svgOperation;
    }

    public SVGOperation() {

    }

    public String getOperation() {

        return this.svgOperation;
    }

    public void setOperation(String svgOperation) {
        this.svgOperation = svgOperation;
    }
}
