package drago.rtc;

import java.util.Objects;

public class Material {

    private Color color = new Color(1, 1, 1);
    private double ambient = 0.1;
    private double diffuse = 0.9;
    private double specular = 0.9;
    private double shininess = 200.0;
    private Pattern pattern;

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void setDiffuse(double diffuse) {
        this.diffuse = diffuse;
    }

    double getDiffuse() {
        return diffuse;
    }

    public void setSpecular(double specular) {
        this.specular = specular;
    }

    double getSpecular() {
        return specular;
    }

    void setShininess(double shininess) {
        this.shininess = shininess;
    }

    double getShininess() {
        return shininess;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Material material = (Material) o;
        return Double.compare(material.ambient, ambient) == 0 &&
                Double.compare(material.diffuse, diffuse) == 0 &&
                Double.compare(material.specular, specular) == 0 &&
                Double.compare(material.shininess, shininess) == 0 &&
                Objects.equals(color, material.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, diffuse, specular, shininess);
    }

    @Override
    public String toString() {
        return "Material{" +
                "color=" + color +
                ", ambient=" + ambient +
                ", diffuse=" + diffuse +
                ", specular=" + specular +
                ", shininess=" + shininess +
                '}';
    }

    public void setAmbient(double ambient) {
        this.ambient = ambient;
    }

    double getAmbient() {
        return ambient;
    }

    Color lighting(Light light, Shape object, Tuple point, Tuple eyeV, Tuple normalV, boolean inShadow) {

        Color ambientColor;
        Color diffuseColor = Color.BLACK;
        Color specularColor = Color.BLACK;

        Color effectiveColor = color;

        if(pattern != null) {
            effectiveColor = pattern.colorAtObject(object, point);
        }

        effectiveColor.hadamardProduct(light.getIntensity());

        ambientColor = effectiveColor.scale(this.ambient);

        if(!inShadow) {
            Tuple lightV = light.getPosition().subtract(point).normalise();
            double lightDotNormal = lightV.dot(normalV);

            if (lightDotNormal >= 0) {
                diffuseColor = effectiveColor.scale(this.diffuse * lightDotNormal);

                Tuple reflectV = lightV.scale(-1).reflect(normalV);
                double reflectDotEye = reflectV.dot(eyeV);

                if (reflectDotEye > 0) {
                    double factor = Math.pow(reflectDotEye, this.shininess);
                    specularColor = light.getIntensity().scale(this.specular * factor);
                }
            }
        }

        return ambientColor.add(diffuseColor).add(specularColor);
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }
}
