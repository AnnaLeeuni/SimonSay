import java.awt.Color;
import java.util.Random;

import tester.Tester;
import javalib.funworld.*;

import javalib.worldimages.*;

// SimonWorld has one subclass with a ton of field
// I know it's a little massive, but it's clear having all the
// field together and update them when needed, it's actually
// really useful than you see

class SimonWorld extends World {

  // add fields needed to keep track of the state of the world
  ILoButton simonSequence;
  ILoButton playerSequence;
  boolean isGameStarted;
  boolean isGameEnded;
  boolean isPlayerTurn;
  boolean nowCreateseq;
  boolean buttonCreate;
  int sequenceIndex;
  int playerInputIndex;
  WorldImage yellowButton;
  WorldImage greenButton;
  WorldImage redButton;
  WorldImage blueButton;

  Random rand;
  Random randTester;

  // Constants
  int sceneSize = 500;
  int buttonSize = 150;

  // add constructors needed
  SimonWorld() {
    this.simonSequence = new MtLoButton();
    this.playerSequence = new MtLoButton();
    this.isGameStarted = false;
    this.isGameEnded = false;
    this.isPlayerTurn = false;
    this.nowCreateseq = false;
    this.buttonCreate = true;
    this.sequenceIndex = 1;
    this.playerInputIndex = 0;
    this.yellowButton = new Button(Color.YELLOW).drawDark();
    this.greenButton = new Button(Color.GREEN).drawDark();
    this.redButton = new Button(Color.RED).drawDark();
    this.blueButton = new Button(Color.BLUE).drawDark();

    this.rand = new Random();
    this.randTester = new Random(731496594); // for testing purposes
  }

  // constructor for testing purposes
  SimonWorld(boolean buttonCreate, ILoButton simonSequence, ILoButton playerSequence,
      boolean isGameStarted, boolean isGameEnded, boolean isPlayerTurn, boolean nowCreateseq, 
      int sequenceIndex, int playerInputIndex,
      WorldImage yellowButton, WorldImage greenButton, WorldImage redButton, WorldImage blueButton,

      Random rand, Random randTester) {

    this.buttonCreate = true;
    this.simonSequence = simonSequence;
    this.playerSequence = playerSequence;
    this.isGameStarted = isGameStarted;
    this.isGameEnded = isGameEnded;
    this.isPlayerTurn = isPlayerTurn;
    this.nowCreateseq = nowCreateseq;
    this.sequenceIndex = sequenceIndex;
    this.playerInputIndex = playerInputIndex;
    this.yellowButton = yellowButton;
    this.greenButton = greenButton;
    this.redButton = redButton;
    this.blueButton = blueButton;

    this.rand = rand;
    this.randTester = randTester;
  }

  // Draw the current state of the world
  // there are four buttons and a start sign
  public WorldScene makeScene() {

    WorldScene backscence = new WorldScene(sceneSize, sceneSize);
    WorldImage theFourButton = new VisiblePinholeImage(
        new BesideImage(new AboveImage(this.greenButton, this.yellowButton),
            new AboveImage(this.blueButton, this.redButton)));
    WorldImage startSign = new OverlayImage(new TextImage("Start Game", 30, Color.GREEN),
        new RectangleImage(200, 100, OutlineMode.OUTLINE, Color.RED));
    WorldScene background = backscence.placeImageXY(startSign, 250, 50).placeImageXY(theFourButton,
        250, 250);

    return background;
  }

  // Returns the final scene with the given message displayed
  // "Game End"
  public WorldEnd worldEnds() {
    if (isGameEnded) {
      WorldImage startButton = new OverlayImage(new TextImage("Game End", 40, Color.BLACK),
          new RectangleImage(200, 100, OutlineMode.OUTLINE, Color.BLACK));

      return new WorldEnd(true, new WorldScene(500, 500).placeImageXY(startButton, 200, 200));
    }
    else {
      return new WorldEnd(false, this.makeScene());
    }
  }

