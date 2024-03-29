package drago.rtc;

import drago.rtc.foundations.Tuple;
import drago.rtc.pattern.Pattern;
import drago.rtc.shape.Shape;

import java.util.Objects;

public class Material {

    private Color color = new Color(1, 1, 1);
    private double ambient = 0.1;
    private double diffuse = 0.9;
    private double specular = 0.9;
    private double shininess = 200.0;
    private double reflective = 0.0;
    private Pattern pattern;
    private double transparency = 0.0;
    private double refractiveIndex = 1.0;

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void setDiffuse(double diffuse) {
        this.diffuse = diffuse;
    }

    public double getDiffuse() {
        return diffuse;
    }

    public void setSpecular(double specular) {
        this.specular = specular;
    }

    public double getSpecular() {
        return specular;
    }

    public void setShininess(double shininess) {
        this.shininess = shininess;
    }

    public double getShininess() {
        return shininess;
    }


    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public void setReflective(double reflective) {
        this.reflective = reflective;
    }

    public double getReflective() {
        return reflective;
    }

    public void setAmbient(double ambient) {
        this.ambient = ambient;
    }

    public double getAmbient() {
        return ambient;
    }

    public void setTransparency(double transparency) {
        this.transparency = transparency;
    }

    public double getTransparency() {
        return transparency;
    }

    public void setRefractiveIndex(double refractiveIndex) {
        this.refractiveIndex = refractiveIndex;
    }

    public double getRefractiveIndex() {
        return refractiveIndex;
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

    Color lighting(Light light, Shape object, Tuple point, Tuple eyeV, Tuple normalV, boolean inShadow) {

        Color ambientColor;
        Color diffuseColor = Color.BLACK;
        Color specularColor = Color.BLACK;

        Color effectiveColor = color;

        if(pattern != null) {
            effectiveColor = pattern.patternAtShape(object, point);
        }

        effectiveColor = effectiveColor.hadamardProduct(light.getIntensity());

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
}
