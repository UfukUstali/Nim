package Graphic;

public interface Hoverable {
    static Hoverable isHovered(Hoverable... hoverables) {
        for (var hoverable : hoverables) {
            if (hoverable.isHovered()) return hoverable;
        }
        return null;
    }
    boolean isHovered();
}