  // handles mouse clicks and is given the mouse location
  public SimonWorld onMouseClicked(Posn pos) {
    // if "start game" is clicked
    if (!isGameStarted && pos.x > 150 && pos.x < 350 && pos.y > 0 && pos.y < 100) {
      this.isGameStarted = true;
      this.nowCreateseq = true;
      System.out.println("you clicked the start butoton");
    }

    // if green button is clicked
    if (isPlayerTurn && pos.x > 100 && pos.x < 250 && pos.y > 100 && pos.y < 250) {
      this.playerSequence = new ConsLoButton(new Button(Color.GREEN), this.playerSequence);

      if (this.playerSequence.reverseSequence().beginsWith(this.simonSequence.reverseSequence())) {
        this.greenButton = new Button(Color.GREEN).drawLit();
        this.playerInputIndex = this.playerInputIndex + 1;
      }
      else {
        this.isGameEnded = true;
      }
      System.out.println("green");
    }

    // if red button is clicked
    if (isPlayerTurn && pos.x > 250 && pos.x < 400 && pos.y > 250 && pos.y < 400) {
      this.playerSequence = new ConsLoButton(new Button(Color.RED), this.playerSequence);

      if (this.playerSequence.reverseSequence().beginsWith(this.simonSequence.reverseSequence())) {
        this.redButton = new Button(Color.RED).drawLit();
        this.playerInputIndex = this.playerInputIndex + 1;
      }
      else {
        this.isGameEnded = true;
      }
      System.out.println("red");
    }

    // if blue button is clicked
    if (isPlayerTurn && pos.x > 250 && pos.x < 400 && pos.y > 100 && pos.y < 250) {

      this.playerSequence = new ConsLoButton(new Button(Color.BLUE), this.playerSequence);

      if (this.playerSequence.reverseSequence().beginsWith(this.simonSequence.reverseSequence())) {
        this.blueButton = new Button(Color.BLUE).drawLit();
        this.playerInputIndex = this.playerInputIndex + 1;
      }
      else {
        this.isGameEnded = true;
      }

      System.out.println("blue");
    }

    // if yellow button is clicked
    if (isPlayerTurn && pos.x > 100 && pos.x < 250 && pos.y > 250 && pos.y < 400) {

      this.playerSequence = new ConsLoButton(new Button(Color.YELLOW), this.playerSequence);

      if (this.playerSequence.reverseSequence().beginsWith(this.simonSequence.reverseSequence())) {
        this.yellowButton = new Button(Color.YELLOW).drawLit();
        this.playerInputIndex = this.playerInputIndex + 1;
      }
      else {
        this.isGameEnded = true;
      }

      System.out.println("yellow");
    }

    return this;
  }

  // handles clock tick and changing the field of the world
  // under each required condition
  public World onTick() {

    // keep them dark at all time
    this.redButton = new Button(Color.RED).drawDark();
    this.greenButton = new Button(Color.GREEN).drawDark();
    this.blueButton = new Button(Color.BLUE).drawDark();
    this.yellowButton = new Button(Color.YELLOW).drawDark();

    if (this.isGameStarted) {

      if (!isPlayerTurn && nowCreateseq) {
        System.out.println("hi");
        System.out.println(this.sequenceIndex);
        if (buttonCreate) {
          Random random = new Random();
          int randnum = random.nextInt(4);

          // first generate one random button
          Button currentGeButton = generateButtonByRand(randnum);

          // then, cons it into the current Simon sequence with the rest of the list
          this.simonSequence = new ConsLoButton(currentGeButton, this.simonSequence);
        }

        if (this.simonSequence.reverseSequence().getButtonByIndex(this.sequenceIndex)
            .sameButton(new Button(Color.RED))) {
          this.redButton = new Button(Color.RED).drawLit();

        }
        if (this.simonSequence.reverseSequence().getButtonByIndex(this.sequenceIndex)
            .sameButton(new Button(Color.GREEN))) {
          this.greenButton = new Button(Color.GREEN).drawLit();

          // this.sequenceIndex = this.sequenceIndex + 1;
        }
        if (this.simonSequence.reverseSequence().getButtonByIndex(this.sequenceIndex)
            .sameButton(new Button(Color.BLUE))) {
          this.blueButton = new Button(Color.BLUE).drawLit();

          // this.sequenceIndex = this.sequenceIndex + 1;

        }
        if (this.simonSequence.reverseSequence().getButtonByIndex(this.sequenceIndex)
            .sameButton(new Button(Color.YELLOW))) {
          this.yellowButton = new Button(Color.YELLOW).drawLit();
        }

        if (this.sequenceIndex == this.simonSequence.getLength()) {

          this.buttonCreate = true;
          this.sequenceIndex = 1;
          this.isPlayerTurn = true;
        }
        else {

          this.buttonCreate = false;
          this.sequenceIndex = this.sequenceIndex + 1;
        }

      }
      else {
        if (playerInputIndex == this.simonSequence.getLength()) {
          this.isPlayerTurn = false;
          this.playerSequence = new MtLoButton();
          this.playerInputIndex = 0;
        }

      }

    }

    return this;
  }

