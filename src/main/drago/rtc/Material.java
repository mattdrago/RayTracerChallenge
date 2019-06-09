package drago.rtc;

import java.util.Objects;

class Material {

    private Color color = new Color(1, 1, 1);
    private double ambient = 0.1;
    private double diffuse = 0.9;
    private double specular = 0.9;
    private double shininess = 200.0;

    public Color getColor() {
        return color;
    }

    double getDiffuse() {
        return diffuse;
    }

    double getSpecular() {
        return specular;
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

    public double getAmbient() {
        return ambient;
    }

    public Color lighting(Light light, Tuple point, Tuple eyeV, Tuple normalV) {

        Color ambientColor;
        Color diffuseColor = Color.BLACK;
        Color specularColor = Color.BLACK;

        Color effectiveColor = color.hadamardProduct(light.getIntensity());

        Tuple lightV = light.getPosition().subtract(point).normalise();
        ambientColor = effectiveColor.scale(this.ambient);

        double lightDotNormal = lightV.dot(normalV);

        if(lightDotNormal >= 0) {
            diffuseColor = effectiveColor.scale(this.diffuse * lightDotNormal);

            Tuple reflectV = lightV.scale(-1).reflect(normalV);
            double reflectDotEye = reflectV.dot(eyeV);

            if(reflectDotEye > 0) {
                double factor = Math.pow(reflectDotEye, this.shininess);
                specularColor = light.getIntensity().scale(this.specular * factor);
            }
        }

        return ambientColor.add(diffuseColor).add(specularColor);
    }
}
