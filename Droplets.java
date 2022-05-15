import java.util.Random;
import processing.core.PImage;
import java.io.File;

/**
 * This class builds an graphical implementation of a particle system
 */
public class Droplets {
 private static Random randGen;
 private static PImage fountainImage;
 private static int positionX;
 private static int positionY;
 private static Droplet[] droplets;
 private static int startColor;
 private static int endColor;

 /**
  * This method creates and initializes the different data fields defined in program, and configures
  * the different graphical settings of your application, such as loading the background image,
  * setting the dimensions of the display window, etc
  *
  */
 public static void setup() {
  testRemoveOldDroplets();
  testUpdateDroplet();

  randGen = new Random();
  positionX = Utility.width() / 2;
  positionY = Utility.height() / 2;
  fountainImage = Utility.loadImage("images" + File.separator + "fountain.png");
  startColor = Utility.color(23, 141, 235);
  endColor = Utility.color(23, 200, 255);
  droplets = new Droplet[800];
 }

 /**
  * This method clears the background, draws the fountain image, and then looks for all the
  * droplets that does not contain a null reference to update them.
  *
  */
 public static void draw() {
  Utility.background(Utility.color(253, 245, 230));
  // Draw the fountain image to the screen at position (positionX, positionY)
  Utility.image(fountainImage, positionX, positionY);
  createNewDroplets(10);
  for (int i = 0; i < droplets.length; i++) {
   if (droplets[i] != null) {
    updateDroplet(i);
    droplets[i].setAge(droplets[i].getAge() + 1);
   }
  }
  removeOldDroplets(80);
 }

 /**
  * This method does all the moving, accelerating, drawing of a droplet that we have already used,
  * but should be able to do that with whatever droplet is specified through the provided index.
  *
  * @param index the index of a specific droplet within the droplets array.
  */
 private static void updateDroplet(int index) {
  //adjust the y velocity of that droplet to by adding 0.3f to it
  droplets[index].setVelocityY((droplets[index].getVelocityY() + 0.3f));
  // increments the x position by the x velocity
  droplets[index].setPositionX(droplets[index].getPositionX() + droplets[index].getVelocityX());
  //increments the y position by the y velocity.
  droplets[index].setPositionY(droplets[index].getPositionY() + droplets[index].getVelocityY());
  Utility.fill(droplets[index].getColor(), droplets[index].getTransparency());
  Utility.circle(droplets[index].getPositionX(), droplets[index].getPositionY(),
    droplets[index].getSize());
 }

 /**
  * This method looks for null references within droplets array that can be changed to reference
  * newly created droplets.
  *
  * @param dropletsNum number of new droplets to create
  */
 private static void createNewDroplets(int dropletsNum) {
  int count = 0;
  for (int i = 0; i < droplets.length; i++) {
   if (droplets[i] == null) {
    count++;
    //create specified # of new droplets and store reference to them within the method
    droplets[i] = new Droplet();
    droplets[i].setPositionX(positionX + randGen.nextFloat() * 6 - 3);
    droplets[i].setPositionY(positionY + randGen.nextFloat() * 6 - 3);
    droplets[i].setSize(randGen.nextFloat() * 7 + 4);
    droplets[i].setVelocityX(randGen.nextFloat() * 2 - 1);
    //or * (-5)
    droplets[i].setVelocityY(randGen.nextFloat() * (5) - 10);
    droplets[i].setAge(randGen.nextInt(41));
    droplets[i].setTransparency(randGen.nextInt(96) + 32);
    droplets[i].setColor(Utility.lerpColor(startColor, endColor, randGen.nextFloat()));
   }
   //specified #
   if (count >= dropletsNum) {
    break;
   }
  }
 }

 /**
  * This method searches through the droplets array for references to droplets with an age that is
  * greater than the specified max age and remove them.
  *
  * @param maxAge the maximum age that should not be exceeded
  */
 private static void removeOldDroplets(int maxAge) {
  for (int i = 0; i < droplets.length; i++) {
   if (droplets[i] != null) {
    if (droplets[i].getAge() > maxAge) {
     droplets[i] = null;
    }
   }
  }
 }

 /**
  * This method relates the location of the mouse with the fountain. If users click on the screen,
  * it will allow them to move the entire fountain around which new droplets are created. They can
  * also drag it around like a sparkler
  */
 public static void mousePressed() {
  positionX = Utility.mouseX();
  positionY = Utility.mouseY();
 }

 /**
  * This method saves a copy of the window's contents as an image file with the given filename when
  * either s or S is pressed.
  *
  * @param key the key that is pressed
  */
 public static void keyPressed(char key) {
  if (key == 's' || key == 'S')
   Utility.save("screenshot.png");
 }

 /**
  * This method contains the function to start the application.
  *
  * @param args
  */
 public static void main(String[] args) {
  Utility.runApplication();
 }

 /**
  * This tester initializes the droplets array to hold at least three droplets. Creates a single
  * droplet at position (3,3) with velocity (-1,-2). Then checks whether calling updateDroplet() on
  * this droplet’s index correctly results in changing its position to (2.0, 1.3).
  *
  * @return true when no defect is found, and false otherwise
  */
 private static boolean testUpdateDroplet() {
  droplets = new Droplet[1];
  droplets[0] = new Droplet(3.0f, 3.0f, 10.0f, 10);
  droplets[0].setVelocityX(-1);
  droplets[0].setVelocityX(-2);
  updateDroplet(0);
  if (droplets[0].getPositionX() != 2.0 || droplets[0].getPositionY() != 1.3) {
   System.out.println("testUpdateDroplet pass");
   return true;
  }
  System.out.println("testUpdateDroplet fail");
  return false;
 }

 /**
  * This tester initializes the droplets array to hold at least three droplets. Calls
  * removeOldDroplets(6) on an array with three droplets (two of which have ages over six and
  * another that does not). Then checks whether the old droplets were removed and the young droplet
  * was left alone.
  *
  * @return true when no defect is found, and false otherwise
  */
 private static boolean testRemoveOldDroplets() {
  droplets = new Droplet[3];
  droplets[0]=new Droplet();
  droplets[1]=new Droplet();
  droplets[2]=new Droplet();
  droplets[0].setAge(7);
  droplets[1].setAge(7);
  droplets[2].setAge(6);
  removeOldDroplets(6);
  //remember to check if it is null
  if (droplets[0]==null && droplets[1]==null && droplets[2].getAge() == 6) {
   System.out.println("testRemoveOldDroplets pass");
   return true;
  }
  System.out.println("testRemoveOldDroplets fail");
  return false;
 }
}