  // produce a random color button out of red, green, blue, and yellow
  Button generateButtonByRand(int randomnum) {

    Button randomButton = new Button(Color.RED);
    if (randomnum == 0) {
      randomButton = new Button(Color.GREEN);
    }
    else if (randomnum == 1) {
      randomButton = new Button(Color.BLUE);
    }
    else if (randomnum == 2) {
      randomButton = new Button(Color.YELLOW);
    }
    else if (randomnum == 3) {
      randomButton = new Button(Color.RED);
    }
    return randomButton;
  }

  /*
   * Fields:
   * ... this.simonsequence ... -- ILoButton
   * ... this.playerSequence ... -- ILoButton
   * ... this.isGameStarted ... -- boolean
   * ... this.isGameEnded ... -- boolean
   * ... this.isPlayersTurn ... -- boolean
   * ... this.nowCreateseq ... -- boolean
   * ... this.buttonCreate ... -- boolean
   * ... this.sequenceIndex ... -- int
   * ... this.playerInputIndex ... -- int
   * ... this.greenButton ... -- WorldImage
   * ... this.blueButton ... -- WorldImage
   * ... this.yellowButton ... -- WorldImage
   * ... this.redButton ... -- WorldImage
   * ... this.rand ... -- Random
   * ... this.randTester -- Random
   * 
   * ... this.color -- Color
   * Methods:
   * ... this.makeScene() ... WorldScene
   * ... this.onTick() ... World
   * ... this.worldEnds() ... WorldScene
   * ... this.onMouseClicked(Posn) ... SimonWorld
   * ... this.generateButtonByRand(int) ... Button
   * 
   * ... this.reverseSequence() ... ILoButton
   * ... this.reverseSequenceHelper() ... ILoButton
   * ... this.getButtonByIndex(int) ... Button
   * ... this.getLength() ... int
   * ... this.beginsWith(ILoButton)... boolean
   * ... this.beginsWithHelper(ConsLoButton) ... boolean
   * 
   * ... this.drawButtonImage(Color) ... WorldImage
   * ... this.drawDark() ... WorldImage
   * ... this.drawLit() ... WorldImage
   * 
   * ... this.sameButton(Button) ... boolean
   * 
   * Methods on Fields:
   * ... this.simonsequence.reverseSequence() ... ILoButton
   * ... this.simonsequence.reverseSequenceHelper() ... ILoButton
   * ... this.simonsequence.getButtonByIndex(int) ... Button
   * ... this.simonsequence.getLength() ... int
   * ... this.simonsequence.beginsWith(ILoButton)... boolean
   * ... this.simonsequence.beginsWithHelper(ConsLoButton) ... boolean
   * ... this.playerSequence.reverseSequence() ... ILoButton
   * ... this.playerSequence.reverseSequenceHelper() ... ILoButton
   * ... this.playerSequence.getButtonByIndex(int) ... Button
   * ... this.playerSequence.getLength() ... int
   * ... this.playerSequence.beginsWith(ILoButton)... boolean
   * ... this.playerSequence.beginsWithHelper(ConsLoButton) ... boolean
   * 
   * 
   *
   * Methods on Parameter:
   * ... other.reverseSequence() ... ILoButton
   * ... other.reverseSequenceHelper() ... ILoButton
   * ... other.getButtonByIndex(int) ... Button
   * ... other.getLength() ... int
   * ... other.beginsWith(ILoButton)... boolean
   * ... other.beginsWithHelper(ConsLoButton) ... boolean
   * 
   * ... other.drawButtonImage(Color) ... WorldImage
   * ... other.drawDark() ... WorldImage
   * ... other.drawLit() ... WorldImage
   * ... other.sameButton(Button) ... boolean
   */
}



