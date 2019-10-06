package pasa.cbentley.swing.skin.run;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSlider;

import pasa.cbentley.core.src4.interfaces.IPrefs;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.swing.run.RunSwingAbstract;
import pasa.cbentley.swing.skin.ctx.SwingSkinCtx;
import pasa.cbentley.swing.skin.main.SwingSkinManager;
import pasa.cbentley.swing.window.CBentleyFrame;

/**
 * TODO make a youtube vid with cripsy bacon with Java power
 * @author Charles Bentley
 *
 */
public class RunSwingSkinManager extends RunSwingAbstract {

   /**
    * Simple test method
    * @param args
    */
   public static void main(String[] args) {
      RunSwingSkinManager runner = new RunSwingSkinManager();
      runner.initUIThreadOutside();
      javax.swing.SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            runner.initUIThreadInside();
         }
      });
   }

   protected final SwingSkinCtx ssc;

   private SwingSkinManager     lfm;

   public RunSwingSkinManager() {
      super();
      ssc = new SwingSkinCtx(sc);
   }

   protected void addI18n(List<String> list) {
      list.add("i18nSwingSkin");
   }

   public void cmdExit() {
      lfm.prefsSave();
      System.exit(0);
   }

   protected void initOutsideUIForPrefs(IPrefs prefs) {

   }

   protected CBentleyFrame initUIThreadInsideSwing() {

      sc.setExitableMain(this);
      sc.setResMissingLog(true);

      lfm = new SwingSkinManager(ssc);
      lfm.setUsingDefaultKeyShortcuts(true);
      lfm.setDefault("com.jtattoo.plaf.mint.MintLookAndFeel", "Medium-Font");
      lfm.prefsInit();
      
      lfm.setIconSelected(new Icon() {
         public int getIconHeight() {
            return 16;
         }

         public int getIconWidth() {
            return 16;
         }

         public void paintIcon(Component c, Graphics g, int x, int y) {
            g.setColor(Color.blue);
            g.fillRect(x, y, 16, 16);
         }
      });
      final CBentleyFrame frame = new CBentleyFrame(sc, "lookfeelframe");

      JMenuBar menuBar = new JMenuBar();
      menuBar.add(lfm.getRootMenu());
      JMenu jm = new JMenu("Options");
      final JRadioButtonMenuItem jiUndecorated = new JRadioButtonMenuItem("Undecorated");
      jiUndecorated.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            boolean isDecorated = !frame.isUndecorated();
            boolean isDecoSupport = JFrame.isDefaultLookAndFeelDecorated();

            System.out.println("isDecorated=" + isDecorated + " isDecoSupport=" + isDecoSupport);

            frame.dispose();
            frame.setUndecorated(true);
            frame.setVisible(true);
         }
      });
      final JRadioButtonMenuItem jiDecorated = new JRadioButtonMenuItem("Decorated");
      jiDecorated.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            boolean isUndecorated = frame.isUndecorated();
            if (isUndecorated) {
               frame.dispose();
               frame.setUndecorated(false);
               frame.setVisible(true);
            }
         }
      });

      final JMenuItem fontIncrease = new JMenuItem("Font size increase");
      fontIncrease.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            lfm.cmdFontSizeIncrease();
         }
      });
      final JMenuItem fontDecrease = new JMenuItem("Font size decrease");
      fontDecrease.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            lfm.cmdFontSizeDecrease();
         }
      });
      final JMenuItem prefsQuit = new JMenuItem("Clear Preferences And Quit");
      prefsQuit.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            sc.getPrefs().clear();
            System.exit(0);
         }
      });
      ButtonGroup group = new ButtonGroup();
      group.add(jiDecorated);
      group.add(jiUndecorated);
      jm.add(jiDecorated);
      jm.add(jiUndecorated);
      jm.addSeparator();
      jm.add(fontIncrease);
      jm.add(fontDecrease);
      jm.addSeparator();
      jm.add(prefsQuit);
      
      menuBar.add(jm);
      frame.setJMenuBar(menuBar);

      JPanel main = getPanelMain();
      frame.getContentPane().add(main);
      frame.pack();
      frame.setSize(300, 200);
      frame.setLocation(400, 400);
      frame.setVisible(true);

      if (frame.isUndecorated()) {
         jiUndecorated.setSelected(true);
      } else {
         jiDecorated.setSelected(true);
      }

      return frame;
   }

   public JPanel getPanelMain() {

      JPanel panel = new JPanel();
      panel.setLayout(new BorderLayout());

      JCheckBox checkBox = new JCheckBox("Java Power");

      JPanel panelSlider = getPanelSlider();

      JButton but = new JButton("Hello World!");

      panel.add(panelSlider, BorderLayout.NORTH);
      panel.add(but, BorderLayout.CENTER);
      panel.add(checkBox, BorderLayout.SOUTH);

      return panel;

   }

   public JPanel getPanelSlider() {
      JPanel panel = new JPanel();

      panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

      JLabel sliderLabel = new JLabel("Rate the power of Look and Feels in Java Swing", JLabel.CENTER);
      sliderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

      //Create the slider.
      JSlider framesPerSecond = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);

      //Turn on labels at major tick marks.

      framesPerSecond.setMajorTickSpacing(10);
      framesPerSecond.setMinorTickSpacing(1);
      framesPerSecond.setPaintTicks(true);
      framesPerSecond.setPaintLabels(true);
      framesPerSecond.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

      //Put everything together.
      panel.add(sliderLabel);
      panel.add(framesPerSecond);
      panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

      return panel;
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, "RunSwingSkinManager");
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "RunSwingSkinManager");
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   private void toStringPrivate(Dctx dc) {

   }

   //#enddebug

}
