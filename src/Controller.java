import java.awt.color.ICC_ColorSpace;

/**
 * Created by USER on 19.02.2016.
 */
public class Controller {
    private double rValue;
    private double gValue;
    private double bValue;
    private double cValue;
    private double mValue;
    private double yValue;
    private double hValue;
    private double lValue;
    private double sValue;
    private double luValue;
    private double uValue;
    private double vValue;

    public void init() {
        rValue = 0;
        gValue = 0;
        bValue = 0;
        fromRGBtoCMY();
        fromRGBtoHLS();
        fromXYZtoLUV(fromRGBtoXYZ());
    }

    public void fromRGBtoCMY() {
        cValue = 1 - (rValue / 255);
        mValue = 1 - (gValue / 255);
        yValue = 1 - (bValue / 255);
    }

    public void fromCMYtoRGB() {
        rValue = (1 - cValue) * 255;
        gValue = (1 - mValue) * 255;
        bValue = (1 - yValue) * 255;
    }

    public void fromRGBtoHLS() { //Observer. = 2°, Illuminant = D65
        double varR = rValue / 255;
        double varG = gValue / 255;
        double varB = bValue / 255;

        double minValue = Math.min(Math.min(varR, varG), varB);
        double maxValue = Math.max(Math.max(varR, varG), varB);
        double delta = maxValue - minValue;

        lValue = (minValue + maxValue) / 2;

        if(maxValue == minValue) {
            hValue = 0;
            sValue = 0;
        }
        else {
            if(lValue < 0.5) {
                sValue = delta / (maxValue + minValue);
            }
            else {
                sValue = delta / (2 - maxValue - minValue);
            }

            double deltaR = (((maxValue - varR) / 6) + (delta / 2)) / delta;
            double deltaG = (((maxValue - varG) / 6) + (delta / 2)) / delta;
            double deltaB = (((maxValue - varB) / 6) + (delta / 2)) / delta;

            if(varR == maxValue) {
                hValue = deltaB - deltaG;
            }
            else if(varG == maxValue) {
                hValue = (1 / 3) + deltaR - deltaB;
            }
            else {
                hValue = (2 / 3) + deltaG - deltaR;
            }

            if(hValue < 0) {
                hValue += 1;
            }
            else if(hValue > 1) {
                hValue -= 1;
            }
        }
    }

    public void fromHLStoRGB() {
        if(sValue == 0) {
            rValue = lValue * 255;
            gValue = lValue * 255;
            bValue = lValue * 255;
        }
        else {
            double var2;
            if(lValue < 0.5) {
                var2 = lValue * (1. + sValue);
            }
            else {
                var2 = (lValue + sValue) - (lValue * sValue);
            }

            double var1 = 2. * lValue - var2;

            rValue = hueFunction(var1, var2, (hValue + (1. / 3.)));
            gValue = hueFunction(var1, var2, hValue);
            bValue = hueFunction(var1, var2, (hValue - (1. / 3.)));

            rValue *= 255;
            gValue *= 255;
            bValue *= 255;
        }
    }

    public double hueFunction(double var1, double var2, double varH) {
        if(varH > 1) {
            varH -= 1;
        }
        else if(varH < 0) {
            varH += 1;
        }

        if((6 * varH) < 1) {
            return (var1 + (var2 - var1) * 6 * varH);
        }
        if((2 * varH) < 1) {
            return var2;
        }
        if((3 * varH) < 2) {
            return (var1 + (var2 - var1) * ((2. / 3.) - varH) * 6);
        }
        return var1;
    }

    public double[] fromRGBtoXYZ() {
        double varR = rValue / 255;
        double varG = gValue / 255;
        double varB = bValue / 255;

        if(varR <= 0.4045) {
            varR /= 12.92;
        }
        else {
            varR = Math.pow((varR + 0.055) / 1.055, 2.4);
        }

        if(varG <= 0.4045) {
            varG /= 12.92;
        }
        else {
            varG = Math.pow((varG + 0.055) / 1.055, 2.4);
        }

        if(varB <= 0.4045) {
            varB /= 12.92;
        }
        else {
            varB = Math.pow((varB + 0.055) / 1.055, 2.4);
        }

        varR *= 100;
        varG *= 100;
        varB *= 100;

        double[] xyz = new double[3];
        xyz[0] = 0.4124 * varR + 0.3576 * varG + 0.1805 * varB;
        xyz[1] = 0.2126 * varR + 0.7152 * varG + 0.0722 * varB;
        xyz[2] = 0.0193 * varR + 0.1192 * varG + 0.9505 * varB;

        //X from 0 to  95.047
        //Y from 0 to 100.000
        //Z from 0 to 108.883

        return xyz;
    }