// Represents a list of buttons
interface ILoButton {

  // reverse the sequence
  ILoButton reverseSequence();

  // reverse helper
  ILoButton reverseSequenceHelper(ILoButton other);

  // get the button by given index
  Button getButtonByIndex(int index);

  // get the length of the sequence
  int getLength();

  // check if this sequence starts the the same as the given sequence 
  boolean beginsWith(ILoButton other);

  // reverse helper
  boolean beginsWithHelper(ConsLoButton consLoButton);

}

// Represents an empty list of buttons
class MtLoButton implements ILoButton {

  
  //reverse the sequence, which return this
  public ILoButton reverseSequence() {
    return this;
  }

  
  public ILoButton reverseSequenceHelper(ILoButton other) {
    return other;
  }
 
  //get the length of the sequence
  public int getLength() {
    return 0;
  }

  
  //get the button by given index, which will never be called
  public Button getButtonByIndex(int index) {
    throw new RuntimeException("can not get the button in mtList");
  }
  
  //check if this sequence starts the the same as the given sequence 
  // always true
  public boolean beginsWith(ILoButton other) {
    return true;
  }

  // helper, always true
  public boolean beginsWithHelper(ConsLoButton cons) {
    return true;
  }

}

// Represents a non-empty list of buttons
class ConsLoButton implements ILoButton {
  Button first;
  ILoButton rest;

  ConsLoButton(Button first, ILoButton rest) {
    this.first = first;
    this.rest = rest;
  }

  
  // reverse a sequence
  public ILoButton reverseSequence() {
    return reverseSequenceHelper(new MtLoButton());
  }

  // reverse helper
  public ILoButton reverseSequenceHelper(ILoButton other) {
    return this.rest.reverseSequenceHelper(new ConsLoButton(this.first, other));
  }
 
  
  // get the length of the sequence
  public int getLength() {
    return 1 + this.rest.getLength();
  }

  
  // get the button in sequence by given index
  public Button getButtonByIndex(int index) {
    if (index == 1) {
      return this.first;
    }
    else {
      return this.rest.getButtonByIndex(index - 1);
    }
  }

  
  // check if this sequence begins with the given sequence
  public boolean beginsWith(ILoButton other) {
    return other.beginsWithHelper(this);
  }

  
  // beginswith helper
  public boolean beginsWithHelper(ConsLoButton cons) {
    return this.first.sameButton(cons.first) && this.rest.beginsWith(cons.rest);
  }

}

// Represents one of the four buttons you can click
class Button {
  Color color;

  Button(Color color) {
    this.color = color;
  }

  // draw the button
  WorldImage drawButtonImage(Color c) {
    return new RectangleImage(150, 150, OutlineMode.SOLID, c);
  }

  // Draw this button dark
  WorldImage drawDark() {
    return this.drawButtonImage(this.color.darker().darker());
  }

  // Draw this button lit
  WorldImage drawLit() {
    return this.drawButtonImage(this.color.brighter().brighter());
  }

  // check if the this button is the same as the given button
  public boolean sameButton(Button other) {
    return (this.color == other.color);
  }
}

// Examples
class ExamplesSimon {
  // put all of your examples and tests here

  // runs the game by creating a world and calling bigBang
  boolean testSimonSays(Tester t) {
    SimonWorld starterWorld = new SimonWorld();
    int sceneSize = 500;
    return starterWorld.bigBang(sceneSize, sceneSize, 1);
  }
  
  

