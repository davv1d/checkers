package com.kodilla.view;
import javafx.scene.image.ImageView;

public class ImageViewPawn extends ImageView {
    private int lastPositionX;
    private int lastPositionY;
    private double mouseX;
    private double mouseY;

    public ImageViewPawn(String url, int lastPositionX, int lastPositionY) {
        super(url);
        this.lastPositionX = lastPositionX;
        this.lastPositionY = lastPositionY;
        this.mouseEvent();
    }

    public int getLastPositionX() {
        return lastPositionX;
    }

    public int getLastPositionY() {
        return lastPositionY;
    }

    public void setLastPositionX(int lastPositionX) {
        this.lastPositionX = lastPositionX;
    }

    public void setLastPositionY(int lastPositionY) {
        this.lastPositionY = lastPositionY;
    }

    private void mouseEvent() {
        this.setOnMousePressed(event -> {
            mouseX = event.getSceneX();
            mouseY = event.getSceneY();
        });
        this.setOnMouseDragged(event -> {
            relocate(event.getSceneX() - mouseX + lastPositionX * CheckersApp.FIELD_SIZE, event.getSceneY() - mouseY + lastPositionY * CheckersApp.FIELD_SIZE);
        });
    }
}
