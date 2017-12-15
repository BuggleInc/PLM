package plm.universe;

/**
 * SVGOperation is sent to the the JS, it contains the drawing of a world in a specific state
 *
 */
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