  // test for drawButtonImage
  boolean testDrawButtonImage(Tester t) {
    return t.checkExpect(this.buttonRed.drawButtonImage(Color.RED.darker().darker()), 
        this.redBuD)
        && t.checkExpect(this.buttonGreen.drawButtonImage(Color.GREEN.darker().darker()), 
            this.greenBuD)
        && t.checkExpect(this.buttonBlue.drawButtonImage(Color.BLUE.darker().darker()), 
            this.blueBuD)
        && t.checkExpect(this.buttonYellow.drawButtonImage(Color.YELLOW.darker().darker()), 
            this.yellowBuD)
        && t.checkExpect(this.buttonRed.drawButtonImage(Color.RED.brighter().brighter()), 
            this.redBuB)
        && t.checkExpect(this.buttonGreen.drawButtonImage(Color.GREEN.brighter().brighter()), 
                this.greenBuB)
        && t.checkExpect(this.buttonBlue.drawButtonImage(Color.BLUE.brighter().brighter()), 
                this.blueBuB)
        && t.checkExpect(this.buttonYellow.drawButtonImage(Color.YELLOW.brighter().brighter()), 
                this.yellowBuB);
  }
  
  // test for draDark
  boolean testDrawLit(Tester t) {
    return t.checkExpect(buttonRed.drawLit(), this.redBuB)
        && t.checkExpect(buttonGreen.drawLit(), this.greenBuB);
  }
  
  // test for drawLit
  boolean testDrawDark(Tester t) {
    return t.checkExpect(buttonRed.drawDark(), this.redBuD)
        && t.checkExpect(buttonGreen.drawDark(), this.greenBuD);
  }
  
  // test for sameButton
  boolean testSameButton(Tester t) {
    return t.checkExpect(buttonRed.sameButton(buttonRed), true)
        && t.checkExpect(buttonRed.sameButton(buttonRedB), false)
        && t.checkExpect(buttonRed.sameButton(buttonBlue), false);
  }

  
  
  
  // examples of all possible button
  Button buttonGreen = new Button(Color.green);
  Button buttonGreenD = new Button(Color.green.darker().darker());
  Button buttonGreenB = new Button(Color.green.brighter().brighter());
  Button buttonRed = new Button(Color.red);
  Button buttonRedD = new Button(Color.red.darker().darker());
  Button buttonRedB = new Button(Color.red.brighter().brighter());
  Button buttonYellow = new Button(Color.yellow);
  Button buttonYD = new Button(Color.yellow.darker().darker());
  Button buttonYellowB = new Button(Color.yellow.brighter().brighter());
  Button buttonBlue = new Button(Color.blue);
  Button buttonBlueD = new Button(Color.blue.darker().darker());
  Button buttonBlueB = new Button(Color.blue.brighter().brighter());
  
  
  
  //example of all the worldImage of button in rectangle
 
  WorldImage yellowBu = new RectangleImage(150, 150, OutlineMode.SOLID, Color.yellow);
  WorldImage yellowBuB = new RectangleImage(150, 150, OutlineMode.SOLID, 
      Color.yellow.brighter().brighter());
  WorldImage yellowBuD = new RectangleImage(150, 150, OutlineMode.SOLID, 
      Color.yellow.darker().darker());
  WorldImage greenBu = new RectangleImage(150, 150, OutlineMode.SOLID, Color.green);
  WorldImage greenBuB = new RectangleImage(150, 150, OutlineMode.SOLID, 
      Color.green.brighter().brighter());
  WorldImage greenBuD = new RectangleImage(150, 150, OutlineMode.SOLID, 
      Color.green.darker().darker());
  WorldImage redBu = new RectangleImage(150, 150, OutlineMode.SOLID, Color.red);
  WorldImage redBuB = new RectangleImage(150, 150, OutlineMode.SOLID, 
      Color.red.brighter().brighter());
  WorldImage redBuD = new RectangleImage(150, 150, OutlineMode.SOLID, 
      Color.red.darker().darker());
  WorldImage blueBuD = new RectangleImage(150, 150, OutlineMode.SOLID, 
      Color.blue.darker().darker());
  WorldImage blueBu = new RectangleImage(150, 150, OutlineMode.SOLID, Color.blue);
  WorldImage blueBuB = new RectangleImage(150, 150, OutlineMode.SOLID, 
      Color.blue.brighter().brighter());
  
