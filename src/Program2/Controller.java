package Program2;
//---Imports
import views.MyFrame;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.swing.filechooser.FileNameExtensionFilter;
/**
 *
 * @author  Zachery Soles
 * @project Program 2 Version 3
 * @Program A text editor
 * @Date 3/27/18
 */
public class Controller {
  private final MyFrame frame = new MyFrame();
  boolean edit = false;
  //---Dialog box (File Chooser) that opens and lets the user see the user directory
  private static JFileChooser getFileChoice() {
    JFileChooser choice = new JFileChooser();
    //---Specify where chooser should open up
    File workingDirectory = new File(System.getProperty("user.dir"));
    choice.setCurrentDirectory(workingDirectory);
    //---Only show files that are .txt
    choice.addChoosableFileFilter(new FileNameExtensionFilter(".txt or .java files", "txt" , "java"));
    //---Do not accept all files
    choice.setAcceptAllFileFilterUsed(false);
    return choice;
  }
  //---Sets the modified content alert to 0, and sets the save & save as menu to true
  public void resetMenu()
  {
    frame.getModTextField().setText(null);
    frame.getSaveMenuItem().setEnabled(true);
    frame.getSaveAsMenuItem().setEnabled(true);
  }
  //---Resets the content, and mod text field to blank
  //---Sets title text field to new file
  public void newMenu()
  {
    frame.getTitleTextField().setText("<NEW FILE>");
    frame.getModTextField().setText(null);
    frame.getContentTextField().setText(null);
    frame.getContentTextField().setEditable(true);
    //---Sets save as to enables
    frame.getSaveAsMenuItem().setEnabled(true);
    frame.getSaveMenuItem().setEnabled(false);
    edit = true;
  }
  //---Sets the content text field to editable
  public void openMenu()
  {
    frame.getContentTextField().setEditable(true);
    edit = true;
    resetMenu();
  }
  //---Sets SaveMenu, SaveAs, and ContentTextField to not enabled and editable
  public void noEdit()
  {
    frame.getSaveMenuItem().setEnabled(false);
    frame.getSaveAsMenuItem().setEnabled(false);
    frame.getContentTextField().setEditable(false);
    edit = false;
  }
  //---Checks to see if a file is in the subdir folder
  public static boolean inSubDir(File f) 
  {
    if(f.getPath().contains("subdir"))
      return true;
    else
      return false;
  }
  //---Constructor
  public Controller()
  {
    noEdit();
    //---Sets save as, save as, and content text field to non editable
    frame.setTitle(getClass().getSimpleName());
    frame.setLocationRelativeTo(null);
    JFileChooser choice = getFileChoice();
    //---Action Listener for New Menu
    frame.getNewMenuItem().addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        //---Checks to see if content is empty
        if(frame.getModTextField().getText().isEmpty())
        {
          newMenu();
        }
        //---if content is not empty
        else
        {
          //---Shows a yes or no menu to discard the text
          int options = JOptionPane.YES_NO_CANCEL_OPTION;
          int dialogResult = JOptionPane.showConfirmDialog(null,"OK to discard changes", "Select an option", options);
          if(dialogResult == JOptionPane.YES_OPTION)
          {
            newMenu();
          }
        }
      }
    });
    //---Action Listener for Open Menu
    frame.getOpenMenuItem().addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        //---JFile choice
        int status = choice.showOpenDialog(frame);
        if(status != JFileChooser.APPROVE_OPTION)
        {
          return;
        }
        File f = choice.getSelectedFile();
        Path path = f.toPath();
        String fileName;
        try
        {
          //---Content has not been edited
          String content = new String(Files.readAllBytes(path));
          if(frame.getModTextField().getText().isEmpty())
          {
            //---Prints info on file
            System.out.println("-OPEN-");
            System.out.println(f.toString());
            //---Checks to see if file is in a subdirectory
            if(inSubDir(f) == true)
              fileName = "subdir/" + f.getName();
            else
              fileName = f.getName();
            //---Opens the file
            frame.getTitleTextField().setText(fileName);
            frame.getContentTextField().setText(content);
            openMenu();
          }
          else
          {
            //---Yes or no option if content has been edited
            int options = JOptionPane.YES_NO_CANCEL_OPTION;
            int dialogResult = JOptionPane.showConfirmDialog(null, "OK to discard changes?", "Select an option", options);
            if(dialogResult == JOptionPane.YES_OPTION)
            {
              //---Prints info on file
              System.out.println("-OPEN-");
              System.out.println(f.toString());
              //---Checks to see if file is in a subdirectory
              if(inSubDir(f) == true)
                fileName = "subdir/" + f.getName();
              else
                fileName = f.getName();
              //---Opens the file
              frame.getTitleTextField().setText(fileName);
              frame.getContentTextField().setText(content);
              openMenu();
            }
          }
        }
        //---Catches error messages
        catch (IOException ex)
        {
          ex.printStackTrace(System.err);
          JOptionPane.showMessageDialog(frame, "Cannot open file");
        }
      }
    });
    //---Action Listener for Save As Menu
    frame.getSaveAsMenuItem().addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        String fileName;
        //---JFile Chooder
        int status = choice.showSaveDialog(frame);
        if(status != JFileChooser.APPROVE_OPTION)
        {
          return;
        }
        File f = choice.getSelectedFile();
        //---Sees if there is an extention in the file name
        if(!f.toString().contains("."))
        {
          //---Adds '.txt. if its not there
          String name = f.getAbsoluteFile()+".txt";
          f = new File(name);
        }
        Path path = f.toPath();
        try
        {
          //---Yes, no option to overwrite file
          if(f.exists())
          {
            int options = JOptionPane.YES_NO_CANCEL_OPTION;
            int dialogResult = JOptionPane.showConfirmDialog(null,"OK to overwrite", "Select an option", options);
            if(dialogResult == JOptionPane.YES_OPTION)
            {
              //---Prints info on file
              System.out.println("\n-Save to-");
              System.out.println(f.toString());
              //---Saves the file
              String content = frame.getContentTextField().getText();
              Files.write(path, content.getBytes());
              //---Checks to see if file is in subdir
              if(inSubDir(f) == true)
                fileName = "subdir/" + f.getName();
              else
                fileName = f.getName();
              frame.getTitleTextField().setText(fileName);
              resetMenu();
            }
          }
          else
          {
            //---Prints info on file
            System.out.println("\n-Save to-");
            System.out.println(f.toString());
            //---Saves the file
            String content = frame.getContentTextField().getText();
            Files.write(path, content.getBytes());
            //---Checks to see if file is in subdir
            if(inSubDir(f) == true)
                fileName = "subdir/" + f.getName();
              else
                fileName = f.getName();
            frame.getTitleTextField().setText(fileName);
            resetMenu();
          } 
        }
        //---Catches error messages
        catch(IOException ex)
        {
          ex.printStackTrace();
          JOptionPane.showMessageDialog(frame, "Cannot save file");
        }
      }
    });
    //---Action Listener for Save Menu
    frame.getSaveMenuItem().addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        File f = choice.getSelectedFile();
        Path path = f.toPath();
        try
        {
          if(frame.getModTextField().getText().isEmpty())
          {
            //---Prints info on file
            System.out.println("\n-Save to-");
            System.out.println(f.toString());
            //---Saves the file
            String content = frame.getContentTextField().getText();
            Files.write(path, content.getBytes());
            frame.getModTextField().setText(null);
          }
          else
          {
            //---Yes or no dialog to overwrite the file
            int options = JOptionPane.YES_NO_CANCEL_OPTION;
            int dialogResult = JOptionPane.showConfirmDialog(null,"OK to overwrite", "Select an option", options);
            if(dialogResult == JOptionPane.YES_OPTION)
            {
              //---Prints info on file
              System.out.println("\n-Save to-");
              System.out.println(f.toString());
              //---Saves the file
              String content = frame.getContentTextField().getText();
              Files.write(path, content.getBytes());
              frame.getModTextField().setText(null);
            }
          }
        }
        //---Catches error messages
        catch (IOException ex)
        {
          ex.printStackTrace(System.err);
          JOptionPane.showMessageDialog(frame, "Cannot save file.");
        }
      }
    });
    //---KeyListener for modified content indicator
    frame.getContentTextField().addKeyListener(new KeyListener()
    {
      @Override
      public void keyReleased(KeyEvent ke)
      {
      }
      @Override
      public void keyPressed(KeyEvent ke)
      {
      }
      @Override
      public void keyTyped(KeyEvent ke)
      {
        //---Enables the '*' if the content text field is set to editable or not
        if(edit == true)
          //---if a key is pressed, then the modtextfield is '*'
          frame.getModTextField().setText("*");
      }
    });
  }
  //---Main method
  public static void main(String[] args) {
    // TODO code application logic here
    Controller app = new Controller();
    app.frame.setVisible(true);
  }
}