    public boolean fromXYZtoRGB(double[] xyz) {
        double xVar = xyz[0] / 100;
        double yVar = xyz[1] / 100;
        double zVar = xyz[2] / 100;

        double varR = 3.2406 * xVar - 1.5372 * yVar - 0.4986 * zVar;
        double varG = -0.9689 * xVar + 1.8758 * yVar + 0.0415 * zVar;
        double varB = 0.0557 * xVar - 0.2040 * yVar + 1.0570 * zVar;

        if(varR > 0.0031308) {
            varR = 1.055 * (Math.pow(varR, 1 / 2.4)) - 0.055;
        }
        else {
            varR *= 12.92;
        }

        if(varG > 0.0031308) {
            varG = 1.055 * (Math.pow(varG, 1 / 2.4)) - 0.055;
        }
        else {
            varG *= 12.92;
        }

        if(varB > 0.0031308) {
            varB = 1.055 * (Math.pow(varB, 1 / 2.4)) - 0.055;
        }
        else {
            varB *= 12.92;
        }

        rValue = varR * 255;
        gValue = varG * 255;
        bValue = varB * 255;

        boolean colorsPure = true;

        if(rValue < 0) {
            colorsPure = false;
            rValue = 0;
        }
        if(rValue > 255) {
            colorsPure = false;
            rValue = 255;
        }
        if(gValue < 0) {
            colorsPure = false;
            gValue = 0;
        }
        if(gValue > 255) {
            colorsPure = false;
            gValue = 255;
        }
        if(bValue < 0) {
            colorsPure = false;
            bValue = 0;
        }
        if(bValue > 255) {
            colorsPure = false;
            bValue = 255;
        }

        return colorsPure;
        //System.out.println(rValue + " " + gValue + " " + bValue);
    }

    public void fromXYZtoLUV(double[] xyz) {
        luValue = xyz[1];
        if(xyz[0] == 0 && xyz[2] == 0) {
            uValue = 0;
            vValue = 0;
        }
        else {
            uValue = 4 * xyz[0] / (xyz[0] + 15 * xyz[1] + 3 * xyz[2]);
            vValue = 9 * xyz[1] / (xyz[0] + 15 * xyz[1] + 3 * xyz[2]);
        }

        //System.out.println(luValue + " " + uValue + " " + vValue);
    }

    public double[] fromLUVtoXYZ() {
        double[] xyz = new double[3];
        if(vValue == 0) {
            xyz[0] = 0;
            xyz[1] = 0;
            xyz[2] = 0;
            return xyz;
        }
        xyz[1] = luValue;
        double eq = 9 * xyz[1] / vValue;
        xyz[0] = uValue * eq / 4;
        xyz[2] = (eq - xyz[0] - 15 * xyz[1]) / 3;
        return xyz;
    }

    public double getrValue() {
        return rValue;
    }

    public double getgValue() {
        return gValue;
    }

    public double getbValue() {
        return bValue;
    }

    public double getcValue() {
        return cValue;
    }

    public double getmValue() {
        return mValue;
    }

    public double getyValue() {
        return yValue;
    }

    public double gethValue() {
        return hValue;
    }

    public double getlValue() {
        return lValue;
    }

    public double getsValue() {
        return sValue;
    }

    public double getLuValue() {
        return luValue;
    }

    public double getuValue() {
        return uValue;
    }

    public double getvValue() {
        return vValue;
    }

    public void setrValue(double rValue) {
        this.rValue = rValue;
    }

    public void setgValue(double gValue) {
        this.gValue = gValue;
    }

    public void setbValue(double bValue) {
        this.bValue = bValue;
    }

    public void setcValue(double cValue) {
        this.cValue = cValue;
    }

    public void setmValue(double mValue) {
        this.mValue = mValue;
    }

    public void setyValue(double yValue) {
        this.yValue = yValue;
    }

    public void sethValue(double hValue) {
        this.hValue = hValue;
    }

    public void setlValue(double lValue) {
        this.lValue = lValue;
    }

    public void setsValue(double sValue) {
        this.sValue = sValue;
    }

    public void setLuValue(double luValue) {
        this.luValue = luValue;
    }

    public void setuValue(double uValue) {
        this.uValue = uValue;
    }

    public void setvValue(double vValue) {
        this.vValue = vValue;
    }
}