  // ILoButton example for beginsWith
  ILoButton mtLoButton = new MtLoButton();
  ILoButton ex1R = new ConsLoButton(new Button(Color.RED),this.mtLoButton);
  ILoButton ex2RY = new ConsLoButton(new Button(Color.RED), 
      new ConsLoButton(new Button(Color.YELLOW),this.mtLoButton));
  ILoButton ex3Y = new ConsLoButton(new Button(Color.YELLOW),this.mtLoButton);
  ILoButton ex4RYG = new ConsLoButton(new Button(Color.RED), 
      new ConsLoButton(new Button(Color.YELLOW), 
          new ConsLoButton(new Button(Color.GREEN), this.mtLoButton)));
  ILoButton ex5YR = new ConsLoButton(new Button(Color.YELLOW), 
      new ConsLoButton(new Button(Color.RED),this.mtLoButton));
  ILoButton ex6G = new ConsLoButton(new Button(Color.GREEN),this.mtLoButton);
  
  
  // test for beginsWith 
  boolean testBeginsWith(Tester t) {
    return t.checkExpect(this.ex1R.beginsWith(this.ex1R), true)
        && t.checkExpect(this.ex2RY.beginsWith(this.ex1R), true)
        && t.checkExpect(this.mtLoButton.beginsWith(this.mtLoButton), true)
        && t.checkExpect(this.ex1R.beginsWith(this.mtLoButton), true)
        && t.checkExpect(this.ex1R.beginsWith(this.ex2RY), true)
        && t.checkExpect(this.ex4RYG.beginsWith(this.ex3Y), false)
        && t.checkExpect(this.mtLoButton.beginsWith(this.ex1R), true);
  }
  
  // test for beginsWithHelper 
  boolean testBeginsWithHelper(Tester t) {
    return t.checkExpect(this.ex1R.beginsWithHelper((ConsLoButton) this.ex1R), true)
        && t.checkExpect(this.ex1R.beginsWithHelper((ConsLoButton) this.ex2RY), true)
        && t.checkExpect(this.ex2RY.beginsWithHelper((ConsLoButton)this.ex1R), true)
        && t.checkExpect(this.mtLoButton.beginsWithHelper((ConsLoButton) ex1R), true)
        && t.checkExpect(this.ex3Y.beginsWithHelper((ConsLoButton) this.ex4RYG), false);
      
  }
  
  boolean testGetButtonByIndex(Tester t) {
    return t.checkExpect(this.ex4RYG.getButtonByIndex(1), this.buttonRed)
        && t.checkExpect(this.ex4RYG.getButtonByIndex(2), this.buttonYellow);
    //&& t.checkExpect(mtLoButton.getButtonByIndex(2), 
    //new RuntimeException("can not get the button in mtList"));
    // empty case
  }
  
  // test for reverseSequence
  boolean testReverseSequence(Tester t) {
    return t.checkExpect(this.ex1R.reverseSequence(), this.ex1R)
        && t.checkExpect(this.mtLoButton.reverseSequence(), this.mtLoButton)
        && t.checkExpect(this.ex2RY.reverseSequence(), this.ex5YR);
  }
  
  //test for reverseSequenceHelper
  boolean testReverseSequenceHelper(Tester t) {
    return t.checkExpect(this.ex1R.reverseSequenceHelper(this.mtLoButton), this.ex1R)
        && t.checkExpect(this.mtLoButton.reverseSequenceHelper(this.ex1R), this.ex1R)
        && t.checkExpect(this.ex2RY.reverseSequenceHelper(this.mtLoButton), this.ex5YR)
        && t.checkExpect(this.ex5YR.reverseSequenceHelper(this.ex6G), this.ex4RYG);
  }
 
