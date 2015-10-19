import java.awt.image.BufferedImage;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.FileImageOutputStream;
import java.io.*;

public class prog{
    public static void main(String[] args) {

        int side = 1000;
        int half_frames = 50; //Half the number of frames in the final gif
        //The gif will play forwards first, then backwards

        BufferedImage[] imageArray = new BufferedImage[100];

        for (int p = 0; p < half_frames; p++){

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
                    int col = RGBI((int)(B.GetMagnitude()*100), 0, (int)(A.GetMagnitude()*100));
                    image.setRGB((side/2)+(int)_x, (side/2)+(int)_y, col);
                }
            }

            imageArray[p] = image;
        }
        System.out.println("Done!");

        File file = new File("result.gif");
        try{
            file.createNewFile();

            ImageOutputStream outputFile = new FileImageOutputStream(file);
            int delay = 1; //Delay in second between frames
            boolean noLoop = false; //Whether loop stops or not
            GifSequenceWriter writer = new GifSequenceWriter(outputFile, imageArray[0].getType(), delay, noLoop);

            //Write the file
            for (int i=0;i<half_frames;i++){
                writer.writeToSequence(imageArray[i]);
            }
            for (int i=half_frames-1;i>-1;i--){
                writer.writeToSequence(imageArray[i]);
            }
            writer.close();
            outputFile.close();
        }catch (IOException e){
            System.out.println("An IO error occured.");
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



