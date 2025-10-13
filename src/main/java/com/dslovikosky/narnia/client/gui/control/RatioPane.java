package com.dslovikosky.narnia.client.gui.control;

public class RatioPane extends GuiPane {
    private final int widthRatio;
    private final int heightRatio;

    public RatioPane(int widthRatio, int heightRatio) {
        this.widthRatio = widthRatio;
        this.heightRatio = heightRatio;
    }

    @Override
    public void negotiateDimensions(double width, Double height) {
        final double cappedWidth = Math.min(width, getPrefSize().isRelative() ? width * getPrefSize().width() : getPrefSize().width());
        final double cappedHeight = Math.min(height, getPrefSize().isRelative() ? height * getPrefSize().height() : getPrefSize().height());
        final double squareWidth = cappedWidth / widthRatio;
        final double squareHeight = cappedHeight / heightRatio;
        final double squareMin = Math.min(squareWidth, squareHeight);
        this.setWidth((int) Math.round(squareMin * widthRatio));
        this.setHeight((int) Math.round(squareMin * heightRatio));

        // Reset the inbounds flag
        setInBounds(true);
    }
}