  // test for GetLength
  boolean testGetLength(Tester t) {
    return t.checkExpect(this.mtLoButton.getLength(), 0)
        && t.checkExpect(this.ex2RY.getLength(), 2);
    
  }
    

  
  
  
  // test for generateButtonByRand
  boolean testGenerateButtonByRand(Tester t) {
    return t.checkExpect(generateButtonByRand(0), this.buttonGreen)
        && t.checkExpect(generateButtonByRand(2), this.buttonYellow);
  }
  
  Button generateButtonByRand(int randomnum) {
    Button randomButton = new Button(Color.RED);
    if (randomnum == 0) {
      randomButton = new Button(Color.GREEN);
    }
    else if (randomnum == 1) {
      randomButton = new Button(Color.BLUE);
    }
    else if (randomnum == 2) {
      randomButton = new Button(Color.YELLOW);
    }
    else if (randomnum == 3) {
      randomButton = new Button(Color.RED);
    }
    return randomButton;
  }

  // some example scene for for the testMakeScene
  WorldScene backscence = new WorldScene(500, 500);
  WorldImage theFourButton = new VisiblePinholeImage(
      new BesideImage(new AboveImage(this.greenBuD, this.yellowBuD),
          new AboveImage(this.blueBuD, this.redBuD)));
  WorldImage startSign = new OverlayImage(new TextImage("Start Game", 30, Color.GREEN),
      new RectangleImage(200, 100, OutlineMode.OUTLINE, Color.RED));
  
  // some example of SimonWorld 
  // initial world
  SimonWorld testerWorld1 = new SimonWorld(true, this.mtLoButton, this.mtLoButton, false, false,
      false, false, 1, 0, this.yellowBuD, this.greenBuD,
      this.redBuD, this.blueBuD, new Random(), 
      new Random(731496594));
  
  //end game
  SimonWorld testerWorld2 = new SimonWorld(true, this.mtLoButton, this.mtLoButton, false, true,
      false, false, 1, 0, this.yellowBuD, this.greenBuD,
      this.redBuD, this.blueBuD, new Random(), 
      new Random(731496594));
  
  // game just start
  SimonWorld testerWorld3 = new SimonWorld(true, this.mtLoButton, this.mtLoButton, true, false,
      false, true, 1, 0, this.yellowBuD, this.greenBuD,
      this.redBuD, this.blueBuD, new Random(), 
      new Random(731496594));
  
  // then clicked green
  SimonWorld testerWorld4 = new SimonWorld(true, this.mtLoButton, this.mtLoButton, true, false,
      false, true, 1, 0, this.yellowBuD, this.greenBuD,
      this.redBuD, this.blueBuD, new Random(), 
      new Random(731496594));
  
  // test for makeScene
  boolean testMakeScene(Tester t) {
    return t.checkExpect(
        this.testerWorld1.makeScene(), 
        new WorldScene(500, 500).placeImageXY(this.startSign, 250, 50)
        .placeImageXY(this.theFourButton, 250, 250));    
  }
  
  // example for last worldscene
  WorldImage endSign = new OverlayImage(new TextImage("Game End", 40, Color.BLACK),
      new RectangleImage(200, 100, OutlineMode.OUTLINE, Color.BLACK));
  
  // test for worldEnds
  boolean testWorldEnds(Tester t) {
    return t.checkExpect(this.testerWorld2.worldEnds(), 
        new WorldEnd(true, new WorldScene(500, 500).placeImageXY(this.endSign, 200, 200)));
  }
  
  // some example Posns of the position clicked
  Posn startPos = new Posn(250, 50);
  Posn greenPos = new Posn(150, 150);
  
  
  // test for OnMouseClicked
  boolean testOnMouseClicked(Tester t) {
    return t.checkExpect(this.testerWorld1.onMouseClicked(this.startPos), this.testerWorld3)
        && t.checkExpect(this.testerWorld3.onMouseClicked(this.greenPos), this.testerWorld4);
    
        
  }
  
  
  
  
  
  
  
  
}