import java.awt.image.BufferedImage;
import javax.imageio.ImageIO.*;
import java.io.*;

public class prog{
    public static void main(String[] args) {

        int side = 1000;

        for (int p = 0; p < 100; p++){

            //Set up the image
            BufferedImage image = new BufferedImage(side, side, BufferedImage.TYPE_INT_ARGB);

            double delta = 4.0/((double)side);
            int maxIter = 10+3*p;

            Complex A;
            Complex B;

            for (double x = -2; x < 2; x += delta){
                for (double y = -2; y < 2; y += delta){

                    int iterations = 0;
                    A = new Complex(x,y);
                    B = new Complex(0,0);

                    while (B.GetMagnitude() < (2-(double)p/100) && iterations < maxIter){
                        B = B.Multiply(B).Add(A);//Shit is also funky here. Play around with this.
                        iterations++;
                    }

                    double _x = x*side/4.0;
                    double _y = y*side/4.0;

                    //This is where shit gets funky
                    //Play around with this col variable to change your fractals up
                    int col = RGBI((int)(B.GetMagnitude()*(100-p)), 0, (int)(A.GetMagnitude()*(100-p)));
                    image.setRGB((side/2)+(int)_x, (side/2)+(int)_y, col);
                }
            }


            //Output the image
            File outputfile = new File("saved" + p + ".png");
            try {javax.imageio.ImageIO.write(image, "png", outputfile);}
            catch (IOException e){System.err.println("Caught IOException: " +  e.getMessage());}
            finally{}

            System.out.println("Done: " + p);
        }
    }

    public static int RGBI(int Red, int Green, int Blue){
        int r = (Red << 16) & 0x00FF0000;
        int g = (Green << 8) & 0x0000FF00;
        int b = Blue & 0x000000FF;

        return 0xFF000000 | r | g | b;
    }
}

class Complex {

    public double r = 0;
    public double i = 0;

    public Complex(double real, double imaginary){

        r = real;
        i = imaginary;
    }

    public double GetMagnitude(){
        return Math.sqrt(Math.pow(r,2) + Math.pow(i,2));
    }

    public Complex Multiply(Complex c){

        double _r = r * c.r - i * c.i;
        double _i = r * c.i + i * c.r;

        return new Complex(_r, _i);
    }

    public Complex Add(Complex c){

        double _r = r + c.r;
        double _i = i + c.i;

        return new Complex(_r, _i);
    }
}
