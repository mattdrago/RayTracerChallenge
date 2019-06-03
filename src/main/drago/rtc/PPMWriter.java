package drago.rtc;

public class PPMWriter {

    private static final int MAX_COLOR = 255;

    public String write(Canvas canvas) {
        StringBuilder ppmStringBuilder = new StringBuilder("P3\n");
        ppmStringBuilder.append(canvas.getWidth()).append(" ").append(canvas.getHeight()).append("\n");
        ppmStringBuilder.append(MAX_COLOR).append("\n");

        for (int hi = 0; hi < canvas.getHeight(); hi++) {
            StringBuilder line = new StringBuilder();
            String sepStr = "";

            for (int wi = 0; wi < canvas.getWidth(); wi++) {
                line.append(sepStr).append(colorInPPM(canvas.pixelAt(wi, hi)));
                sepStr = " ";
            }

            splitLine(line);
            ppmStringBuilder.append(line).append("\n");
        }

        return ppmStringBuilder.toString();
    }

    private String colorInPPM(Color color) {
        Color ppmColor = color.scale(MAX_COLOR);

        return ppmClamp(ppmColor.getRed()) + " "
                + ppmClamp(ppmColor.getGreen()) + " "
                + ppmClamp(ppmColor.getBlue());
    }

    private int ppmClamp(double colorComponent) {
        int ppmComponent = (int) (colorComponent + 0.5);

        if(ppmComponent < 0) {
            ppmComponent = 0;
        } else if(ppmComponent > MAX_COLOR) {
            ppmComponent = MAX_COLOR;
        }

        return ppmComponent;
    }

    private void splitLine(StringBuilder line) {
        int maxLength = 70;

        while(line.length() > maxLength) {
            int lastSpace = line.lastIndexOf(" ", maxLength);
            line.replace(lastSpace, lastSpace + 1, "\n");
            maxLength = lastSpace + 70;
        }
    }
}
